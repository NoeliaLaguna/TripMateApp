package com.tripmateapp

import android.os.Build
import androidx.annotation.RequiresApi
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDao
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioEntity
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias.ItinerarioDiaDao
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias.ItinerarioDiaEntity
import com.tripmateapp.BaseDatos.ItinerarioDiaActividades.ItinerarioDiaActividadDao
import com.tripmateapp.BaseDatos.ItinerarioDiaActividades.ItinerarioDiaActividadEntity
import com.tripmateapp.BaseDatos.ItinerarioDiaRestaurantes.ItinerarioDiaRestauranteDao
import com.tripmateapp.BaseDatos.ItinerarioDiaRestaurantes.ItinerarioDiaRestauranteEntity
import com.tripmateapp.BaseDatos.ItinerarioDiaTransportes.ItinerarioDiaTransporteDao
import com.tripmateapp.BaseDatos.ItinerarioDiaTransportes.ItinerarioDiaTransporteEntity
import com.tripmateapp.BaseDatos.ItinerarioDiaLugaresTuristicos.ItinerarioDiaLugarTuristicoDao
import com.tripmateapp.BaseDatos.ItinerarioDiaLugaresTuristicos.ItinerarioDiaLugarTuristicoEntity
import com.tripmateapp.BaseDatos.actividades.ActividadEntity
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteEntity
import com.tripmateapp.BaseDatos.Transporte.TransporteEntity
import com.tripmateapp.BaseDatos.LugaresTuristicos.LugarTuristicoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
object ItinerarioManager {
    
    fun addActividadToItinerary(
        actividad: ActividadEntity,
        dia: LocalDate,
        diasViaje: List<LocalDate>,
        destinoId: Int,
        itinerarioDao: ItinerarioDao,
        itinerarioDiaDao: ItinerarioDiaDao,
        itinerarioDiaActividadDao: ItinerarioDiaActividadDao,
        scope: CoroutineScope
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                // 1. Obtener o crear el itinerario para este destino
                val itinerarios = itinerarioDao.getByViaje(destinoId)
                val itinerario = if (itinerarios.isEmpty()) {
                    val nuevoItinerario = ItinerarioEntity(
                        idViaje = destinoId,
                        nombre = "Itinerario $destinoId",
                        fecha = LocalDate.now().toString()
                    )
                    itinerarioDao.insert(nuevoItinerario)
                    nuevoItinerario
                } else {
                    itinerarios.first()
                }

                // 2. Obtener o crear el día del itinerario
                val diaIndex = diasViaje.indexOf(dia)
                val itinerarioDiaId = itinerario.id + diaIndex + 1000
                
                // Verificar si el día ya existe
                var itinerarioDia = itinerarioDiaDao.getById(itinerarioDiaId)
                if (itinerarioDia == null) {
                    itinerarioDia = ItinerarioDiaEntity(
                        id = itinerarioDiaId,
                        idItinerario = itinerario.id,
                        fecha = dia.toString(),
                        horaInicio = "00:00",
                        horaFin = "23:59"
                    )
                    itinerarioDiaDao.insert(itinerarioDia)
                }

                // 3. Añadir la actividad al día del itinerario
                val itinerarioActividad = ItinerarioDiaActividadEntity(
                    idItinerarioDia = itinerarioDia.id,
                    idActividad = actividad.id,
                    orden = actividad.orden,
                    horaInicio = actividad.horaInicio
                )
                itinerarioDiaActividadDao.insert(itinerarioActividad)
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addRestauranteToItinerary(
        restaurante: RestauranteEntity,
        dia: LocalDate,
        diasViaje: List<LocalDate>,
        destinoId: Int,
        itinerarioDao: ItinerarioDao,
        itinerarioDiaDao: ItinerarioDiaDao,
        itinerarioDiaRestauranteDao: ItinerarioDiaRestauranteDao,
        scope: CoroutineScope
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                // 1. Obtener o crear el itinerario
                val itinerarios = itinerarioDao.getByViaje(destinoId)
                val itinerario = if (itinerarios.isEmpty()) {
                    val nuevoItinerario = ItinerarioEntity(
                        idViaje = destinoId,
                        nombre = "Itinerario $destinoId",
                        fecha = LocalDate.now().toString()
                    )
                    itinerarioDao.insert(nuevoItinerario)
                    nuevoItinerario
                } else {
                    itinerarios.first()
                }

                // 2. Obtener o crear el día del itinerario
                val diaIndex = diasViaje.indexOf(dia)
                val itinerarioDiaId = itinerario.id + diaIndex + 1000
                
                var itinerarioDia = itinerarioDiaDao.getById(itinerarioDiaId)
                if (itinerarioDia == null) {
                    itinerarioDia = ItinerarioDiaEntity(
                        id = itinerarioDiaId,
                        idItinerario = itinerario.id,
                        fecha = dia.toString(),
                        horaInicio = "00:00",
                        horaFin = "23:59"
                    )
                    itinerarioDiaDao.insert(itinerarioDia)
                }

                // 3. Añadir el restaurante al día del itinerario
                val itinerarioRestaurante = ItinerarioDiaRestauranteEntity(
                    idItinerarioDia = itinerarioDia.id,
                    idRestaurante = restaurante.id,
                    tipoComida = "almuerzo"
                )
                itinerarioDiaRestauranteDao.insert(itinerarioRestaurante)
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addTransporteToItinerary(
        transporte: TransporteEntity,
        dia: LocalDate,
        diasViaje: List<LocalDate>,
        destinoId: Int,
        itinerarioDao: ItinerarioDao,
        itinerarioDiaDao: ItinerarioDiaDao,
        itinerarioDiaTransporteDao: ItinerarioDiaTransporteDao,
        scope: CoroutineScope
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                // 1. Obtener o crear el itinerario
                val itinerarios = itinerarioDao.getByViaje(destinoId)
                val itinerario = if (itinerarios.isEmpty()) {
                    val nuevoItinerario = ItinerarioEntity(
                        idViaje = destinoId,
                        nombre = "Itinerario $destinoId",
                        fecha = LocalDate.now().toString()
                    )
                    itinerarioDao.insert(nuevoItinerario)
                    nuevoItinerario
                } else {
                    itinerarios.first()
                }

                // 2. Obtener o crear el día del itinerario
                val diaIndex = diasViaje.indexOf(dia)
                val itinerarioDiaId = itinerario.id + diaIndex + 1000
                
                var itinerarioDia = itinerarioDiaDao.getById(itinerarioDiaId)
                if (itinerarioDia == null) {
                    itinerarioDia = ItinerarioDiaEntity(
                        id = itinerarioDiaId,
                        idItinerario = itinerario.id,
                        fecha = dia.toString(),
                        horaInicio = "00:00",
                        horaFin = "23:59"
                    )
                    itinerarioDiaDao.insert(itinerarioDia)
                }

                // 3. Añadir el transporte al día del itinerario
                val itinerarioTransporte = ItinerarioDiaTransporteEntity(
                    idItinerarioDia = itinerarioDia.id,
                    idTransporte = transporte.id,
                    hora = transporte.horario
                )
                itinerarioDiaTransporteDao.insert(itinerarioTransporte)
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addLugarTuristicoToItinerary(
        lugar: LugarTuristicoEntity,
        dia: LocalDate,
        diasViaje: List<LocalDate>,
        destinoId: Int,
        itinerarioDao: ItinerarioDao,
        itinerarioDiaDao: ItinerarioDiaDao,
        itinerarioDiaLugarTuristicoDao: ItinerarioDiaLugarTuristicoDao,
        scope: CoroutineScope
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                // 1. Obtener o crear el itinerario
                val itinerarios = itinerarioDao.getByViaje(destinoId)
                val itinerario = if (itinerarios.isEmpty()) {
                    val nuevoItinerario = ItinerarioEntity(
                        idViaje = destinoId,
                        nombre = "Itinerario $destinoId",
                        fecha = LocalDate.now().toString()
                    )
                    itinerarioDao.insert(nuevoItinerario)
                    nuevoItinerario
                } else {
                    itinerarios.first()
                }

                // 2. Obtener o crear el día del itinerario
                val diaIndex = diasViaje.indexOf(dia)
                val itinerarioDiaId = itinerario.id + diaIndex + 1000
                
                var itinerarioDia = itinerarioDiaDao.getById(itinerarioDiaId)
                if (itinerarioDia == null) {
                    itinerarioDia = ItinerarioDiaEntity(
                        id = itinerarioDiaId,
                        idItinerario = itinerario.id,
                        fecha = dia.toString(),
                        horaInicio = "00:00",
                        horaFin = "23:59"
                    )
                    itinerarioDiaDao.insert(itinerarioDia)
                }

                // 3. Añadir el lugar turístico al día del itinerario
                val itinerarioLugar = ItinerarioDiaLugarTuristicoEntity(
                    idItinerarioDia = itinerarioDia.id,
                    idLugarTuristico = lugar.id
                )
                itinerarioDiaLugarTuristicoDao.insert(itinerarioLugar)
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
