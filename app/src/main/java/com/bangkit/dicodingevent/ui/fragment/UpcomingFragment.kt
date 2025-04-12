package com.bangkit.dicodingevent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dicodingevent.databinding.FragmentUpcomingBinding
import com.bangkit.dicodingevent.ui.adapter.UpcomingListAdapter
import com.bangkit.dicodingevent.ui.viewmodel.EventViewModel
import com.bangkit.dicodingevent.ui.viewmodel.SearchViewModel
import com.bangkit.dicodingevent.ui.viewmodelfactory.SearchViewModelFactory
import com.bangkit.dicodingevent.ui.viewmodelfactory.ViewModelFactory
import com.bangkit.dicodingevent.utils.ResponseState
import com.bangkit.dicodingevent.utils.ToDetail
import com.google.android.material.snackbar.Snackbar

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val factory = ViewModelFactory.getInstance()
    private val searchFactory = SearchViewModelFactory.getInstance()
    private val upcomingViewModel by viewModels<EventViewModel> { factory }
    private val searchViewModel by viewModels<SearchViewModel>{ searchFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.upcomingSearchView.apply {
            this.setupWithSearchBar(binding.searchBar)
            this.editText.let {
                it.setOnEditorActionListener { _, _, _ ->
                    initSearch(this.text.toString())
                    false
                }
                it.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus){
                        resetSearch()
                    }
                }
            }
        }

        upcomingViewModel.getUpcoming(null)
        loadUpcoming()

    }


    private fun resetSearch(){
        binding.error.visibility = View.GONE
        upcomingViewModel.getFinished(null).apply {
            upcomingViewModel.finished.observe(viewLifecycleOwner){state->
                when(state){
                    is ResponseState.Success -> {
                        binding.error.visibility = View.GONE
                        binding.upcomingSearchRv.apply {
                            this.adapter = UpcomingListAdapter(state.data.listEvents)
                        }
                    }else->{
                    binding.error.visibility = View.VISIBLE
                }
                }
            }
        }
    }

    private fun initSearch(query: String){
        searchViewModel.searchEvent(query,true).apply {
            searchViewModel.search.observe(viewLifecycleOwner){state->
                when(state){
                    is ResponseState.Error -> {
                        binding.upcomingSearchPlaceholder.apply {
                            this.visibility = View.GONE
                            this.stopShimmer()
                        }
                        binding.upcomingSearchRv.visibility = View.GONE
                        binding.error.visibility = View.VISIBLE
                    }
                    is ResponseState.Loading -> {
                        binding.upcomingSearchPlaceholder.apply {
                            this.visibility = View.VISIBLE
                            this.startShimmer()
                        }
                        binding.upcomingSearchRv.visibility = View.GONE
                        binding.error.visibility = View.GONE
                    }
                    is ResponseState.Success -> {
                        if (state.data.listEvents.isEmpty()){
                            binding.upcomingSearchPlaceholder.apply {
                                this.visibility = View.GONE
                                this.stopShimmer()
                            }
                            binding.upcomingSearchRv.visibility = View.GONE
                            binding.error.visibility = View.VISIBLE
                        }else{
                            binding.upcomingSearchPlaceholder.apply {
                                this.visibility = View.GONE
                                this.stopShimmer()
                            }
                            binding.error.visibility = View.GONE
                            binding.upcomingSearchRv.apply {
                                val searchAdapter = UpcomingListAdapter(state.data.listEvents)

                                this.visibility = View.VISIBLE
                                this.adapter = searchAdapter
                                this.layoutManager = LinearLayoutManager(activity)

                                searchAdapter.onEventClicked(object : UpcomingListAdapter.OnEventClick{
                                    override fun onUpcomingEventClick(id: Int) {
                                        ToDetail.toDetail(requireActivity(),id,false)
                                    }

                                })
                            }
                        }

                    }
                }
            }
        }
    }

    private fun loadUpcoming(){
            upcomingViewModel.upcoming.observe(viewLifecycleOwner){state->
                when(state){
                    is ResponseState.Error -> {
                        Snackbar.make(binding.root,state.message,Snackbar.LENGTH_LONG)
                            .setAction("Refresh"){
                                loadUpcoming()
                            }
                            .show()
                    }
                    is ResponseState.Loading -> {
                        binding.upcomingPlaceholder.apply {
                            this.startShimmer()
                            this.visibility = View.VISIBLE
                        }
                        binding.upcomingEventRv.visibility = View.INVISIBLE
                    }
                    is ResponseState.Success -> {
                        binding.upcomingPlaceholder.apply {
                            this.stopShimmer()
                            this.visibility = View.GONE
                        }
                        binding.upcomingEventRv.apply {
                            val upcomingAdapter = UpcomingListAdapter(state.data.listEvents)

                            this.adapter = upcomingAdapter
                            this.layoutManager = LinearLayoutManager(activity)
                            this.visibility = View.VISIBLE

                            upcomingAdapter.onEventClicked(object: UpcomingListAdapter.OnEventClick{
                                override fun onUpcomingEventClick(id: Int) {
                                    ToDetail.toDetail(requireActivity(),id,true)
                                }

                            })
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