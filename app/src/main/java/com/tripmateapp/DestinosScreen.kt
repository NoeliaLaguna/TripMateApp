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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteDao
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteEntity
import com.tripmateapp.BaseDatos.Transporte.TransporteDao
import com.tripmateapp.BaseDatos.Transporte.TransporteEntity
import com.tripmateapp.BaseDatos.actividades.ActividadDao
import com.tripmateapp.BaseDatos.actividades.ActividadEntity
import java.text.Normalizer

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
    onDestinoSeleccionado: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }

    val destinos by destinoDao.getAllFlow().collectAsState(initial = emptyList())

    var destinoSeleccionado by remember { mutableStateOf<DestinoEntity?>(null) }

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
                    val queryNorm = query.normalize()

                    // 1️⃣ Buscar por ciudad
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
                            // 2️⃣ Buscar por país
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
                                                Text(ciudad.nombre, style = MaterialTheme.typography.titleMedium)
                                            }
                                        }
                                    }
                                }
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
                0 -> ActividadesList(destinoSeleccionado!!.id, actividadDao)
                1 -> RestaurantesList(destinoSeleccionado!!.id, restauranteDao)
                2 -> TransportesList(destinoSeleccionado!!.id, transporteDao)
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
    actividadDao: ActividadDao,
    onAddToItinerary: (ActividadEntity) -> Unit = {}
) {
    val actividades by actividadDao.getByDestino(destinoId).collectAsState(initial = emptyList())

    if (actividades.isEmpty()) {
        Text("No hay actividades disponibles", modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn {
            items(actividades) { actividad ->
                ActividadCardExpandable(
                    actividad = actividad,
                    onAddToItinerary = onAddToItinerary
                )
            }
        }
    }
}


@Composable
fun ActividadCardExpandable(
    actividad: ActividadEntity,
    onAddToItinerary: (ActividadEntity) -> Unit
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
}


@Composable
fun RestaurantesList(
    destinoId: Int,
    restauranteDao: RestauranteDao,
    onAddToItinerary: (RestauranteEntity) -> Unit = {}
) {
    val restaurantes by restauranteDao.getByDestino(destinoId).collectAsState(initial = emptyList())

    LazyColumn {
        items(restaurantes) { rest ->
            RestauranteCardExpandable(
                restaurante = rest,
                onAddToItinerary = onAddToItinerary
            )
        }
    }
}

@Composable
fun RestauranteCardExpandable(
    restaurante: RestauranteEntity,
    onAddToItinerary: (RestauranteEntity) -> Unit
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
                Text(restaurante.nombre, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
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
                        onAddToItinerary(restaurante)
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
fun TransportesList(
    destinoId: Int,
    transporteDao: TransporteDao,
    onAddToItinerary: (TransporteEntity) -> Unit = {}
) {
    val transportes by transporteDao.getByDestino(destinoId).collectAsState(initial = emptyList())

    LazyColumn {
        items(transportes) { tr ->
            TransporteCardExpandable(
                transporte = tr,
                onAddToItinerary = onAddToItinerary
            )
        }
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


