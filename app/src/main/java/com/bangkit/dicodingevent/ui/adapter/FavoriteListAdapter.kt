package com.bangkit.dicodingevent.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dicodingevent.R
import com.bangkit.dicodingevent.data.db.entity.EventEntity
import com.bangkit.dicodingevent.databinding.FavoriteItemBinding
import com.bangkit.dicodingevent.utils.ToDetail
import com.bumptech.glide.Glide

class FavoriteListAdapter(private val favorite: List<EventEntity>): RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: FavoriteItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: EventEntity,context: Context){
            binding.eventName.text = item.name
            binding.eventLocation.text = item.cityName
            Glide
                .with(context)
                .load(item.mediaCover)
                .into(binding.favoriteEventImage)
            binding.favoriteEventCategory.text = item.category
            binding.eventRegistrant.text = context.getString(R.string.quota,item.registrants,item.quota)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = favorite.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fav = favorite[position]
        holder.bind(fav, holder.itemView.context)
        holder.itemView.setOnClickListener{
            ToDetail.toDetail(holder.itemView.context,fav.id,fav.isUpcoming)
        }
    }

}