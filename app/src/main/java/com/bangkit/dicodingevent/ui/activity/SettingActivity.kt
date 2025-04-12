package com.bangkit.dicodingevent.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bangkit.dicodingevent.data.datastore.ThemePreferences
import com.bangkit.dicodingevent.data.datastore.dataStore
import com.bangkit.dicodingevent.databinding.ActivitySettingBinding
import com.bangkit.dicodingevent.ui.viewmodel.ThemeViewModel
import com.bangkit.dicodingevent.ui.viewmodelfactory.ThemeViewModelFactory

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var themeViewModelFactory: ThemeViewModelFactory
    private val themeViewModel by viewModels<ThemeViewModel> { themeViewModelFactory }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted:Boolean->
        binding.allowNotificationSwitch.isChecked = isGranted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= 33){

            binding.allowNotification.visibility = View.VISIBLE
            binding.allowNotificationSwitch.visibility = View.VISIBLE

            binding.allowNotificationSwitch.setOnCheckedChangeListener { _: CompoundButton?, isAllowed: Boolean ->
                if(!isAllowed){
                        requestPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)

                }
            }
        }

        val pref = ThemePreferences.getInstance(application.dataStore)
        themeViewModelFactory = ThemeViewModelFactory.getInstance(pref)

        themes()

        binding.darkModeSwitch.setOnCheckedChangeListener{_:CompoundButton?,isDark:Boolean->
            themeViewModel.setTheme(isDark)
        }



    }

    private fun themes(){
        themeViewModel.getTheme().observe(this@SettingActivity){isDark->
            if (isDark){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.darkModeSwitch.isChecked = true
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.darkModeSwitch.isChecked = false
            }
        }
    }
}