package com.tripmateapp.BaseDatos.actividades

import androidx.room.*

@Dao
interface ActividadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actividad: ActividadEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(actividades: List<ActividadEntity>)


    @Update
    suspend fun update(actividad: ActividadEntity)

    @Delete
    suspend fun delete(actividad: ActividadEntity)

    @Query("SELECT * FROM actividades")
    suspend fun getAll(): List<ActividadEntity>

    @Query("SELECT * FROM actividades WHERE id = :id")
    suspend fun getById(id: Int): ActividadEntity?

    @Query("SELECT * FROM actividades WHERE idItinerarioDia = :idItinerarioDia ORDER BY orden ASC")
    suspend fun getByItinerarioDia(idItinerarioDia: Int): List<ActividadEntity>
}