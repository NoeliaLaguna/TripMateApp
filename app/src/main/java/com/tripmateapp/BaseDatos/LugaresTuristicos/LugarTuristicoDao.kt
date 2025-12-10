package com.tripmateapp.BaseDatos.LugaresTuristicos

import androidx.room.*

@Dao
interface LugarTuristicoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lugar: LugarTuristicoEntity)

    @Update
    suspend fun update(lugar: LugarTuristicoEntity)

    @Delete
    suspend fun delete(lugar: LugarTuristicoEntity)

    @Query("SELECT * FROM lugares_turisticos")
    suspend fun getAll(): List<LugarTuristicoEntity>

    @Query("SELECT * FROM lugares_turisticos WHERE id = :id")
    suspend fun getById(id: Int): LugarTuristicoEntity?

    @Query("SELECT * FROM lugares_turisticos WHERE destinoId = :destinoId")
    suspend fun getByDestino(destinoId: Int): List<LugarTuristicoEntity>

    @Query("SELECT * FROM lugares_turisticos WHERE categoria = :categoria")
    suspend fun getByCategoria(categoria: String): List<LugarTuristicoEntity>
}