package com.tripmateapp.BaseDatos

import DatabaseCallback
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


/**
 * DatabaseProvider
 *
 * Singleton encargado de:
 * - Crear la instancia única de AppDatabase
 * - Configurar Room (nombre de la BD, callbacks, contexto)
 *
 * ✔ Garantiza que solo exista UNA base de datos en toda la app
 * ✔ Usa applicationContext para evitar memory leaks
 *
 * USO:
 * val db = DatabaseProvider.getDatabase(context)
 */

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "tripmate_db"
            )
                .addCallback(
                    DatabaseCallback(
                        scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
                        dbProvider = { INSTANCE!! }
                    )
                )
                .build()

            INSTANCE = instance
            instance
        }
    }
}
