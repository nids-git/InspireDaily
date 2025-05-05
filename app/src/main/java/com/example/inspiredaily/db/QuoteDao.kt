package com.example.inspiredaily.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inspiredaily.data.model.QuoteResponse
import kotlinx.coroutines.flow.Flow

@Dao

interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(quote : QuoteResponse) : Long

    @Query("Select * From quotes")
    fun getQuotesList() : Flow<List<QuoteResponse>>

    @Query("SELECT * FROM quotes ORDER BY timestamp DESC LIMIT 1")
    fun getQuote() : Flow<QuoteResponse?>

}