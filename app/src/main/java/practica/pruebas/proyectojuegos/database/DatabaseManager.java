package practica.pruebas.proyectojuegos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseManager";
    private static final String DATABASE_NAME = "puntuaciones.db";
    private static final int DATABASE_VERSION = 1;

    // Definición de la tabla y columnas
    public static final String TABLE_PUNTUACIONES = "puntuaciones";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JUGADOR = "jugador";
    public static final String COLUMN_PUNTOS = "puntos";
    public static final String COLUMN_FECHA = "fecha";

    // Instancia Singleton para evitar múltiples conexiones a la BD
    private static DatabaseManager instance;

    // Metodo para obtener la instancia única del DatabaseManager
    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    // Constructor privado para implementar el patrón Singleton
    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Se ejecuta cuando se crea la base de datos por primera vez.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PUNTUACIONES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_JUGADOR + " TEXT NOT NULL, "
                + COLUMN_PUNTOS + " INTEGER NOT NULL, "
                + COLUMN_FECHA + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";
        db.execSQL(createTable);
        Log.d(TAG, "Base de datos creada con la tabla: " + TABLE_PUNTUACIONES);
    }

    // Se ejecuta cuando se detecta un cambio en la versión de la base de datos.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Para desarrollo, se elimina la tabla y se recrea.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUNTUACIONES);
        onCreate(db);
        Log.d(TAG, "Base de datos actualizada de versión " + oldVersion + " a " + newVersion);
    }

    // Ejemplo de Metodo para insertar una puntuación
    public long insertarPuntuacion(String jugador, int puntos) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_JUGADOR, jugador);
        values.put(COLUMN_PUNTOS, puntos);
        long id = db.insert(TABLE_PUNTUACIONES, null, values);
        Log.d(TAG, "Insertado registro con ID: " + id);
        return id;
    }

    // Ejemplo de Metodo para obtener un Cursor con los jugadores ordenados por puntos en forma descendente
    public Cursor obtenerJugadores() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PUNTUACIONES + " ORDER BY " + COLUMN_PUNTOS + " DESC";
        return db.rawQuery(query, null);
    }
}
