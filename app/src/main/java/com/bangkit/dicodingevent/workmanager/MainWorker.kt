package com.bangkit.dicodingevent.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bangkit.dicodingevent.R
import com.bangkit.dicodingevent.data.api.ApiClient
import com.bangkit.dicodingevent.utils.DateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainWorker(context: Context, workerParameters: WorkerParameters): CoroutineWorker(context,workerParameters) {


    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        getLastest()
    }

    private var resultStatus: Result? = null

    companion object {
        const val CHANNEL_ID = "channel 01"
        const val CHANNEL_NAME = "event reminder"
        const val NOTIFICATION_ID = 1
    }

    private suspend fun getLastest(): Result{
        try {
            val response = ApiClient.apiService.getLastest()
            showNotif(response.listEvents[0].name, DateFormat.formatDate(response.listEvents[0].beginTime))
            resultStatus = Result.success()
        }catch (e : HttpException){
            resultStatus = Result.retry()
        }catch (e: Exception){
            resultStatus = Result.failure()
        }
        return resultStatus as Result
    }

    private fun showNotif(title:String,desc: String){
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            CHANNEL_ID)
            .setSmallIcon(R.drawable.notif)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
            notificationManager.notify(NOTIFICATION_ID,notification.build())

    }

}