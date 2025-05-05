package com.example.inspiredaily.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.inspiredaily.data.api.ApiService
import com.example.inspiredaily.data.repository.QuoteRepository
import com.example.inspiredaily.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltWorker
class QuoteSyncWorker @AssistedInject constructor(
        @Assisted appContext: Context,
        @Assisted workerParams : WorkerParameters,
        private val quoteRepository: QuoteRepository
) : CoroutineWorker(appContext, workerParams) {

      private val notificationHelper = NotificationHelper(applicationContext)

    override suspend fun doWork(): Result {
        Log.d("OnSuccessWorker","dowork")

        return try {
            val quote = quoteRepository.fetchQuoteAndSave()
            Log.d("OnSuccess",quote.toString())
            quote?.let {
                notificationHelper.sendNotification("Daily Quote",quote.q)
                Log.d("OnSuccessWorker",quote.toString())
            }
            val outpouData = Data.Builder()

            Result.success()
        } catch (e:Exception){
            Log.d("Worker Error", e.localizedMessage)
            Result.retry()
        }
    }

}