package com.tripmateapp.BaseDatos.ItinerarioDiaActividades

import androidx.room.*

@Dao
interface ItinerarioDiaActividadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itinerarioDiaActividad: ItinerarioDiaActividadEntity)

    @Query("SELECT * FROM itinerario_dia_actividades WHERE idItinerarioDia = :idItinerarioDia ORDER BY orden")
    suspend fun getByItinerarioDia(idItinerarioDia: Int): List<ItinerarioDiaActividadEntity>

    @Query("DELETE FROM itinerario_dia_actividades WHERE idItinerarioDia = :idItinerarioDia")
    suspend fun deleteByItinerarioDia(idItinerarioDia: Int)
}
