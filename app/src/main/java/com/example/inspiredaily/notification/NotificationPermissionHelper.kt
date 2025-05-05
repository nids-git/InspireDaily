package com.example.inspiredaily.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object NotificationPermissionHelper {

    fun shouldRequestPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun isPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionIfNeeded(
        context: Context,
        launcher: ActivityResultLauncher<String>
    ) {
        if (shouldRequestPermission() && !isPermissionGranted(context)) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}