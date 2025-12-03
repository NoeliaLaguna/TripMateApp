package com.tripmateapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.tripmateapp.BaseDatos.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel(app: Application) : AndroidViewModel(app)  {
    private val db = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "messages.db"
    ).build()

    private val _message = MutableStateFlow("Escribe algo y guarda :)")
    val message: StateFlow<String> = _message

    /*fun saveMessage(text: String) {
        viewModelScope.launch {
            db.messageDao().insertMessage(MessageEntity(text = text))
            val last = db.messageDao().getLastMessage()
            _message.value = last.text
        }
    }*/
}