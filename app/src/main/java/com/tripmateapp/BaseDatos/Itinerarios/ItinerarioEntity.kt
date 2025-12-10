package com.tripmateapp.BaseDatos.Itinerarios

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itinerarios")
data class ItinerarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idViaje: Int,
    val nombre: String?,
    val fecha: String,
)