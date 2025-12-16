package com.tripmateapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tripmateapp.BaseDatos.DatabaseProvider

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

    // ACCEDE A LA BD
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val destinoDao = database.destinoDao()


    NavHost(
        navController = navController,
        startDestination = Rutas.DESTINOS
    ) {
        composable(Rutas.DESTINOS) {
            DestinosScreen(
                destinoDao = destinoDao,
                onDestinoSeleccionado = { destinoId ->
                    navController.navigate(Rutas.crearViaje(destinoId))
                }
            )
        }

        composable(
            route = Rutas.CREAR_VIAJE,
            arguments = listOf(
                navArgument("destinoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val destinoId = backStackEntry.arguments?.getInt("destinoId") ?: 0
            CrearViajeScreen(destinoId)
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
