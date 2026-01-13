import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tripmateapp.BaseDatos.AppDatabase
import com.tripmateapp.BaseDatos.Destinos.DestinoEntity
import com.tripmateapp.BaseDatos.LugaresTuristicos.LugarTuristicoEntity
import com.tripmateapp.BaseDatos.Restaurantes.RestauranteEntity
import com.tripmateapp.BaseDatos.Transporte.TransporteEntity
import com.tripmateapp.BaseDatos.actividades.ActividadEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DatabaseCallback(
    private val scope: CoroutineScope,
    private val dbProvider: () -> AppDatabase
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        Log.d("DATABASE", "Creando base de datos con datos reales")

        scope.launch {
            val database = dbProvider()
            insertarActividades(database)
            insertarDestinos(database)
            insertarLugares(database)
            insertarRestaurantes(database)
            insertarTransportes(database)
        }
    }
}

/* ===========================
   DESTINOS (10)
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

private suspend fun insertarActividades(db: AppDatabase) {
    db.actividadDao().insertAll(
        listOf(
            // ================= PARÍS (destinoId = 1) =================
            ActividadEntity(1, null, 1, "Turismo", 1, "Visita a la Torre Eiffel", null, null),
            ActividadEntity(2, null, 1, "Cultura", 2, "Free tour por el centro histórico", null, null),
            ActividadEntity(3, null, 1, "Gastronomía", 3, "Comida en bistró parisino", null, null),
            ActividadEntity(4, null, 1, "Relax", 4, "Tarde de spa y bienestar", null, null),
            ActividadEntity(5, null, 1, "Ocio nocturno", 5, "Paseo nocturno por el Sena", null, null),

            // ================= ROMA (destinoId = 2) =================
            ActividadEntity(6, null, 2, "Turismo", 1, "Visita al Coliseo y Foro Romano", null, null),
            ActividadEntity(7, null, 2, "Cultura", 2, "Recorrido por el Vaticano", null, null),
            ActividadEntity(8, null, 2, "Gastronomía", 3, "Degustación de pasta tradicional", null, null),
            ActividadEntity(9, null, 2, "Relax", 4, "Descanso en termas urbanas", null, null),
            ActividadEntity(10, null, 2, "Ocio nocturno", 5, "Paseo nocturno por Trastevere", null, null),

            // ================= TOKIO (destinoId = 3) =================
            ActividadEntity(11, null, 3, "Turismo", 1, "Recorrido por Shibuya y Shinjuku", null, null),
            ActividadEntity(12, null, 3, "Cultura", 2, "Visita a templo Senso-ji", null, null),
            ActividadEntity(13, null, 3, "Gastronomía", 3, "Ruta de sushi tradicional", null, null),
            ActividadEntity(14, null, 3, "Relax", 4, "Experiencia onsen japonesa", null, null),
            ActividadEntity(15, null, 3, "Ocio nocturno", 5, "Luces de neón en Akihabara", null, null),

            // ================= NUEVA YORK (destinoId = 4) =================
            ActividadEntity(16, null, 4, "Turismo", 1, "Visita a Central Park y Midtown", null, null),
            ActividadEntity(17, null, 4, "Cultura", 2, "Museo Metropolitano de Arte", null, null),
            ActividadEntity(18, null, 4, "Gastronomía", 3, "Comida típica americana", null, null),
            ActividadEntity(19, null, 4, "Deporte", 4, "Paseo en bicicleta por Brooklyn", null, null),
            ActividadEntity(20, null, 4, "Ocio nocturno", 5, "Broadway show nocturno", null, null),

            // ================= LONDRES (destinoId = 5) =================
            ActividadEntity(21, null, 5, "Turismo", 1, "Visita a Westminster y Big Ben", null, null),
            ActividadEntity(22, null, 5, "Cultura", 2, "British Museum", null, null),
            ActividadEntity(23, null, 5, "Gastronomía", 3, "Fish & Chips tradicional", null, null),
            ActividadEntity(24, null, 5, "Relax", 4, "Paseo por Hyde Park", null, null),
            ActividadEntity(25, null, 5, "Ocio nocturno", 5, "Pub crawl nocturno", null, null),

            // ================= BARCELONA (destinoId = 6) =================
            ActividadEntity(26, null, 6, "Turismo", 1, "Visita a la Sagrada Familia", null, null),
            ActividadEntity(27, null, 6, "Cultura", 2, "Recorrido modernista por el Eixample", null, null),
            ActividadEntity(28, null, 6, "Gastronomía", 3, "Ruta de tapas", null, null),
            ActividadEntity(29, null, 6, "Relax", 4, "Tarde de playa en la Barceloneta", null, null),
            ActividadEntity(30, null, 6, "Ocio nocturno", 5, "Noche en el barrio del Born", null, null),

            // ================= BERLÍN (destinoId = 7) =================
            ActividadEntity(31, null, 7, "Turismo", 1, "Visita al Muro de Berlín", null, null),
            ActividadEntity(32, null, 7, "Cultura", 2, "Isla de los Museos", null, null),
            ActividadEntity(33, null, 7, "Gastronomía", 3, "Degustación de comida alemana", null, null),
            ActividadEntity(34, null, 7, "Relax", 4, "Descanso en parque Tiergarten", null, null),
            ActividadEntity(35, null, 7, "Ocio nocturno", 5, "Club nocturno berlinés", null, null),

            // ================= ÁMSTERDAM (destinoId = 8) =================
            ActividadEntity(36, null, 8, "Turismo", 1, "Paseo en barco por los canales", null, null),
            ActividadEntity(37, null, 8, "Cultura", 2, "Museo Van Gogh", null, null),
            ActividadEntity(38, null, 8, "Gastronomía", 3, "Comida local holandesa", null, null),
            ActividadEntity(39, null, 8, "Relax", 4, "Paseo en bicicleta urbana", null, null),
            ActividadEntity(40, null, 8, "Ocio nocturno", 5, "Barrio Rojo nocturno", null, null),

            // ================= LISBOA (destinoId = 9) =================
            ActividadEntity(41, null, 9, "Turismo", 1, "Recorrido por Belém", null, null),
            ActividadEntity(42, null, 9, "Cultura", 2, "Tranvía histórico por Alfama", null, null),
            ActividadEntity(43, null, 9, "Gastronomía", 3, "Degustación de bacalao y pasteles", null, null),
            ActividadEntity(44, null, 9, "Relax", 4, "Miradores al atardecer", null, null),
            ActividadEntity(45, null, 9, "Ocio nocturno", 5, "Noche de fado", null, null),

            // ================= PRAGA (destinoId = 10) =================
            ActividadEntity(46, null, 10, "Turismo", 1, "Visita al Castillo de Praga", null, null),
            ActividadEntity(47, null, 10, "Cultura", 2, "Free tour por la ciudad vieja", null, null),
            ActividadEntity(48, null, 10, "Gastronomía", 3, "Cena tradicional checa", null, null),
            ActividadEntity(49, null, 10, "Relax", 4, "Paseo por el río Moldava", null, null),
            ActividadEntity(50, null, 10, "Ocio nocturno", 5, "Tour nocturno de leyendas", null, null)
        )
    )
}


/* ===========================
   LUGARES TURÍSTICOS (50)
   =========================== */
private suspend fun insertarLugares(db: AppDatabase) {
    db.lugarTuristicoDao().insertAll(
        listOf(
            // ================= PARÍS (destinoId = 1) =================
            LugarTuristicoEntity(1, 1, "Torre Eiffel", "Icono de París", "Avenue Anatole France 5", 4.8, "Histórico"),
            LugarTuristicoEntity(2, 1, "Museo del Louvre", "Museo de arte más famoso del mundo", "Rue de Rivoli 99", 4.9, "Cultural"),
            LugarTuristicoEntity(3, 1, "Notre Dame", "Catedral gótica histórica", "6 Parvis Notre-Dame", 4.7, "Religioso"),
            LugarTuristicoEntity(4, 1, "Montmartre", "Barrio bohemio y artístico", "Rue Lepic 36", 4.6, "Cultural"),
            LugarTuristicoEntity(5, 1, "Campos Elíseos", "Avenida más famosa de París", "Avenue des Champs-Élysées 50", 4.5, "Urbano"),

            // ================= ROMA (destinoId = 2) =================
            LugarTuristicoEntity(6, 2, "Coliseo", "Anfiteatro romano emblemático", "Piazza del Colosseo 1", 4.8, "Histórico"),
            LugarTuristicoEntity(7, 2, "Foro Romano", "Centro político de la antigua Roma", "Via della Salara Vecchia 5", 4.7, "Histórico"),
            LugarTuristicoEntity(8, 2, "Fontana di Trevi", "Fuente barroca más famosa", "Piazza di Trevi 1", 4.6, "Cultural"),
            LugarTuristicoEntity(9, 2, "Panteón", "Templo romano mejor conservado", "Piazza della Rotonda", 4.7, "Histórico"),
            LugarTuristicoEntity(10, 2, "Vaticano", "Estado independiente y sede papal", "Viale Vaticano", 4.9, "Religioso"),

            // ================= TOKIO (destinoId = 3) =================
            LugarTuristicoEntity(11, 3, "Shibuya Crossing", "Cruce peatonal más famoso del mundo", "Shibuya City Center", 4.6, "Urbano"),
            LugarTuristicoEntity(12, 3, "Senso-ji", "Templo budista más antiguo de Tokio", "2 Chome-3-1 Asakusa", 4.7, "Religioso"),
            LugarTuristicoEntity(13, 3, "Tokyo Tower", "Torre de telecomunicaciones", "4 Chome-2-8 Shibakoen", 4.5, "Urbano"),
            LugarTuristicoEntity(14, 3, "Akihabara", "Distrito tecnológico y anime", "Sotokanda 1 Chome", 4.6, "Cultural"),
            LugarTuristicoEntity(15, 3, "Meiji Shrine", "Santuario sintoísta", "1-1 Yoyogikamizonocho", 4.8, "Religioso"),

            // ================= NUEVA YORK (destinoId = 4) =================
            LugarTuristicoEntity(16, 4, "Central Park", "Parque urbano icónico", "59th St to 110th St", 4.8, "Naturaleza"),
            LugarTuristicoEntity(17, 4, "Times Square", "Zona comercial y turística", "Broadway & 7th Ave", 4.6, "Urbano"),
            LugarTuristicoEntity(18, 4, "Estatua de la Libertad", "Símbolo de libertad", "Liberty Island", 4.7, "Histórico"),
            LugarTuristicoEntity(19, 4, "Empire State", "Rascacielos histórico", "350 5th Avenue", 4.6, "Urbano"),
            LugarTuristicoEntity(20, 4, "Brooklyn Bridge", "Puente histórico", "Brooklyn Bridge Blvd", 4.5, "Histórico"),

            // ================= LONDRES (destinoId = 5) =================
            LugarTuristicoEntity(21, 5, "Big Ben", "Reloj histórico del Parlamento", "Westminster SW1A 0AA", 4.7, "Histórico"),
            LugarTuristicoEntity(22, 5, "London Eye", "Noria panorámica", "Riverside Building SE1 7PB", 4.6, "Urbano"),
            LugarTuristicoEntity(23, 5, "Buckingham Palace", "Residencia real", "Buckingham Palace Rd", 4.7, "Histórico"),
            LugarTuristicoEntity(24, 5, "British Museum", "Museo de historia mundial", "Great Russell St WC1B 3DG", 4.8, "Cultural"),
            LugarTuristicoEntity(25, 5, "Tower Bridge", "Puente levadizo histórico", "Tower Bridge Rd SE1", 4.6, "Histórico"),

            // ================= BARCELONA (destinoId = 6) =================
            LugarTuristicoEntity(26, 6, "Sagrada Familia", "Basílica modernista", "Carrer de Mallorca 401", 4.8, "Religioso"),
            LugarTuristicoEntity(27, 6, "Park Güell", "Parque diseñado por Gaudí", "Carrer d'Olot 5", 4.7, "Cultural"),
            LugarTuristicoEntity(28, 6, "La Rambla", "Avenida céntrica", "La Rambla 1", 4.5, "Urbano"),
            LugarTuristicoEntity(29, 6, "Barceloneta", "Playa urbana", "Passeig Marítim", 4.4, "Naturaleza"),
            LugarTuristicoEntity(30, 6, "Camp Nou", "Estadio del FC Barcelona", "Carrer d'Arístides Maillol 12", 4.6, "Deportivo"),

            // ================= BERLÍN (destinoId = 7) =================
            LugarTuristicoEntity(31, 7, "Puerta de Brandeburgo", "Monumento histórico", "Pariser Platz", 4.7, "Histórico"),
            LugarTuristicoEntity(32, 7, "Muro de Berlín", "Restos del muro histórico", "Bernauer Strasse", 4.6, "Histórico"),
            LugarTuristicoEntity(33, 7, "Isla de los Museos", "Complejo museístico", "Museumsinsel", 4.8, "Cultural"),
            LugarTuristicoEntity(34, 7, "Alexanderplatz", "Plaza céntrica", "Alexanderplatz 1", 4.5, "Urbano"),
            LugarTuristicoEntity(35, 7, "Checkpoint Charlie", "Antiguo paso fronterizo", "Friedrichstrasse 43", 4.6, "Histórico"),

            // ================= ÁMSTERDAM (destinoId = 8) =================
            LugarTuristicoEntity(36, 8, "Canales", "Red de canales históricos", "Canal Ring", 4.8, "Cultural"),
            LugarTuristicoEntity(37, 8, "Museo Van Gogh", "Museo del pintor", "Museumplein 6", 4.7, "Cultural"),
            LugarTuristicoEntity(38, 8, "Rijksmuseum", "Museo nacional", "Museumstraat 1", 4.8, "Cultural"),
            LugarTuristicoEntity(39, 8, "Casa de Ana Frank", "Casa museo histórica", "Prinsengracht 263", 4.7, "Histórico"),
            LugarTuristicoEntity(40, 8, "Vondelpark", "Parque urbano", "Vondelpark 1", 4.6, "Naturaleza"),

            // ================= LISBOA (destinoId = 9) =================
            LugarTuristicoEntity(41, 9, "Torre de Belém", "Torre defensiva histórica", "Av. Brasília", 4.7, "Histórico"),
            LugarTuristicoEntity(42, 9, "Monasterio de los Jerónimos", "Monasterio manuelino", "Praça do Império", 4.8, "Histórico"),
            LugarTuristicoEntity(43, 9, "Barrio de Alfama", "Barrio más antiguo", "Alfama", 4.6, "Cultural"),
            LugarTuristicoEntity(44, 9, "Elevador de Santa Justa", "Ascensor histórico", "Rua do Ouro", 4.5, "Urbano"),
            LugarTuristicoEntity(45, 9, "Mirador Senhora do Monte", "Mirador panorámico", "Largo Monte", 4.7, "Naturaleza"),

            // ================= PRAGA (destinoId = 10) =================
            LugarTuristicoEntity(46, 10, "Puente de Carlos", "Puente medieval icónico", "Karlův most", 4.8, "Histórico"),
            LugarTuristicoEntity(47, 10, "Castillo de Praga", "Complejo histórico", "Hradčany 119", 4.9, "Histórico"),
            LugarTuristicoEntity(48, 10, "Reloj Astronómico", "Reloj medieval", "Staroměstské náměstí", 4.7, "Histórico"),
            LugarTuristicoEntity(49, 10, "Plaza de la Ciudad Vieja", "Plaza histórica", "Staroměstské náměstí", 4.6, "Urbano"),
            LugarTuristicoEntity(50, 10, "Barrio Malá Strana", "Barrio histórico", "Malá Strana", 4.6, "Cultural")
        )
    )
}



/* ===========================
   RESTAURANTES (50)
   =========================== */
private suspend fun insertarRestaurantes(db: AppDatabase) {
    db.restauranteDao().insertAll(
        listOf(
            // ================= PARÍS (destinoId = 1) =================
            RestauranteEntity(1, 1, "Le Jules Verne", "Avenue Gustave Eiffel 5", "Mediterránea", 4.6, "12:00-23:00", "80-150€"),
            RestauranteEntity(2, 1, "Café de Flore", "Boulevard Saint-Germain 172", "Mediterránea", 4.3, "08:00-00:00", "20-40€"),
            RestauranteEntity(3, 1, "Chez Janou", "Rue Roger Verlomme 2", "Mediterránea", 4.5, "12:00-22:00", "25-45€"),
            RestauranteEntity(4, 1, "Le Comptoir", "Carrefour de l'Odéon 9", "Mediterránea", 4.5, "12:00-22:30", "30-50€"),
            RestauranteEntity(5, 1, "L'As du Fallafel", "Rue des Rosiers 34", "Mediterránea", 4.4, "11:00-23:00", "10-20€"),

            // ================= ROMA (destinoId = 2) =================
            RestauranteEntity(6, 2, "Da Enzo", "Via dei Vascellari 29", "Mediterránea", 4.7, "12:30-22:30", "15-30€"),
            RestauranteEntity(7, 2, "Roscioli", "Via dei Giubbonari 21", "Mediterránea", 4.6, "12:00-23:00", "25-50€"),
            RestauranteEntity(8, 2, "Pizzarium", "Via della Meloria 43", "Mediterránea", 4.5, "11:00-22:00", "10-20€"),
            RestauranteEntity(9, 2, "La Pergola", "Via Alberto Cadlolo 101", "Mediterránea", 4.9, "19:00-23:00", "150€+"),
            RestauranteEntity(10, 2, "Tonnarello", "Via della Paglia 1", "Mediterránea", 4.6, "12:00-23:00", "15-25€"),

            // ================= TOKIO (destinoId = 3) =================
            RestauranteEntity(11, 3, "Sukiyabashi Jiro", "Ginza 4-2-15", "Asiática", 4.8, "11:30-20:00", "200€+"),
            RestauranteEntity(12, 3, "Ichiran Ramen", "Jinnan 1-22-7", "Asiática", 4.6, "10:00-23:00", "10-20€"),
            RestauranteEntity(13, 3, "Gonpachi", "Nishiazabu 1-13-11", "Asiática", 4.5, "11:30-23:00", "20-40€"),
            RestauranteEntity(14, 3, "Uobei Sushi", "Dogenzaka 2-29-11", "Asiática", 4.4, "11:00-22:00", "15-25€"),
            RestauranteEntity(15, 3, "Tempura Kondo", "Ginza 5-5-13", "Asiática", 4.7, "12:00-21:00", "80-120€"),

            // ================= NUEVA YORK (destinoId = 4) =================
            RestauranteEntity(16, 4, "Katz's Delicatessen", "E Houston St 205", "Americana", 4.6, "08:00-22:45", "15-30€"),
            RestauranteEntity(17, 4, "Shake Shack", "Madison Ave 23", "Americana", 4.4, "11:00-23:00", "10-20€"),
            RestauranteEntity(18, 4, "The River Café", "Water St 1", "Internacional", 4.7, "18:00-22:00", "100-150€"),
            RestauranteEntity(19, 4, "Joe's Pizza", "Carmine St 7", "Americana", 4.5, "11:00-23:00", "5-15€"),
            RestauranteEntity(20, 4, "Le Bernardin", "W 51st St 155", "Mediterránea", 4.9, "12:00-22:00", "150€+"),

            // ================= LONDRES (destinoId = 5) =================
            RestauranteEntity(21, 5, "Dishoom", "Upper St Martin's Ln 12", "Asiática", 4.6, "08:00-23:00", "20-40€"),
            RestauranteEntity(22, 5, "The Ledbury", "Ledbury Rd 127", "Mediterránea", 4.8, "18:00-22:30", "120€+"),
            RestauranteEntity(23, 5, "Flat Iron", "Beak St 17", "Americana", 4.5, "12:00-22:00", "15-25€"),
            RestauranteEntity(24, 5, "Sketch", "Conduit St 9", "Mediterránea", 4.4, "12:00-23:00", "50-80€"),
            RestauranteEntity(25, 5, "Borough Market", "Southwark St 8", "Internacional", 4.6, "10:00-18:00", "10-20€"),

            // ================= BARCELONA (destinoId = 6) =================
            RestauranteEntity(26, 6, "Tickets", "Avinguda del Paral·lel 164", "Mediterránea", 4.7, "13:00-23:00", "70-120€"),
            RestauranteEntity(27, 6, "Can Culleretes", "Carrer d'en Quintana 5", "Mediterránea", 4.5, "13:00-22:00", "20-35€"),
            RestauranteEntity(28, 6, "La Paradeta", "Carrer Comercial 7", "Mediterránea", 4.4, "12:00-23:00", "20-40€"),
            RestauranteEntity(29, 6, "Disfrutar", "Carrer de Villarroel 163", "Mediterránea", 4.9, "13:00-22:00", "150€+"),
            RestauranteEntity(30, 6, "El Xampanyet", "Carrer de Montcada 22", "Mediterránea", 4.6, "12:00-22:00", "10-20€"),

            // ================= BERLÍN (destinoId = 7) =================
            RestauranteEntity(31, 7, "Mustafa's Gemüse Kebap", "Mehringdamm 32", "Mediterránea", 4.5, "10:00-02:00", "5-10€"),
            RestauranteEntity(32, 7, "Curry 36", "Mehringdamm 36", "Mediterránea", 4.4, "09:00-03:00", "5-10€"),
            RestauranteEntity(33, 7, "Tim Raue", "Rudi-Dutschke-Strasse 26", "Asiática", 4.8, "18:00-23:00", "120€+"),
            RestauranteEntity(34, 7, "Zur Letzten Instanz", "Waisenstrasse 14", "Mediterránea", 4.3, "12:00-22:00", "20-35€"),
            RestauranteEntity(35, 7, "Burgermeister", "Schlesische Strasse 18", "Americana", 4.6, "11:00-03:00", "10-15€"),

            // ================= ÁMSTERDAM (destinoId = 8) =================
            RestauranteEntity(36, 8, "The Pancake Bakery", "Prinsengracht 191", "Mediterránea", 4.5, "09:00-22:00", "15-25€"),
            RestauranteEntity(37, 8, "Foodhallen", "Bellamyplein 51", "Internacional", 4.6, "11:00-23:00", "10-20€"),
            RestauranteEntity(38, 8, "Ciel Bleu", "Ferdinand Bolstraat 333", "Mediterránea", 4.8, "18:30-22:30", "150€+"),
            RestauranteEntity(39, 8, "Moeders", "Rozengracht 251", "Mediterránea", 4.4, "17:00-22:00", "20-35€"),
            RestauranteEntity(40, 8, "De Kas", "Kamerlingh Onneslaan 3", "Mediterránea", 4.7, "12:00-22:00", "60-90€"),

            // ================= LISBOA (destinoId = 9) =================
            RestauranteEntity(41, 9, "Time Out Market", "Av. 24 de Julho 49", "Internacional", 4.6, "10:00-00:00", "10-25€"),
            RestauranteEntity(42, 9, "Ramiro", "Av. Almirante Reis 1", "Mediterránea", 4.7, "12:00-00:00", "25-40€"),
            RestauranteEntity(43, 9, "Cervejaria Trindade", "Rua Nova da Trindade 20", "Mediterránea", 4.5, "12:00-23:00", "15-30€"),
            RestauranteEntity(44, 9, "Belcanto", "Rua Serpa Pinto 10", "Mediterránea", 4.9, "19:00-23:00", "150€+"),
            RestauranteEntity(45, 9, "Solar dos Presuntos", "Rua Portas de Santo Antão 150", "Mediterránea", 4.6, "12:00-22:30", "30-50€"),

            // ================= PRAGA (destinoId = 10) =================
            RestauranteEntity(46, 10, "Lokál", "Dlouhá 33", "Mediterránea", 4.6, "11:00-23:00", "10-20€"),
            RestauranteEntity(47, 10, "U Modré Kachničky", "Nebovidská 6", "Mediterránea", 4.7, "17:00-23:00", "25-45€"),
            RestauranteEntity(48, 10, "Café Louvre", "Národní 22", "Mediterránea", 4.5, "08:00-22:00", "15-30€"),
            RestauranteEntity(49, 10, "Field", "U Milosrdných 12", "Mediterránea", 4.8, "18:00-23:00", "120€+"),
            RestauranteEntity(50, 10, "Kolkovna", "V Kolkovně 8", "Mediterránea", 4.4, "11:00-22:00", "15-25€")
        )
    )
}



/* ===========================
   TRANSPORTES (50)
   =========================== */
private suspend fun insertarTransportes(db: AppDatabase) {
    db.transporteDao().insertAll(
        listOf(
            // ================= PARÍS (destinoId = 1) =================
            TransporteEntity(1, 1, "Metro", "Línea 1 – La Défense / Vincennes", "05:30-00:30", 1.90),
            TransporteEntity(2, 1, "Metro", "Línea 4 – Porte de Clignancourt / Bagneux", "05:30-00:30", 1.90),
            TransporteEntity(3, 1, "Bus", "Bus 69 – Champ de Mars / Gambetta", "06:00-22:30", 2.00),
            TransporteEntity(4, 1, "Tranvía", "T3a – Pont du Garigliano", "05:00-00:00", 1.90),
            TransporteEntity(5, 1, "Bici", "Vélib – Estación Torre Eiffel", "24h", 3.00),

            // ================= ROMA (destinoId = 2) =================
            TransporteEntity(6, 2, "Metro", "Línea A – Battistini / Anagnina", "05:30-23:30", 1.50),
            TransporteEntity(7, 2, "Metro", "Línea B – Rebibbia / Laurentina", "05:30-23:30", 1.50),
            TransporteEntity(8, 2, "Bus", "Bus 64 – Termini / Vaticano", "06:00-22:00", 1.50),
            TransporteEntity(9, 2, "Tranvía", "Tranvía 8 – Casaletto / Argentina", "05:30-23:30", 1.50),
            TransporteEntity(10, 2, "Taxi", "Taxi oficial Roma", "24h", 5.00),

            // ================= TOKIO (destinoId = 3) =================
            TransporteEntity(11, 3, "Metro", "JR Yamanote – Línea circular", "05:00-01:00", 1.70),
            TransporteEntity(12, 3, "Metro", "Ginza Line – Asakusa / Shibuya", "05:30-23:30", 1.70),
            TransporteEntity(13, 3, "Bus", "Toei Bus – Red urbana", "06:00-22:00", 1.60),
            TransporteEntity(14, 3, "Tren", "Shinkansen – Tokyo Station", "06:00-23:00", 20.00),
            TransporteEntity(15, 3, "Taxi", "Taxi urbano Tokio", "24h", 6.00),

            // ================= NUEVA YORK (destinoId = 4) =================
            TransporteEntity(16, 4, "Metro", "Línea A – Inwood / Far Rockaway", "24h", 2.75),
            TransporteEntity(17, 4, "Metro", "Línea C – Manhattan / Brooklyn", "24h", 2.75),
            TransporteEntity(18, 4, "Bus", "M15 – East Harlem / South Ferry", "24h", 2.75),
            TransporteEntity(19, 4, "Ferry", "Staten Island Ferry", "24h", 0.00),
            TransporteEntity(20, 4, "Taxi", "Yellow Cab NYC", "24h", 5.00),

            // ================= LONDRES (destinoId = 5) =================
            TransporteEntity(21, 5, "Metro", "Central Line – West Ruislip / Epping", "05:00-00:30", 2.80),
            TransporteEntity(22, 5, "Metro", "Piccadilly Line – Heathrow / Cockfosters", "05:00-00:30", 2.80),
            TransporteEntity(23, 5, "Bus", "Bus 24 – Hampstead Heath / Pimlico", "24h", 1.75),
            TransporteEntity(24, 5, "Tren", "London Overground", "05:00-00:30", 3.00),
            TransporteEntity(25, 5, "Taxi", "Black Cab Londres", "24h", 5.50),

            // ================= BARCELONA (destinoId = 6) =================
            TransporteEntity(26, 6, "Metro", "Línea L3 – Zona Universitària / Trinitat Nova", "05:00-00:00", 2.40),
            TransporteEntity(27, 6, "Metro", "Línea L5 – Cornellà / Vall d'Hebron", "05:00-00:00", 2.40),
            TransporteEntity(28, 6, "Bus", "Bus H16 – Diagonal Mar / Fòrum", "06:00-23:00", 2.40),
            TransporteEntity(29, 6, "Tranvía", "Trambaix – T1/T2/T3", "05:00-00:00", 2.40),
            TransporteEntity(30, 6, "Bici", "Bicing – Estación Plaça Catalunya", "24h", 2.00),

            // ================= BERLÍN (destinoId = 7) =================
            TransporteEntity(31, 7, "Metro", "U-Bahn U2 – Pankow / Ruhleben", "04:30-01:00", 2.90),
            TransporteEntity(32, 7, "Metro", "S-Bahn S7 – Potsdam / Ahrensfelde", "04:30-01:00", 2.90),
            TransporteEntity(33, 7, "Bus", "Bus 100 – Alexanderplatz / Zoo", "24h", 2.90),
            TransporteEntity(34, 7, "Tranvía", "Tranvía M10 – Warschauer Str.", "24h", 2.90),
            TransporteEntity(35, 7, "Bici", "Nextbike – Estación Mitte", "24h", 3.00),

            // ================= ÁMSTERDAM (destinoId = 8) =================
            TransporteEntity(36, 8, "Metro", "Línea 52 – Noord / Zuid", "05:30-00:30", 3.20),
            TransporteEntity(37, 8, "Tranvía", "Tranvía 2 – Centraal Station", "05:30-00:30", 3.20),
            TransporteEntity(38, 8, "Bus", "Bus 21 – Centraal / Geuzenveld", "06:00-23:00", 3.20),
            TransporteEntity(39, 8, "Ferry", "Ferry IJ – Centraal / Noord", "24h", 0.00),
            TransporteEntity(40, 8, "Bici", "OV-fiets – Estación Central", "24h", 4.00),

            // ================= LISBOA (destinoId = 9) =================
            TransporteEntity(41, 9, "Metro", "Línea Azul – Reboleira / Santa Apolónia", "06:30-01:00", 1.50),
            TransporteEntity(42, 9, "Tranvía", "Tranvía 28 – Graça / Campo Ourique", "06:00-23:00", 3.00),
            TransporteEntity(43, 9, "Bus", "Carris – Red urbana", "06:00-22:00", 2.00),
            TransporteEntity(44, 9, "Tren", "Linha de Cascais", "06:00-00:00", 2.25),
            TransporteEntity(45, 9, "Taxi", "Taxi urbano Lisboa", "24h", 5.00),

            // ================= PRAGA (destinoId = 10) =================
            TransporteEntity(46, 10, "Metro", "Línea A – Nemocnice Motol / Depo Hostivař", "05:00-00:00", 1.20),
            TransporteEntity(47, 10, "Metro", "Línea B – Zličín / Černý Most", "05:00-00:00", 1.20),
            TransporteEntity(48, 10, "Tranvía", "Tranvía 22 – Malá Strana", "24h", 1.20),
            TransporteEntity(49, 10, "Bus", "Bus 119 – Aeropuerto / Nádraží Veleslavín", "05:00-23:00", 1.20),
            TransporteEntity(50, 10, "Taxi", "Taxi oficial Praga", "24h", 4.50)
        )
    )
}


