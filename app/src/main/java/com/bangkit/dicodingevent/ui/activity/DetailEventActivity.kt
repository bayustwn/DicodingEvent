package com.bangkit.dicodingevent.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bangkit.dicodingevent.R
import com.bangkit.dicodingevent.data.db.entity.EventEntity
import com.bangkit.dicodingevent.databinding.ActivityDetailEventBinding
import com.bangkit.dicodingevent.ui.viewmodel.EventViewModel
import com.bangkit.dicodingevent.ui.viewmodel.FavoriteViewModel
import com.bangkit.dicodingevent.ui.viewmodelfactory.FavoriteViewModelFactory
import com.bangkit.dicodingevent.ui.viewmodelfactory.ViewModelFactory
import com.bangkit.dicodingevent.utils.DateFormat
import com.bangkit.dicodingevent.utils.ResponseState
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private var factory : ViewModelFactory = ViewModelFactory.getInstance()
    private lateinit var favModelFactory: FavoriteViewModelFactory
    private val favViewModel by viewModels<FavoriteViewModel> {favModelFactory}
    private val eventViewModel by viewModels<EventViewModel>{factory}

    companion object{
        const val EVENT_ID = "EVENT_ID"
        const val UPCOMING = "UPCOMING"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val id: Int = intent.getIntExtra(EVENT_ID,0)
        val upcoming: Boolean = intent.getBooleanExtra(UPCOMING,false)

        favModelFactory = FavoriteViewModelFactory.getInstance(application)

        loadDetail(id,upcoming)
    }

    private fun loadDetail(id:Int,upcoming: Boolean){
        eventViewModel.getDetail(id).apply {
            eventViewModel.detail.observe(this@DetailEventActivity){event->
                when(event){
                    is ResponseState.Error -> {
                        Snackbar.make(
                            binding.root,
                            event.message,
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Refresh"){
                                loadDetail(id,upcoming)
                            }
                            .show()
                    }
                    is ResponseState.Loading -> {
                        binding.placeholderDetailEvent.apply {
                            this.visibility = View.VISIBLE
                            this.startShimmer()
                        }
                        binding.detail.visibility = View.INVISIBLE
                    }
                    is ResponseState.Success -> {
                        binding.placeholderDetailEvent.apply {
                            this.visibility = View.GONE
                            this.stopShimmer()
                        }
                        binding.detail.visibility = View.VISIBLE
                        val data = event.data.listEvents
                        val thisEvent = EventEntity(
                            data.id,
                            data.name,
                            data.mediaCover,
                            data.category,
                            data.cityName,
                            data.quota,
                            data.registrants,
                            data.beginTime,
                            upcoming
                        )
                        if (upcoming){
                            binding.buttonOpen.text = getString(R.string.register)
                        }else{
                            binding.buttonOpen.text = getString(R.string.open)
                        }

                        favViewModel.isFav(data.id).observe(this@DetailEventActivity){isFav->
                            if (isFav) binding.favButton.setImageResource(R.drawable.favorite) else binding.favButton.setImageResource(R.drawable.not_favorite)

                            binding.favButtonContainer.setOnClickListener {
                               favViewModel.toggleFav(thisEvent,isFav)
                            }

                        }

                        binding.apply {
                            this.buttonOpen.setOnClickListener{ _->
                                val toRegister = Intent(
                                    Intent.ACTION_VIEW
                                ).setData(Uri.parse(data.link))
                                startActivity(toRegister)
                            }
                            Glide
                                .with(applicationContext)
                                .load(data.mediaCover)
                                .into(this.detailEventImage)

                            this.eventName.text = data.name
                            this.eventLocation.text = data.cityName
                            this.eventOwner.text = data.ownerName
                            this.category.text = data.category
                            this.eventSummary.text = data.summary
                            this.eventDesc.text = Html.fromHtml(data.description,HtmlCompat.FROM_HTML_MODE_LEGACY)
                            this.participant.text = getString(R.string.participans_value, data.registrants)
                            this.quota.text = getString(R.string.quota_remain, data.quota - data.registrants)
                            this.eventDate.text = DateFormat.formatDate(data.beginTime)
                            this.timeEvent.text = getString(
                                R.string.event_time,
                                DateFormat.formatTime(data.beginTime),
                                DateFormat.formatTime(data.endTime)
                            )
                        }
                    }
                }
            }
        }
    }
}