package com.tripmateapp.BaseDatos.Restaurantes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurantes")
data class RestauranteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val destinoId: Int,         // Relación con Destino
    val nombre: String,
    val ubicacion: String,
    val tipoComida: String,
    val puntuacionMedia: Double,
    val horarioApertura: String,
    val rangoPrecio: String
)