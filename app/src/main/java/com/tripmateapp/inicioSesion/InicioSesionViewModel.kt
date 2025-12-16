package com.tripmateapp.inicioSesion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripmateapp.BaseDatos.AppDatabase
import com.tripmateapp.utilidades.PreferenciasLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InicioSesionViewModel(
    private val database: AppDatabase,
    private val preferenciasLogin: PreferenciasLogin
) : ViewModel() {

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    /**
     * LÓGICA DE LOGIN
     */
    fun iniciarSesion(
        correo: String,
        contrasena: String,
        recordar: Boolean,
        onLoginCorrecto: () -> Unit
    ) {
        if (correo.isBlank() || contrasena.isBlank()) {
            _mensaje.value = "Debes rellenar todos los campos"
            return
        }

        viewModelScope.launch {
            val usuarioEncontrado =
                database.usuarioDao().getByCorreo(correo)

            when {
                usuarioEncontrado == null -> {
                    _mensaje.value = "El usuario no existe. Regístrate primero."
                }

                usuarioEncontrado.contrasenya != contrasena -> {
                    _mensaje.value = "Contraseña incorrecta."
                }

                else -> {
                    _mensaje.value = ""

                    if (recordar) {
                        preferenciasLogin.guardarDatosLogin(
                            correo,
                            contrasena,
                            true
                        )
                    } else {
                        preferenciasLogin.borrarDatos()
                    }

                    onLoginCorrecto()
                }
            }
        }
    }
}
