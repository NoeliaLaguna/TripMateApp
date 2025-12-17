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
    onDestinoSeleccionado: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val destinos by destinoDao.getAll().collectAsState(initial = emptyList())

    // Destinos que se mostrarán después de pulsar buscar
    var destinosVisibles by remember { mutableStateOf<List<DestinoEntity>>(emptyList()) }

    // Controla si se muestran resultados o no
    var mostrarResultados by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TripMateTopBar(
                query = query,
                onQueryChange = { query = it },
                onSearchClick = {
                    // Activar resultados
                    mostrarResultados = true

                    val normalizedQuery = query.normalize()

                    // Filtrado usando normalización
                    destinosVisibles = destinos.filter { destino ->
                        destino.nombre.normalize().contains(normalizedQuery) ||
                                destino.pais.normalize().contains(normalizedQuery) ||
                                (destino.descripcion?.normalize()?.contains(normalizedQuery) == true)
                    }
                }
            )
        },
        containerColor = Color(0xFFF7F7F7)
    ) { innerPadding ->

        // SI NO SE HA BUSCADO → INNERPADDING VACÍO O MENSAJE
        if (!mostrarResultados) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Introduce un destino y pulsa buscar",
                    color = Color.Gray
                )
            }
        } else {
            // RESULTADOS
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                items(destinosVisibles) { destino ->
                    DestinoCardAirbnb(
                        destino = destino,
                        onSelect = { onDestinoSeleccionado(destino.id) }
                    )
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
//                        TARJETA DE DESTINO
// ------------------------------------------------------------------
@Composable
fun DestinoCardAirbnb(
    destino: DestinoEntity,
    onSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { onSelect() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = destino.nombre,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(destino.nombre, style = MaterialTheme.typography.titleLarge)
                Text(destino.pais, color = Color.Gray)

                destino.descripcion?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(text = it, maxLines = 2)
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = onSelect,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Seleccionar")
                }
            }
        }
    }
}
