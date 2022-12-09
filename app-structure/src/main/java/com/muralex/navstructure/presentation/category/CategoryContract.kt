package com.muralex.navstructure.presentation.category

import com.muralex.shared.domain.data.article.Article
import com.muralex.shared.domain.data.article.SectionWithArticles
import com.muralex.navstructure.presentation.utils.UiEffect
import com.muralex.navstructure.presentation.utils.UiEvent
import com.muralex.navstructure.presentation.utils.UiIntent
import com.muralex.navstructure.presentation.utils.UiState

class CategoryContract {

    sealed class UserAction : UiEvent {
        data class LaunchScreen(val sectionID: String) : UserAction()
        data class ListItemClick(val article: Article, val sectionID: String) : UserAction()
    }

    sealed class ViewIntent : UiIntent {
        data class GetSection(val sectionID: String) : ViewIntent()
        data class Navigate(val article: Article, val sectionID: String) : ViewIntent()
    }

    sealed class ModelAction {
        data class GetSection(val sectionID: String) : ModelAction()
        data class Navigate(val article: Article, val sectionID: String) : ModelAction()
    }

    companion object {
        fun intentToAction(intent: ViewIntent): ModelAction {
            return when (intent) {
                is ViewIntent.GetSection -> ModelAction.GetSection(intent.sectionID)
                is ViewIntent.Navigate -> ModelAction.Navigate(intent.article, intent.sectionID)
            }
        }
    }

    sealed class ViewState : UiState {
        object EmptyList : ViewState()
        data class ListLoaded(val data: SectionWithArticles) : ViewState()
    }

    sealed class ViewEffect : UiEffect {
        data class Navigate(val article: Article, val sectionID: String) : ViewEffect()
    }

}