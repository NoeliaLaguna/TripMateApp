package com.tripmateapp.inicioSesion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tripmateapp.BaseDatos.DatabaseProvider
import com.tripmateapp.utilidades.PreferenciasLogin
import kotlinx.coroutines.launch

@Composable
fun InicioSesionScreen(
    onLoginCorrecto: () -> Unit,
    onIrARegistro: () -> Unit
) {

    // ---------------- CONTEXTO Y DEPENDENCIAS ----------------
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val usuarioDao = database.usuarioDao()
    val preferenciasLogin = PreferenciasLogin(context)
    val scope = rememberCoroutineScope()

    // ---------------- ESTADO UI ----------------
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var recordar by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    // ---------------- UI ----------------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Text(
            text = "INICIO DE SESIÓN",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFDCB6C3))
                .padding(15.dp),
            color = Color.White,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        TextField(
            value = correo,
            onValueChange = { correo = it },
            placeholder = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            placeholder = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = recordar,
                onCheckedChange = { recordar = it }
            )
            Text("Recordar datos")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (correo.isBlank() || contrasena.isBlank()) {
                    mensaje = "Debes rellenar todos los campos"
                    return@Button
                }

                scope.launch {
                    val usuarioEncontrado =
                        usuarioDao.getByCorreo(correo)

                    when {
                        usuarioEncontrado == null -> {
                            mensaje = "El usuario no existe. Regístrate primero."
                        }

                        usuarioEncontrado.contrasenya != contrasena -> {
                            mensaje = "Contraseña incorrecta."
                        }

                        else -> {
                            mensaje = ""

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
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Iniciar sesión", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = Color(0xFFEF4343),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onIrARegistro,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}
