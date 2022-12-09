package com.muralex.compose.viewmodel

import com.muralex.shared.domain.usecases.structure.GetSectionsListUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muralex.compose.viewmodel.HomeContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSectionsListUseCase: GetSectionsListUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.EmptyList)
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    private val _viewEffect: Channel<ViewEffect> = Channel()
    val viewEffect = _viewEffect.receiveAsFlow()

    fun setIntent(intent: ViewIntent) {
        val action = HomeContract.intentToAction(intent)
        handleModelAction(action)
    }

    private fun setEffect(effect: ViewEffect) = viewModelScope.launch {
        _viewEffect.send(effect)
    }

    private fun handleModelAction(modelAction: ModelAction) {
        when (modelAction) {
            is ModelAction.GetSections -> getData()
            is ModelAction.Navigate -> setEffect(ViewEffect.Navigate(modelAction.section))
        }
    }

    private fun getData() = viewModelScope.launch {
        val list = getSectionsListUseCase()
        if (list.isEmpty()) _viewState.value = ViewState.EmptyList
        else _viewState.value = (ViewState.ListLoaded(list))
    }


}

