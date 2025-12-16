package com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioEntity
import java.sql.Timestamp

@Entity(
    tableName = "itinerarios_dia",
    foreignKeys = [
        ForeignKey(
            entity = ItinerarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["idItinerario"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItinerarioDiaEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val idItinerario: Int,   // Relación directa con ItinerarioEntity
    val fecha: String,
    val horaInicio: String,
    val horaFin: String
)