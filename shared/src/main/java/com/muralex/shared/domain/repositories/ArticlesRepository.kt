package com.muralex.shared.domain.repositories

import android.os.Bundle
import com.muralex.shared.data.model.articles.ArticleData
import com.muralex.shared.data.model.structure.SectionDataWithArticles
import kotlinx.coroutines.flow.Flow

interface ArticlesRepository {
    suspend fun getSectionDataWithArticles(parentID: String): Flow<SectionDataWithArticles>
    fun getArticlesList(sectionID: String): Flow<List<ArticleData>>
    suspend fun getArticleById(articleID: String): ArticleData
    suspend fun getArticleNeighbors(articleId: String, sectionID: String): Bundle

}