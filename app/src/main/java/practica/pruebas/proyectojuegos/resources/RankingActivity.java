package practica.pruebas.proyectojuegos.resources;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import practica.pruebas.proyectojuegos.R;
import practica.pruebas.proyectojuegos.database.DatabaseManager;

public class RankingActivity extends AppCompatActivity {

    private TableLayout tableRanking;
    private Button btnDeleteRanking;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle("Ranking");

        // Obtener la instancia de DatabaseManager
        dbManager = DatabaseManager.getInstance(this);

        // Obtener referencias del layout
        tableRanking = findViewById(R.id.tableRanking);
        Button backToMenuButton = findViewById(R.id.btn_back_to_menu);
        backToMenuButton.setOnClickListener(v -> finish());
        btnDeleteRanking = findViewById(R.id.btnEliminarRanking);
        btnDeleteRanking.setOnClickListener(v -> {
            int filasEliminadas = dbManager.vaciarRanking();
            Toast.makeText(this, "Ranking eliminado. " + filasEliminadas + " registros borrados", Toast.LENGTH_SHORT).show();
            cargarRanking();
        });

        // Cargar el ranking
        cargarRanking();
    }

    private void cargarRanking() {
        Cursor cursor = dbManager.obtenerJugadores(false, true);
        if (cursor != null && cursor.moveToFirst()) {
            // Eliminar filas anteriores de la tabla, excepto la cabecera
            int childCount = tableRanking.getChildCount();
            if(childCount > 1) {
                tableRanking.removeViews(1, childCount - 1);
            }

            int posicion = 1;
            do {
                if (posicion > 10) break; // Solo mostramos las 10 mejores puntuaciones

                String nombreJugador = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_JUGADOR));
                int puntos = cursor.getInt(cursor.getColumnIndex(DatabaseManager.COLUMN_PUNTOS));
                long tiempoMs = cursor.getLong(cursor.getColumnIndex(DatabaseManager.COLUMN_TIEMPO));

                // Convertir milisegundos a minutos y segundos
                int totalSeconds = (int) (tiempoMs / 1000);
                int minutes = totalSeconds / 60;
                int seconds = totalSeconds % 60;
                String tiempoFormateado = String.format("%02d:%02d", minutes, seconds);

                // Crear una nueva fila para el ranking
                TableRow row = new TableRow(this);
                row.setPadding(16, 16, 16, 16);
                //row.setBackgroundResource(R.drawable.table_row_bg); // Opcional: para un fondo bonito

                // LayoutParams para los TextViews
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                params.setMargins(50, 8, 50, 8);

                // TextView para el nombre y posición
                TextView tvNombre = new TextView(this);
                tvNombre.setText(posicion + ". " + nombreJugador);
                tvNombre.setTextSize(18);
                tvNombre.setTextColor(getResources().getColor(android.R.color.black));
                tvNombre.setLayoutParams(params);
                tvNombre.setGravity(Gravity.CENTER_VERTICAL);

                // TextView para la puntuación
                TextView tvPuntos = new TextView(this);
                tvPuntos.setText(String.valueOf(puntos));
                tvPuntos.setTextSize(18);
                tvPuntos.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                tvPuntos.setLayoutParams(params);
                tvPuntos.setGravity(Gravity.CENTER_VERTICAL);

                // TextView para el tiempo jugado
                TextView tvTiempo = new TextView(this);
                tvTiempo.setText(tiempoFormateado);
                tvTiempo.setTextSize(18);
                tvTiempo.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                tvTiempo.setLayoutParams(params);
                tvTiempo.setGravity(Gravity.CENTER_VERTICAL);

                // Añadimos los TextViews a la fila
                row.addView(tvNombre);
                row.addView(tvPuntos);
                row.addView(tvTiempo);

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
