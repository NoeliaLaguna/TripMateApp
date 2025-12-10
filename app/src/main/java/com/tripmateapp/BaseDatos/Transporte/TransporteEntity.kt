package com.tripmateapp.BaseDatos.Transporte

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transportes")
data class TransporteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val destinoId: Int,         // Relación con Destino
    val tipo: String,          // avión, tren, bus...
    val nombre: String?,
    val horario: String?,
    val precio: Double?
)