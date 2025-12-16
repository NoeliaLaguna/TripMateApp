package com.tripmateapp.utilidades

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "preferencias_login"
)

class PreferenciasLogin(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val CLAVE_CORREO = stringPreferencesKey("correo")
        private val CLAVE_PASSWORD = stringPreferencesKey("password")
        private val CLAVE_RECORDAR = booleanPreferencesKey("recordar_datos")
    }

    suspend fun guardarDatosLogin(
        correo: String,
        password: String,
        recordar: Boolean
    ) {
        dataStore.edit { preferencias ->
            preferencias[CLAVE_CORREO] = correo
            preferencias[CLAVE_PASSWORD] = password
            preferencias[CLAVE_RECORDAR] = recordar
        }
    }

    suspend fun borrarDatos() {
        dataStore.edit { it.clear() }
    }

    val correo: Flow<String> =
        dataStore.data.map { it[CLAVE_CORREO] ?: "" }

    val password: Flow<String> =
        dataStore.data.map { it[CLAVE_PASSWORD] ?: "" }

    val recordar: Flow<Boolean> =
        dataStore.data.map { it[CLAVE_RECORDAR] ?: false }
}
