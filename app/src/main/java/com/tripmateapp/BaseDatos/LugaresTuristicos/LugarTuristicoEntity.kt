package com.tripmateapp.BaseDatos.LugaresTuristicos


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lugares_turisticos")
data class LugarTuristicoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val destinoId: Int,             // Relación con Destino
    val nombre: String,
    val descripcion: String?,
    val ubicacion: String,
    val valoracion: Double?,
    val categoria: String?
)