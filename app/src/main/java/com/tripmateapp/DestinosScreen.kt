package com.tripmateapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tripmateapp.BaseDatos.Destinos.DestinoDao
import com.tripmateapp.BaseDatos.Destinos.DestinoEntity
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteDao
import com.tripmateapp.BaseDatos.Transporte.TransporteDao
import com.tripmateapp.BaseDatos.actividades.ActividadDao
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

    val destinos by destinoDao.getAll().collectAsState(initial = emptyList())

    var destinoSeleccionado by remember { mutableStateOf<DestinoEntity?>(null) }

    // Tabs seleccionadas
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TripMateTopBar(
                query = query,
                onQueryChange = { query = it },
                onSearchClick = {
                    destinoSeleccionado = destinos.find { d ->
                        d.nombre.normalize().contains(query.normalize())
                    }
                }
            )
        }
    ) { innerPadding ->

        // Si aún no se ha buscado nada
        if (destinoSeleccionado == null) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Introduce un destino y pulsa buscar", color = Color.Gray)
            }
            return@Scaffold
        }

        // DESTINO ENCONTRADO → MOSTRAR TABS
        Column(modifier = Modifier.padding(innerPadding)) {

            // 🔥 TABS
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

            // 🔥 CONTENIDO SEGÚN TAB SELECCIONADA
            when (selectedTab) {

                0 -> ActividadesList(
                    destinoId = destinoSeleccionado!!.id,
                    actividadDao = actividadDao
                )

                1 -> RestaurantesList(
                    destinoId = destinoSeleccionado!!.id,
                    restauranteDao = restauranteDao
                )

                2 -> TransportesList(
                    destinoId = destinoSeleccionado!!.id,
                    transporteDao = transporteDao
                )
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

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Logo TripMate",
                modifier = Modifier.height(40.dp)
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

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Destino",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelMedium
                )

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
fun ActividadesList(destinoId: Int, actividadDao: ActividadDao) {
    val actividades by actividadDao.getByDestino(destinoId).collectAsState(initial = emptyList())

    if (actividades.isEmpty()) {
        Text("No hay actividades disponibles", modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn {
            items(actividades) { actividad ->
                Text(
                    actividad.tipoActividad,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun RestaurantesList(destinoId: Int, restauranteDao: RestauranteDao) {
    val restaurantes by restauranteDao.getByDestino(destinoId).collectAsState(initial = emptyList())

    LazyColumn {
        items(restaurantes) { rest ->
            Text(
                rest.nombre,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun TransportesList(destinoId: Int, transporteDao: TransporteDao) {
    val transportes by transporteDao.getByDestino(destinoId).collectAsState(initial = emptyList())

    LazyColumn {
        items(transportes) { tr ->
            Text(
                "${tr.tipo} - ${tr.nombre ?: ""}",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

