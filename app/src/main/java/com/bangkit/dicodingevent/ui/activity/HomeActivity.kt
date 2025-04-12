package com.bangkit.dicodingevent.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bangkit.dicodingevent.R
import com.bangkit.dicodingevent.data.datastore.ThemePreferences
import com.bangkit.dicodingevent.data.datastore.dataStore
import com.bangkit.dicodingevent.databinding.ActivityHomeBinding
import com.bangkit.dicodingevent.ui.viewmodel.ThemeViewModel
import com.bangkit.dicodingevent.ui.viewmodelfactory.ThemeViewModelFactory
import com.bangkit.dicodingevent.workmanager.MainWorker
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var themeViewModelFactory: ThemeViewModelFactory
    private val themeViewModel by viewModels<ThemeViewModel> { themeViewModelFactory }
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val pref = ThemePreferences.getInstance(application.dataStore)
        themeViewModelFactory = ThemeViewModelFactory.getInstance(pref)
        workManager = WorkManager.getInstance(this)

        themeViewModel.getTheme().observe(this@HomeActivity){isDark->
            if (isDark){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        startPeriodicTask()

        val navController = findNavController(R.id.nav_host)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home,
                R.id.upcoming,
                R.id.finished,
                R.id.favorite
            )
        )

        setupActionBarWithNavController(navController,appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

    }

    private fun startPeriodicTask(){
        val contrarians = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest = PeriodicWorkRequest.Builder(MainWorker::class.java,1, TimeUnit.DAYS)
            .setConstraints(contrarians)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "EventReminder",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

}