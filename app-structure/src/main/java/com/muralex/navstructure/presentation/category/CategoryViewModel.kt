package com.muralex.navstructure.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muralex.shared.domain.usecases.articles.GetSectionWithArticlesUseCase
import com.muralex.navstructure.presentation.category.CategoryContract.ModelAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val sectionWithArticlesUseCase: GetSectionWithArticlesUseCase,
) : ViewModel() {
    private val _viewState =
        MutableStateFlow<CategoryContract.ViewState>(CategoryContract.ViewState.EmptyList)
    val viewState: StateFlow<CategoryContract.ViewState> = _viewState.asStateFlow()

    private val _viewEffect: Channel<CategoryContract.ViewEffect> = Channel()
    val viewEffect = _viewEffect.receiveAsFlow()

    fun setIntent(intent: CategoryContract.ViewIntent) {
        val action = CategoryContract.intentToAction(intent)
        handleModelAction(action)
    }

    private fun setEffect(effect: CategoryContract.ViewEffect) = viewModelScope.launch {
        _viewEffect.send(effect)
    }

    private fun handleModelAction(modelAction: ModelAction) {
        when (modelAction) {
            is ModelAction.GetSection -> getData(modelAction.sectionID)
            is ModelAction.Navigate -> setEffect(
                CategoryContract.ViewEffect.Navigate(modelAction.article, modelAction.sectionID)
            )
        }
    }

    private fun getData(sectionID: String) {
        viewModelScope.launch {
            sectionWithArticlesUseCase(sectionID).collect { list ->
                if (list.articles.isEmpty()) _viewState.value = CategoryContract.ViewState.EmptyList
                else _viewState.value = CategoryContract.ViewState.ListLoaded(list)
            }
        }
    }
}

