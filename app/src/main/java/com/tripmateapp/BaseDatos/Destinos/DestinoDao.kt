package com.tripmateapp.BaseDatos.Destinos

import androidx.room.*
import kotlinx.coroutines.flow.Flow

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
    fun getAll(): Flow<List<DestinoEntity>>

    // 🔍 Búsqueda por nombre (para searchDestinos del repository)
    @Query("SELECT * FROM destinos WHERE nombre LIKE '%' || :query || '%'")
    fun searchDestinos(query: String): Flow<List<DestinoEntity>>

    // 🔍 Búsqueda por pais (para searchDestinos del repository)
    @Query("SELECT * FROM destinos WHERE pais LIKE '%' || :query || '%'")
    fun searchDestinosPorPais(query: String): Flow<List<DestinoEntity>>

    // Si quieres mantener esto también como Flow
    @Query("SELECT * FROM destinos WHERE id = :id")
    fun getById(id: Int): Flow<DestinoEntity?>
}