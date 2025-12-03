package com.tripmateapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String
)