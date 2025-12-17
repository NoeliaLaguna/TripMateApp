package com.tripmateapp.BaseDatos.actividades

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actividad: ActividadEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(actividades: List<ActividadEntity>)

    @Query("SELECT * FROM actividades WHERE destinoId = :destinoId")
    fun getByDestino(destinoId: Int): Flow<List<ActividadEntity>>

    @Update
    suspend fun update(actividad: ActividadEntity)

    @Delete
    suspend fun delete(actividad: ActividadEntity)

    @Query("SELECT * FROM actividades")
    suspend fun getAll(): List<ActividadEntity>

    @Query("SELECT * FROM actividades WHERE id = :id")
    suspend fun getById(id: Int): ActividadEntity?

    @Query("SELECT * FROM actividades WHERE idItinerarioDia = :idItinerarioDia ORDER BY orden ASC")
    fun getByItinerarioDia(idItinerarioDia: Int): Flow<List<ActividadEntity>>
}