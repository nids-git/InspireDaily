package com.example.inspiredaily.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.inspiredaily.data.model.QuoteResponse
import com.example.inspiredaily.data.repository.QuoteRepository
import com.example.inspiredaily.workers.QuoteSyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: QuoteRepository
) : ViewModel() {

    private val _quote = MutableLiveData<QuoteResponse?>()
    val quote : LiveData<QuoteResponse?> get() = _quote

    init {
        checkAndFetchInitialQuote()
    }

    fun getQuoteFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLatestQuote().collect { localQuote ->
                _quote.postValue(localQuote)
            }
        }
    }

    fun checkAndFetchInitialQuote() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val localQuote = repository.getLatestQuote().firstOrNull()
                    if(localQuote == null){
                        val remoteQuote = repository.fetchQuoteAndSave()
                        remoteQuote?.let {
                            _quote.postValue(it)
                        }
                    } else {
                        _quote.postValue(localQuote)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                // Optionally: show error to user via another LiveData
            }
        }
    }
}