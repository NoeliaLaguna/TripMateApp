package com.tripmateapp.BaseDatos.ItinerarioDiaActividades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias.ItinerarioDiaEntity
import com.tripmateapp.BaseDatos.actividades.ActividadEntity

@Entity(
    tableName = "itinerario_dia_actividades",
    foreignKeys = [
        ForeignKey(
            entity = ItinerarioDiaEntity::class,
            parentColumns = ["id"],
            childColumns = ["idItinerarioDia"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActividadEntity::class,
            parentColumns = ["id"],
            childColumns = ["idActividad"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItinerarioDiaActividadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idItinerarioDia: Int,
    val idActividad: Int,
    val orden: Int? = null,
    val horaInicio: String? = null,
    val notas: String? = null
)
