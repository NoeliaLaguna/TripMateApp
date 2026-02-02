package com.tripmateapp.BaseDatos.ItinerarioDiaTransportes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias.ItinerarioDiaEntity
import com.tripmateapp.BaseDatos.Transporte.TransporteEntity

@Entity(
    tableName = "itinerario_dia_transportes",
    foreignKeys = [
        ForeignKey(
            entity = ItinerarioDiaEntity::class,
            parentColumns = ["id"],
            childColumns = ["idItinerarioDia"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TransporteEntity::class,
            parentColumns = ["id"],
            childColumns = ["idTransporte"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItinerarioDiaTransporteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idItinerarioDia: Int,
    val idTransporte: Int,
    val hora: String? = null,
    val notas: String? = null
)
