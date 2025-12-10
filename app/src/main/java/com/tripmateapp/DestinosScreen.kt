package com.tripmateapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tripmateapp.BaseDatos.Destinos.DestinoEntity
import com.tripmateapp.ui.theme.DestinosViewModel

@Composable
fun DestinosScreen(
    viewModel: DestinosViewModel,
    onDestinoSeleccionado: (Int) -> Unit   // Navegación al siguiente paso
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔍 Barra de búsqueda
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = { viewModel.onSearchChanged(it) }
        )

        // 📋 Contenido según estado
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            uiState.destinos.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron destinos")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(uiState.destinos) { destino ->
                        DestinoCard(
                            destino = destino,
                            onSelect = { onDestinoSeleccionado(destino.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        label = { Text("Buscar destino…") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
    )
}

@Composable
fun DestinoCard(
    destino: DestinoEntity,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onSelect() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = destino.nombre,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = destino.pais,
                style = MaterialTheme.typography.bodyMedium
            )

            destino.descripcion?.let {
                Text(
                    text = it,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSelect,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Seleccionar")
            }
        }
    }
}
