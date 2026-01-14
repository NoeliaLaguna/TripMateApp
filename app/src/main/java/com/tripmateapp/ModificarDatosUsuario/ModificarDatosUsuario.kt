package com.tripmateapp.ModificarDatosUsuario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


class ModificarDatosUsuario : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ModificarDatosUsuarioScreen(
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModificarDatosUsuarioScreen(
    onBack: () -> Unit
) {
    // ---------------- CONTEXTO + BD ----------------
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val usuarioDao = database.usuarioDao()
    val scope = rememberCoroutineScope()

    // ---------------- ESTADO USUARIO ----------------
    var usuario by remember { mutableStateOf<UsuarioEntity?>(null) }

    // Campos editables
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var presupuesto by remember { mutableStateOf(500f) }

    // Preferencias
    var relax by remember { mutableStateOf(false) }
    var aventura by remember { mutableStateOf(false) }
    var internacional by remember { mutableStateOf(false) }

    var mensaje by remember { mutableStateOf("") }

    // ---------------- CARGAR USUARIO ----------------
    LaunchedEffect(Unit) {
        // ⚠️ Ejemplo: cogemos el primer usuario
        val u = usuarioDao.getAll().firstOrNull()
        usuario = u

        u?.let {
            nombre = it.nombre
            correo = it.correo
            contrasena = it.contrasenya ?: ""
            presupuesto = (it.presupuesto ?: 500.0).toFloat()

            val tipo = it.tipoDeViaje ?: ""
            relax = tipo.contains("Relax")
            aventura = tipo.contains("Aventura")
            internacional = tipo.contains("Internacional")
        }
    }

    // ---------------- UI ----------------
    Scaffold(
        topBar = {
            DatosUsuarioTopBar(onBack = onBack)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ---------- FOTO USUARIO ----------
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable {
                        // Aquí luego podrías abrir galería/cámara
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto usuario",
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // ---------- DATOS PERSONALES ----------
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // ---------- PREFERENCIAS ----------
            Text("Preferencias de viaje", style = MaterialTheme.typography.titleMedium)

            PreferenceItem("Planes de relax", relax) { relax = it }
            PreferenceItem("Viajes de aventura", aventura) { aventura = it }
            PreferenceItem("Viajes internacionales", internacional) { internacional = it }

            Spacer(Modifier.height(24.dp))

            // ---------- PRESUPUESTO ----------
            Text(
                text = "Presupuesto (€${presupuesto.toInt()})",
                style = MaterialTheme.typography.titleMedium
            )

            Slider(
                value = presupuesto,
                onValueChange = { presupuesto = it },
                valueRange = 100f..5000f
            )

            Spacer(Modifier.height(24.dp))

            // ---------- MENSAJE ----------
            if (mensaje.isNotEmpty()) {
                Text(mensaje, color = Color(0xFF2E7D32))
                Spacer(Modifier.height(12.dp))
            }

            // ---------- GUARDAR ----------
            Button(
                onClick = {
                    scope.launch {
                        usuario?.let { u ->
                            val tipoViaje = buildList {
                                if (relax) add("Relax")
                                if (aventura) add("Aventura")
                                if (internacional) add("Internacional")
                            }.joinToString(", ")

                            val actualizado = u.copy(
                                nombre = nombre,
                                correo = correo,
                                contrasenya = contrasena,
                                presupuesto = presupuesto.toDouble(),
                                tipoDeViaje = tipoViaje
                            )

                            usuarioDao.update(actualizado)
                            mensaje = "Datos actualizados correctamente"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    }
}

// ---------------- TOP APP BAR ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosUsuarioTopBar(
    onBack: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
        },
        title = {
            Text("Datos de usuario")
        },
        actions = {
            Image(
                painter = painterResource(id = R.drawable.logo_tripmate),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(28.dp)
                    .padding(end = 12.dp)
            )
        }
    )
}

// ---------------- PREFERENCIA REUTILIZABLE ----------------
@Composable
fun PreferenceItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text)
    }
}