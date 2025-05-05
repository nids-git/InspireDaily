package com.example.inspiredaily.common

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

fun Context.isNetworkAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo?.isConnectedOrConnecting == true
}

fun Context.showToast(message: String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}

fun makeVisible(boolean: Boolean){

}