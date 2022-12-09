package com.muralex.shared.domain.mappers

import com.muralex.shared.app.data.EntityMapper
import com.muralex.shared.data.model.articles.ArticleData
import com.muralex.shared.domain.data.article.Article
import javax.inject.Inject

class ArticleDataToArticleMapper @Inject constructor() : EntityMapper<ArticleData, Article> {

    override fun mapFromEntity(data: ArticleData): Article {
        return Article(
            id = data.id,
            title = data.title,
            desc = data.desc,
            text = data.text,
            image = data.image,
        )
    }

    fun mapFromEntityList(entitiesList: List<ArticleData>): List<Article> {
        return entitiesList.map { mapFromEntity(it) }
    }

}
