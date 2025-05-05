package com.example.inspiredaily.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteResponse(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val q : String,
    val a : String,
    var timestamp: Long = System.currentTimeMillis() // Added for sorting

)

