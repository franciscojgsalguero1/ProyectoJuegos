package practica.pruebas.proyectojuegos.resources;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import practica.pruebas.proyectojuegos.R;
import practica.pruebas.proyectojuegos.database.DatabaseManager;

public class RankingActivity extends AppCompatActivity {

    private TableLayout tableRanking;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle("Ranking");

        // Obtenemos la instancia de DatabaseManager
        dbManager = DatabaseManager.getInstance(this);

        // Obtenemos el TableLayout del layout activity_ranking.xml
        tableRanking = findViewById(R.id.tableRanking);

        // Cargamos los datos del ranking
        cargarRanking();
    }

    private void cargarRanking() {
        Cursor cursor = dbManager.obtenerJugadores();
        if (cursor != null && cursor.moveToFirst()) {
            // Si ya hay filas en la tabla (por ejemplo, de una carga anterior), las eliminamos
            tableRanking.removeViews(1, tableRanking.getChildCount() - 1); // conservamos la fila de cabecera

            int posicion = 1;
            do {
                String nombreJugador = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_JUGADOR));
                int puntos = cursor.getInt(cursor.getColumnIndex(DatabaseManager.COLUMN_PUNTOS));

                // Creamos una nueva fila para el ranking
                TableRow row = new TableRow(this);
                row.setPadding(8, 8, 8, 8);

                // Creamos TextViews para el nombre y la puntuación
                TextView tvNombre = new TextView(this);
                tvNombre.setText(posicion + ". " + nombreJugador);
                tvNombre.setPadding(8, 8, 8, 8);

                TextView tvPuntos = new TextView(this);
                tvPuntos.setText(String.valueOf(puntos));
                tvPuntos.setPadding(8, 8, 8, 8);

                // Añadimos los TextViews a la fila
                row.addView(tvNombre);
                row.addView(tvPuntos);

                // Añadimos la fila al TableLayout
                tableRanking.addView(row);

                posicion++;
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "No hay datos de ranking", Toast.LENGTH_SHORT).show();
        }
    }
}
