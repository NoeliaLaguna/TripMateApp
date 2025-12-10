package com.tripmateapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tripmateapp.ui.theme.DestinosViewModel

// ------------------------------
// RUTAS DE LA APP
// ------------------------------
object Rutas {
    const val DESTINOS = "destinos"
    const val CREAR_VIAJE = "crearViaje/{destinoId}"

    fun crearViaje(destinoId: Int) = "crearViaje/$destinoId"
}

// ------------------------------
// GRAFICO DE NAVEGACIÓN
// ------------------------------
@Composable
fun Navegacion() {

    val navController = rememberNavController()
    val destinosViewModel: DestinosViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Rutas.DESTINOS
    ) {

        // 🗺️ PANTALLA DE LISTA DE DESTINOS
        composable(Rutas.DESTINOS) {
            DestinosScreen(
                viewModel = destinosViewModel,
                onDestinoSeleccionado = { destinoId ->
                    navController.navigate(Rutas.crearViaje(destinoId))
                }
            )
        }

        // 🧳 PANTALLA PARA CREAR VIAJE (recibe destinoId)
        composable(
            route = Rutas.CREAR_VIAJE,
            arguments = listOf(
                navArgument("destinoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val destinoId = backStackEntry.arguments?.getInt("destinoId") ?: 0

            CrearViajeScreen(destinoId = destinoId)
        }
    }
}

// ------------------------------
// PANTALLA TEMPORAL
// ------------------------------
@Composable
fun CrearViajeScreen(destinoId: Int) {
    Text(text = "Crear viaje para destinoId = $destinoId")
}
