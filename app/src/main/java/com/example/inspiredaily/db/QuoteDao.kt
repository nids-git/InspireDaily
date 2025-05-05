package com.example.inspiredaily.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inspiredaily.data.model.QuoteResponse

@Dao

interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(quote : QuoteResponse) : Long

   // @Query("SELECT * FROM QuoteResponse ORDER BY timestamp DESC LIMIT 1")
    @Query("SELECT * FROM quotes ORDER BY timestamp DESC")
     fun getQuote() : LiveData<QuoteResponse?>

}