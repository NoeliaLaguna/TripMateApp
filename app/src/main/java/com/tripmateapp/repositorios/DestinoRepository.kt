package com.tripmateapp.repositorios

import com.tripmateapp.BaseDatos.Destinos.DestinoDao
import com.tripmateapp.BaseDatos.Destinos.DestinoEntity
import kotlinx.coroutines.flow.Flow

class DestinoRepository(
    private val destinoDao: DestinoDao
) {

    // Obtener todos los destinos de la base de datos
    fun getDestinos(): Flow<List<DestinoEntity>> =
        destinoDao.getAll()

    // Buscar destinos por nombre
    fun searchDestinos(query: String): Flow<List<DestinoEntity>> =
        destinoDao.searchDestinos(query)
}