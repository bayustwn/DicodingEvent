package com.bangkit.dicodingevent.utils

import android.content.Context
import android.content.Intent
import com.bangkit.dicodingevent.ui.activity.DetailEventActivity

object ToDetail {

    fun toDetail(context: Context, id: Int, upcoming: Boolean){
        val intent = Intent(context, DetailEventActivity::class.java)
            .putExtra(DetailEventActivity.EVENT_ID, id)
            .putExtra(DetailEventActivity.UPCOMING, upcoming)
        context.startActivity(intent)
    }

}