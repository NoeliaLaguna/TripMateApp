package com.tripmateapp.BaseDatos.BarraNavegacion

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tripmateapp.BaseDatos.BarraNavegacion.ui.BottomBar
import com.tripmateapp.BaseDatos.BarraNavegacion.ui.BuscarScreen
import com.tripmateapp.BaseDatos.BarraNavegacion.ui.MisViajesScreen
import com.tripmateapp.BaseDatos.BarraNavegacion.ui.PerfilScreen
import com.tripmateapp.BaseDatos.BarraNavegacion.ui.SoporteScreen

//Cambio 3
@Composable
fun TripMateNavHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Navigation.Screen.Buscar.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(Navigation.Screen.Buscar.route) {
                BuscarScreen(navController)
            }

            composable(Navigation.Screen.MisViajes.route) {
                MisViajesScreen(navController)
            }

            composable(Navigation.Screen.Soporte.route) {
                SoporteScreen()
            }

           // composable(Navigation.Screen.Perfil.route) { PerfilScreen() }
        }
    }
}