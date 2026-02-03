package com.tripmateapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDao
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias.ItinerarioDiaDao
import com.tripmateapp.BaseDatos.ItinerarioDiaActividades.ItinerarioDiaActividadDao
import com.tripmateapp.BaseDatos.ItinerarioDiaRestaurantes.ItinerarioDiaRestauranteDao
import com.tripmateapp.BaseDatos.ItinerarioDiaTransportes.ItinerarioDiaTransporteDao
import com.tripmateapp.BaseDatos.ItinerarioDiaLugaresTuristicos.ItinerarioDiaLugarTuristicoDao
import com.tripmateapp.BaseDatos.actividades.ActividadDao
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteDao
import com.tripmateapp.BaseDatos.LugaresTuristicos.LugarTuristicoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class ItineraryViewModel(
    val viajeId: Int,
    private val itinerarioDao: ItinerarioDao,
    private val itinerarioDiaDao: ItinerarioDiaDao,
    private val actividadDao: ItinerarioDiaActividadDao,
    private val restauranteDao: ItinerarioDiaRestauranteDao,
    private val transporteDao: ItinerarioDiaTransporteDao,
    private val lugarTuristicoDao: ItinerarioDiaLugarTuristicoDao,
    private val actividadEntityDao: ActividadDao,
    private val restauranteEntityDao: RestauranteDao,
    private val lugarTuristicoEntityDao: LugarTuristicoDao,
    private val travelDates: List<LocalDate> = emptyList()
) : ViewModel() {

    private val _items = MutableStateFlow<List<ItineraryItem>>(emptyList())
    val items: StateFlow<List<ItineraryItem>> = _items

    private val _selectedDay = MutableStateFlow<LocalDate?>(null)
    val selectedDay: StateFlow<LocalDate?> = _selectedDay

    val availableDays: List<LocalDate>
        get() = {
            val itemDates = _items.value.map { it.date }.distinct()
            val allDates = (travelDates + itemDates).distinct().sorted()
            allDates
        }()

    init {
        cargarItinerario()
    }

    private fun cargarItinerario() {
        viewModelScope.launch {
            println("DEBUG: Loading itinerary for viajeId $viajeId")
            val itinerario = itinerarioDao.getByViaje(viajeId).firstOrNull()
            if (itinerario == null) {
                println("DEBUG: No itinerary found for viajeId $viajeId")
                return@launch
            }

            println("DEBUG: Found itinerary with ID ${itinerario.id}")
            val dias = itinerarioDiaDao.getByItinerario(itinerario.id)
            println("DEBUG: Found ${dias.size} days in itinerary")

            val resultado = mutableListOf<ItineraryItem>()

            dias.forEach { dia ->
                println("DEBUG: Processing day ${dia.id} with date ${dia.fecha}")

                val fecha = LocalDate.parse(dia.fecha)

                actividadDao.getByItinerarioDia(dia.id).forEach {
                    val actividadEntity = actividadEntityDao.getById(it.idActividad)
                    println("DEBUG: Found actividad ${it.idActividad} for day ${dia.id}")
                    resultado.add(
                        ItineraryItem(
                            id = it.idActividad,
                            title = actividadEntity?.tipoActividad ?: "Actividad",
                            subtitle = actividadEntity?.descripcion ?: "",
                            date = fecha,
                            type = ItineraryType.ACTIVIDAD
                        )
                    )
                }

                restauranteDao.getByItinerarioDia(dia.id).forEach {
                    val restauranteEntity = restauranteEntityDao.getById(it.idRestaurante)
                    resultado.add(
                        ItineraryItem(
                            id = it.idRestaurante,
                            title = restauranteEntity?.nombre ?: "Restaurante",
                            subtitle = buildString {
                                val ubicacion = restauranteEntity?.ubicacion
                                val tipo = restauranteEntity?.tipoComida ?: it.tipoComida
                                if (!ubicacion.isNullOrBlank()) {
                                    append(ubicacion)
                                }
                                if (!tipo.isNullOrBlank()) {
                                    if (isNotEmpty()) append(" · ")
                                    append(tipo)
                                }
                            },
                            date = fecha,
                            type = ItineraryType.RESTAURANTE
                        )
                    )
                }

                transporteDao.getByItinerarioDia(dia.id).forEach {
                    resultado.add(
                        ItineraryItem(
                            id = it.idTransporte,
                            title = "Transporte",
                            subtitle = it.hora,
                            date = fecha,
                            type = ItineraryType.TRANSPORTE
                        )
                    )
                }

                lugarTuristicoDao.getByItinerarioDia(dia.id).forEach {
                    val lugarEntity = lugarTuristicoEntityDao.getById(it.idLugarTuristico)
                    resultado.add(
                        ItineraryItem(
                            id = it.idLugarTuristico,
                            title = lugarEntity?.nombre ?: "Lugar Turístico",
                            subtitle = lugarEntity?.descripcion?.takeIf { desc -> desc.isNotBlank() }
                                ?: buildString {
                                    val ubicacion = lugarEntity?.ubicacion
                                    val categoria = lugarEntity?.categoria
                                    if (!ubicacion.isNullOrBlank()) {
                                        append(ubicacion)
                                    }
                                    if (!categoria.isNullOrBlank()) {
                                        if (isNotEmpty()) append(" · ")
                                        append(categoria)
                                    }
                                },
                            date = fecha,
                            type = ItineraryType.LUGAR_TURISTICO
                        )
                    )
                }
            }

            _items.value = resultado.sortedBy { it.date }
            println("DEBUG: Total items loaded: ${resultado.size}")
            
            // Auto-select first day if no day is selected
            if (_selectedDay.value == null) {
                val allAvailableDays = availableDays
                if (allAvailableDays.isNotEmpty()) {
                    _selectedDay.value = allAvailableDays.first()
                    println("DEBUG: Auto-selected day: ${_selectedDay.value}")
                }
            }
        }
    }

    fun selectDay(date: LocalDate) {
        _selectedDay.value = date
    }

    fun getItemsForSelectedDay(): List<ItineraryItem> {
        val selected = _selectedDay.value ?: return emptyList()
        val allItems = _items.value
        val filteredItems = allItems.filter { it.date == selected }.sortedBy { it.date }
        println("DEBUG: getItemsForSelectedDay - selected: $selected, total items: ${allItems.size}, filtered items: ${filteredItems.size}")
        return filteredItems
    }

    fun refreshItinerary() {
        cargarItinerario()
    }

    fun clearItinerary() {
        _items.value = emptyList()
        // ⚠️ aquí podrías borrar también de Room si quieres
    }
}
