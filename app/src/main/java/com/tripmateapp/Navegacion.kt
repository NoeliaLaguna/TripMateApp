package com.tripmateapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.tripmateapp.BaseDatos.BarraNavegacion.ui.BottomBar
import com.tripmateapp.BaseDatos.BarraNavegacion.ui.BuscarScreen
import com.tripmateapp.BaseDatos.BarraNavegacion.ui.MisViajesScreen
import com.tripmateapp.BaseDatos.BarraNavegacion.ui.SoporteScreen
import com.tripmateapp.BaseDatos.DatabaseProvider
import com.tripmateapp.ModificarDatosUsuario.ModificarDatosUsuarioScreen
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
    const val MODIFICAR_USUARIO = "modificarUsuario"

    // ✅ RUTA DEL ITINERARIO
    const val ITINERARIO = "itinerario/{destinoId}"

    fun crearViaje(destinoId: Int) = "crearViaje/$destinoId"
}

// ------------------------------
// GRAFICO DE NAVEGACIÓN
// ------------------------------
@SuppressLint("ViewModelConstructorInComposable")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navegacion() {

    val navController = rememberNavController()

    // ---------------- BD ----------------
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)

    val destinoDao = database.destinoDao()
    val actividadDao = database.actividadDao()
    val restauranteDao = database.restauranteDao()
    val transporteDao = database.transporteDao()
    val lugarTuristicoDao = database.lugarTuristicoDao()

    val itinerarioDao = database.itinerarioDao()
    val itinerarioDiaDao = database.itinerarioDiaDao()
    val itinerarioDiaActividadDao = database.itinerarioDiaActividadDao()
    val itinerarioDiaRestauranteDao = database.itinerarioDiaRestauranteDao()
    val itinerarioDiaTransporteDao = database.itinerarioDiaTransporteDao()
    val itinerarioDiaLugarTuristicoDao = database.itinerarioDiaLugarTuristicoDao()

    // ---------------- BOTTOM BAR ----------------
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    val shouldShowBottomBar =
        currentRoute != Rutas.LOGIN && currentRoute != Rutas.REGISTRO

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomBar(navController)
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Rutas.LOGIN,
            modifier = Modifier.padding(padding)
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
                        navController.navigate(Rutas.REGISTRO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            // ---------------- REGISTRO ----------------
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
            composable(Rutas.DESTINOS) {
                DestinosScreen(
                    destinoDao = destinoDao,
                    actividadDao = actividadDao,
                    restauranteDao = restauranteDao,
                    transporteDao = transporteDao,
                    lugarTuristicoDao = lugarTuristicoDao,
                    itinerarioDao = itinerarioDao,
                    itinerarioDiaDao = itinerarioDiaDao,
                    itinerarioDiaActividadDao = itinerarioDiaActividadDao,
                    itinerarioDiaRestauranteDao = itinerarioDiaRestauranteDao,
                    itinerarioDiaTransporteDao = itinerarioDiaTransporteDao,
                    itinerarioDiaLugarTuristicoDao = itinerarioDiaLugarTuristicoDao,
                    onIrAModificarUsuario = {
                        navController.navigate(Rutas.MODIFICAR_USUARIO)
                    },
                    onCerrarSesionClick = {
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.DESTINOS) { inclusive = true }
                        }
                    }
                )
            }

            // ---------------- ITINERARIO ----------------
            composable(
                route = Rutas.ITINERARIO,
                arguments = listOf(
                    navArgument("destinoId") { type = NavType.IntType }
                )
            ) { backStackEntry ->

                val destinoId =
                    backStackEntry.arguments?.getInt("destinoId") ?: return@composable

                val viewModel = ItineraryViewModel(
                    destinoId = destinoId,
                    itinerarioDao = itinerarioDao,
                    itinerarioDiaDao = itinerarioDiaDao,
                    actividadDao = itinerarioDiaActividadDao,
                    restauranteDao = itinerarioDiaRestauranteDao,
                    transporteDao = itinerarioDiaTransporteDao
                )

                ItineraryScreen(
                    navController = navController,
                    viewModel = viewModel
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

            // ---------------- MODIFICAR USUARIO ----------------
            composable(Rutas.MODIFICAR_USUARIO) {
                ModificarDatosUsuarioScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // ---------------- BOTTOM BAR ----------------
            composable("buscar") { BuscarScreen(navController) }
            composable("mis_viajes") { MisViajesScreen() }
            composable("soporte") { SoporteScreen() }
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
