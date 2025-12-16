package com.tripmateapp.BaseDatos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tripmateapp.BaseDatos.Destinos.DestinoDao
import com.tripmateapp.BaseDatos.Destinos.DestinoEntity
import com.tripmateapp.BaseDatos.DestinosFavoritos.DestinoFavoritoDao
import com.tripmateapp.BaseDatos.DestinosFavoritos.DestinoFavoritoEntity
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDao
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias.ItinerarioDiaDao
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioEntity
import com.tripmateapp.BaseDatos.LugaresTuristicos.LugarTuristicoDao
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteDao
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteEntity
import com.tripmateapp.BaseDatos.Transporte.TransporteDao
import com.tripmateapp.BaseDatos.Transporte.TransporteEntity
import com.tripmateapp.BaseDatos.Usuarios.UsuarioDao
import com.tripmateapp.BaseDatos.Usuarios.UsuarioEntity
import com.tripmateapp.BaseDatos.Viajes.ViajeDao
import com.tripmateapp.BaseDatos.Viajes.ViajeEntity
import com.tripmateapp.BaseDatos.actividades.ActividadDao
import com.tripmateapp.BaseDatos.actividades.ActividadEntity
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDias.ItinerarioDiaEntity
import com.tripmateapp.BaseDatos.LugaresTuristicos.LugarTuristicoEntity


/**
 * AppDatabase
 *
 * Clase principal de Room que define:
 * - Todas las ENTIDADES (tablas) de la base de datos
 * - Todos los DAO (interfaces de acceso a datos)
 *
 * IMPORTANTE:
 * - Aquí NO se crean instancias ni se insertan datos.
 * - Solo se declara la estructura de la base de datos.
 *
 * La instancia de esta base de datos se obtiene a través de DatabaseProvider.
 */
@Database(
    entities = [
        UsuarioEntity::class,
        DestinoEntity::class,
        RestauranteEntity::class,
        TransporteEntity::class,
        ItinerarioEntity::class,
        ViajeEntity::class,
        LugarTuristicoEntity::class,
        ActividadEntity::class,
        ItinerarioDiaEntity::class,
        DestinoFavoritoEntity ::class

    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun destinoDao(): DestinoDao
    abstract fun restauranteDao(): RestauranteDao
    abstract fun transporteDao(): TransporteDao
    abstract fun itinerarioDao(): ItinerarioDao
    abstract fun viajeDao(): ViajeDao
    abstract fun lugarTuristicoDao(): LugarTuristicoDao
    abstract fun actividadDao(): ActividadDao
    abstract fun itinerarioDiaDao(): ItinerarioDiaDao
    abstract fun destinoFavoritoDao(): DestinoFavoritoDao
}
