package practica.pruebas.proyectojuegos;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import practica.pruebas.proyectojuegos.database.DatabaseManager;

public class RankingActivity extends AppCompatActivity {

    private RecyclerView rankingRecyclerView;
    private DatabaseManager dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        rankingRecyclerView = findViewById(R.id.rankingRecyclerView);
        rankingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = DatabaseManager.getInstance(this);
        //dbHelper.insertarJugador("JugadorPrueba", 1);
        mostrarRanking();
    }

    private void mostrarRanking() {
        List<RankingItem> rankingLista = new ArrayList<>();
        Cursor cursor = dbHelper.obtenerJugadores();
        int posicion = 1;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int colNombre = cursor.getColumnIndex("jugador");
                int colPuntuacion = cursor.getColumnIndex("puntos");


                // Verifica si las columnas existen antes de acceder a ellas
                if (colNombre != -1 && colPuntuacion != -1) {
                    String nombre = cursor.getString(colNombre);
                    int puntuacion = cursor.getInt(colPuntuacion);
                    rankingLista.add(new RankingItem(posicion++, nombre, puntuacion));
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        RankingAdapter adapter = new RankingAdapter(rankingLista);
        rankingRecyclerView.setAdapter(adapter);
    }
}
