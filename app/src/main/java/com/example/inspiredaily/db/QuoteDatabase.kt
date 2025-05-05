package com.example.inspiredaily.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.inspiredaily.data.model.QuoteResponse

@Database(entities = [QuoteResponse::class], version = 2, exportSchema = false)
abstract class QuoteDatabase : RoomDatabase() {

    abstract fun quoteDao(): QuoteDao

}