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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class ItineraryViewModel(
    private val destinoId: Int,
    private val itinerarioDao: ItinerarioDao,
    private val itinerarioDiaDao: ItinerarioDiaDao,
    private val actividadDao: ItinerarioDiaActividadDao,
    private val restauranteDao: ItinerarioDiaRestauranteDao,
    private val transporteDao: ItinerarioDiaTransporteDao
) : ViewModel() {

    private val _items = MutableStateFlow<List<ItineraryItem>>(emptyList())
    val items: StateFlow<List<ItineraryItem>> = _items

    init {
        cargarItinerario()
    }

    private fun cargarItinerario() {
        viewModelScope.launch {
            val itinerario = itinerarioDao.getByViaje(destinoId).firstOrNull()
                ?: return@launch

            val dias = itinerarioDiaDao.getByItinerario(itinerario.id)

            val resultado = mutableListOf<ItineraryItem>()

            dias.forEach { dia ->

                val fecha = LocalDate.parse(dia.fecha)

                actividadDao.getByItinerarioDia(dia.id).forEach {
                    resultado.add(
                        ItineraryItem(
                            id = it.idActividad,
                            title = "Actividad",
                            subtitle = "Orden ${it.orden}",
                            date = fecha,
                            type = ItineraryType.ACTIVIDAD
                        )
                    )
                }

                restauranteDao.getByItinerarioDia(dia.id).forEach {
                    resultado.add(
                        ItineraryItem(
                            id = it.idRestaurante,
                            title = "Restaurante",
                            subtitle = it.tipoComida,
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
            }

            _items.value = resultado.sortedBy { it.date }
        }
    }

    fun clearItinerary() {
        _items.value = emptyList()
        // ⚠️ aquí podrías borrar también de Room si quieres
    }
}
