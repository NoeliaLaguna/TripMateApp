package com.tripmateapp.BaseDatos.BarraNavegacion.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.tripmateapp.BaseDatos.DatabaseProvider
import com.tripmateapp.ItineraryScreen
import com.tripmateapp.ItineraryViewModel
import com.tripmateapp.utilidades.TravelDatesManager
import com.tripmateapp.utilidades.SelectedDestinationManager
import com.tripmateapp.utilidades.ActiveTripManager
import com.tripmateapp.utilidades.UserSessionManager
import kotlinx.coroutines.launch
import java.time.LocalDate

//Cambio 5
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisViajesScreen(navController: NavController) {
    // Get the selected destination ID from DestinosScreen
    val context = LocalContext.current
    val selectedDestinationManager = SelectedDestinationManager(context)
    val activeTripManager = ActiveTripManager(context)
    val userSessionManager = UserSessionManager(context)
    val usuarioId = userSessionManager.getUserId()
    
    // Get database instance
    val database = DatabaseProvider.getDatabase(context)
    val travelDatesManager = TravelDatesManager(context)
    val viajeDao = database.viajeDao()
    val destinoDao = database.destinoDao()

    val viajes by viajeDao.getByUsuario(usuarioId).collectAsState(initial = emptyList())
    val destinos by destinoDao.getAllFlow().collectAsState(initial = emptyList())
    val destinosById = remember(destinos) { destinos.associateBy { it.id } }

    var activeViajeId by remember { mutableStateOf(activeTripManager.getActiveViajeId()) }
    LaunchedEffect(viajes) {
        if (activeViajeId == 0 && viajes.isNotEmpty()) {
            activeViajeId = viajes.first().id
            activeTripManager.setActiveViajeId(activeViajeId)
        }
    }

    val activeViaje = viajes.firstOrNull { it.id == activeViajeId }
    val travelDates = remember(activeViaje) {
        val inicio = activeViaje?.fechaInicio?.let { LocalDate.parse(it) }
        val fin = activeViaje?.fechaFin?.let { LocalDate.parse(it) }
        if (inicio != null && fin != null) {
            generateSequence(inicio) { it.plusDays(1) }
                .takeWhile { !it.isAfter(fin) }
                .toList()
        } else {
            travelDatesManager.getTravelDates()
        }
    }
    
    val itinerarioDao = database.itinerarioDao()
    val itinerarioDiaDao = database.itinerarioDiaDao()
    val itinerarioDiaActividadDao = database.itinerarioDiaActividadDao()
    val itinerarioDiaRestauranteDao = database.itinerarioDiaRestauranteDao()
    val itinerarioDiaTransporteDao = database.itinerarioDiaTransporteDao()
    val itinerarioDiaLugarTuristicoDao = database.itinerarioDiaLugarTuristicoDao()
    val actividadEntityDao = database.actividadDao()
    
    val viewModel = ItineraryViewModel(
        viajeId = activeViajeId,
        itinerarioDao = itinerarioDao,
        itinerarioDiaDao = itinerarioDiaDao,
        actividadDao = itinerarioDiaActividadDao,
        restauranteDao = itinerarioDiaRestauranteDao,
        transporteDao = itinerarioDiaTransporteDao,
        lugarTuristicoDao = itinerarioDiaLugarTuristicoDao,
        actividadEntityDao = actividadEntityDao,
        travelDates = travelDates
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                viajes.forEach { viaje ->
                    val destino = destinosById[viaje.destinoId]
                    val label = buildString {
                        append(viaje.nombre.ifBlank { "Viaje ${viaje.id}" })
                        if (destino != null) {
                            append(" - ")
                            append(destino.nombre)
                        }
                        append(" (")
                        append(viaje.fechaInicio)
                        append(" - ")
                        append(viaje.fechaFin)
                        append(")")
                    }
                    NavigationDrawerItem(
                        label = { Text(label) },
                        selected = viaje.id == activeViajeId,
                        onClick = {
                            activeViajeId = viaje.id
                            activeTripManager.setActiveViajeId(viaje.id)
                        }
                    )
                }
            }
        }
    ) {
        ItineraryScreen(
            navController = navController,
            viewModel = viewModel,
            viajeDao = viajeDao,
            activeTripManager = activeTripManager,
            selectedDestinationManager = selectedDestinationManager,
            travelDatesManager = travelDatesManager,
            userSessionManager = userSessionManager,
            onOpenDrawer = {
                scope.launch { drawerState.open() }
            }
        )
    }
}
