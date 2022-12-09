package com.muralex.navstructure.presentation.section

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
import com.muralex.navstructure.databinding.FragmentHomeBinding
import com.muralex.shared.domain.data.navstructure.Section
import com.muralex.navstructure.presentation.home.SectionsListAdapter
import com.muralex.navstructure.presentation.section.SectionContract.UserAction
import com.muralex.navstructure.presentation.section.SectionContract.ViewIntent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SectionFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SectionViewModel by viewModels()
    private lateinit var listAdapter: SectionsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNewsList()
        setUpObservation()
        val sectionID = arguments?.getString(Constants.SECTION_ARG_KEY) ?: ""
        processUiEvent(UserAction.LaunchScreen(sectionID))
    }

    private fun setUpObservation() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            renderViewState(state)
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
            is UserAction.LaunchScreen -> setIntent(ViewIntent.GetSection(userAction.sectionID))
            is UserAction.ListItemClick -> setIntent(ViewIntent.Navigate(userAction.section))
        }
    }

    private fun setIntent(intent: ViewIntent) {
        viewModel.setIntent(intent)
    }

    private fun renderViewState(state: SectionContract.ViewState) {
        when (state) {
            is SectionContract.ViewState.EmptyList -> {}
            is SectionContract.ViewState.ListLoaded -> {
                setToolbarTitle(state.data.section.title)
                refreshList(state.data.subSections)
            }
        }
    }

    private fun renderViewEffect(effect: SectionContract.ViewEffect) {
        when (effect) {
            is SectionContract.ViewEffect.Navigate -> navigateToDetail(effect.section)
        }
    }

    private fun refreshList(data: List<Section>?) {
        listAdapter.submitList(data)
//        if (data.isNullOrEmpty()) emptyUI()
//        else notEmptyUI()
    }

    private fun navigateToDetail(item: Section) {
        val bundle = bundleOf(Constants.SECTION_ARG_KEY to item.id)
        findNavController().navigate(R.id.action_sectionFragment_to_SecondFragment, bundle)
    }

    private fun setToolbarTitle(title: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}