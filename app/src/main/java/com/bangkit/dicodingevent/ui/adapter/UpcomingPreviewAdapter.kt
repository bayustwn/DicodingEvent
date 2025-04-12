package com.bangkit.dicodingevent.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dicodingevent.R
import com.bangkit.dicodingevent.data.response.ListEventsItem
import com.bangkit.dicodingevent.databinding.UpcomingPreviewItemBinding
import com.bangkit.dicodingevent.utils.DateFormat
import com.bumptech.glide.Glide

class UpcomingPreviewAdapter(private val upcomingList: List<ListEventsItem>): RecyclerView.Adapter<UpcomingPreviewAdapter.ViewHolder>() {

    private lateinit var onEventClickListener: OnEventClick

    fun onEventClicked(onItemClickListener: OnEventClick){
        this.onEventClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UpcomingPreviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = if (upcomingList.size < 5) upcomingList.size else 5

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = upcomingList[position]
        holder.bind(data,holder.itemView.context)
        holder.itemView.setOnClickListener { onEventClickListener.onUpcomingEventClick(data.id) }
    }

    inner class ViewHolder(private val binding: UpcomingPreviewItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ListEventsItem,context: Context){

            Glide
                .with(context)
                .load(item.mediaCover)
                .into(binding.eventImage)
            binding.eventName.text = item.name
            binding.eventLocation.text = item.cityName
            binding.eventDate.text = DateFormat.formatDate(item.beginTime)
            binding.eventCategoty.text = item.category
            binding.eventQuota.text =
                context.getString(R.string.quota,item.registrants,item.quota)
        }
    }

    interface OnEventClick {
        fun onUpcomingEventClick(id: Int)
    }
}