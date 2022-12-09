package com.muralex.shared.domain.usecases.articles

import com.muralex.shared.domain.data.article.Article
import com.muralex.shared.domain.mappers.ArticleDataToArticleMapper
import com.muralex.shared.domain.repositories.ArticlesRepository
import javax.inject.Inject

class GetArticleUseCase @Inject constructor(
    private val repository: ArticlesRepository,
    private val mapper: ArticleDataToArticleMapper,
) {
    suspend operator fun invoke(articleId: String): Article {
        return mapper.mapFromEntity(repository.getArticleById(articleId))
    }
}