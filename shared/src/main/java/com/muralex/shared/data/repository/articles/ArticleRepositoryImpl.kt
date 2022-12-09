package com.muralex.shared.data.repository.articles

import android.os.Bundle
import androidx.core.os.bundleOf
import com.muralex.shared.data.model.articles.ArticleData
import com.muralex.shared.data.model.structure.SectionDataWithArticles
import com.muralex.shared.data.repository.articles.datasource.ArticlesDataSource
import com.muralex.shared.data.repository.navstructure.datasource.NavDataSource
import com.muralex.shared.domain.repositories.ArticlesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArticleRepositoryImpl(
    private val dataSource: ArticlesDataSource,
    private val navDataSource: NavDataSource,
) : ArticlesRepository {

    override suspend fun getSectionDataWithArticles(parentID: String): Flow<SectionDataWithArticles> {
        val section = navDataSource.getSectionById(parentID)
        return flow {
            dataSource.getSectionArticles(parentID).collect {
                emit(SectionDataWithArticles(section, it))
            }
        }
    }

    override fun getArticlesList(sectionID: String): Flow<List<ArticleData>> {
        return dataSource.getSectionArticles(sectionID)
    }

    override suspend fun getArticleById(articleID: String): ArticleData {
        return dataSource.getArticleById(articleID)
    }

    override suspend fun getArticleNeighbors(articleId: String, sectionID: String): Bundle {
        val sectionArticlesList = dataSource.getSectionArticlesList(sectionID)
        val article = sectionArticlesList.find { it.id == articleId }
        val articleIndex = sectionArticlesList.indexOf(article)

        val previous = previousItem(sectionArticlesList, articleIndex)
        val next = nextItem(sectionArticlesList, articleIndex)

        return bundleOf("prev" to previous, "next" to next)

    }

    private fun previousItem(sectionArticlesList: List<ArticleData>, articleIndex: Int): String? {
        val iterator = sectionArticlesList.listIterator(articleIndex)
        return if (iterator.hasPrevious()) iterator.previous().id else null
    }

    private fun nextItem(sectionArticlesList: List<ArticleData>, articleIndex: Int): String? {
        val iterator = sectionArticlesList.listIterator(articleIndex)
        iterator.next()
        return if (iterator.hasNext()) iterator.next().id else null
    }

}