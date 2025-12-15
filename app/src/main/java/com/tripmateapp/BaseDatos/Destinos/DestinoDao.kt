package com.tripmateapp.BaseDatos.Destinos

import androidx.room.*

@Dao
interface DestinoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(destino: DestinoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(destinos: List<DestinoEntity>)

    @Update
    suspend fun update(destino: DestinoEntity)

    @Delete
    suspend fun delete(destino: DestinoEntity)

    @Query("SELECT * FROM destinos")
    suspend fun getAll(): List<DestinoEntity>

    @Query("SELECT * FROM destinos WHERE id = :id")
    suspend fun getById(id: Int): DestinoEntity?
}