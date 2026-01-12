package com.tripmateapp.BaseDatos.Itinerarios

import androidx.room.*

@Dao
interface ItinerarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itinerario: ItinerarioEntity)

    @Update
    suspend fun update(itinerario: ItinerarioEntity)

    @Delete
    suspend fun delete(itinerario: ItinerarioEntity)

    @Query("SELECT * FROM itinerarios")
    suspend fun getAll(): List<ItinerarioEntity>

    @Query("SELECT * FROM itinerarios WHERE idViaje = :viajeId")
    suspend fun getByViaje(viajeId: Int): List<ItinerarioEntity>
}