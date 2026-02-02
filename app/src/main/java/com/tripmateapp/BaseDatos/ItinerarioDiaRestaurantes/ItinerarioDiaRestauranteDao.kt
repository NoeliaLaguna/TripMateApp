package com.tripmateapp.BaseDatos.ItinerarioDiaRestaurantes

import androidx.room.*

@Dao
interface ItinerarioDiaRestauranteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itinerarioDiaRestaurante: ItinerarioDiaRestauranteEntity)

    @Query("SELECT * FROM itinerario_dia_restaurantes WHERE idItinerarioDia = :idItinerarioDia")
    suspend fun getByItinerarioDia(idItinerarioDia: Int): List<ItinerarioDiaRestauranteEntity>

    @Query("DELETE FROM itinerario_dia_restaurantes WHERE idItinerarioDia = :idItinerarioDia")
    suspend fun deleteByItinerarioDia(idItinerarioDia: Int)
}
