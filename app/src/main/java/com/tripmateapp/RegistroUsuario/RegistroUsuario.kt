package com.tripmateapp.RegistroUsuario

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tripmateapp.BaseDatos.DatabaseProvider
import com.tripmateapp.BaseDatos.Usuarios.UsuarioEntity
import com.tripmateapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onRegistroCorrecto: () -> Unit,
    onIrAInicioSesion: () -> Unit
) {

    // CONTEXTO Y DEPENDENCIAS
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val usuarioDao = database.usuarioDao()
    val scope = rememberCoroutineScope()

    // ESTADO UI
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "REGISTRO",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFDCB6C3),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // LOGO
                Image(
                    painter = painterResource(id = R.drawable.logo_tripmate),
                    contentDescription = "Logo TripMate",
                    modifier = Modifier
                        .size(170.dp)
                        .padding(bottom = 40.dp)
                        .offset(y = (-20).dp)
                )

                // NOMBRE
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // CORREO
                TextField(
                    value = correo,
                    onValueChange = { correo = it },
                    placeholder = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // CONTRASEÑA
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

                        if (contrasena.length < 6) {
                            mensaje = "La contraseña debe tener al menos 6 caracteres"
                            return@Button
                        }

                        scope.launch {
                            try {
                                val existente = usuarioDao.getByCorreo(correo)

                                if (existente != null) {
                                    mensaje = "El correo ya está registrado. Inicia sesión."
                                } else {

                                    val nuevoUsuario = UsuarioEntity(
                                        nombre = nombre,
                                        correo = correo,
                                        contrasenya = contrasena,
                                        presupuesto = 0.0,
                                        tipoDeViaje = "General"
                                    )

                                    usuarioDao.insert(nuevoUsuario)
                                    mensaje = ""
                                    onRegistroCorrecto()
                                }

                            } catch (e: Exception) {
                                mensaje = "Error al registrar. Inténtalo de nuevo."
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrarse", fontSize = 20.sp)
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

                TextButton(onClick = onIrAInicioSesion) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }
            }
        }
    }
}

