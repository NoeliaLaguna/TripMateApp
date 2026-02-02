package com.tripmateapp.BaseDatos.ItinerarioDiaRestaurantes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias.ItinerarioDiaEntity
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteEntity

@Entity(
    tableName = "itinerario_dia_restaurantes",
    foreignKeys = [
        ForeignKey(
            entity = ItinerarioDiaEntity::class,
            parentColumns = ["id"],
            childColumns = ["idItinerarioDia"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RestauranteEntity::class,
            parentColumns = ["id"],
            childColumns = ["idRestaurante"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItinerarioDiaRestauranteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idItinerarioDia: Int,
    val idRestaurante: Int,
    val tipoComida: String? = null, // "desayuno", "almuerzo", "cena"
    val hora: String? = null,
    val notas: String? = null
)
