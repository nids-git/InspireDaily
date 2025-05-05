package com.example.inspiredaily.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.inspiredaily.data.model.QuoteResponse
import com.example.inspiredaily.data.repository.QuoteRepository
import com.example.inspiredaily.workers.QuoteSyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: QuoteRepository
) : ViewModel() {

    private val _quote = MutableLiveData<QuoteResponse>()
    val quote : LiveData<QuoteResponse?> = _quote
    //val quote : LiveData<QuoteResponse?> = repository.getLatestQuote()

    fun getRandomQuotes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.fetchQuoteAndSave()
                val localQuote = repository.getLatestQuote()

//                if(localQuote != null){
//                    _quote.postValue(localQuote)
//                } else {
//                    val remoteQuote = repository.fetchQuoteAndSave()
//                    if(remoteQuote != null){
//                       //  repository.insertAllData(remoteQuote)
//                        val response = repository.getLatestQuote()
//                        _quote.value = response.value
//                    }
//                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Optionally: show error to user via another LiveData
            }
        }
    }



    fun setUpPeriodicRequest(context : Context){
        val workRequest = PeriodicWorkRequest.Builder(
            QuoteSyncWorker::class.java,
            60,
            TimeUnit.SECONDS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "QuoteSyncWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )

    }
}