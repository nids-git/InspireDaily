package com.example.inspiredaily.data.api

import com.example.inspiredaily.data.model.QuoteResponse
import retrofit2.http.GET

interface ApiService {

    @GET("random")
    suspend fun getQuote() : List<QuoteResponse>

}