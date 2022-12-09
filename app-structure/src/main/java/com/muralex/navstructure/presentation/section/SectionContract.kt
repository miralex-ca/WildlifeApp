package com.muralex.navstructure.presentation.section

import com.muralex.shared.domain.data.navstructure.Section
import com.muralex.shared.domain.data.navstructure.SectionWithChildren
import com.muralex.navstructure.presentation.utils.UiEffect
import com.muralex.navstructure.presentation.utils.UiEvent
import com.muralex.navstructure.presentation.utils.UiIntent
import com.muralex.navstructure.presentation.utils.UiState

class SectionContract {

    sealed class UserAction : UiEvent {
        data class LaunchScreen(val sectionID: String) : UserAction()
        data class ListItemClick(val section: Section) : UserAction()
    }

    sealed class ViewIntent : UiIntent {
        data class GetSection(val sectionID: String) : ViewIntent()
        data class Navigate(val section: Section) : ViewIntent()
    }

    companion object {
        fun intentToAction(intent: ViewIntent): ModelAction {
            return when (intent) {
                is ViewIntent.GetSection -> ModelAction.GetSection(intent.sectionID)
                is ViewIntent.Navigate -> ModelAction.Navigate(intent.section)
            }
        }
    }

    sealed class ModelAction {
        data class GetSection(val sectionID: String) : ModelAction()
        data class Navigate(val section: Section) : ModelAction()
    }

    sealed class ViewState : UiState {
        object EmptyList : ViewState()
        data class ListLoaded(val data: SectionWithChildren) : ViewState()
    }

    sealed class ViewEffect : UiEffect {
        data class Navigate(val section: Section) : ViewEffect()
    }

}