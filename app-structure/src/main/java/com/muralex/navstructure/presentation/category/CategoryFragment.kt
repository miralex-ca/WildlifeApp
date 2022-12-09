package com.muralex.navstructure.presentation.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muralex.navstructure.R
import com.muralex.shared.app.utils.Constants
import com.muralex.navstructure.databinding.FragmentCategoryBinding
import com.muralex.shared.domain.data.article.Article
import com.muralex.navstructure.presentation.category.CategoryContract.UserAction
import com.muralex.navstructure.presentation.category.CategoryContract.ViewIntent
import com.muralex.navstructure.presentation.utils.DelayProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by viewModels()
    private lateinit var listAdapter: ArticlesListAdapter

    @Inject
    lateinit var delayProvider: DelayProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sectionID = arguments?.getString(Constants.SECTION_ARG_KEY) ?: ""
        setupNewsList(sectionID)
        setUpObservation()
        processUiEvent(UserAction.LaunchScreen(sectionID))
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

    private fun setupNewsList(sectionID: String) {
        listAdapter = ArticlesListAdapter()

        listAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        listAdapter.setOnItemClickListener { _, item ->
            processUiEvent(UserAction.ListItemClick(item, sectionID))
        }

        binding.rvList.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = listAdapter
        }
    }

    private fun processUiEvent(userAction: UserAction) {
        when (userAction) {
            is UserAction.LaunchScreen -> setIntent(ViewIntent.GetSection(userAction.sectionID))
            is UserAction.ListItemClick -> setIntent(ViewIntent.Navigate(userAction.article,
                userAction.sectionID))
        }
    }

    private fun setIntent(intent: ViewIntent) {
        viewModel.setIntent(intent)
    }

    private fun renderViewState(state: CategoryContract.ViewState) {
        when (state) {
            is CategoryContract.ViewState.EmptyList -> {}
            is CategoryContract.ViewState.ListLoaded -> {
                setToolbarTitle(state.data.section.title)
                refreshList(state.data.articles)
            }
        }
    }

    private fun renderViewEffect(effect: CategoryContract.ViewEffect) {
        when (effect) {
            is CategoryContract.ViewEffect.Navigate -> navigateToDetail(effect.article,
                effect.sectionID)
        }
    }

    private fun refreshList(data: List<Article>?) {
        listAdapter.submitList(data)

    }

    private fun navigateToDetail(item: Article, sectionID: String) = lifecycleScope.launch {
        val bundle = bundleOf(
            Constants.ARTICLE_ARG_KEY to item.id,
            Constants.SECTION_ARG_KEY to sectionID
        )
        delayProvider.delayTransition()
        findNavController().navigate(R.id.action_categoryFragment_to_detailFragment, bundle)
    }

    private fun setToolbarTitle(title: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}