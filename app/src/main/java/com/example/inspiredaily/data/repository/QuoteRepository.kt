package com.example.inspiredaily.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.inspiredaily.data.api.ApiService
import com.example.inspiredaily.data.model.QuoteResponse
import com.example.inspiredaily.db.QuoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuoteRepository @Inject constructor(
    private val dao : QuoteDao,
    private val apiService: ApiService
) {
    suspend fun fetchQuoteAndSave() : QuoteResponse? {
        return  try {
            withContext(Dispatchers.IO){
                val quoteList =  apiService.getQuote()
                val quote = quoteList.firstOrNull()
                Log.d("Api Quote", quote.toString())
                quote?.let { insertAll(it) }
                quote
            }
        } catch (e:Exception) {
            Log.e("Api Error", e.message.toString())
            null
        }
    }

    private suspend fun insertAll(quoteResponse: QuoteResponse){
        val model = QuoteResponse(
            a = quoteResponse.a,
            q = quoteResponse.q,
            timestamp = System.currentTimeMillis()
        )
        dao.insertAll(model)
    }

    fun getLatestQuote() : Flow<QuoteResponse?>{
       return  dao.getQuote()
    }

    suspend fun getAllQuotes() {
        dao.getQuotesList().collectLatest{
            Log.d("AllQuotes", it.toString())
        }
    }


}