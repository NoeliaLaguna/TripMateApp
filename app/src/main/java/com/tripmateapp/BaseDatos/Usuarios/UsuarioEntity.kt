package com.tripmateapp.BaseDatos.Usuarios
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contrasenya: String?,
    val presupuesto: Double,
    val tipoDeViaje: String
)