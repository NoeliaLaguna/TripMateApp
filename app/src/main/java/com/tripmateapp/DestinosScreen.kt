package com.tripmateapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tripmateapp.BaseDatos.Destinos.DestinoDao
import com.tripmateapp.BaseDatos.Destinos.DestinoEntity
import com.tripmateapp.BaseDatos.LugaresTuristicos.LugarTuristicoDao
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteDao
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteEntity
import com.tripmateapp.BaseDatos.Transporte.TransporteDao
import com.tripmateapp.BaseDatos.Transporte.TransporteEntity
import com.tripmateapp.BaseDatos.actividades.ActividadDao
import com.tripmateapp.BaseDatos.actividades.ActividadEntity
import java.text.Normalizer

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun generarDiasViaje(
    fechaInicio: Long,
    fechaFin: Long
): List<LocalDate> {

    val start = Instant.ofEpochMilli(fechaInicio)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val end = Instant.ofEpochMilli(fechaFin)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return generateSequence(start) { it.plusDays(1) }
        .takeWhile { !it.isAfter(end) }
        .toList()
}


// --------------------------------------------------------------
// 🔧 FUNCIÓN PARA NORMALIZAR (QUITAR ACENTOS Y MINUSCULIZAR)
// --------------------------------------------------------------
fun String.normalize(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .lowercase()
}

// ------------------------------------------------------------------
//                  PANTALLA PRINCIPAL DE DESTINOS
// ------------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinosScreen(
    destinoDao: DestinoDao,
    actividadDao: ActividadDao,
    restauranteDao: RestauranteDao,
    transporteDao: TransporteDao,
    lugarTuristicoDao: LugarTuristicoDao
) {
    var query by remember { mutableStateOf("") }

    val destinos by destinoDao.getAllFlow().collectAsState(initial = emptyList())

    var destinoSeleccionado by remember { mutableStateOf<DestinoEntity?>(null) }

    var opcionesFiltrado by remember { mutableStateOf<List<DestinoEntity>>(emptyList()) }

    var mostrarSelectorCiudades by remember { mutableStateOf(false) }

    // 📅 FECHAS DEL VIAJE
    var fechaInicio by remember { mutableStateOf<Long?>(null) }
    var fechaFin by remember { mutableStateOf<Long?>(null) }

    var mostrarDatePickerInicio by remember { mutableStateOf(false) }
    var mostrarDatePickerFin by remember { mutableStateOf(false) }


    // Tabs seleccionadas
    var selectedTab by remember { mutableStateOf(0) }

    val diasViaje = remember(fechaInicio, fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            generarDiasViaje(fechaInicio!!, fechaFin!!)
        } else {
            emptyList()
        }
    }


    if (fechaInicio != null && fechaFin != null) {
        when (selectedTab) {
            0 -> ActividadesList(
                destinoSeleccionado!!.id,
                actividadDao,
                diasViaje
            )
            1 -> RestaurantesList(
                destinoSeleccionado!!.id,
                restauranteDao,
                diasViaje
            )
            2 -> TransportesList(
                destinoSeleccionado!!.id,
                transporteDao,
                diasViaje
            )
        }
    }





// --------------------
// DATE PICKERS
// --------------------
    if (mostrarDatePickerInicio) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { mostrarDatePickerInicio = false },
            confirmButton = {
                TextButton(onClick = {
                    fechaInicio = datePickerState.selectedDateMillis
                    mostrarDatePickerInicio = false
                }) { Text("Aceptar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (mostrarDatePickerFin) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { mostrarDatePickerFin = false },
            confirmButton = {
                TextButton(onClick = {
                    fechaFin = datePickerState.selectedDateMillis
                    mostrarDatePickerFin = false
                }) { Text("Aceptar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }


    Scaffold(
        topBar = {
            Column {
                TripMateMaterialTopAppBar(
                    onDatosUsuarioClick = {
                        // aquí luego puedes navegar a datos usuario
                    }
                )

                TripMateTopBar(
                    query = query,
                    onQueryChange = { query = it },
                    onSearchClick = {
                        val queryNorm = query.normalize()

                        val ciudades = destinos.filter { d ->
                            d.nombre.normalize().contains(queryNorm)
                        }

                        destinoSeleccionado = when {
                            ciudades.size == 1 -> ciudades.first()
                            ciudades.size > 1 -> {
                                opcionesFiltrado = ciudades
                                null
                            }
                            else -> {
                                val paises = destinos.filter { d ->
                                    d.pais.normalize().contains(queryNorm)
                                }

                                when {
                                    paises.size == 1 -> paises.first()
                                    paises.size > 1 -> {
                                        opcionesFiltrado = paises
                                        null
                                    }
                                    else -> null
                                }
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {

            // ⭐ ⭐ ⭐
            // 1️⃣ SI HAY FILTRO DE CIUDADES → MOSTRAR OPCIONES
            // ⭐ ⭐ ⭐
            if (opcionesFiltrado.isNotEmpty()) {

                Text(
                    "Hemos encontrado varias ciudades. Selecciona una:",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                LazyColumn {
                    items(opcionesFiltrado) { destino ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    destinoSeleccionado = destino
                                    opcionesFiltrado = emptyList()
                                },
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(destino.nombre, style = MaterialTheme.typography.titleLarge)
                                Text(destino.pais, color = Color.Gray)
                            }
                        }
                    }
                }

                return@Column
            }

            // ⭐ ⭐ ⭐
            // 2️⃣ MOSTRAR INFO DEL DESTINO SELECCIONADO
            // ⭐ ⭐ ⭐
            destinoSeleccionado?.let { destino ->

                // Buscar todas las ciudades del mismo país
                val ciudadesMismoPais = destinos.filter { it.pais == destino.pais }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        // NOMBRE DEL DESTINO
                        Text(destino.nombre, style = MaterialTheme.typography.titleLarge)

                        // PAÍS
                        Text(destino.pais, color = Color.Gray)

                        // DESCRIPCIÓN
                        destino.descripcion?.let {
                            Spacer(Modifier.height(6.dp))
                            Text(it)
                        }

                        // ⭐ MOSTRAR BOTÓN SI HAY MÁS DE UNA CIUDAD
                        if (ciudadesMismoPais.size > 1) {

                            Spacer(Modifier.height(12.dp))

                            TextButton(onClick = {
                                mostrarSelectorCiudades = !mostrarSelectorCiudades
                            }) {
                                Text("Cambiar ciudad (${ciudadesMismoPais.size})")
                                Icon(
                                    imageVector = if (mostrarSelectorCiudades)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }

                            // ⭐ DESPLEGABLE DE CIUDADES
                            if (mostrarSelectorCiudades) {

                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                ) {

                                    ciudadesMismoPais.forEach { ciudad ->

                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(6.dp)
                                                .clickable {
                                                    destinoSeleccionado = ciudad
                                                    mostrarSelectorCiudades = false
                                                },
                                            shape = RoundedCornerShape(10.dp),
                                            elevation = CardDefaults.cardElevation(2.dp)
                                        ) {
                                            Column(Modifier.padding(12.dp)) {
                                                Text(
                                                    ciudad.nombre,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

// ⭐ ⭐ ⭐
// 2️⃣ BIS – SELECCIÓN DE FECHAS
// ⭐ ⭐ ⭐
            destinoSeleccionado?.let {

                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text("Fechas del viaje", style = MaterialTheme.typography.titleMedium)

                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            OutlinedButton(
                                onClick = { mostrarDatePickerInicio = true },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    fechaInicio?.let {
                                        "Inicio: ${
                                            java.text.SimpleDateFormat("dd/MM/yyyy")
                                                .format(it)
                                        }"
                                    } ?: "Fecha inicio"
                                )
                            }

                            OutlinedButton(
                                onClick = { mostrarDatePickerFin = true },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    fechaFin?.let {
                                        "Fin: ${
                                            java.text.SimpleDateFormat("dd/MM/yyyy")
                                                .format(it)
                                        }"
                                    } ?: "Fecha fin"
                                )
                            }
                        }
                    }
                }
            }


            // Si aún no se ha seleccionado destino → parar aquí
            if (destinoSeleccionado == null) {
                return@Column
            }

            Spacer(Modifier.height(12.dp))

            if (fechaInicio == null || fechaFin == null) {
                Text(
                    "Selecciona las fechas del viaje para continuar",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
                return@Column
            }


            // ⭐ ⭐ ⭐
            // 3️⃣ MOSTRAR TABS SOLO CUANDO YA HAY DESTINO
            // ⭐ ⭐ ⭐
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Actividades") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Restaurantes") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Transporte") }
                )
            }

            Spacer(Modifier.height(16.dp))

            when (selectedTab) {
                0 -> ActividadesList(destinoSeleccionado!!.id, actividadDao, diasViaje = diasViaje)
                1 -> RestaurantesList(destinoSeleccionado!!.id, restauranteDao, diasViaje = diasViaje)
                2 -> TransportesList(destinoSeleccionado!!.id, transporteDao, diasViaje = diasViaje)
            }
        }
    }


}

// ------------------------------------------------------------------
//                  TOP BAR ESTILO AIRBNB
// ------------------------------------------------------------------
@Composable
fun TripMateTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 30.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFF2F2F2))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.5f)) {
                Text(
                    text = "Destino:",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Column(modifier = Modifier.weight(2f)) {


                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = { Text("Buscar destinos") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

            }

            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF385C))
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.White
                )
            }
        }
    }
}

// ------------------------------------------------------------------
//                        TARJETAS DE DESTINO
// ------------------------------------------------------------------
@Composable
fun ActividadesList(
    destinoId: Int,
    actividadDao: ActividadDao,
    diasViaje: List<LocalDate>,
    onAddToItinerary: (ActividadEntity, LocalDate) -> Unit = { _, _ -> }
) {
    val actividades by actividadDao.getByDestino(destinoId)
        .collectAsState(initial = emptyList())

    LazyColumn {
        items(actividades) { actividad ->
            ActividadCardExpandable(
                actividad = actividad,
                diasViaje = diasViaje,
                onAddToItinerary = onAddToItinerary
            )
        }
    }
}



@Composable
fun ActividadCardExpandable(
    actividad: ActividadEntity,
    diasViaje: List<LocalDate>,
    onAddToItinerary: (ActividadEntity, LocalDate) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var mostrarDialogoDias by remember { mutableStateOf(false) }
    var diaSeleccionado by remember { mutableStateOf<LocalDate?>(null) }


    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ⭐ SOLO EL TÍTULO EXPANDE LA TARJETA
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = expanded,
                        onValueChange = { expanded = it }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    actividad.descripcion ?: "Sin descripción",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            // EXPANDED CONTENT
            if (expanded) {
                Spacer(Modifier.height(8.dp))

                Text("Tipo de actividad: ${actividad.tipoActividad}")
                Text("Inicio: ${actividad.horaInicio ?: "-"}")
                Text("Fin: ${actividad.horaFin ?: "-"}")
                Text("Orden sugerido: ${actividad.orden}")

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        mostrarDialogoDias = true
                    },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Añadir a itinerario")
                }

            }
        }
    }

    if (mostrarDialogoDias) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoDias = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        diaSeleccionado?.let {
                            onAddToItinerary(actividad, it)
                            mostrarDialogoDias = false
                            expanded = false
                        }
                    },
                    enabled = diaSeleccionado != null
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoDias = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("¿Qué día quieres añadirla?") },
            text = {
                if (diasViaje.isEmpty()) {
                    Text("No hay días disponibles")
                    return@AlertDialog
                }

                Column {
                    diasViaje.forEach { dia ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { diaSeleccionado = dia }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = diaSeleccionado == dia,
                                onClick = { diaSeleccionado = dia }
                            )
                            Text(
                                dia.toString(),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        )
    }

}


@Composable
fun RestaurantesList(
    destinoId: Int,
    restauranteDao: RestauranteDao,
    diasViaje: List<LocalDate>,
    onAddToItinerary: (RestauranteEntity, LocalDate) -> Unit = { _, _ -> }
) {
    val restaurantes by restauranteDao.getByDestino(destinoId)
        .collectAsState(initial = emptyList())

    LazyColumn {
        items(restaurantes) { rest ->
            RestauranteCardExpandable(
                restaurante = rest,
                diasViaje = diasViaje,
                onAddToItinerary = onAddToItinerary
            )
        }
    }
}


@Composable
fun RestauranteCardExpandable(
    restaurante: RestauranteEntity,
    diasViaje: List<LocalDate>,
    onAddToItinerary: (RestauranteEntity, LocalDate) -> Unit
)
 {
     var expanded by remember { mutableStateOf(false) }
     var mostrarDialogoDias by remember { mutableStateOf(false) }
     var diaSeleccionado by remember { mutableStateOf<LocalDate?>(null) }


     Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = expanded,
                        onValueChange = { expanded = it }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    restaurante.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            if (expanded) {
                Spacer(Modifier.height(8.dp))

                Text("Ubicación: ${restaurante.ubicacion}")
                Text("Tipo de comida: ${restaurante.tipoComida}")
                Text("Puntuación: ${restaurante.puntuacionMedia}")
                Text("Horario: ${restaurante.horarioApertura}")
                Text("Precio: ${restaurante.rangoPrecio}")

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        mostrarDialogoDias = true
                    },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Añadir a itinerario")
                }

            }
        }
    }

     if (mostrarDialogoDias) {
         AlertDialog(
             onDismissRequest = { mostrarDialogoDias = false },
             title = { Text("¿Qué día quieres añadirlo?") },
             text = {
                 if (diasViaje.isEmpty()) {
                     Text("No hay días disponibles")
                     return@AlertDialog
                 }

                 Column {
                     diasViaje.forEach { dia ->
                         Row(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .clickable { diaSeleccionado = dia }
                                 .padding(8.dp),
                             verticalAlignment = Alignment.CenterVertically
                         ) {
                             RadioButton(
                                 selected = diaSeleccionado == dia,
                                 onClick = { diaSeleccionado = dia }
                             )
                             Text(
                                 dia.toString(),
                                 modifier = Modifier.padding(start = 8.dp)
                             )
                         }
                     }
                 }
             },
             confirmButton = {
                 TextButton(
                     onClick = {
                         diaSeleccionado?.let {
                             onAddToItinerary(restaurante, it) // o transporte
                             mostrarDialogoDias = false
                             expanded = false
                         }
                     },
                     enabled = diaSeleccionado != null
                 ) {
                     Text("Confirmar")
                 }
             },
             dismissButton = {
                 TextButton(onClick = { mostrarDialogoDias = false }) {
                     Text("Cancelar")
                 }
             }
         )
     }

 }


@Composable
fun TransportesList(
    destinoId: Int,
    transporteDao: TransporteDao,
    diasViaje: List<LocalDate>,
    onAddToItinerary: (TransporteEntity, LocalDate) -> Unit = { _, _ -> }
) {
    val transportes by transporteDao.getByDestino(destinoId)
        .collectAsState(initial = emptyList())

    LazyColumn {
        items(transportes) { tr ->
            TransporteCardExpandable(
                transporte = tr,
                diasViaje = diasViaje,
                onAddToItinerary = onAddToItinerary
            )
        }
    }
}


@Composable
fun TransporteCardExpandable(
    transporte: TransporteEntity,
    diasViaje: List<LocalDate>,
    onAddToItinerary: (TransporteEntity, LocalDate) -> Unit
)
{

    var expanded by remember { mutableStateOf(false) }
    var mostrarDialogoDias by remember { mutableStateOf(false) }
    var diaSeleccionado by remember { mutableStateOf<LocalDate?>(null) }


    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = expanded,
                        onValueChange = { expanded = it }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${transporte.tipo} - ${transporte.nombre ?: ""}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            if (expanded) {
                Spacer(Modifier.height(8.dp))

                Text("Horario: ${transporte.horario ?: "-"}")
                Text("Precio: ${transporte.precio ?: "-"}")

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        mostrarDialogoDias = true
                    },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Añadir a itinerario")
                }

            }
        }
    }

    if (mostrarDialogoDias) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoDias = false },
            title = { Text("¿Qué día quieres añadirlo?") },
            text = {
                if (diasViaje.isEmpty()) {
                    Text("No hay días disponibles")
                    return@AlertDialog
                }

                Column {
                    diasViaje.forEach { dia ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { diaSeleccionado = dia }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = diaSeleccionado == dia,
                                onClick = { diaSeleccionado = dia }
                            )
                            Text(
                                dia.toString(),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        diaSeleccionado?.let {
                            onAddToItinerary(transporte, it)
                            mostrarDialogoDias = false
                            expanded = false
                        }
                    },
                    enabled = diaSeleccionado != null
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoDias = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripMateMaterialTopAppBar(
    onDatosUsuarioClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 🖼️ LOGO A LA IZQUIERDA
                Image(
                    painter = painterResource(id = R.drawable.logo_tripmate),
                    contentDescription = "Logo TripMate",
                    modifier = Modifier
                        .height(32.dp)
                        .padding(end = 8.dp)
                )

                // 🟣 NOMBRE APP
                Text(
                    text = "TripMateApp",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
            }
        },
        actions = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menú"
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Modificar datos usuario") },
                        onClick = {
                            menuExpanded = false
                            onDatosUsuarioClick()
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF3EAF3),
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}



