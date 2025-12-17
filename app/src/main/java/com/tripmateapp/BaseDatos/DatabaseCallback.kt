import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tripmateapp.BaseDatos.AppDatabase
import com.tripmateapp.BaseDatos.Destinos.DestinoEntity
import com.tripmateapp.BaseDatos.LugaresTuristicos.LugarTuristicoEntity
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteEntity
import com.tripmateapp.BaseDatos.Transporte.TransporteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * DatabaseCallback
 *
 * Callback de Room que se ejecuta automáticamente
 * SOLO la primera vez que se crea la base de datos.
 *
 * Se utiliza para:
 * - Insertar datos iniciales (seed)
 * - Precargar tablas con información por defecto
 *
 * No se ejecuta en actualizaciones ni reinicios de la app,
 * solo cuando la base de datos se crea desde cero.
 */

class DatabaseCallback(
    private val scope: CoroutineScope,
    private val dbProvider: () -> AppDatabase
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        Log.d("DATABASENOELIA", "SE HA EJECUTADO EL CALLBACK Y SE INSERTAN DATOS")
        super.onCreate(db)

        scope.launch {
            val database = dbProvider()

            insertarDestinos(database)
            insertarLugaresTuristicos(database)
            insertarRestaurantes(database)
            insertarTransportes(database)
            // actividades → SOLO cuando existan itinerarios/días
        }
    }
}
private suspend fun insertarDestinos(db: AppDatabase) {
    db.destinoDao().insertAll(
        listOf(
            DestinoEntity(nombre = "París", pais = "Francia", descripcion = "Ciudad romántica", coordenadas = "48.8566,2.3522"),
            DestinoEntity(
                nombre = "Roma",
                pais = "Italia",
                descripcion = "Historia milenaria",
                coordenadas = "41.9028,12.4964"
            ),
            DestinoEntity(nombre = "Tokio", pais = "Japón", descripcion = "Tradición y tecnología", coordenadas = "35.6895,139.6917"),
            DestinoEntity(nombre = "Nueva York", pais = "EE.UU.", descripcion = "La ciudad que nunca duerme", coordenadas = "40.7128,-74.0060"),
            DestinoEntity(nombre = "Londres", pais = "Reino Unido", descripcion = "Ciudad multicultural", coordenadas = "51.5074,-0.1278"),
            DestinoEntity(nombre = "Barcelona", pais = "España", descripcion = "Arte y playa", coordenadas = "41.3851,2.1734"),
            DestinoEntity(nombre = "Berlín", pais = "Alemania", descripcion = "Historia moderna", coordenadas = "52.5200,13.4050"),
            DestinoEntity(nombre = "Ámsterdam", pais = "Países Bajos", descripcion = "Canales y cultura", coordenadas = "52.3676,4.9041"),
            DestinoEntity(nombre = "Lisboa", pais = "Portugal", descripcion = "Ciudad costera", coordenadas = "38.7223,-9.1393"),
            DestinoEntity(nombre = "Praga", pais = "República Checa", descripcion = "Arquitectura medieval", coordenadas = "50.0755,14.4378")
        )
    )
}
private suspend fun insertarLugaresTuristicos(db: AppDatabase) {
    db.lugarTuristicoDao().insertAll(
        listOf(
            LugarTuristicoEntity(
                destinoId = 1,
                nombre = "Torre Eiffel",
                descripcion = "Icono de París",
                ubicacion = "Champ de Mars",
                valoracion = 4.8,
                categoria = "Monumento"
            ),
            LugarTuristicoEntity(destinoId = 2, nombre = "Coliseo", descripcion = "Anfiteatro romano", ubicacion = "Centro", valoracion = 4.7, categoria = "Historia"),
            LugarTuristicoEntity(destinoId = 3, nombre = "Shibuya", descripcion = "Cruce famoso", ubicacion = "Tokio", valoracion = 4.6, categoria = "Urbano"),
            LugarTuristicoEntity(destinoId = 4, nombre = "Central Park", descripcion = "Parque urbano", ubicacion = "Manhattan", valoracion = 4.9, categoria = "Naturaleza"),
            LugarTuristicoEntity(destinoId = 5, nombre = "Big Ben", descripcion = "Reloj histórico", ubicacion = "Westminster", valoracion = 4.5, categoria = "Monumento"),
            LugarTuristicoEntity(destinoId = 6, nombre = "Sagrada Familia", descripcion = "Obra de Gaudí", ubicacion = "Barcelona", valoracion = 4.9, categoria = "Arquitectura"),
            LugarTuristicoEntity(destinoId = 7, nombre = "Muro de Berlín", descripcion = "Historia reciente", ubicacion = "Berlín", valoracion = 4.6, categoria = "Historia"),
            LugarTuristicoEntity(destinoId = 8, nombre = "Canales", descripcion = "Recorrido en barco", ubicacion = "Ámsterdam", valoracion = 4.7, categoria = "Cultural"),
            LugarTuristicoEntity(destinoId = 9, nombre = "Torre de Belém", descripcion = "Fortaleza", ubicacion = "Lisboa", valoracion = 4.5, categoria = "Monumento"),
            LugarTuristicoEntity(destinoId = 10, nombre = "Puente de Carlos", descripcion = "Puente histórico", ubicacion = "Praga", valoracion = 4.8, categoria = "Arquitectura")
        )
    )
}
private suspend fun insertarRestaurantes(db: AppDatabase) {
    db.restauranteDao().insertAll(
        listOf(
            RestauranteEntity(destinoId = 1, nombre = "Le Gourmet", ubicacion = "París", tipoComida = "Francesa", puntuacionMedia = 4.6, horarioApertura = "12-23", rangoPrecio = "€€€"),
            RestauranteEntity(
                destinoId = 2,
                nombre = "La Pasta",
                ubicacion = "Roma",
                tipoComida = "Italiana",
                puntuacionMedia = 4.5,
                horarioApertura = "11-22",
                rangoPrecio = "€€"
            ),
            RestauranteEntity(destinoId = 3, nombre = "Sushi Zen", ubicacion = "Tokio", tipoComida = "Japonesa", puntuacionMedia = 4.8, horarioApertura = "12-22", rangoPrecio = "€€€"),
            RestauranteEntity(destinoId = 4, nombre = "Burger City", ubicacion = "NY", tipoComida = "Americana", puntuacionMedia = 4.4, horarioApertura = "10-00", rangoPrecio = "€€"),
            RestauranteEntity(destinoId = 5, nombre = "Tea House", ubicacion = "Londres", tipoComida = "Británica", puntuacionMedia = 4.3, horarioApertura = "09-21", rangoPrecio = "€€"),
            RestauranteEntity(destinoId = 6, nombre = "Tapas Bar", ubicacion = "Barcelona", tipoComida = "Española", puntuacionMedia = 4.7, horarioApertura = "13-23", rangoPrecio = "€€"),
            RestauranteEntity(destinoId = 7, nombre = "Bratwurst Haus", ubicacion = "Berlín", tipoComida = "Alemana", puntuacionMedia = 4.5, horarioApertura = "11-22", rangoPrecio = "€"),
            RestauranteEntity(destinoId = 8, nombre = "Dutch Kitchen", ubicacion = "Ámsterdam", tipoComida = "Holandesa", puntuacionMedia = 4.4, horarioApertura = "12-22", rangoPrecio = "€€"),
            RestauranteEntity(destinoId = 9, nombre = "Mar Azul", ubicacion = "Lisboa", tipoComida = "Mariscos", puntuacionMedia = 4.6, horarioApertura = "12-23", rangoPrecio = "€€"),
            RestauranteEntity(destinoId = 10, nombre = "Bohemian Food", ubicacion = "Praga", tipoComida = "Checa", puntuacionMedia = 4.5, horarioApertura = "11-22", rangoPrecio = "€€")
        )
    )
}
private suspend fun insertarTransportes(db: AppDatabase) {
    db.transporteDao().insertAll(
        listOf(
            TransporteEntity(
                destinoId = 1,
                tipo = "Metro",
                nombre = "Línea 1",
                horario = "05-00",
                precio = 1.9
            ),
            TransporteEntity(destinoId = 2, tipo = "Bus", nombre = "Bus 64", horario = "06-23", precio = 1.5),
            TransporteEntity(destinoId = 3, tipo = "Tren", nombre = "JR Line", horario = "05-01", precio = 2.3),
            TransporteEntity(destinoId = 4, tipo = "Metro", nombre = "Subway", horario = "24h", precio = 2.75),
            TransporteEntity(destinoId = 5, tipo = "Metro", nombre = "Tube", horario = "05-00", precio = 2.5),
            TransporteEntity(destinoId = 6, tipo = "Tranvía", nombre = "TRAM", horario = "06-23", precio = 2.0),
            TransporteEntity(destinoId = 7, tipo = "Bus", nombre = "Bus 100", horario = "05-22", precio = 1.8),
            TransporteEntity(destinoId = 8, tipo = "Tranvía", nombre = "GVB", horario = "06-00", precio = 2.2),
            TransporteEntity(destinoId = 9, tipo = "Metro", nombre = "Metro Lisboa", horario = "06-01", precio = 1.6),
            TransporteEntity(destinoId = 10, tipo = "Tram", nombre = "Tram 22", horario = "06-23", precio = 1.7)
        )
    )
}





