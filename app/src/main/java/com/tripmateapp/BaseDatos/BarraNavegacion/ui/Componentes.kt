package com.tripmateapp.BaseDatos.BarraNavegacion.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tripmateapp.BaseDatos.BarraNavegacion.Navigation


// Cambio 2
@Composable
fun BottomBar(navController: NavController) {

    val items = listOf(
        Navigation.Screen.Buscar,
        Navigation.Screen.MisViajes,
        Navigation.Screen.Soporte,
        //Navigation.Screen.Perfil
    )

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            Navigation.Screen.Buscar -> Icons.Default.Search
                            Navigation.Screen.MisViajes -> Icons.Default.Place
                            Navigation.Screen.Soporte -> Icons.Default.Build
                           // Navigation.Screen.Perfil -> Icons.Default.Person
                        },
                        contentDescription = screen.label
                    )
                },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route)
                    }
                }
            )
        }
    }
}
