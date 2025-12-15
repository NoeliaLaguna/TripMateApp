package com.tripmateapp.BaseDatos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun destinoDao(): DestinoDao
    abstract fun restauranteDao(): RestauranteDao
    abstract fun transporteDao(): TransporteDao
    abstract fun itinerarioDao(): ItinerarioDao
    abstract fun viajeDao(): ViajeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "TripmateDataBase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
