package com.tripmateapp.BaseDatos.Soporte

import androidx.room.*

@Dao
interface SoporteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(soporte: SoporteEntity)

    @Update
    suspend fun update(soporte: SoporteEntity)

    @Delete
    suspend fun delete(soporte: SoporteEntity)

    @Query("SELECT * FROM soporte")
    suspend fun getAll(): List<SoporteEntity>

    @Query("SELECT * FROM soporte WHERE id = :id")
    suspend fun getById(id: Int): SoporteEntity?

    @Query("SELECT * FROM soporte WHERE usuarioId = :usuarioId")
    suspend fun getByUsuario(usuarioId: Int): List<SoporteEntity>

    @Query("SELECT * FROM soporte WHERE estado = :estado")
    suspend fun getByEstado(estado: String): List<SoporteEntity>
}