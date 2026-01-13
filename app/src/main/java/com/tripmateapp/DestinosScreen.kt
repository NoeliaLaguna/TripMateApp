package com.tripmateapp

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import com.tripmateapp.BaseDatos.LugaresTuristicos.LugarTuristicoEntity
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteDao
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteEntity
import com.tripmateapp.BaseDatos.Transporte.TransporteDao
import com.tripmateapp.BaseDatos.Transporte.TransporteEntity
import com.tripmateapp.BaseDatos.actividades.ActividadDao
import com.tripmateapp.BaseDatos.actividades.ActividadEntity
import java.text.Normalizer
import androidx.compose.runtime.rememberCoroutineScope
import com.tripmateapp.BaseDatos.actividades.ActividadEntity
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Euro
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteEntity
import com.tripmateapp.BaseDatos.Transporte.TransporteEntity


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
@Composable
fun DestinosScreen(
    destinoDao: DestinoDao,
    actividadDao: ActividadDao,
    restauranteDao: RestauranteDao,
    transporteDao: TransporteDao,
    lugarDao: LugarTuristicoDao,
) {
    var query by remember { mutableStateOf("") }

    val destinos by destinoDao.getAllFlow().collectAsState(initial = emptyList())

    var destinoIdSeleccionado by remember { mutableStateOf<Int?>(null) }

    var opcionesFiltrado by remember { mutableStateOf<List<DestinoEntity>>(emptyList()) }

    var mostrarSelectorCiudades by remember { mutableStateOf(false) }



    // Tabs seleccionadas
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TripMateTopBar(
                query = query,
                onQueryChange = { query = it },
                onSearchClick = {
                    val encontrado = destinos.find {
                        it.nombre.normalize().contains(query.normalize())
                    }
                    destinoIdSeleccionado = encontrado?.id
                    selectedTab = 0
                }
            )
        }
    ) { innerPadding ->

        // DESTINO ENCONTRADO → MOSTRAR TABS
        Column(modifier = Modifier.padding(innerPadding)) {

            // ✅ LAS TABS SIEMPRE
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selectedTab == 0, { selectedTab = 0 }) { Text("Lugares") }
                Tab(selectedTab == 1, { selectedTab = 1 }) { Text("Actividades") }
                Tab(selectedTab == 2, { selectedTab = 2 }) { Text("Restaurantes") }
                Tab(selectedTab == 3, { selectedTab = 3 }) { Text("Transporte") }
            }

            Spacer(Modifier.height(12.dp))

            // ⬇️ SOLO el contenido depende del destino
            if (destinoIdSeleccionado == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Introduce un destino y pulsa buscar", color = Color.Gray)
                }
            } else {
                when (selectedTab) {
                    0 -> LugaresList(destinoIdSeleccionado!!, lugarDao, actividadDao)
                    1 -> ActividadesList(destinoIdSeleccionado!!, actividadDao)
                    2 -> RestaurantesList(destinoIdSeleccionado!!, restauranteDao, actividadDao)
                    3 -> TransportesList(destinoIdSeleccionado!!, transporteDao, actividadDao)
                }
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

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 40.dp, top = 15.dp)

                .fillMaxWidth()) {
            Image(
                painter = painterResource(R.drawable.logo_tripmate),
                contentDescription = "Logo TripMate",
                modifier = Modifier.height(60.dp)
            )
        }

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
            )}

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
    actividadDao: ActividadDao
) {
    val scope = rememberCoroutineScope()

    // 🔴 NUEVO → actividad a confirmar
    var actividadAConfirmar by remember { mutableStateOf<ActividadEntity?>(null) }

    val actividades by actividadDao
        .getByDestino(destinoId)
        .collectAsState(initial = emptyList())

    if (actividades.isEmpty()) {
        Text(
            text = "No hay actividades disponibles",
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    LazyColumn {
        items(actividades) { actividad ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // ─── TÍTULO + BOTÓN + ───
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = actividad.tipoActividad,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = { actividadAConfirmar = actividad }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Añadir al itinerario"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // ─── DESCRIPCIÓN ───
                    Text(
                        text = actividad.descripcion ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // ─── ORDEN ───
                    Text(
                        text = "Orden del día: ${actividad.orden}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
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
                        onAddToItinerary(actividad)
                        expanded = false
                    },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Añadir a itinerario")
                }
            }
        }
    }

    // ─────────────────────────────
    // 🪟 DIÁLOGO DE CONFIRMACIÓN
    // ─────────────────────────────
    actividadAConfirmar?.let { actividad ->

        AlertDialog(
            onDismissRequest = {
                actividadAConfirmar = null
            },
            title = {
                Text("Añadir actividad")
            },
            text = {
                Text("¿Quieres añadir esta actividad a tu itinerario?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            actividadDao.insert(
                                actividad.copy(
                                    id = 0,
                                    idItinerarioDia = null
                                )
                            )
                        }
                        actividadAConfirmar = null
                    }
                ) {
                    Text("Añadir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { actividadAConfirmar = null }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@Composable
fun RestaurantesList(
    destinoId: Int,
    restauranteDao: RestauranteDao,
    actividadDao: ActividadDao
) {
    val scope = rememberCoroutineScope()
    var restauranteAConfirmar by remember { mutableStateOf<RestauranteEntity?>(null) }

    val restaurantes by restauranteDao
        .getByDestino(destinoId)
        .collectAsState(initial = emptyList())

    LazyColumn {
        items(restaurantes) { rest ->

            val valoracionSegura = rest.puntuacionMedia ?: 0.0

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // ─── NOMBRE + PLUS ───
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = rest.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { restauranteAConfirmar = rest }) {
                            Icon(Icons.Default.Add, contentDescription = "Añadir")
                        }
                    }

                    Text(
                        text = rest.tipoComida,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(8.dp))

                    // ⭐ ESTRELLAS
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < valoracionSegura.toInt())
                                    Color(0xFFFFC107)
                                else Color.LightGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(valoracionSegura.toString())
                    }

                    Spacer(Modifier.height(8.dp))

                    // 💰 PRECIO
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Euro,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(rest.rangoPrecio)
                    }
                }
            }
        }
    }

    // 🪟 DIÁLOGO
    restauranteAConfirmar?.let { rest ->
        AlertDialog(
            onDismissRequest = { restauranteAConfirmar = null },
            title = { Text("Añadir restaurante") },
            text = { Text("¿Quieres añadir ${rest.nombre} a tu itinerario?") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        actividadDao.insert(
                            ActividadEntity(
                                id = 0,
                                idItinerarioDia = null,
                                destinoId = destinoId,
                                tipoActividad = "Gastronomía",
                                orden = 999,
                                descripcion = "Comer en ${rest.nombre}",
                                horaInicio = null,
                                horaFin = null
                            )
                        )
                    }
                    restauranteAConfirmar = null
                }) { Text("Añadir") }
            },
            dismissButton = {
                TextButton(onClick = { restauranteAConfirmar = null }) {
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
    actividadDao: ActividadDao
) {
    val scope = rememberCoroutineScope()
    var transporteAConfirmar by remember { mutableStateOf<TransporteEntity?>(null) }

    val transportes by transporteDao
        .getByDestino(destinoId)
        .collectAsState(initial = emptyList())

    LazyColumn {
        items(transportes) { tr ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${tr.tipo} - ${tr.nombre ?: ""}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { transporteAConfirmar = tr }) {
                            Icon(Icons.Default.Add, contentDescription = "Añadir")
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Text("Horario: ${tr.horario}")

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Euro,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("${tr.precio} €")
                    }
                }
            }
        }
    }

    // 🪟 DIÁLOGO
    transporteAConfirmar?.let { tr ->
        AlertDialog(
            onDismissRequest = { transporteAConfirmar = null },
            title = { Text("Añadir transporte") },
            text = { Text("¿Quieres añadir ${tr.tipo} ${tr.nombre ?: ""} a tu itinerario?") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        actividadDao.insert(
                            ActividadEntity(
                                id = 0,
                                idItinerarioDia = null,
                                destinoId = destinoId,
                                tipoActividad = "Transporte",
                                orden = 999,
                                descripcion = "${tr.tipo} - ${tr.nombre}",
                                horaInicio = null,
                                horaFin = null
                            )
                        )
                    }
                    transporteAConfirmar = null
                }) { Text("Añadir") }
            },
            dismissButton = {
                TextButton(onClick = { transporteAConfirmar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
@Composable
fun TransporteCardExpandable(
    transporte: TransporteEntity,
    onAddToItinerary: (TransporteEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
                Text("${transporte.tipo} - ${transporte.nombre ?: ""}", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
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
                        onAddToItinerary(transporte)
                        expanded = false
                    },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Añadir a itinerario")
                }
            }
        }
    }
}



@Composable
fun LugaresList(
    destinoId: Int,
    lugarDao: LugarTuristicoDao,
    actividadDao: ActividadDao
) {
    val scope = rememberCoroutineScope()

    // 🔴 NUEVO → lugar pendiente de confirmación
    var lugarAConfirmar by remember { mutableStateOf<LugarTuristicoEntity?>(null) }

    val lugares by lugarDao
        .getByDestino(destinoId)
        .collectAsState(initial = emptyList())

    if (lugares.isEmpty()) {
        Text(
            text = "No hay lugares disponibles",
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    LazyColumn {
        items(lugares) { lugar ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(12.dp)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    // ───── FILA SUPERIOR (NOMBRE + BOTÓN +) ─────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = lugar.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )

                        // ➕ BOTÓN DISCRETO
                        IconButton(
                            onClick = {
                                lugarAConfirmar = lugar
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Añadir al itinerario"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // ───── DESCRIPCIÓN ─────
                    Text(
                        text = lugar.descripcion ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // ───── ESTRELLAS + VALORACIÓN ─────
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val valoracionSegura = lugar.valoracion ?: 0.0

                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < valoracionSegura.toInt())
                                    Color(0xFFFFC107)
                                else Color.LightGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = lugar.valoracion.toString(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }

    // ─────────────────────────────────────
    // 🪟 DIÁLOGO DE CONFIRMACIÓN
    // ─────────────────────────────────────
    lugarAConfirmar?.let { lugar ->

        AlertDialog(
            onDismissRequest = {
                lugarAConfirmar = null
            },
            title = {
                Text("Añadir al itinerario")
            },
            text = {
                Text("¿Quieres añadir ${lugar.nombre} a tu itinerario?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            actividadDao.insert(
                                ActividadEntity(
                                    id = 0,
                                    idItinerarioDia = null,
                                    destinoId = destinoId,
                                    tipoActividad = "Turismo",
                                    orden = 999,
                                    descripcion = lugar.descripcion ?: lugar.nombre,
                                    horaInicio = null,
                                    horaFin = null
                                )
                            )
                        }
                        lugarAConfirmar = null
                    }
                ) {
                    Text("Añadir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { lugarAConfirmar = null }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
