package com.tripmateapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tripmateapp.ItineraryViewModel
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.tripmateapp.BaseDatos.Viajes.ViajeDao
import com.tripmateapp.BaseDatos.Viajes.ViajeEntity
import com.tripmateapp.utilidades.ActiveTripManager
import com.tripmateapp.utilidades.SelectedDestinationManager
import com.tripmateapp.utilidades.TravelDatesManager
import com.tripmateapp.utilidades.UserSessionManager
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

// ------------------------------------------------------------------
//                  PANTALLA DE ITINERARIO (VERSIÓN CORRECTA)
// ------------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(
    navController: NavController,
    viewModel: ItineraryViewModel,
    viajeDao: ViajeDao,
    activeTripManager: ActiveTripManager,
    selectedDestinationManager: SelectedDestinationManager,
    travelDatesManager: TravelDatesManager,
    userSessionManager: UserSessionManager,
    onOpenDrawer: (() -> Unit)? = null
) {

    // 🔹 Escuchamos los datos desde el ViewModel
    val itineraryItems by viewModel.items.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val availableDays = viewModel.availableDays

    val itemsForSelectedDay = remember(itineraryItems, selectedDay) {
        val day = selectedDay
        if (day == null) {
            emptyList()
        } else {
            itineraryItems.filter { it.date == day }.sortedBy { it.date }
        }
    }
    
    // 🔹 Refresh data when screen becomes focused
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshItinerary()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val scope = rememberCoroutineScope()
    var showSaveTripDialog by remember { mutableStateOf(false) }
    var tripName by remember { mutableStateOf("") }
    var currentViaje by remember { mutableStateOf<ViajeEntity?>(null) }

    LaunchedEffect(viewModel.viajeId) {
        currentViaje = if (viewModel.viajeId > 0) viajeDao.getById(viewModel.viajeId) else null
        if (tripName.isBlank()) {
            tripName = currentViaje?.nombre ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Itinerario del viaje") },
                navigationIcon = {
                    Row {
                        if (onOpenDrawer != null) {
                            IconButton(onClick = onOpenDrawer) {
                                Icon(Icons.Default.Menu, contentDescription = "Menú")
                            }
                        }
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                actions = {
                    if (itineraryItems.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearItinerary() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Vaciar itinerario",
                                tint = Color.Red
                            )
                        }
                    }
                }
            )
        }
        ,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Guardar viaje") },
                icon = { Icon(Icons.Default.Menu, contentDescription = null) },
                onClick = {
                    tripName = currentViaje?.nombre ?: ""
                    showSaveTripDialog = true
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Day Navigation Bar - Always show if there are available days
            if (availableDays.isNotEmpty()) {
                DayNavigationBar(
                    availableDays = availableDays,
                    selectedDay = selectedDay,
                    onDaySelected = { day -> viewModel.selectDay(day) }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (itineraryItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No has añadido nada al itinerario todavía",
                        color = Color.Gray
                    )
                }
            } else {
                // Items for selected day
                if (itemsForSelectedDay.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay actividades para este día",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(itemsForSelectedDay) { item ->
                            ItineraryItemCard(item)
                        }
                    }
                }
            }
        }
    }

    if (showSaveTripDialog) {
        AlertDialog(
            onDismissRequest = { showSaveTripDialog = false },
            title = { Text("Guardar viaje") },
            text = {
                TextField(
                    value = tripName,
                    onValueChange = { tripName = it },
                    singleLine = true,
                    maxLines = 1,
                    placeholder = { Text("Nombre del viaje") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val nameToSave = tripName.trim()
                        if (nameToSave.isEmpty()) return@TextButton

                        scope.launch {
                            if (viewModel.viajeId > 0) {
                                currentViaje?.let { viajeDao.update(it.copy(nombre = nameToSave)) }
                            } else {
                                val usuarioId = userSessionManager.getUserId()
                                val destinoId = selectedDestinationManager.getSelectedDestination()
                                val inicioMs = travelDatesManager.getFechaInicioMillis()
                                val finMs = travelDatesManager.getFechaFinMillis()

                                if (usuarioId > 0 && destinoId > 0 && inicioMs != null && finMs != null) {
                                    val fechaInicio = Instant.ofEpochMilli(inicioMs)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                        .toString()
                                    val fechaFin = Instant.ofEpochMilli(finMs)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                        .toString()

                                    val newViaje = ViajeEntity(
                                        nombre = nameToSave,
                                        usuarioId = usuarioId,
                                        destinoId = destinoId,
                                        fechaInicio = fechaInicio,
                                        fechaFin = fechaFin,
                                        presupuesto = null
                                    )
                                    val newId = viajeDao.insert(newViaje).toInt()
                                    activeTripManager.setActiveViajeId(newId)
                                }
                            }
                        }
                        showSaveTripDialog = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveTripDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// ------------------------------------------------------------------
//          BARRA DE NAVEGACIÓN DE DÍAS
// ------------------------------------------------------------------
@Composable
fun DayNavigationBar(
    availableDays: List<java.time.LocalDate>,
    selectedDay: java.time.LocalDate?,
    onDaySelected: (java.time.LocalDate) -> Unit
) {
    if (availableDays.isEmpty()) return
    
    val scrollState = rememberScrollState()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Seleccionar día:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableDays.forEach { day ->
                    FilterChip(
                        onClick = { onDaySelected(day) },
                        label = {
                            Text(
                                text = "${day.dayOfMonth}/${day.monthValue}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        selected = selectedDay == day,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

// ------------------------------------------------------------------
//                  TARJETA DE ITEM
// ------------------------------------------------------------------
@Composable
fun ItineraryItemCard(item: ItineraryItem) {

    val icon = when (item.type) {
        ItineraryType.ACTIVIDAD -> Icons.Default.LocalActivity
        ItineraryType.RESTAURANTE -> Icons.Default.Restaurant
        ItineraryType.TRANSPORTE -> Icons.Default.DirectionsBus
        ItineraryType.LUGAR_TURISTICO -> Icons.Default.Place
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
                item.subtitle?.let {
                    Text(
                        text = it,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
