package com.bangkit.dicodingevent.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dicodingevent.R
import com.bangkit.dicodingevent.data.response.ListEventsItem
import com.bangkit.dicodingevent.databinding.UpcomingItemBinding
import com.bumptech.glide.Glide

class UpcomingListAdapter(private val upcomingEvent: List<ListEventsItem> ): RecyclerView.Adapter<UpcomingListAdapter.ViewHolder>() {

    private lateinit var onEventClickListener: OnEventClick

    fun onEventClicked(onItemClickListener: OnEventClick){
        this.onEventClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingListAdapter.ViewHolder {
        val binding = UpcomingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingListAdapter.ViewHolder, position: Int) {
        val event = upcomingEvent[position]
        holder.bind(event,holder.itemView.context)
        holder.itemView.setOnClickListener { onEventClickListener.onUpcomingEventClick(event.id) }
    }

    override fun getItemCount(): Int = upcomingEvent.size

    inner class ViewHolder(private val binding: UpcomingItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ListEventsItem,context: Context){
            Glide
                .with(context)
                .load(item.mediaCover)
                .into(binding.eventImage)
            binding.eventName.text = item.name
            binding.eventLocation.text = item.cityName
            binding.eventCategoty.text = item.category
            binding.eventRegistrant.text = context.getString(R.string.quota,item.registrants,item.quota)
        }
    }

    interface OnEventClick {
        fun onUpcomingEventClick(id: Int)
    }
}