package com.tripmateapp.RegistroUsuario

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
import com.tripmateapp.BaseDatos.Usuarios.UsuarioEntity
import kotlinx.coroutines.launch

@Composable
fun RegistroScreen(
    onRegistroCorrecto: () -> Unit,
    onIrAInicioSesion: () -> Unit
) {

    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val usuarioDao = database.usuarioDao()
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "REGISTRO",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFDCB6C3)
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank()) {
                    mensaje = "Todos los campos son obligatorios"
                    return@Button
                }

                // E2: contraseña insegura
                if (contrasena.length < 6) {
                    mensaje = "La contraseña debe tener al menos 6 caracteres"
                    return@Button
                }

                scope.launch {
                    try {
                        val existente = usuarioDao.getByCorreo(correo)

                        // E1: correo ya registrado
                        if (existente != null) {
                            mensaje = "El correo ya está registrado. Inicia sesión."
                        } else {

                            val nuevoUsuario = UsuarioEntity(
                                nombre = nombre,
                                correo = correo,
                                contrasenya = contrasena,
                                presupuesto = 0.0,          // ✔️ valor inicial
                                tipoDeViaje = "General"     // ✔️ valor inicial
                            )

                            usuarioDao.insert(nuevoUsuario)
                            mensaje = ""
                            onRegistroCorrecto()
                        }

                    } catch (e: Exception) {
                        // E3: error de conexión / BD
                        mensaje = "Error al registrar. Inténtalo de nuevo."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = Color(0xFFEF4343),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onIrAInicioSesion) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}
