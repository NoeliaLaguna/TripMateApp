package com.tripmateapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDAO {
    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM MessageEntity ORDER BY id DESC LIMIT 1")
    suspend fun getLastMessage(): MessageEntity
}
