package com.tripmateapp.BaseDatos.Viajes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viajes")
data class ViajeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,        // Relación con usuario
    val destinoId: Int,        // Relación con destino
    val fechaInicio: String,
    val fechaFin: String,
    val presupuesto: Double?
)