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
 * Se ejecuta SOLO la primera vez que se crea la base de datos.
 * Inserta datos iniciales (seed).
 */
class DatabaseCallback(
    private val scope: CoroutineScope,
    private val dbProvider: () -> AppDatabase
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        Log.d("NOELIA", "SE HA EJECUTADO EL CALLBACK Y SE INSERTAN DATOS")
        super.onCreate(db)

        scope.launch {
            val database = dbProvider()

            insertarDestinos(database)
            insertarLugaresTuristicos(database)
            insertarRestaurantes(database)
            insertarTransportes(database)
        }
    }
}

/* ===========================
   DESTINOS (SIGUIENDO TU MODELO)
   =========================== */
private suspend fun insertarDestinos(db: AppDatabase) {
    db.destinoDao().insertAll(
        listOf(
            DestinoEntity(
                nombre = "París",
                pais = "Francia",
                descripcion = "Ciudad romántica",
                coordenadas = "48.8566,2.3522"
            ),
            DestinoEntity(
                nombre = "Roma",
                pais = "Italia",
                descripcion = "Historia milenaria",
                coordenadas = "41.9028,12.4964"
            ),
            DestinoEntity(
                nombre = "Tokio",
                pais = "Japón",
                descripcion = "Tradición y tecnología",
                coordenadas = "35.6895,139.6917"
            ),
            DestinoEntity(
                nombre = "Nueva York",
                pais = "EE.UU.",
                descripcion = "La ciudad que nunca duerme",
                coordenadas = "40.7128,-74.0060"
            ),
            DestinoEntity(
                nombre = "Londres",
                pais = "Reino Unido",
                descripcion = "Ciudad multicultural",
                coordenadas = "51.5074,-0.1278"
            ),
            DestinoEntity(
                nombre = "Barcelona",
                pais = "España",
                descripcion = "Arte y playa",
                coordenadas = "41.3851,2.1734"
            ),
            DestinoEntity(
                nombre = "Berlín",
                pais = "Alemania",
                descripcion = "Historia moderna",
                coordenadas = "52.5200,13.4050"
            ),
            DestinoEntity(
                nombre = "Ámsterdam",
                pais = "Países Bajos",
                descripcion = "Canales y cultura",
                coordenadas = "52.3676,4.9041"
            ),
            DestinoEntity(
                nombre = "Lisboa",
                pais = "Portugal",
                descripcion = "Ciudad costera",
                coordenadas = "38.7223,-9.1393"
            ),
            DestinoEntity(
                nombre = "Praga",
                pais = "República Checa",
                descripcion = "Arquitectura medieval",
                coordenadas = "50.0755,14.4378"
            )
        )
    )
}

/* ===========================
   LUGARES TURÍSTICOS (5 x destino)
   =========================== */
private suspend fun insertarLugaresTuristicos(db: AppDatabase) {
    val lugares = (1..10).flatMap { destinoId ->
        (1..5).map {
            LugarTuristicoEntity(
                destinoId = destinoId,
                nombre = "Lugar $it - Destino $destinoId",
                descripcion = "Descripción del lugar $it",
                ubicacion = "Ubicación genérica",
                valoracion = 4.0 + it * 0.1,
                categoria = "General"
            )
        }
    }
    db.lugarTuristicoDao().insertAll(lugares)
}

/* ===========================
   RESTAURANTES (5 x destino)
   =========================== */
private suspend fun insertarRestaurantes(db: AppDatabase) {
    val restaurantes = (1..10).flatMap { destinoId ->
        (1..5).map {
            RestauranteEntity(
                destinoId = destinoId,
                nombre = "Restaurante $it - Destino $destinoId",
                ubicacion = "Zona $it",
                tipoComida = "Internacional",
                puntuacionMedia = 4.0 + it * 0.1,
                horarioApertura = "12:00 - 23:00",
                rangoPrecio = "€€"
            )
        }
    }
    db.restauranteDao().insertAll(restaurantes)
}

/* ===========================
   TRANSPORTES (5 x destino)
   =========================== */
private suspend fun insertarTransportes(db: AppDatabase) {
    val transportes = (1..10).flatMap { destinoId ->
        (1..5).map {
            TransporteEntity(
                destinoId = destinoId,
                tipo = listOf("Metro", "Bus", "Tranvía", "Taxi", "Bici")[it - 1],
                nombre = "Transporte $it - Destino $destinoId",
                horario = "06:00 - 23:00",
                precio = 1.5 + it * 0.3
            )
        }
    }
    db.transporteDao().insertAll(transportes)
}