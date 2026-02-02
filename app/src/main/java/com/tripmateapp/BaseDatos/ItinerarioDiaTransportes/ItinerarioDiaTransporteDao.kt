package com.tripmateapp.BaseDatos.ItinerarioDiaTransportes

import androidx.room.*

@Dao
interface ItinerarioDiaTransporteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itinerarioDiaTransporte: ItinerarioDiaTransporteEntity)

    @Query("SELECT * FROM itinerario_dia_transportes WHERE idItinerarioDia = :idItinerarioDia")
    suspend fun getByItinerarioDia(idItinerarioDia: Int): List<ItinerarioDiaTransporteEntity>

    @Query("DELETE FROM itinerario_dia_transportes WHERE idItinerarioDia = :idItinerarioDia")
    suspend fun deleteByItinerarioDia(idItinerarioDia: Int)
}
