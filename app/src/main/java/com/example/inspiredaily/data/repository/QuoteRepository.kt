package com.example.inspiredaily.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.inspiredaily.data.api.ApiService
import com.example.inspiredaily.data.model.QuoteResponse
import com.example.inspiredaily.db.QuoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuoteRepository @Inject constructor(
    private val dao : QuoteDao,
    private val apiService: ApiService
) {
    @SuppressLint("SuspiciousIndentation")
    suspend fun fetchQuoteAndSave() : QuoteResponse? {
        return  try {
            withContext(Dispatchers.IO){
              val quote =  apiService.getQuote().firstOrNull()
                quote?.let {
                   val id =  dao.insertAll(it)
                    Log.d("OnInsertIntoDV",id.toString())
                }

                quote
            }
        } catch (e:Exception) {
            Log.e("Api Error", e.message.toString())
            null
        }
    }

     fun getLatestQuote(): LiveData<QuoteResponse?> {
        return dao.getQuote()
    }



}