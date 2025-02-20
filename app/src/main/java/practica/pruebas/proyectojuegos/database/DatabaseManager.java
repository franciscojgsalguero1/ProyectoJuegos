package practica.pruebas.proyectojuegos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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

    // Instancia Singleton
    private static DatabaseManager instance;

    /**
     * Obtiene la instancia única de DatabaseManager.
     * Se usa el contexto de la aplicación para evitar fugas.
     *
     * @param context Contexto de la aplicación.
     * @return Instancia única de DatabaseManager.
     */
    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    // Constructor privado para el patrón Singleton
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Se ejecuta cuando la base de datos se crea por primera vez.
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
        // En una aplicación real, aquí deberías implementar una migración de datos.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUNTUACIONES);
        onCreate(db);
        Log.d(TAG, "Base de datos actualizada de versión " + oldVersion + " a " + newVersion);
    }

    /**
     * Inserta una nueva puntuación en la tabla.
     *
     * @param jugador Nombre del jugador.
     * @param puntos  Puntuación obtenida.
     * @return El ID del registro insertado o -1 si ocurrió un error.
     */
    public long insertarPuntuacion(String jugador, int puntos) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_JUGADOR, jugador);
        values.put(COLUMN_PUNTOS, puntos);
        long id = db.insert(TABLE_PUNTUACIONES, null, values);
        Log.d(TAG, "Insertado registro con ID: " + id);
        return id;
    }

    /**
     * Actualiza una puntuación en la tabla.
     *
     * @param id      ID del registro a actualizar.
     * @param jugador Nuevo nombre del jugador.
     * @param puntos  Nueva puntuación.
     * @return El número de filas actualizadas.
     */
    public int actualizarPuntuacion(int id, String jugador, int puntos) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_JUGADOR, jugador);
        values.put(COLUMN_PUNTOS, puntos);
        int filasActualizadas = db.update(TABLE_PUNTUACIONES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "Filas actualizadas: " + filasActualizadas);
        return filasActualizadas;
    }

    /**
     * Elimina una puntuación de la tabla.
     *
     * @param id ID del registro a eliminar.
     * @return El número de filas eliminadas.
     */
    public int eliminarPuntuacion(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasEliminadas = db.delete(TABLE_PUNTUACIONES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "Filas eliminadas: " + filasEliminadas);
        return filasEliminadas;
    }

    /**
     * Obtiene un cursor con todos los jugadores ordenados por puntos en orden descendente.
     *
     * @return Cursor que contiene los registros de la tabla de puntuaciones.
     */
    public Cursor obtenerJugadores() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PUNTUACIONES + " ORDER BY " + COLUMN_PUNTOS + " DESC";
        return db.rawQuery(query, null);
    }

    /**
     * Prueba la conexión a la base de datos.
     *
     * @return true si la conexión es exitosa, false en caso contrario.
     */
    public boolean probarConexion() {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            if (db != null) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return false;
    }
}
