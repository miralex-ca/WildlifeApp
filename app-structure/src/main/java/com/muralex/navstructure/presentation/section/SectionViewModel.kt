package com.muralex.navstructure.presentation.section

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muralex.shared.app.utils.Dispatchers
import com.muralex.shared.domain.usecases.structure.GetSectionWithChildrenUseCase
import com.muralex.navstructure.presentation.section.SectionContract.ModelAction
import com.muralex.navstructure.presentation.section.SectionContract.ViewEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SectionViewModel @Inject constructor(
    private val sectionWithChildrenUseCase: GetSectionWithChildrenUseCase,
    private val dispatchers: Dispatchers
) : ViewModel() {

    private val _viewState = MutableLiveData<SectionContract.ViewState>()
    val viewState: LiveData<SectionContract.ViewState>
        get() = _viewState

    private val _viewEffect: Channel<ViewEffect> = Channel()
    val viewEffect = _viewEffect.receiveAsFlow()

    fun setIntent(intent: SectionContract.ViewIntent) {
        val action = SectionContract.intentToAction(intent)
        handleModelAction(action)
    }

    private fun setEffect(effect: ViewEffect) = viewModelScope.launch {
        _viewEffect.send(effect)
    }

    private fun handleModelAction(modelAction: ModelAction) {
        when (modelAction) {
            is ModelAction.GetSection -> getData(modelAction.sectionID)
            is ModelAction.Navigate -> setEffect(ViewEffect.Navigate(modelAction.section))
        }
    }

    private fun getData(sectionID: String) {
        dispatchers.launchBackground(viewModelScope) {
            val list = sectionWithChildrenUseCase(sectionID)
            if (list.subSections.isEmpty()) _viewState.postValue(SectionContract.ViewState.EmptyList)
            else _viewState.postValue(SectionContract.ViewState.ListLoaded(list))
        }
    }

}

