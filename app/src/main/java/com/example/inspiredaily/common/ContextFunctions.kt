package com.example.inspiredaily.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun Context.isNetworkAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo?.isConnectedOrConnecting == true
}

fun Context.showToast(message: String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}


fun Activity.captureAndShareScreen() {
    // 1. Capture the screen
    val rootView = window.decorView.rootView
    rootView.isDrawingCacheEnabled = true
    val bitmap = Bitmap.createBitmap(rootView.drawingCache)
    rootView.isDrawingCacheEnabled = false

    // 2. Save to cache
    val cachePath = File(cacheDir, "images")
    cachePath.mkdirs()
    val file = File(cachePath, "screenshot.png")
    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    // 3. Get URI with FileProvider
    val screenshotUri: Uri = FileProvider.getUriForFile(
        this,
        "$packageName.provider",
        file
    )

    // 4. Share via Intent
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, screenshotUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    startActivity(Intent.createChooser(shareIntent, "Share Screenshot"))
}