package com.jcdc.newstationsofthecross.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jcdc.newstationsofthecross.R
import com.jcdc.newstationsofthecross.databinding.FragmentStationListBinding
import com.jcdc.newstationsofthecross.data.repository.StationRepository
import com.jcdc.newstationsofthecross.ui.adapter.StationAdapter
import com.jcdc.newstationsofthecross.ui.viewmodel.StationViewModel
import com.jcdc.newstationsofthecross.ui.viewmodel.StationViewModelFactory

class StationListFragment : Fragment() {

    private var _binding: FragmentStationListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: StationViewModel
    private lateinit var adapter: StationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = StationRepository(requireContext())
        val factory = StationViewModelFactory(repository)
        // Use activity-scoped ViewModel to share state with DetailFragment
        viewModel = ViewModelProvider(requireActivity(), factory)[StationViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        viewModel.loadStations()
    }

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.main_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_reset -> {
                    viewModel.clearAllProgress()
                    Toast.makeText(requireContext(), "Progress reset", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = StationAdapter(emptyList(), emptySet()) { item, sharedView ->
            val bundle = Bundle().apply {
                putSerializable("item", item)
            }
            val extras = FragmentNavigatorExtras(
                sharedView to sharedView.transitionName
            )
            findNavController().navigate(
                R.id.action_stationListFragment_to_stationDetailFragment,
                bundle,
                null,
                extras
            )
        }
        binding.rvStations.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvStations.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.gridItems.observe(viewLifecycleOwner) { items ->
            adapter.updateItems(items)
        }
        viewModel.completedTitles.observe(viewLifecycleOwner) { completedTitles ->
            adapter.updateCompletedTitles(completedTitles)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
