package com.example.inspiredaily.ui

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.inspiredaily.R
import com.example.inspiredaily.common.showToast
import com.example.inspiredaily.databinding.ActivityMainBinding
import com.example.inspiredaily.notification.NotificationPermissionHelper
import com.example.inspiredaily.viewmodels.MainViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialization()
        clickListeners()

    }

    private fun clickListeners() {
        binding.layoutToolbar.imgMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            this.showToast("Permission granted successfully")
            Log.d("Permission", "Notification permission granted")
            // You can now send notifications
        } else {
            this.showToast("Permission is denied")
            Log.d("Permission", "Notification permission denied")
        }
    }
    private fun initialization() {
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.settingsFragment),
            binding.drawerLayout
        )
        binding.navigationView.setupWithNavController(navController)

        NotificationPermissionHelper.requestPermissionIfNeeded(
            this,
            notificationPermissionLauncher
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    fun setToolbarTitle(title: String) {
        if (::binding.isInitialized)
        binding.layoutToolbar.tvTitle.text = title
    }

}