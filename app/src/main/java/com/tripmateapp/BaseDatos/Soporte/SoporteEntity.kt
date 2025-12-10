package com.tripmateapp.BaseDatos.Soporte

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "soporte")
data class SoporteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,             // Relación con usuario
    val mensaje: String,
    val fecha: String,
    val estado: String              // ejemplo: "pendiente", "resuelto"
)