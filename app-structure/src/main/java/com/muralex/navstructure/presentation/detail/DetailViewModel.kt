package com.muralex.navstructure.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muralex.shared.app.utils.SettingsManager
import com.muralex.shared.domain.usecases.articles.GetDetailArticleUseCase
import com.muralex.navstructure.presentation.detail.DetailContract.ModelAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailArticleUseCase: GetDetailArticleUseCase,
    private val settingsManager: SettingsManager,
) : ViewModel() {

    private var currentArticleId: String? = null
    private var articleSectionId: String? = null

    private val _viewState =
        MutableStateFlow<DetailContract.ViewState>(DetailContract.ViewState.Idle)
    val viewState: StateFlow<DetailContract.ViewState> = _viewState.asStateFlow()

    private val _viewEffect: Channel<DetailContract.ViewEffect> = Channel()
    val viewEffect = _viewEffect.receiveAsFlow()

    fun setIntent(intent: DetailContract.ViewIntent) {
        val action = DetailContract.intentToAction(intent)
        handleModelAction(action)
    }

    private fun setEffect(effect: DetailContract.ViewEffect) = viewModelScope.launch {
        _viewEffect.send(effect)
    }

    private fun handleModelAction(modelAction: ModelAction) {
        when (modelAction) {
            is ModelAction.ChangeNavigationSetting -> changeNavigationDisplay()
            is ModelAction.GetArticle -> {
                if (currentArticleId == null) currentArticleId = modelAction.articleID
                if (articleSectionId == null) articleSectionId = modelAction.sectionID
                getData()
            }

            is ModelAction.ReplaceToArticle -> {
                currentArticleId = modelAction.articleID
                articleSectionId = modelAction.sectionID
                getData()
            }
        }
    }

    private fun changeNavigationDisplay() {
        val display = !settingsManager.isDetailNavigationEnabled()
        settingsManager.setDetailNavigation(display)
        setEffect(DetailContract.ViewEffect.DisplayNavigation(display))
    }

    private fun getData() {
        viewModelScope.launch {
            currentArticleId?.let {
                val articleDetail = getDetailArticleUseCase(it, articleSectionId ?: "")
                if (articleDetail.article.id.isBlank()) _viewState.value =
                    DetailContract.ViewState.Idle
                else _viewState.value = DetailContract.ViewState.ArticleLoaded(articleDetail)
            }
        }
    }

}

