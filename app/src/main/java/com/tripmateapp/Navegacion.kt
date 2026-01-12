package com.tripmateapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.tripmateapp.BaseDatos.DatabaseProvider
import com.tripmateapp.RegistroUsuario.RegistroScreen
import com.tripmateapp.inicioSesion.InicioSesionScreen

// ------------------------------
// RUTAS DE LA APP
// ------------------------------
object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
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

    // CONTEXTO Y BD
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val destinoDao = database.destinoDao()

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {

        // ---------------- LOGIN ----------------
        composable(Rutas.LOGIN) {
            InicioSesionScreen(
                onLoginCorrecto = {
                    navController.navigate(Rutas.DESTINOS) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onIrARegistro = {
                    navController.navigate(Rutas.DESTINOS) {
                        popUpTo(Rutas.REGISTRO) { inclusive = true }
                    }                }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onRegistroCorrecto = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.REGISTRO) { inclusive = true }
                    }
                },

                onIrAInicioSesion = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // ---------------- DESTINOS ----------------
        val actividadDao = database.actividadDao()
        val restauranteDao = database.restauranteDao()
        val transporteDao = database.transporteDao()
        composable(Rutas.DESTINOS) {
            DestinosScreen(
                destinoDao = destinoDao,
                actividadDao = actividadDao,
                restauranteDao = restauranteDao,
                transporteDao = transporteDao,
                onDestinoSeleccionado = { destinoId ->
                    navController.navigate(Rutas.crearViaje(destinoId))
                }
            )
        }

        // ---------------- CREAR VIAJE ----------------
        composable(
            route = Rutas.CREAR_VIAJE,
            arguments = listOf(
                navArgument("destinoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val destinoId =
                backStackEntry.arguments?.getInt("destinoId") ?: 0

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
