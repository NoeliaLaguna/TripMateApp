package com.tripmateapp.BaseDatos.Restaurantes


import androidx.room.*

@Dao
interface RestauranteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurante: RestauranteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(restaurantes: List<RestauranteEntity>)

    @Update
    suspend fun update(restaurante: RestauranteEntity)

    @Delete
    suspend fun delete(restaurante: RestauranteEntity)

    @Query("SELECT * FROM restaurantes")
    suspend fun getAll(): List<RestauranteEntity>

    @Query("SELECT * FROM restaurantes WHERE destinoId = :destinoId")
    suspend fun getByDestino(destinoId: Int): List<RestauranteEntity>
}