package com.tripmateapp.BaseDatos.ItinerarioDiaLugaresTuristicos

import androidx.room.*

@Dao
interface ItinerarioDiaLugarTuristicoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itinerarioDiaLugarTuristico: ItinerarioDiaLugarTuristicoEntity)

    @Query("SELECT * FROM itinerario_dia_lugares_turisticos WHERE idItinerarioDia = :idItinerarioDia")
    suspend fun getByItinerarioDia(idItinerarioDia: Int): List<ItinerarioDiaLugarTuristicoEntity>

    @Query("DELETE FROM itinerario_dia_lugares_turisticos WHERE idItinerarioDia = :idItinerarioDia")
    suspend fun deleteByItinerarioDia(idItinerarioDia: Int)
}
