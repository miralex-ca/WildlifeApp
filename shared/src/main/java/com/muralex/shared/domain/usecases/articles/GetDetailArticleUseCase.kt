package com.muralex.shared.domain.usecases.articles

import com.muralex.shared.app.utils.Dispatchers
import com.muralex.shared.app.utils.SettingsManager
import com.muralex.shared.domain.data.article.DetailArticleUI
import com.muralex.shared.domain.mappers.ArticleDataToArticleMapper
import com.muralex.shared.domain.repositories.ArticlesRepository
import kotlinx.coroutines.async
import javax.inject.Inject

class GetDetailArticleUseCase @Inject constructor(
    private val repository: ArticlesRepository,
    private val mapper: ArticleDataToArticleMapper,
    private val settingsManager: SettingsManager,
    private val dispatcher: Dispatchers,

    ) {
    suspend operator fun invoke(articleId: String, sectionID: String) = dispatcher.background {
        val displayNavigation = settingsManager.isDetailNavigationEnabled()
        val neighborsAsync = async {repository.getArticleNeighbors(articleId, sectionID)}
        val articleAsync = async { repository.getArticleById(articleId)}

        val neighbors = neighborsAsync.await()
        val article = articleAsync.await()

        DetailArticleUI(
            article = mapper.mapFromEntity(article),
            sectionId = sectionID,
            previousId = neighbors.getString("prev"),
            nextId = neighbors.getString("next"),
            displayNavigation = displayNavigation
        )
    }
}