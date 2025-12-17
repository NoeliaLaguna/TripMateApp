package com.tripmateapp.BaseDatos

import DatabaseCallback
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


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
            // 1️⃣ Crear la instancia SIN callback automático
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "tripmate_db"
            )
                .fallbackToDestructiveMigration()
                .build()

            // 2️⃣ Asignamos INSTANCE
            INSTANCE = instance

            // 3️⃣ Ejecutamos el callback manualmente AHORA
            val callback = DatabaseCallback(
                scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
                dbProvider = { instance }  // <-- aquí pasamos la instancia real
            )

            CoroutineScope(Dispatchers.IO).launch {
                callback.onCreate(instance.openHelper.writableDatabase)
            }

            instance
        }
    }
    fun getInstanceUnsafe(): AppDatabase = INSTANCE
        ?: throw IllegalStateException("Database not initialized yet")
}
