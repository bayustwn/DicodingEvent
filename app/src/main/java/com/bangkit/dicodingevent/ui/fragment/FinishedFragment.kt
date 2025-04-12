package com.bangkit.dicodingevent.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dicodingevent.databinding.FragmentFinishedBinding
import com.bangkit.dicodingevent.ui.adapter.FinishedListAdapter
import com.bangkit.dicodingevent.ui.viewmodel.EventViewModel
import com.bangkit.dicodingevent.ui.viewmodel.SearchViewModel
import com.bangkit.dicodingevent.ui.viewmodelfactory.SearchViewModelFactory
import com.bangkit.dicodingevent.ui.viewmodelfactory.ViewModelFactory
import com.bangkit.dicodingevent.utils.ResponseState
import com.bangkit.dicodingevent.utils.ToDetail
import com.google.android.material.snackbar.Snackbar

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val factory = ViewModelFactory.getInstance()
    private val searchFactory = SearchViewModelFactory.getInstance()
    private val eventViewModel by viewModels<EventViewModel> { factory }
    private val searchViewModel by viewModels<SearchViewModel>{ searchFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.finishedSearchView.apply {
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

        loadFinished()

    }

    private fun resetSearch(){
        binding.error.visibility = View.GONE
        eventViewModel.getFinished(null).apply {
            eventViewModel.finished.observe(viewLifecycleOwner){state->
                when(state){
                    is ResponseState.Success -> {
                        binding.error.visibility = View.GONE
                        binding.searchRvFinished.apply {
                            this.adapter = FinishedListAdapter(state.data.listEvents)
                        }
                    }else->{
                    binding.error.visibility = View.VISIBLE
                }
                }
            }
        }
    }

    private fun initSearch(query: String){
        searchViewModel.searchEvent(query,false).apply {
            searchViewModel.search.observe(viewLifecycleOwner){state->
                when(state){
                    is ResponseState.Error -> {
                        binding.searchFinishedPlaceholder.apply {
                            this.visibility = View.GONE
                            this.stopShimmer()
                        }
                        binding.searchRvFinished.visibility = View.GONE
                        binding.error.visibility = View.VISIBLE
                    }
                    is ResponseState.Loading -> {
                        binding.searchFinishedPlaceholder.apply {
                            this.visibility = View.VISIBLE
                            this.startShimmer()
                        }
                        binding.searchRvFinished.visibility = View.GONE
                        binding.error.visibility = View.GONE
                    }
                    is ResponseState.Success -> {
                        if (state.data.listEvents.isEmpty()){
                            binding.searchFinishedPlaceholder.apply {
                                this.visibility = View.GONE
                                this.stopShimmer()
                            }
                            binding.searchRvFinished.visibility = View.GONE
                            binding.error.visibility = View.VISIBLE
                        }else{
                            binding.searchFinishedPlaceholder.apply {
                                this.visibility = View.GONE
                                this.stopShimmer()
                            }
                            binding.error.visibility = View.GONE
                            binding.searchRvFinished.apply {
                                val searchAdapter = FinishedListAdapter(state.data.listEvents)

                                this.visibility = View.VISIBLE
                                this.adapter = searchAdapter
                                this.layoutManager = LinearLayoutManager(activity)

                                searchAdapter.onEventClicked(object : FinishedListAdapter.OnEventClick{
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

    private fun loadFinished(){
         eventViewModel.getFinished(null).apply {
             eventViewModel.finished.observe(viewLifecycleOwner){state->
                 when(state){
                     is ResponseState.Error -> {
                         Snackbar.make(binding.root,state.message,Snackbar.LENGTH_LONG)
                             .setAction("Refresh"){
                                 loadFinished()
                             }
                             .show()
                     }
                     is ResponseState.Loading -> {
                         binding.finishedEventPlaceholder.apply {
                             this.visibility = View.VISIBLE
                             this.startShimmer()
                         }
                         binding.eventFinishedRv.visibility = View.INVISIBLE
                     }
                     is ResponseState.Success -> {
                         binding.finishedEventPlaceholder.apply {
                             this.visibility = View.GONE
                             this.stopShimmer()
                         }
                         binding.searchRvFinished.apply {
                             val searchAdapter = FinishedListAdapter(state.data.listEvents)

                             this.visibility = View.VISIBLE
                             this.adapter = searchAdapter
                             this.layoutManager = LinearLayoutManager(activity)

                             searchAdapter.onEventClicked(object : FinishedListAdapter.OnEventClick{
                                 override fun onUpcomingEventClick(id: Int) {
                                     ToDetail.toDetail(requireActivity(),id,false)
                                 }

                             })
                         }
                         binding.eventFinishedRv.apply {
                             val finishedAdapter = FinishedListAdapter(state.data.listEvents)

                             this.visibility = View.VISIBLE
                             this.adapter = finishedAdapter
                             this.layoutManager = LinearLayoutManager(activity)

                             finishedAdapter.onEventClicked(object: FinishedListAdapter.OnEventClick{
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}