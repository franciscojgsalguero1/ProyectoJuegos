package practica.pruebas.proyectojuegos;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import practica.pruebas.proyectojuegos.database.DatabaseManager;

public class RankingActivity extends AppCompatActivity {

    private RecyclerView rankingRecyclerView;
    private RankingAdapter rankingAdapter;
    private List<RankingItem> rankingList;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // Inicializar RecyclerView y su LayoutManager
        rankingRecyclerView = findViewById(R.id.rankingRecyclerView);
        rankingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista de ranking
        rankingList = new ArrayList<>();

        // Obtener la instancia del DatabaseManager
        dbManager = DatabaseManager.getInstance(this);

        // Cargar datos del ranking desde la base de datos
        cargarRanking();
    }

    /**
     * Carga los datos del ranking desde la base de datos y configura el adapter del RecyclerView.
     */
    private void cargarRanking() {
        Cursor cursor = dbManager.obtenerJugadores();
        if (cursor != null && cursor.moveToFirst()) {
            rankingList.clear();
            int posicion = 1;
            do {
                // Usamos las constantes definidas en DatabaseManager para obtener los índices de columna
                String jugador = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_JUGADOR));
                int puntos = cursor.getInt(cursor.getColumnIndex(DatabaseManager.COLUMN_PUNTOS));
                rankingList.add(new RankingItem(posicion, jugador, puntos));
                posicion++;
            } while (cursor.moveToNext());
            cursor.close();

            // Configuramos el adapter con la lista de ranking y lo asignamos al RecyclerView
            rankingAdapter = new RankingAdapter(rankingList);
            rankingRecyclerView.setAdapter(rankingAdapter);
        } else {
            Toast.makeText(this, "No hay datos de ranking", Toast.LENGTH_SHORT).show();
        }
    }
}