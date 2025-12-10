package com.tripmateapp.BaseDatos.Viajes

import androidx.room.*

@Dao
interface ViajeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(viaje: ViajeEntity)

    @Update
    suspend fun update(viaje: ViajeEntity)

    @Delete
    suspend fun delete(viaje: ViajeEntity)

    @Query("SELECT * FROM viajes")
    suspend fun getAll(): List<ViajeEntity>

    @Query("SELECT * FROM viajes WHERE usuarioId = :usuarioId")
    suspend fun getByUsuario(usuarioId: Int): List<ViajeEntity>

    @Query("SELECT * FROM viajes WHERE destinoId = :destinoId")
    suspend fun getByDestino(destinoId: Int): List<ViajeEntity>
}