package com.bangkit.dicodingevent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dicodingevent.databinding.FragmentFavoriteBinding
import com.bangkit.dicodingevent.ui.adapter.FavoriteListAdapter
import com.bangkit.dicodingevent.ui.viewmodel.FavoriteViewModel
import com.bangkit.dicodingevent.ui.viewmodelfactory.FavoriteViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding?=null
    private val binding get() = _binding!!
    private lateinit var favModelFactory: FavoriteViewModelFactory
    private val favoriteViewModel by viewModels<FavoriteViewModel> { favModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favModelFactory = FavoriteViewModelFactory.getInstance(requireActivity().application)
        loadFavorite()
    }

    private fun loadFavorite(){
        favoriteViewModel.getAllFav().observe(viewLifecycleOwner){event->
            val favAdapter = FavoriteListAdapter(event)
            if (event.isEmpty()){
                binding.empty.visibility = View.VISIBLE
                binding.rvFav.visibility = View.INVISIBLE
            }else{
                binding.empty.visibility = View.GONE
                binding.rvFav.apply {
                    this.adapter = favAdapter
                    this.layoutManager = LinearLayoutManager(activity)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}