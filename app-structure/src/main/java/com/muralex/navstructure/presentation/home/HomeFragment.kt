package com.muralex.navstructure.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muralex.navstructure.R
import com.muralex.shared.app.utils.Constants
import com.muralex.shared.app.utils.Constants.SectionType
import com.muralex.shared.app.utils.hasType
import com.muralex.navstructure.databinding.FragmentHomeBinding
import com.muralex.shared.domain.data.navstructure.Section
import com.muralex.navstructure.presentation.home.HomeContract.UserAction
import com.muralex.navstructure.presentation.home.HomeContract.ViewIntent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var listAdapter: SectionsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNewsList()
        setUpObservation()
        processUiEvent(UserAction.LaunchScreen)
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

    private fun setupNewsList() {
        listAdapter = SectionsListAdapter()

        listAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        listAdapter.setOnItemClickListener { _, item ->
            processUiEvent(UserAction.ListItemClick(item))
        }

        binding.rvList.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = listAdapter
        }
    }

    private fun processUiEvent(userAction: UserAction) {
        when (userAction) {
            is UserAction.LaunchScreen -> setIntent(ViewIntent.GetData)
            is UserAction.ListItemClick -> setIntent(ViewIntent.Navigate(userAction.section))
        }
    }

    private fun setIntent(intent: ViewIntent) {
        viewModel.setIntent(intent)
    }

    private fun renderViewState(state: HomeContract.ViewState) {
        when (state) {
            is HomeContract.ViewState.EmptyList -> {}
            is HomeContract.ViewState.ListLoaded -> refreshList(state.data)
        }
    }

    private fun renderViewEffect(effect: HomeContract.ViewEffect) {
        when (effect) {
            is HomeContract.ViewEffect.Navigate -> navigateTo(effect.section)
        }
    }

    private fun refreshList(data: List<Section>?) {
        listAdapter.submitList(data)
//        if (data.isNullOrEmpty()) emptyUI()
//        else notEmptyUI()
    }

    private fun navigateTo(section: Section) = lifecycleScope.launch {
        val bundle = bundleOf(Constants.SECTION_ARG_KEY to section.id)
        if (section.hasType(SectionType.SECTIONS)) {
            findNavController().navigate(R.id.action_homeFragment_to_sectionFragment, bundle)
        } else if (section.hasType(SectionType.ITEMS)) {
            findNavController().navigate(R.id.action_HomeFragment_to_categoryFragment, bundle)
        }
    }

    private fun isSectionType(section: Section) =
        section.type.lowercase() == SectionType.ITEMS.name.lowercase()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


