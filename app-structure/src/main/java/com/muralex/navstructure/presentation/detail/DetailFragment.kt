package com.muralex.navstructure.presentation.detail

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.muralex.navstructure.R
import com.muralex.navstructure.app.utils.displayIf
import com.muralex.shared.app.utils.Constants
import com.muralex.navstructure.databinding.FragmentDetailBinding
import com.muralex.shared.domain.data.article.DetailArticleUI
import com.muralex.navstructure.presentation.detail.DetailContract.UserAction
import com.muralex.navstructure.presentation.detail.DetailContract.ViewIntent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private var navigationMenuItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setUpObservation()
        val sectionID = arguments?.getString(Constants.SECTION_ARG_KEY) ?: ""
        val articleID = arguments?.getString(Constants.ARTICLE_ARG_KEY) ?: ""
        processUiEvent(UserAction.LaunchScreen(articleID, sectionID))
    }

    private fun setUpObservation() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { uiState ->
                renderViewState(uiState)
            }
        }

        viewModel.viewEffect.onEach {
            renderViewEffect(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun processUiEvent(userAction: UserAction) {
        when (userAction) {
            is UserAction.LaunchScreen -> setIntent(ViewIntent.GetArticle(userAction.articleID,
                userAction.sectionID))
            is UserAction.NextButtonClick -> setIntent(ViewIntent.GetPreviousArticle(userAction.nextID,
                userAction.sectionID))
            is UserAction.PreviousButtonClick -> setIntent(ViewIntent.GetPreviousArticle(userAction.previousID,
                userAction.sectionID))
            is UserAction.NavigationSettingSelected -> setIntent(ViewIntent.ChangeNavigationSetting)
        }
    }

    private fun setIntent(intent: ViewIntent) {
        viewModel.setIntent(intent)
    }

    private fun renderViewState(state: DetailContract.ViewState) {
        when (state) {
            is DetailContract.ViewState.Idle -> {}
            is DetailContract.ViewState.ArticleLoaded -> {
                setToolbarTitle(state.data.article.title)
                refreshUI(state.data)
                setButtonsListeners(state.data)
                setNavigationMenuItemTitle(state.data.displayNavigation)
            }
        }
    }

    private fun setButtonsListeners(data: DetailArticleUI) {
        binding.btnNext.setOnClickListener {
            processUiEvent(UserAction.NextButtonClick(data.nextId, data.sectionId))
        }
        binding.btnPrev.setOnClickListener {
            processUiEvent(UserAction.PreviousButtonClick(data.previousId, data.sectionId))
        }
    }

    private fun renderViewEffect(effect: DetailContract.ViewEffect) {
        when (effect) {
            is DetailContract.ViewEffect.DisplayNavigation -> displayNavigation(effect.display)
        }
    }

    private fun displayNavigation(display: Boolean) {
        binding.detailNavigation.displayIf(display)
        setNavigationMenuItemTitle(display)
    }

    private fun refreshUI(data: DetailArticleUI?) {
        binding.article = data
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
    }

    private fun setToolbarTitle(title: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
    }

    private fun setNavigationMenuItemTitle(navigationDisplay: Boolean) {
        navigationMenuItem?.title =
            if (navigationDisplay) getString(R.string.menu_detail_navigation_off)
            else getString(R.string.menu_detail_navigation_on)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu_detail, menu)
                navigationMenuItem = menu.findItem(R.id.action_navigation_setting)
                checkNavigationStatus()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_navigation_setting -> {
                        processUiEvent(UserAction.NavigationSettingSelected)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun checkNavigationStatus() {
        val state = viewModel.viewState.value
        if (state is DetailContract.ViewState.ArticleLoaded) {
            setNavigationMenuItemTitle(state.data.displayNavigation)
        }
    }
}