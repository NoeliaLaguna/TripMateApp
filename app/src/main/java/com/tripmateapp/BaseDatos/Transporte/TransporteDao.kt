package com.tripmateapp.BaseDatos.Transporte


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransporteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transporte: TransporteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transportes: List<TransporteEntity>)

    @Update
    suspend fun update(transporte: TransporteEntity)

    @Delete
    suspend fun delete(transporte: TransporteEntity)

    @Query("SELECT * FROM transportes")
    suspend fun getAll(): List<TransporteEntity>

    @Query("SELECT * FROM transportes WHERE destinoId = :destinoId")
     fun getByDestino(destinoId: Int): Flow<List<TransporteEntity>>
}