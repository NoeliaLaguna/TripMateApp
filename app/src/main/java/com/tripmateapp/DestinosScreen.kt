package com.tripmateapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun DestinosScreen(
    destinoDao: DestinoDao,
    onDestinoSeleccionado: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }

    val destinos by destinoDao.getAll().collectAsState(initial = emptyList())

    val destinosFiltrados = destinos.filter {
        it.nombre.contains(query, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)) // Fondo gris claro tipo Airbnb
    ) {

        // Barra de búsqueda estilo Airbnb
        SearchBar(query) { query = it }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            items(destinosFiltrados) { destino ->
                DestinoCardAirbnb(
                    destino = destino,
                    onSelect = { onDestinoSeleccionado(destino.id) }
                )
            }
        }
    }
}

@Composable
fun SearchBar(value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text("¿Dónde quieres viajar?") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = Color.Black
        )
    )
}

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

            // 🔥 Imagen local (sin internet)
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
                Text(
                    text = destino.nombre,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = destino.pais,
                    color = MaterialTheme.colorScheme.secondary
                )

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
