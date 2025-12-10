package com.tripmateapp.BaseDatos.DestinosFavoritos

import androidx.room.Entity


@Entity(
    tableName = "destinos_favoritos",
    primaryKeys = ["usuarioId", "destinoId"]   // PRIMARY KEY COMPUESTA
)
data class DestinoFavoritoEntity(
    val usuarioId: Int,      // Relación con usuario
    val destinoId: Int,      // Relación con destino
)