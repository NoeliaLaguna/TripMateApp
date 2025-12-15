package com.tripmateapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tripmateapp.BaseDatos.Destinos.DestinoDao
import com.tripmateapp.BaseDatos.Destinos.DestinoEntity


@Composable
fun DestinosScreen(
    destinoDao: DestinoDao,
    onDestinoSeleccionado: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }

    // 📌 Recogemos los destinos directamente del Flow del DAO
    val destinos by destinoDao.getAll().collectAsState(initial = emptyList())

    // Filtro por nombre
    val destinosFiltrados = destinos.filter {
        it.nombre.contains(query, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔍 Barra búsqueda
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar destino") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(destinosFiltrados) { destino ->
                DestinoCard(
                    destino = destino,
                    onSelect = { onDestinoSeleccionado(destino.id) }
                )
            }
        }
    }
}

@Composable
fun DestinoCard(
    destino: DestinoEntity,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(destino.nombre, style = MaterialTheme.typography.titleLarge)
            Text(destino.pais, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onSelect,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Seleccionar")
            }
        }
    }
}
