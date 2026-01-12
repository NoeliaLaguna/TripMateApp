package com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias

import androidx.room.*

@Dao
interface ItinerarioDiaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itinerarioDia: ItinerarioDiaEntity)

    @Update
    suspend fun update(itinerarioDia: ItinerarioDiaEntity)

    @Delete
    suspend fun delete(itinerarioDia: ItinerarioDiaEntity)

    @Query("SELECT * FROM itinerarios_dia")
    suspend fun getAll(): List<ItinerarioDiaEntity>

    @Query("SELECT * FROM itinerarios_dia WHERE id = :id")
    suspend fun getById(id: Int): ItinerarioDiaEntity?

    @Query("SELECT * FROM itinerarios_dia WHERE idItinerario = :idItinerario")
    suspend fun getByItinerario(idItinerario: Int): List<ItinerarioDiaEntity>
}