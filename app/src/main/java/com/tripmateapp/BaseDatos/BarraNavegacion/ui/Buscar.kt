package com.tripmateapp.BaseDatos.BarraNavegacion.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


//Cambio 1
@Composable
fun BuscarScreen(navController: NavController) {
    Text("Buscar viajes")
    // Navegar a la pantalla de destinos
    navController.navigate("destinos")
}
