package com.muralex.shared.domain.usecases.articles

import com.muralex.shared.domain.data.article.Article
import com.muralex.shared.domain.mappers.ArticleDataToArticleMapper
import com.muralex.shared.domain.repositories.ArticlesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetArticlesListUseCase @Inject constructor(
    private val repository: ArticlesRepository,
    private val mapper: ArticleDataToArticleMapper,
) {
    operator fun invoke(sectionId: String): Flow<List<Article>> {
        return flow {
            repository.getArticlesList(sectionId).collect {
                emit(mapper.mapFromEntityList(it))
            }
        }
    }
}