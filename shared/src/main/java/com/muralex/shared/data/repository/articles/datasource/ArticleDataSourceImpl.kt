package com.muralex.shared.data.repository.articles.datasource

import com.muralex.shared.data.database.ArticlesDao
import com.muralex.shared.data.mappers.db_data.ArticleDBToDataMapper
import com.muralex.shared.data.mappers.db_data.DetailDBToDataMapper
import com.muralex.shared.data.mappers.json_data.ArticleJsonToDataMapper
import com.muralex.shared.data.mappers.json_data.DetailJsonToDataMapper
import com.muralex.shared.data.mappers.json_data.SectionsArticlesJsonToDataMapper
import com.muralex.shared.data.model.articles.ArticleData
import com.muralex.shared.data.model.articles.DetailData
import com.muralex.shared.data.model.articles.SectionArticlesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class ArticleDataSourceImpl(
    private val articlesJsonUtil: ArticlesJsonUtil,
    private val articlesMapper: ArticleJsonToDataMapper,
    private val collectionsMapper: SectionsArticlesJsonToDataMapper,
    private val detailMapper: DetailJsonToDataMapper,
    private val dao: ArticlesDao,
    private val articleDBToDataMapper: ArticleDBToDataMapper,
    private val detailDBToDataMapper: DetailDBToDataMapper,
) : ArticlesDataSource {

    override fun getSectionArticles(sectionId: String): Flow<List<ArticleData>> {
        return getArticlesBySectionIdFromDB(sectionId)
    }

    override suspend fun getSectionArticlesList(sectionId: String): List<ArticleData> {
        return getArticlesListBySectionIdFromDB(sectionId)
    }

    override suspend fun getArticleById(articleId: String): ArticleData {
        return getDetailedArticleByIdFromDB(articleId)
    }

    private fun getArticlesListBySectionIdFromDB(sectionId: String): List<ArticleData> {
        val listFromDB = dao.getArticlesListBySectionId(sectionId)
        return articleDBToDataMapper.mapFromEntityList(listFromDB)
    }

    private fun getArticlesBySectionIdFromDB(sectionId: String): Flow<List<ArticleData>> {
        return flow {
            dao.getArticlesBySectionId(sectionId)
                .collect {
                    emit(articleDBToDataMapper.mapFromEntityList(it))
                }
        }
    }

    private suspend fun getDetailedArticleByIdFromDB(articleId: String): ArticleData {
        val articleFromDB = dao.getArticleById(articleId)
        val detailFromDB = dao.getDetailById(articleId)
        val article = articleDBToDataMapper.mapFromEntity(articleFromDB)

        return detailFromDB?.let {
            detailedArticleData(
                article,
                detailDBToDataMapper.mapFromEntity(detailFromDB))
        } ?: article
    }

    private fun detailedArticleData(article: ArticleData, detail: DetailData): ArticleData {
        return article.copy(
            title = detail.title.ifBlank { article.title },
            desc = detail.desc.ifBlank { article.desc },
            text = detail.text.ifBlank { article.text },
            image = detail.image.ifBlank { article.image }
        )
    }

    private suspend fun getAllArticles(): List<ArticleData> {
        val dataFromJson = articlesJsonUtil.getArticles()
        return articlesMapper.mapFromEntityList(dataFromJson)
    }

    private suspend fun getAllCollections(): List<SectionArticlesData> {
        val dataFromJson = articlesJsonUtil.getCollections()
        return collectionsMapper.mapFromEntityList(dataFromJson)
    }

    private suspend fun getAllDetails(): List<DetailData> {
        val dataFromJson = articlesJsonUtil.getDetails()
        return detailMapper.mapFromEntityList(dataFromJson)
    }

    ///// functions to use without database
    private suspend fun getArticleByIdFromJson(articleId: String): ArticleData {
        val articles = getAllArticles()
        val details = getAllDetails()
        val article = articles.first { it.id == articleId }

        return try {
            val detail = details.first { it.id == articleId }
            detailedArticleData(article, detail)
        } catch (e: NoSuchElementException) {
            article
        }
    }

    private suspend fun getArticlesBySectionIdFromJson(sectionId: String): List<ArticleData> {
        return try {
            val collection = getAllCollections().first { it.sectionId == sectionId }
            val articles = getAllArticles()
            val sectionArticles = articles.filter { article ->
                collection.articlesIdsList.contains(article.id)
            }
            sectionArticles
        } catch (e: Exception) {
            Timber.e("Section error: ${e.message}")
            emptyList()
        }
    }
}