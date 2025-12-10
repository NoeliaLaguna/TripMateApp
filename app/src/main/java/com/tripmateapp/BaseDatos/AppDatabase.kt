package com.tripmateapp.BaseDatos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tripmateapp.BaseDatos.Destinos.DestinoDao
import com.tripmateapp.BaseDatos.Destinos.DestinoEntity
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioDao
import com.tripmateapp.BaseDatos.Itinerarios.ItinerarioEntity
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteDao
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteEntity
import com.tripmateapp.BaseDatos.Transporte.TransporteDao
import com.tripmateapp.BaseDatos.Transporte.TransporteEntity
import com.tripmateapp.BaseDatos.Usuarios.UsuarioDao
import com.tripmateapp.BaseDatos.Usuarios.UsuarioEntity
import com.tripmateapp.BaseDatos.Viajes.ViajeDao
import com.tripmateapp.BaseDatos.Viajes.ViajeEntity

@Database(
    entities = [
        UsuarioEntity::class,
        DestinoEntity::class,
        RestauranteEntity::class,
        TransporteEntity::class,
        ItinerarioEntity::class,
        ViajeEntity::class
    ],
    version = 1
)
import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        DestinoEntity::class,
        LugarTuristicoEntity::class,
        RestauranteEntity::class,
        TransporteEntity::class,
        MessageEntity::class,
        ViajeEntity::class,
        ItinerarioEntity::class,
        DestinoFavoritoEntity::class,
        SoporteEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun destinoDao(): DestinoDao
    abstract fun restauranteDao(): RestauranteDao
    abstract fun transporteDao(): TransporteDao
    abstract fun itinerarioDao(): ItinerarioDao
    abstract fun viajeDao(): ViajeDao

    abstract fun destinoDao(): DestinoDAO
    abstract fun lugarTuristicoDao(): LugarTuristicoDAO
    abstract fun restauranteDao(): RestauranteDAO
    abstract fun transporteDao(): TransporteDAO

    abstract fun messageDao(): MessageDAO
    abstract fun viajeDao(): ViajeDAO
    abstract fun itinerarioDao(): ItinerarioDAO
    abstract fun favoritoDao(): DestinoFavoritoDAO
    abstract fun soporteDao(): SoporteDAO

    companion object {

        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "tripmate.db"
                )
                    .addCallback(DatabaseCallback())
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

/* ---------------------------------------------------------
   CALLBACK CON LOS INSERTS AUTOMÁTICOS
---------------------------------------------------------- */

class DatabaseCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {

            val database = AppDatabase.INSTANCE ?: return@launch

            /* ---------------------------------------------------------
               1. INSERT DESTINOS (10 registros)
            ---------------------------------------------------------- */
            database.destinoDao().insertAll(
                listOf(
                    DestinoEntity(1, "París", "Francia", "Ciudad romántica con monumentos icónicos"),
                    DestinoEntity(2, "Roma", "Italia", "Capital histórica con ruinas antiguas"),
                    DestinoEntity(3, "Tokio", "Japón", "Mezcla de modernidad y tradición"),
                    DestinoEntity(4, "Nueva York", "EE.UU.", "La ciudad que nunca duerme"),
                    DestinoEntity(5, "Londres", "Reino Unido", "Ciudad multicultural con historia"),
                    DestinoEntity(6, "Barcelona", "España", "Arte, arquitectura y playa"),
                    DestinoEntity(7, "Berlín", "Alemania", "Cultura moderna y memoria histórica"),
                    DestinoEntity(8, "Sídney", "Australia", "Ciudad costera con el Opera House"),
                    DestinoEntity(9, "Buenos Aires", "Argentina", "Cuna del tango y cultura vibrante"),
                    DestinoEntity(10, "El Cairo", "Egipto", "Ciudad milenaria junto a las pirámides")
                )
            )

            /* ---------------------------------------------------------
               2. INSERT LUGARES TURÍSTICOS (10 registros)
            ---------------------------------------------------------- */
            database.lugarTuristicoDao().insertAll(
                listOf(
                    LugarTuristicoEntity(1, "Torre Eiffel", "París", "Monumento icónico de Francia", "10:00"),
                    LugarTuristicoEntity(2, "Coliseo Romano", "Roma", "Anfiteatro de la antigua Roma", "09:00"),
                    LugarTuristicoEntity(3, "Templo Senso-ji", "Tokio", "Templo budista más antiguo de Tokio", "11:00"),
                    LugarTuristicoEntity(4, "Estatua de la Libertad", "Nueva York", "Símbolo de libertad", "10:30"),
                    LugarTuristicoEntity(5, "Big Ben", "Londres", "Torre del reloj más famosa del mundo", "12:00"),
                    LugarTuristicoEntity(6, "Sagrada Familia", "Barcelona", "Obra maestra de Gaudí", "09:30"),
                    LugarTuristicoEntity(7, "Muro de Berlín", "Berlín", "Sitio histórico mundial", "14:00"),
                    LugarTuristicoEntity(8, "Opera House", "Sídney", "Símbolo arquitectónico australiano", "13:00"),
                    LugarTuristicoEntity(9, "Caminito", "Buenos Aires", "Barrio colorido y cultural", "15:00"),
                    LugarTuristicoEntity(10, "Pirámides de Giza", "El Cairo", "Una de las maravillas del mundo", "08:00")
                )
            )

            /* ---------------------------------------------------------
               3. INSERT RESTAURANTES (10 registros)
            ---------------------------------------------------------- */
            database.restauranteDao().insertAll(
                listOf(
                    RestauranteEntity(1, "Le Jules Verne", "París", "Restaurante en la Torre Eiffel", "14:00"),
                    RestauranteEntity(2, "Da Enzo al 29", "Roma", "Trattoria romana tradicional", "13:00"),
                    RestauranteEntity(3, "Sukiyabashi Jiro", "Tokio", "Sushi de prestigio mundial", "19:00"),
                    RestauranteEntity(4, "Katz's Delicatessen", "Nueva York", "Deli clásico famoso por su pastrami", "12:00"),
                    RestauranteEntity(5, "Dishoom", "Londres", "Comida india moderna", "20:00"),
                    RestauranteEntity(6, "El Nacional", "Barcelona", "Gastronomía variada", "21:00"),
                    RestauranteEntity(7, "Hofbräuhaus", "Múnich", "Cervecería tradicional alemana", "18:00"),
                    RestauranteEntity(8, "Quay", "Sídney", "Alta cocina con vistas al puerto", "19:30"),
                    RestauranteEntity(9, "Don Julio", "Buenos Aires", "Parrilla argentina reconocida", "13:00"),
                    RestauranteEntity(10, "Abou El Sid", "El Cairo", "Cocina egipcia tradicional", "20:00")
                )
            )

            /* ---------------------------------------------------------
               4. INSERT TRANSPORTES (10 registros)
            ---------------------------------------------------------- */
            database.transporteDao().insertAll(
                listOf(
                    TransporteEntity(1, "Metro de París", "París", "Principal red de transporte urbano", "08:00"),
                    TransporteEntity(2, "Leonardo Express", "Roma", "Tren rápido al aeropuerto", "07:30"),
                    TransporteEntity(3, "Shinkansen", "Tokio", "Tren bala japonés", "09:00"),
                    TransporteEntity(4, "NYC Subway", "Nueva York", "Metro más grande del mundo", "06:30"),
                    TransporteEntity(5, "London Underground", "Londres", "Metro más antiguo del mundo", "07:00"),
                    TransporteEntity(6, "Metro de Barcelona", "Barcelona", "Transporte urbano moderno", "08:30"),
                    TransporteEntity(7, "S-Bahn", "Berlín", "Tren rápido urbano", "09:45"),
                    TransporteEntity(8, "Ferry de Sídney", "Sídney", "Transporte marítimo turístico", "10:15"),
                    TransporteEntity(9, "Colectivo Porteño", "Buenos Aires", "Bus típico de la ciudad", "11:00"),
                    TransporteEntity(10, "Taxi de El Cairo", "El Cairo", "Transporte común urbano", "12:30")
                )
            )
        }
    }
}
