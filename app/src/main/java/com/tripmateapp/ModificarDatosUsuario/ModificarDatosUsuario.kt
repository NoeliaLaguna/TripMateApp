package com.tripmateapp.ModificarDatosUsuario

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tripmateapp.BaseDatos.DatabaseProvider
import com.tripmateapp.BaseDatos.Usuarios.UsuarioEntity
import com.tripmateapp.R
import kotlinx.coroutines.launch

class ModificarDatosUsuario : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ModificarDatosUsuarioScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModificarDatosUsuarioScreen(onBack: () -> Unit) {

    // CONTEXTO Y BD
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val usuarioDao = database.usuarioDao()
    val scope = rememberCoroutineScope()

    // USUARIO ORIGINAL
    var usuarioOriginal by remember { mutableStateOf<UsuarioEntity?>(null) }

    // CAMPOS
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var presupuesto by remember { mutableStateOf(500f) }

    // PREFERENCIAS
    var relax by remember { mutableStateOf(false) }
    var aventura by remember { mutableStateOf(false) }
    var familiar by remember { mutableStateOf(false) }
    var internacional by remember { mutableStateOf(false) }

    var romantico by remember { mutableStateOf(false) }
    var urbano by remember { mutableStateOf(false) }
    var naturaleza by remember { mutableStateOf(false) }
    var playa by remember { mutableStateOf(false) }
    var mochilero by remember { mutableStateOf(false) }
    var mascotas by remember { mutableStateOf(false) }

    // VER MÁS / MENOS
    var verMas by remember { mutableStateOf(false) }

    var mensaje by remember { mutableStateOf("") }

    // CARGAR USUARIO
    LaunchedEffect(Unit) {
        val u = usuarioDao.getAll().firstOrNull()
        usuarioOriginal = u

        u?.let {
            nombre = it.nombre
            correo = it.correo
            contrasena = it.contrasenya ?: ""
            presupuesto = (it.presupuesto ?: 500.0).toFloat()

            val tipo = it.tipoDeViaje ?: ""
            relax = tipo.contains("Relax")
            aventura = tipo.contains("Aventura")
            familiar = tipo.contains("Familiar")
            internacional = tipo.contains("Internacional")
            romantico = tipo.contains("Romántico")
            urbano = tipo.contains("Urbano")
            naturaleza = tipo.contains("Naturaleza")
            playa = tipo.contains("Playa")
            mochilero = tipo.contains("Mochilero")
            mascotas = tipo.contains("Mascotas")
        }
    }

    Scaffold(
        topBar = { DatosUsuarioTopBar(onBack) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // FOTO
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp))
            }

            Spacer(Modifier.height(24.dp))

            // DATOS
            OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(correo, { correo = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                contrasena,
                { contrasena = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // PREFERENCIAS
            Text("Preferencias de viaje", style = MaterialTheme.typography.titleMedium)

            PreferenceItem("Relax", relax) { relax = it }
            PreferenceItem("Aventura", aventura) { aventura = it }
            PreferenceItem("Familiar", familiar) { familiar = it }
            PreferenceItem("Internacional", internacional) { internacional = it }

            if (verMas) {
                PreferenceItem("Romántico", romantico) { romantico = it }
                PreferenceItem("Urbano / Ciudades", urbano) { urbano = it }
                PreferenceItem("Naturaleza", naturaleza) { naturaleza = it }
                PreferenceItem("Playa", playa) { playa = it }
                PreferenceItem("Mochilero", mochilero) { mochilero = it }
                PreferenceItem("Viajes con mascotas", mascotas) { mascotas = it }
            }

            TextButton(
                onClick = { verMas = !verMas },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(if (verMas) "Ver menos" else "Ver más")
            }

            Spacer(Modifier.height(24.dp))

            // PRESUPUESTO
            Text("Presupuesto (€${presupuesto.toInt()})")
            Slider(presupuesto, { presupuesto = it }, valueRange = 100f..5000f)

            Spacer(Modifier.height(16.dp))

            if (mensaje.isNotEmpty()) {
                Text(mensaje, color = Color(0xFFEF4343))
                Spacer(Modifier.height(8.dp))
            }

            // GUARDAR
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        val original = usuarioOriginal ?: return@launch

                        if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank()) {
                            mensaje = "Todos los campos son obligatorios"
                            return@launch
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                            mensaje = "Correo electrónico no válido"
                            return@launch
                        }

                        if (contrasena.length < 6) {
                            mensaje = "La contraseña debe tener al menos 6 caracteres"
                            return@launch
                        }

                        val existente = usuarioDao.getByCorreo(correo)
                        if (existente != null && existente.id != original.id) {
                            mensaje = "Ese correo ya está en uso"
                            return@launch
                        }

                        val tipoViaje = buildList {
                            if (relax) add("Relax")
                            if (aventura) add("Aventura")
                            if (familiar) add("Familiar")
                            if (internacional) add("Internacional")
                            if (romantico) add("Romántico")
                            if (urbano) add("Urbano")
                            if (naturaleza) add("Naturaleza")
                            if (playa) add("Playa")
                            if (mochilero) add("Mochilero")
                            if (mascotas) add("Mascotas")
                        }.joinToString(", ")

                        val hayCambios =
                            original.nombre != nombre ||
                                    original.correo != correo ||
                                    (original.contrasenya ?: "") != contrasena ||
                                    (original.presupuesto ?: 0.0) != presupuesto.toDouble() ||
                                    (original.tipoDeViaje ?: "") != tipoViaje

                        if (!hayCambios) {
                            mensaje = "No hay cambios para guardar"
                            return@launch
                        }

                        val actualizado = original.copy(
                            nombre = nombre,
                            correo = correo,
                            contrasenya = contrasena,
                            presupuesto = presupuesto.toDouble(),
                            tipoDeViaje = tipoViaje
                        )

                        usuarioDao.update(actualizado)
                        usuarioOriginal = actualizado
                        mensaje = "Datos actualizados correctamente"
                    }
                }
            ) {
                Text("Guardar cambios")
            }
        }
    }
}

// TOP BAR
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosUsuarioTopBar(onBack: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
        },
        title = { Text("Datos de usuario") },
        actions = {
            Image(
                painter = painterResource(id = R.drawable.logo_tripmate),
                contentDescription = null,
                modifier = Modifier.height(28.dp).padding(end = 12.dp)
            )
        }
    )
}

// CHECKBOX REUTILIZABLE
@Composable
fun PreferenceItem(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text)
    }
}
