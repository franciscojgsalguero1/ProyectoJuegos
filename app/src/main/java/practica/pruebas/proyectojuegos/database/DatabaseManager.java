package practica.pruebas.proyectojuegos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Chronometer;

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
    public static final String COLUMN_TIEMPO = "tiempo";

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
                + COLUMN_TIEMPO + " INTEGER NOT NULL, "
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

    // Ejemplo de Metodo para obtener un Cursor con los jugadores ordenados por puntos en forma descendente
    public Cursor obtenerJugadores(boolean orden, boolean maximo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String ordenacion = COLUMN_PUNTOS + " DESC";
        if (orden) {
            ordenacion = COLUMN_JUGADOR + " ASC";
        }
        if (maximo) {
            ordenacion += " LIMIT 10";
        }
        String query = "SELECT * FROM " + TABLE_PUNTUACIONES + " ORDER BY " + ordenacion ;
        return db.rawQuery(query, null);
    }

    public void insertarPuntuacion(String jugador, int puntos, Long tiempo) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Opcional: consultar si ya existe un registro para este jugador.
        Cursor cursor = db.query(TABLE_PUNTUACIONES,
                new String[]{COLUMN_ID, COLUMN_PUNTOS},
                COLUMN_JUGADOR + " = ?",
                new String[]{jugador},
                null, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_JUGADOR, jugador);
        values.put(COLUMN_PUNTOS, puntos);
        values.put(COLUMN_TIEMPO, tiempo);

        // Inserta el registro (o bien podrías actualizar si ya existe, según tu lógica)
        long idInsertado = db.insert(TABLE_PUNTUACIONES, null, values);
        Log.d(TAG, "Se insertó el registro con ID: " + idInsertado + " para el jugador: " + jugador);

        if (cursor != null) {
            cursor.close();
        }
    }


    public int vaciarRanking() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Elimina todos los registros de la tabla sin condiciones
        int filasEliminadas = db.delete(TABLE_PUNTUACIONES, null, null);
        Log.d(TAG, "Se han eliminado " + filasEliminadas + " registros del ranking.");
        return filasEliminadas;
    }

}
