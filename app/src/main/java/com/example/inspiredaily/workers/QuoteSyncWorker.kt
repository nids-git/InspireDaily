package com.example.inspiredaily.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.inspiredaily.data.api.ApiService
import com.example.inspiredaily.data.repository.QuoteRepository
import com.example.inspiredaily.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class QuoteSyncWorker @AssistedInject constructor(
        @Assisted appContext: Context,
        @Assisted workerParams : WorkerParameters,
        private val apiService: ApiService,
        private val quoteRepository: QuoteRepository
) : CoroutineWorker(appContext, workerParams) {

        @Inject
        lateinit var notificationHelper: NotificationHelper

     override suspend fun doWork(): Result {
        return try {
            val quote = quoteRepository.fetchQuoteAndSave()
            quote?.let {
                notificationHelper.sendNotification("Daily Quote",quote.q)
                Log.d("OnSuccessWorker",quote.toString())
            }
            val outputData = Data.Builder()
                .putString("quote", quote?.q)
                .build()
            Result.success(outputData)
        } catch (e:Exception){
            Log.d("Worker Error", e.localizedMessage)
            Result.retry()
        }
    }

}