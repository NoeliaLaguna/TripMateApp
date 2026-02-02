package com.tripmateapp.BaseDatos.ItinerarioDiaLugaresTuristicos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias.ItinerarioDiaEntity
import com.tripmateapp.BaseDatos.LugaresTuristicos.LugarTuristicoEntity

@Entity(
    tableName = "itinerario_dia_lugares_turisticos",
    foreignKeys = [
        ForeignKey(
            entity = ItinerarioDiaEntity::class,
            parentColumns = ["id"],
            childColumns = ["idItinerarioDia"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LugarTuristicoEntity::class,
            parentColumns = ["id"],
            childColumns = ["idLugarTuristico"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItinerarioDiaLugarTuristicoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idItinerarioDia: Int,
    val idLugarTuristico: Int,
    val horaVisita: String? = null,
    val duracion: String? = null,
    val notas: String? = null
)
