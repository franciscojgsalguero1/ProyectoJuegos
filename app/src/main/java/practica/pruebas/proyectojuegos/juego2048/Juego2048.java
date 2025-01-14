package practica.pruebas.proyectojuegos.juego2048;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import practica.pruebas.proyectojuegos.R;

public class Juego2048 extends AppCompatActivity {

    private int[][] grid = new int[4][4];
    private GridLayout gridLayout;
    private TextView scoreTextView;
    private int score = 0;
    private int highScore = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.juego2048);
            // Placeholder for Game 1 logic

            gridLayout = findViewById(R.id.grid_layout);
            scoreTextView = findViewById(R.id.score_text_view);
            Button backToMenuButton = findViewById(R.id.btn_back_to_menu);

            SharedPreferences prefs = getSharedPreferences("Game2048", MODE_PRIVATE);
            highScore = prefs.getInt("highScore", 0);

            backToMenuButton.setOnClickListener(v -> {
                finish();
            });

            initializeGame();
        }

    private void initializeGame() {
        addRandomNumber();
        addRandomNumber();
        updateUI();
    }

    private void addRandomNumber() {
        // Implementación de añadir número aleatorio...
    }

    private void updateUI() {
        if (score > highScore) {
            highScore = score;
        }
        scoreTextView.setText("Score: " + score + " | High Score: " + highScore);
        gridLayout.removeAllViews();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                TextView cell = new TextView(this);
                cell.setText(grid[i][j] == 0 ? "" : String.valueOf(grid[i][j]));
                cell.setTextSize(24);
                cell.setGravity(View.TEXT_ALIGNMENT_CENTER);
                cell.setBackgroundColor(getCellColor(grid[i][j]));
                cell.setTextColor(getResources().getColor(android.R.color.white));
                cell.setScaleX(0.8f);
                cell.setScaleY(0.8f);
                cell.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = gridLayout.getWidth() / 4;
                params.height = gridLayout.getHeight() / 4;
                cell.setLayoutParams(params);
                gridLayout.addView(cell);
            }
        }
    }

    private int getCellColor(int value) {
        switch (value) {
            case 2: return getResources().getColor(R.color.color2);
            case 4: return getResources().getColor(R.color.color4);
            case 8: return getResources().getColor(R.color.color8);
            case 16: return getResources().getColor(R.color.color16);
            case 32: return getResources().getColor(R.color.color32);
            case 64: return getResources().getColor(R.color.color64);
            case 128: return getResources().getColor(R.color.color128);
            case 256: return getResources().getColor(R.color.color256);
            case 512: return getResources().getColor(R.color.color512);
            case 1024: return getResources().getColor(R.color.color1024);
            case 2048: return getResources().getColor(R.color.color2048);
            default: return getResources().getColor(android.R.color.darker_gray);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("Game2048", MODE_PRIVATE).edit();
        editor.putInt("highScore", highScore);
        editor.apply();

    }
}
