package com.example.inspiredaily.di

import android.content.Context
import androidx.room.Room
import com.example.inspiredaily.constants.BASE_URL
import com.example.inspiredaily.data.api.ApiService
import com.example.inspiredaily.data.repository.QuoteRepository
import com.example.inspiredaily.db.QuoteDao
import com.example.inspiredaily.db.QuoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val loggingInterceptor =  HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }



    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : QuoteDatabase =
        Room.databaseBuilder(context,QuoteDatabase::class.java,"quote_database")
            .build()

    @Provides
    fun provideQuoteDao(db : QuoteDatabase) : QuoteDao = db.quoteDao()

    @Provides
    @Singleton
    fun provideQuoteRepository(quoteDao: QuoteDao,apiService: ApiService) : QuoteRepository{
        return QuoteRepository(quoteDao,apiService)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun providesQuoteApi(retrofit: Retrofit) : ApiService =
        retrofit.create(ApiService::class.java)

}