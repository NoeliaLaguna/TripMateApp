package com.tripmateapp.BaseDatos.DestinosFavoritos

import androidx.room.*

@Dao
interface DestinoFavoritoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorito: DestinoFavoritoEntity)

    @Delete
    suspend fun delete(favorito: DestinoFavoritoEntity)

    @Query("SELECT * FROM destinos_favoritos WHERE usuarioId = :usuarioId")
    suspend fun getFavoritosDeUsuario(usuarioId: Int): List<DestinoFavoritoEntity>

    @Query("SELECT * FROM destinos_favoritos WHERE usuarioId = :usuarioId AND destinoId = :destinoId")
    suspend fun isFavorito(usuarioId: Int, destinoId: Int): DestinoFavoritoEntity?
}