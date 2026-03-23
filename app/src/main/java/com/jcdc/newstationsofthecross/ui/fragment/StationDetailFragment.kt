package com.jcdc.newstationsofthecross.ui.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jcdc.newstationsofthecross.R
import com.jcdc.newstationsofthecross.data.model.StationGridItem
import com.jcdc.newstationsofthecross.databinding.FragmentStationDetailBinding
import com.jcdc.newstationsofthecross.data.repository.StationRepository
import com.jcdc.newstationsofthecross.ui.viewmodel.StationViewModel
import com.jcdc.newstationsofthecross.ui.viewmodel.StationViewModelFactory

class StationDetailFragment : Fragment() {

    private var _binding: FragmentStationDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: StationViewModel
    private var currentItem: StationGridItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = StationRepository(requireContext())
        val factory = StationViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory)[StationViewModel::class.java]

        currentItem = arguments?.getSerializable("item") as? StationGridItem ?: return

        setupToolbar()
        setupFab()
        displayItem(currentItem!!)
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupFab() {
        binding.fabDone.setOnClickListener {
            currentItem?.let {
                viewModel.toggleStationCompleted(it.title)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.completedTitles.observe(viewLifecycleOwner) { completedTitles ->
            val isDone = completedTitles.contains(currentItem?.title)
            if (isDone) {
                binding.fabDone.text = "Completed"
                binding.fabDone.setIconResource(R.drawable.ic_done)
                binding.fabDone.extend()
            } else {
                binding.fabDone.text = "Mark as Done"
                binding.fabDone.setIconResource(R.drawable.ic_done)
                binding.fabDone.extend()
            }
        }
    }

    private fun displayItem(item: StationGridItem) {
        when (item) {
            is StationGridItem.IntroductionItem -> {
                val intro = item.introduction
                binding.toolbar.title = item.title
                binding.cvImageContainer.visibility = View.VISIBLE
                binding.ivDetailStation.setImageResource(R.drawable.conclusion)
                binding.ivDetailStation.transitionName = "station_image_intro"

                binding.tvDetailTitle.text = item.title

                val songContent = if (intro.openingSong != null) {
                    "${intro.openingSong.title}\n\n${intro.openingSong.lyrics}"
                } else {
                    ""
                }
                binding.tvReflectionOrText.text = songContent
                binding.labelReflection.text = "PAMBUNGAD NA AWIT"
                binding.labelReflection.visibility = if (songContent.isNotEmpty()) View.VISIBLE else View.GONE
                binding.tvReflectionOrText.visibility = if (songContent.isNotEmpty()) View.VISIBLE else View.GONE

                binding.tvPrayer.text = intro.openingPrayer
                binding.labelPrayer.text = "PAMBUNGAD NA PANALANGIN"
                binding.labelPrayer.visibility = View.VISIBLE
                binding.tvPrayer.visibility = View.VISIBLE

                binding.layoutStationOnly.visibility = View.GONE
            }
            is StationGridItem.StationItem -> {
                val station = item.station
                binding.toolbar.title = station.title
                binding.cvImageContainer.visibility = View.VISIBLE
                binding.labelReflection.text = "PAGNINILAY"
                binding.labelReflection.visibility = View.VISIBLE
                binding.tvReflectionOrText.visibility = View.VISIBLE
                binding.labelPrayer.text = "PANALANGIN"
                binding.labelPrayer.visibility = View.VISIBLE
                binding.tvPrayer.visibility = View.VISIBLE

                binding.ivDetailStation.transitionName = "station_image_${station.id}"

                val resourceId = requireContext().resources.getIdentifier(
                    "station_${station.id}",
                    "drawable",
                    requireContext().packageName
                )
                if (resourceId != 0) {
                    binding.ivDetailStation.setImageResource(resourceId)
                }

                binding.tvDetailTitle.text = station.title
                binding.tvCommonResponse.text = station.commonResponse
                binding.tvScriptureRef.text = station.scripture.reference
                binding.tvScriptureText.text = station.scripture.text
                binding.tvReflectionOrText.text = station.reflection
                binding.tvPrayer.text = station.prayer

                binding.layoutStationOnly.visibility = View.VISIBLE
            }
            is StationGridItem.ConclusionItem -> {
                val conclusion = item.conclusion
                binding.toolbar.title = conclusion.title
                binding.cvImageContainer.visibility = View.VISIBLE
                binding.ivDetailStation.setImageResource(R.drawable.conclusion)
                binding.ivDetailStation.transitionName = "station_image_conclusion"

                binding.tvDetailTitle.text = conclusion.title
                binding.tvReflectionOrText.text = conclusion.text

                binding.layoutStationOnly.visibility = View.GONE
                binding.labelReflection.text = "PANALANGIN"
                binding.labelReflection.visibility = View.VISIBLE
                binding.tvReflectionOrText.visibility = View.VISIBLE
                binding.labelPrayer.visibility = View.GONE
                binding.tvPrayer.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
