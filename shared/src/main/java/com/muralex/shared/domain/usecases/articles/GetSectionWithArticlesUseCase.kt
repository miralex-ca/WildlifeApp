package com.muralex.shared.domain.usecases.articles

import com.muralex.shared.app.utils.Dispatchers
import com.muralex.shared.app.utils.SettingsManager
import com.muralex.shared.domain.data.article.SectionWithArticles
import com.muralex.shared.domain.mappers.ArticleDataToArticleMapper
import com.muralex.shared.domain.mappers.SectionDataToSectionMapper
import com.muralex.shared.domain.repositories.ArticlesRepository
import com.muralex.shared.app.utils.DataDelayProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSectionWithArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository,
    private val mapper: SectionDataToSectionMapper,
    private val articlesMapper: ArticleDataToArticleMapper,
    private val dispatcher: Dispatchers,
    private val settingsManager: SettingsManager,
    private val dataDelayProvider: DataDelayProvider
    ) {
    suspend operator fun invoke(sectionID: String): Flow<SectionWithArticles> = dispatcher.background {

        if (settingsManager.isFirstLaunch()) {
            dataDelayProvider.delayLoading()
            settingsManager.setFirstLaunch(false)
        }

         flow {
            repository.getSectionDataWithArticles(sectionID).collect { sectionData ->
                val section = mapper.mapFromEntity(sectionData.section)
                val articles = articlesMapper.mapFromEntityList(sectionData.articles)
                emit(SectionWithArticles(section, articles))
            }
        }
    }
}