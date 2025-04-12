package com.bangkit.dicodingevent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dicodingevent.R
import com.bangkit.dicodingevent.databinding.FragmentHomeBinding
import com.bangkit.dicodingevent.ui.adapter.FinishedListAdapter
import com.bangkit.dicodingevent.ui.adapter.UpcomingPreviewAdapter
import com.bangkit.dicodingevent.ui.viewmodel.EventViewModel
import com.bangkit.dicodingevent.ui.viewmodelfactory.ViewModelFactory
import com.bangkit.dicodingevent.utils.ResponseState
import com.bangkit.dicodingevent.utils.ToDetail
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val factory = ViewModelFactory.getInstance()
    private val eventViewModel by viewModels<EventViewModel> { factory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUpcomingPreview()
        loadFinishedPreview()

        binding.setting.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_settingActivity)
        }

    }

    private fun loadFinishedPreview(){
        eventViewModel.getFinished(5).apply {
            eventViewModel.finished.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is ResponseState.Error -> {
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG)
                            .setAction("Refresh") {
                                loadFinishedPreview()
                            }
                            .show()
                    }

                    is ResponseState.Loading -> {
                        binding.finishedPlaceholder.apply {
                            this.startShimmer()
                            this.visibility = View.VISIBLE
                        }
                        binding.rvFinishedPreview.visibility = View.GONE
                    }

                    is ResponseState.Success -> {
                        binding.finishedPlaceholder.apply {
                            this.stopShimmer()
                            this.visibility = View.GONE
                        }
                        binding.rvFinishedPreview.apply {
                            val finishedAdapter = FinishedListAdapter(state.data.listEvents)

                            this.visibility = View.VISIBLE
                            this.adapter = finishedAdapter
                            this.layoutManager = LinearLayoutManager(activity)

                            finishedAdapter.onEventClicked(object :
                                FinishedListAdapter.OnEventClick {
                                override fun onUpcomingEventClick(id: Int) {
                                    ToDetail.toDetail(requireActivity(), id, false)
                                }

                            })
                        }
                    }
                }
            }
        }
    }

    private fun loadUpcomingPreview(){
        eventViewModel.getUpcoming(5).apply {
            eventViewModel.upcoming.observe(viewLifecycleOwner){state->
                when(state){
                    is ResponseState.Error -> {
                        Snackbar.make(binding.root,state.message,Snackbar.LENGTH_LONG)
                            .setAction("Refresh"){
                                loadUpcomingPreview()
                            }
                            .show()
                    }
                    is ResponseState.Loading -> {
                        binding.upcomingPreviewEventPlaceholder.apply {
                            this.startShimmer()
                            this.visibility = View.VISIBLE
                        }
                        binding.rvUpcomingEvent.visibility = View.GONE
                    }
                    is ResponseState.Success -> {
                        binding.upcomingPreviewEventPlaceholder.apply {
                            this.stopShimmer()
                            this.visibility = View.GONE
                        }
                        binding.rvUpcomingEvent.apply {
                            val upcomingAdapter = UpcomingPreviewAdapter(state.data.listEvents)

                            this.visibility = View.VISIBLE
                            this.adapter = upcomingAdapter
                            this.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

                            upcomingAdapter.onEventClicked(object: UpcomingPreviewAdapter.OnEventClick{
                                override fun onUpcomingEventClick(id: Int) {
                                    ToDetail.toDetail(requireActivity(),id,true)
                                }

                            })

                        }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}