package com.tripmateapp.inicioSesion

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
import com.tripmateapp.R
import com.tripmateapp.utilidades.PreferenciasLogin
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioSesionScreen(
    onLoginCorrecto: () -> Unit,
    onIrARegistro: () -> Unit
) {

    // CONTEXTO Y DEPENDENCIAS
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val usuarioDao = database.usuarioDao()
    val preferenciasLogin = PreferenciasLogin(context)
    val scope = rememberCoroutineScope()

    // ESTADO UI
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var recordar by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "INICIO DE SESIÓN",
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

                // 🔥 IMAGEN / LOGO (MUCHO MÁS ARRIBA)
                Image(
                    painter = painterResource(id = R.drawable.logo_tripmate),
                    contentDescription = "Logo TripMate",
                    modifier = Modifier
                        .size(170.dp)
                        .padding(top = 0.dp, bottom = 40.dp)
                        .offset(y = (-20).dp) // 🔥 subida extra fina
                )

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

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
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
                    }
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

                TextButton(onClick = onIrARegistro) {
                    Text("¿No tienes cuenta? Regístrate")
                }
            }
        }
    }
}
