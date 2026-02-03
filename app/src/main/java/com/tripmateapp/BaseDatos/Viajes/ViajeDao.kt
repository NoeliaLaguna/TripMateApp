package com.tripmateapp.BaseDatos.Viajes

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ViajeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(viaje: ViajeEntity): Long

    @Update
    suspend fun update(viaje: ViajeEntity)

    @Delete
    suspend fun delete(viaje: ViajeEntity)

    @Query("SELECT * FROM viajes")
    suspend fun getAll(): List<ViajeEntity>

    @Query("SELECT * FROM viajes WHERE id = :id")
    suspend fun getById(id: Int): ViajeEntity?

    @Query("SELECT * FROM viajes WHERE usuarioId = :usuarioId")
    fun getByUsuario(usuarioId: Int): Flow<List<ViajeEntity>>

    @Query("SELECT * FROM viajes WHERE destinoId = :destinoId")
    suspend fun getByDestino(destinoId: Int): List<ViajeEntity>
}