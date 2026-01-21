package com.tripmateapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tripmateapp.model.ItineraryItem
import com.tripmateapp.model.ItineraryType

//ItineraryScreen(
//    navController = navController,
//    itineraryItems = itineraryItems,
//    onRemoveItem = { item ->
//        itineraryItems = itineraryItems.filterNot { it == item }
//    },
//    onClearItinerary = {
//        itineraryItems = emptyList()
//    }
//)

// ------------------------------------------------------------------
//                  PANTALLA DE ITINERARIO COMPLETA
// ------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(
    navController: NavController,
    itineraryItems: List<ItineraryItem>,
    onRemoveItem: (ItineraryItem) -> Unit,
    onClearItinerary: () -> Unit
) {

    val itemsAgrupadosPorDia = itineraryItems
        .sortedBy { it.date }
        .groupBy { it.date }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Itinerario del viaje") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    if (itineraryItems.isNotEmpty()) {
                        IconButton(onClick = onClearItinerary) {
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
    ) { innerPadding ->

        if (itineraryItems.isEmpty()) {

            // 🟡 MENSAJE SI NO HAY NADA EN EL ITINERARIO
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No has añadido nada al itinerario todavía",
                    color = Color.Gray
                )
            }

        } else {

            // 🟢 LISTA DEL ITINERARIO
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {

                itemsAgrupadosPorDia.forEach { (dia, itemsDelDia) ->

                    // 📅 CABECERA DEL DÍA
                    item {
                        Text(
                            text = "📅 $dia",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }

                    // 📌 ELEMENTOS DEL DÍA
                    items(itemsDelDia) { item ->
                        ItineraryItemCard(
                            item = item,
                            onRemove = { onRemoveItem(item) }
                        )
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------
//                  TARJETA DE ELEMENTO DEL ITINERARIO
// ------------------------------------------------------------------
@Composable
fun ItineraryItemCard(
    item: ItineraryItem,
    onRemove: () -> Unit
) {

    val icon = when (item.type) {
        ItineraryType.ACTIVIDAD -> Icons.Default.LocalActivity
        ItineraryType.RESTAURANTE -> Icons.Default.Restaurant
        ItineraryType.TRANSPORTE -> Icons.Default.DirectionsBus
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

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

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

                Text(
                    text = when (item.type) {
                        ItineraryType.ACTIVIDAD -> "Actividad"
                        ItineraryType.RESTAURANTE -> "Restaurante"
                        ItineraryType.TRANSPORTE -> "Transporte"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Red
                )
            }
        }
    }
}
