package com.bangkit.dicodingevent.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dicodingevent.R
import com.bangkit.dicodingevent.data.response.ListEventsItem
import com.bangkit.dicodingevent.databinding.FinishedItemBinding
import com.bangkit.dicodingevent.utils.DateFormat
import com.bumptech.glide.Glide

class FinishedListAdapter(private val upcomingList: List<ListEventsItem>): RecyclerView.Adapter<FinishedListAdapter.ViewHolder>() {

    private lateinit var onEventClickListener: OnEventClick

    fun onEventClicked(onItemClickListener: OnEventClick){
        this.onEventClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FinishedItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return upcomingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = upcomingList[position]
        holder.bind(data,holder.itemView.context)
        holder.itemView.setOnClickListener { onEventClickListener.onUpcomingEventClick(data.id) }
    }

    inner class ViewHolder(private val binding: FinishedItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ListEventsItem,context: Context){

            Glide
                .with(context)
                .load(item.imageLogo)
                .into(binding.eventImage)
            binding.eventParticipants.text =
                context.getString(R.string.participans_value, item.registrants)
            binding.eventLocation.text = item.cityName
            binding.eventCategoty.text = item.category
            binding.eventDate.text = DateFormat.formatDate(item.beginTime)
            binding.eventName.text=item.name
        }
    }

    interface OnEventClick {
        fun onUpcomingEventClick(id: Int)
    }

}