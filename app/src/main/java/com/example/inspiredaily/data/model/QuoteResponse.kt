package com.example.inspiredaily.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteResponse(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val q : String,
    val a : String,
   // val tagsInfo : List<String> = mutableListOf(),
    val authorSlug : String,
    val timestamp: Long = System.currentTimeMillis() // Added for sorting

)

