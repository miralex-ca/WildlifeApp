package com.muralex.navstructure.presentation.detail

import com.muralex.shared.domain.data.article.DetailArticleUI
import com.muralex.navstructure.presentation.utils.UiEffect
import com.muralex.navstructure.presentation.utils.UiEvent
import com.muralex.navstructure.presentation.utils.UiIntent
import com.muralex.navstructure.presentation.utils.UiState

class DetailContract {

    sealed class UserAction : UiEvent {
        data class LaunchScreen(val articleID: String, val sectionID: String) : UserAction()
        data class PreviousButtonClick(val previousID: String?, val sectionID: String) :
            UserAction()
        data class NextButtonClick(val nextID: String?, val sectionID: String) : UserAction()
        object NavigationSettingSelected : UserAction()
    }

    sealed class ViewIntent : UiIntent {
        data class GetArticle(val articleID: String, val sectionID: String) : ViewIntent()
        data class GetPreviousArticle(val articleID: String?, val sectionID: String) : ViewIntent()
        data class GetNextArticle(val articleID: String?, val sectionID: String) : ViewIntent()
        object ChangeNavigationSetting : ViewIntent()
    }

    sealed class ModelAction {
        data class GetArticle(val articleID: String?, val sectionID: String) : ModelAction()
        data class ReplaceToArticle(val articleID: String?, val sectionID: String) : ModelAction()
        object ChangeNavigationSetting : ModelAction()
    }

    companion object {
        fun intentToAction(intent: ViewIntent): ModelAction {
            return when (intent) {
                is ViewIntent.GetArticle -> ModelAction.GetArticle(intent.articleID,
                    intent.sectionID)
                is ViewIntent.GetNextArticle -> ModelAction.ReplaceToArticle(intent.articleID,
                    intent.sectionID)
                is ViewIntent.GetPreviousArticle -> ModelAction.ReplaceToArticle(intent.articleID,
                    intent.sectionID)
                ViewIntent.ChangeNavigationSetting -> ModelAction.ChangeNavigationSetting
            }
        }
    }

    sealed class ViewState : UiState {
        object Idle : ViewState()
        data class ArticleLoaded(val data: DetailArticleUI) : ViewState()
    }

    sealed class ViewEffect : UiEffect {
        data class DisplayNavigation(val display: Boolean) : ViewEffect()
    }

}