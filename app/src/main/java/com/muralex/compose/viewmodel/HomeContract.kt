package com.muralex.compose.viewmodel

import com.muralex.shared.domain.data.navstructure.Section

class HomeContract {

    sealed class UserAction {
        object LaunchScreen : UserAction()
        data class ListItemClick(val section: Section) : UserAction()
    }

    sealed class ViewIntent {
        object GetData : ViewIntent()
        data class Navigate(val section: Section) : ViewIntent()
    }

    companion object {
        fun intentToAction(intent: ViewIntent): ModelAction {
            return when (intent) {
                is ViewIntent.GetData -> ModelAction.GetSections
                is ViewIntent.Navigate -> ModelAction.Navigate(intent.section)
            }
        }
    }

    sealed class ModelAction {
        object GetSections : ModelAction()
        data class Navigate(val section: Section) : ModelAction()
    }

    sealed class ViewState  {
        object EmptyList : ViewState()
        data class ListLoaded(val data: List<Section>) : ViewState()
    }

    sealed class ViewEffect {
        data class Navigate(val section: Section) : ViewEffect()
    }

}