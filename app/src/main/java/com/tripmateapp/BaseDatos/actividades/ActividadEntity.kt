package com.tripmateapp.BaseDatos.actividades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias.ItinerarioDiaEntity
import java.sql.Timestamp


@Entity(
    tableName = "actividades",
    foreignKeys = [
        ForeignKey(
            entity = ItinerarioDiaEntity::class,
            parentColumns = ["id"],
            childColumns = ["idItinerarioDia"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ActividadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idItinerarioDia: Int,   // Relación directa con ItinerarioDiaEntity
    val tipoActividad: String,
    val orden: Int,
    val descripcion: String?,
    val horaInicio: String?,
    val horaFin: String?
)