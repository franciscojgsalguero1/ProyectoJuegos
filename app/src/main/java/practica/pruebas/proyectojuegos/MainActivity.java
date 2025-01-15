package practica.pruebas.proyectojuegos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import practica.pruebas.proyectojuegos.LaEscoba.JuegoLaEscoba;
import practica.pruebas.proyectojuegos.juego2048.Juego2048;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        View text = findViewById(R.id.menuTitle);
        text.setAlpha(0);
        text.animate().alpha(1).setDuration(2000);

        Button startGame2048Button = findViewById(R.id.btn_start_game_2048);
        Button startGameLaEscobaButton = findViewById(R.id.btn_start_game_la_escoba);

        startGame2048Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Juego2048.class);
                startActivity(intent);
            }
        });

        startGameLaEscobaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JuegoLaEscoba.class);
                startActivity(intent);
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menuTitle), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}