package com.tripmateapp.BaseDatos.Destinos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "destinos")
data class DestinoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val pais: String,
    val descripcion: String?,
    val coordenadas: String
)