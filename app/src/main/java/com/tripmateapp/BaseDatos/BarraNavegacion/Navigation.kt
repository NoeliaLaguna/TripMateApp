package com.tripmateapp.BaseDatos.BarraNavegacion


// Cambio 8
class Navigation {
    sealed class Screen(val route: String, val label: String) {

        // App principal
        object Buscar : Screen("buscar", "Buscar")
        object MisViajes : Screen("mis_viajes", "Viajes")
        object Soporte : Screen("soporte", "Soporte")
        //object Perfil : Screen("perfil", "Perfil")
    }
}