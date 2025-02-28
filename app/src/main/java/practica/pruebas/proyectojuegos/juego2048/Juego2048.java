package practica.pruebas.proyectojuegos.juego2048;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.widget.Chronometer;

import practica.pruebas.proyectojuegos.database.DatabaseManager;
import practica.pruebas.proyectojuegos.resources.OnSwipeTouchListener;


import practica.pruebas.proyectojuegos.R;
public class Juego2048 extends AppCompatActivity {

    private DatabaseManager dbManager; // Referencia al DatabaseManager
    private GridLayout gridLayout;
    private Ficha[][] fichas;
    private Ficha[][] turnoAnterior;
    private static int GRID_SIZE = 4;
    private int score;
    private TextView scoreLabel;
    private String playerName;
    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializamos dbManager
        dbManager = DatabaseManager.getInstance(this);

        preguntarTamanoTablero(); // Preguntar el tamaño del tablero
        AskPlayerName();

        setContentView(R.layout.activity_juego2048);
        gridLayout = findViewById(R.id.gridLayout);
        scoreLabel = findViewById(R.id.scoreLabel);
        Button backToMenuButton = findViewById(R.id.btn_back_to_menu);
        Button turnComeback = findViewById(R.id.btn_turn_comeback);
        chronometer = findViewById(R.id.chronometer);
        // Establece la base del cronómetro al tiempo actual
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        backToMenuButton.setOnClickListener(v -> {
            finish();
        });

        turnComeback.setOnClickListener(v -> {
            if (turnoAnterior != null) {
                for (int i = 0; i < GRID_SIZE; i++) {
                    for (int j = 0; j < GRID_SIZE; j++) {
                        fichas[i][j].setValor(turnoAnterior[i][j].getValor());
                        updateFichaView(fichas[i][j]);
                    }
                }
                Toast.makeText(this, "Turno restaurado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No hay un turno anterior guardado.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void preguntarTamanoTablero() {
        // Crear un EditText para que el usuario introduzca el tamaño
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER); // Solo permite números
        input.setHint("Por ejemplo: 4");

        new AlertDialog.Builder(this)
                .setTitle("Tamaño del tablero")
                .setMessage("Introduce un número para el tamaño del tablero (mínimo 3 y máximo 8):")
                .setView(input)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    try {
                        // Intentar convertir la entrada a un número
                        int tamano = Integer.parseInt(input.getText().toString());

                        // Validar que el tamaño esté en el rango permitido
                        if (tamano >= 3 && tamano <= 8) {
                            GRID_SIZE = tamano; // Establecer el tamaño del tablero

                            iniciarJuego(); // Llamar al metodo para inicializar el tablero
                        } else {
                            Toast.makeText(this, "El tamaño debe estar entre 3 y 8.", Toast.LENGTH_SHORT).show();
                            preguntarTamanoTablero(); // Volver a preguntar
                        }
                    } catch (NumberFormatException e) {
                        // Manejar errores si el usuario no introduce un número válido
                        Toast.makeText(this, "Introduce un número válido.", Toast.LENGTH_SHORT).show();
                        preguntarTamanoTablero(); // Volver a preguntar
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    Toast.makeText(this, "Debe seleccionarse un tamaño para el tablero.", Toast.LENGTH_SHORT).show();
                    preguntarTamanoTablero(); // Volver a preguntar si cancela
                })
                .setCancelable(false) // Evitar que el usuario cierre el diálogo sin elegir
                .show();
    }

    private void iniciarJuego() {
        gridLayout.removeAllViews(); // Limpiar el tablero si ya existe
        gridLayout.setColumnCount(GRID_SIZE);
        gridLayout.setRowCount(GRID_SIZE);

        fichas = new Ficha[GRID_SIZE][GRID_SIZE];
        initializeGameBoard();

        // añadimor 2 fichas random
        for (int i = 0; i < 2; i++) {
            addRandomFicha();
        }
        configurarGestos(); // Configurar el listener de gestos
    }

    private void initializeGameBoard() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Ficha ficha = new Ficha(0, i, j);
                fichas[i][j] = ficha;
                addFichaToGrid(ficha);
            }
        }
    }

    private void addFichaToGrid(Ficha ficha) {

        TextView tile = new TextView(this);
        tile.setGravity(Gravity.CENTER);
        tile.setTextSize(24);
        tile.setBackgroundColor(ContextCompat.getColor(this, R.color.tile_empty));
        tile.setText(ficha.getValor() > 0 ? String.valueOf(ficha.getValor()) : "");

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = 0;
        params.columnSpec = GridLayout.spec(ficha.getColumna(), 1, 1.0f);
        params.rowSpec = GridLayout.spec(ficha.getFila(), 1, 1.0f);
        params.setMargins(8, 8, 8, 8);

        gridLayout.addView(tile, params);

    }

    private void updateFichaView(Ficha ficha) {
        TextView tile = (TextView) gridLayout.getChildAt(ficha.getFila() * GRID_SIZE + ficha.getColumna());
        if (ficha.getValor() == 0) {
            tile.setBackgroundColor(ContextCompat.getColor(this, R.color.tile_empty));
            tile.setText("");
        } else {
            tile.setBackgroundColor(ContextCompat.getColor(this, ficha.getColor()));
            tile.setText(String.valueOf(ficha.getValor()));
        }
    }

    private void addRandomFicha() {
        List<Ficha> emptySlots = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (fichas[i][j].getValor() == 0) {
                    //fichas[i][j].setValor(1024); para comprobar la victoria
                    emptySlots.add(fichas[i][j]);
                }
            }
        }

        if (!emptySlots.isEmpty()) {
            Ficha ficha = emptySlots.get(new Random().nextInt(emptySlots.size()));
            ficha.setValor(new Random().nextInt(10) < 9 ? 2 : 4);
            updateFichaView(ficha);
        }
    }

    private void updateScore(int points) {
        this.score += points;
        scoreLabel.setText("Score: " + score);
    }

    private void configurarGestos() {
        gridLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeUp() {
                moveUp(); // Mover las fichas hacia arriba
                verificarEstadoJuego();
            }

            @Override
            public void onSwipeDown() {
                moveDown(); // Mover las fichas hacia abajo
                verificarEstadoJuego();
            }

            @Override
            public void onSwipeLeft() {
                moveLeft(); // Mover las fichas hacia la izquierda
                verificarEstadoJuego();
            }

            @Override
            public void onSwipeRight() {
                moveRight(); // Mover las fichas hacia la derecha
                verificarEstadoJuego();
            }
        });
    }

    public void moveUp() {
        guardarEstadoAnterior(); // Guardar el turno antes de mover

        boolean moved = false;
        for (int col = 0; col < GRID_SIZE; col++) {
            for (int row = 1; row < GRID_SIZE; row++) {
                if (fichas[row][col].getValor() != 0) {
                    int targetRow = row;
                    for (int nextRow = row - 1; nextRow >= 0; nextRow--) {
                        if (fichas[nextRow][col].getValor() == 0) {
                            targetRow = nextRow;
                        } else if (fichas[nextRow][col].getValor() == fichas[row][col].getValor() && !fichas[nextRow][col].isMerged()) {
                            targetRow = nextRow;
                            updateScore(fichas[row][col].getValor() * 2);
                            break;
                        } else {
                            break;
                        }
                    }

                    if (targetRow != row) {
                        if (fichas[targetRow][col].getValor() == fichas[row][col].getValor() && !fichas[targetRow][col].isMerged()) {
                            fichas[targetRow][col].fusionar(fichas[row][col]);
                        } else {
                            fichas[targetRow][col].setValor(fichas[row][col].getValor());
                            fichas[row][col].setValor(0);
                        }

                        updateFichaView(fichas[targetRow][col]);
                        updateFichaView(fichas[row][col]);
                        moved = true;
                    }
                }
            }
        }

        resetMergedStatus();
        if (moved) {
            addRandomFicha();
        }
    }


    private void moveDown() {
        guardarEstadoAnterior(); // Guardar el turno antes de mover
        boolean moved = false;
        for (int col = 0; col < GRID_SIZE; col++) {
            for (int row = GRID_SIZE - 2; row >= 0; row--) {
                if (fichas[row][col].getValor() != 0) {
                    int targetRow = row;
                    for (int nextRow = row + 1; nextRow < GRID_SIZE; nextRow++) {
                        if (fichas[nextRow][col].getValor() == 0) {
                            targetRow = nextRow;
                        } else if (fichas[nextRow][col].getValor() == fichas[row][col].getValor() && !fichas[nextRow][col].isMerged()) {
                            targetRow = nextRow;
                            updateScore(fichas[row][col].getValor() * 2);
                            break;
                        } else {
                            break;
                        }
                    }
                    if (targetRow != row) {
                        if (fichas[targetRow][col].getValor() == fichas[row][col].getValor() && !fichas[targetRow][col].isMerged()) {
                            fichas[targetRow][col].fusionar(fichas[row][col]);
                        } else {
                            fichas[targetRow][col].setValor(fichas[row][col].getValor());
                            fichas[row][col].setValor(0);
                        }
                        updateFichaView(fichas[targetRow][col]);
                        updateFichaView(fichas[row][col]);
                        moved = true;
                    }
                }
            }
        }
        resetMergedStatus();
        if (moved) {
            addRandomFicha();
        }
    }

    private void moveLeft() {
        guardarEstadoAnterior(); // Guardar el turno antes de mover
        boolean moved = false;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 1; col < GRID_SIZE; col++) {
                if (fichas[row][col].getValor() != 0) {
                    int targetCol = col;
                    for (int nextCol = col - 1; nextCol >= 0; nextCol--) {
                        if (fichas[row][nextCol].getValor() == 0) {
                            targetCol = nextCol;
                        } else if (fichas[row][nextCol].getValor() == fichas[row][col].getValor() && !fichas[row][nextCol].isMerged()) {
                            targetCol = nextCol;
                            updateScore(fichas[row][col].getValor() * 2);
                            break;
                        } else {
                            break;
                        }
                    }
                    if (targetCol != col) {
                        if (fichas[row][targetCol].getValor() == fichas[row][col].getValor() && !fichas[row][targetCol].isMerged()) {
                            fichas[row][targetCol].fusionar(fichas[row][col]);
                        } else {
                            fichas[row][targetCol].setValor(fichas[row][col].getValor());
                            fichas[row][col].setValor(0);
                        }
                        updateFichaView(fichas[row][targetCol]);
                        updateFichaView(fichas[row][col]);
                        moved = true;
                    }
                }
            }
        }
        resetMergedStatus();
        if (moved) {
            addRandomFicha();
        }
    }

    private void moveRight() {
        guardarEstadoAnterior(); // Guardar el turno antes de mover
        boolean moved = false;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = GRID_SIZE - 2; col >= 0; col--) {
                if (fichas[row][col].getValor() != 0) {
                    int targetCol = col;
                    for (int nextCol = col + 1; nextCol < GRID_SIZE; nextCol++) {
                        if (fichas[row][nextCol].getValor() == 0) {
                            targetCol = nextCol;
                        } else if (fichas[row][nextCol].getValor() == fichas[row][col].getValor() && !fichas[row][nextCol].isMerged()) {
                            targetCol = nextCol;
                            updateScore(fichas[row][col].getValor() * 2);
                            break;
                        } else {
                            break;
                        }
                    }
                    if (targetCol != col) {
                        if (fichas[row][targetCol].getValor() == fichas[row][col].getValor() && !fichas[row][targetCol].isMerged()) {
                            fichas[row][targetCol].fusionar(fichas[row][col]);
                        } else {
                            fichas[row][targetCol].setValor(fichas[row][col].getValor());
                            fichas[row][col].setValor(0);
                        }
                        updateFichaView(fichas[row][targetCol]);
                        updateFichaView(fichas[row][col]);
                        moved = true;
                    }
                }
            }
        }
        resetMergedStatus();
        if (moved) {
            addRandomFicha();
        }
    }

    private void resetMergedStatus() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                fichas[i][j].reiniciarFusion();
            }
        }
    }

    private void guardarEstadoAnterior() {
        turnoAnterior = new Ficha[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                turnoAnterior[i][j] = new Ficha(fichas[i][j].getValor(), i, j);
            }
        }
    }


    private void verificarEstadoJuego() {
        if (checkVictory()) {
            finalizarJuego("¡Has ganado!");
            long tiempoJugado = SystemClock.elapsedRealtime() - chronometer.getBase();
            dbManager.insertarPuntuacion(this.playerName, this.score, tiempoJugado);
        } else if (checkGameOver()) {
            finalizarJuego("No hay movimientos posibles. ¡Has perdido!");
            long tiempoJugado = SystemClock.elapsedRealtime() - chronometer.getBase();
            dbManager.insertarPuntuacion(this.playerName, this.score, tiempoJugado);
        }
    }

    private boolean checkVictory() {
        boolean victoria = false;
        int valorVictoria = 2048;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (fichas[i][j].getValor() == valorVictoria) {
                    victoria = true;
                    break;
                }
            }
        }
        return victoria;
    }

    private boolean checkGameOver() {
        boolean derrota = true;
        // 1. Verificar si hay espacios vacíos (juego continúa si hay al menos uno)
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (fichas[i][j].getValor() == 0) {
                    derrota = false; // Aún hay espacio, el juego no ha terminado
                    break;
                }
            }
        }

        if(derrota) {
            // 2. Verificar si hay movimientos posibles (fusiones permitidas)
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    int valorActual = fichas[i][j].getValor();

                    // Comparar con la ficha de arriba
                    if (i > 0 && valorActual == fichas[i - 1][j].getValor()) {
                        derrota = false;
                        break;
                    }
                    // Comparar con la ficha de abajo
                    if (i < GRID_SIZE - 1 && valorActual == fichas[i + 1][j].getValor()) {
                        derrota =  false;
                        break;
                    }
                    // Comparar con la ficha de la izquierda
                    if (j > 0 && valorActual == fichas[i][j - 1].getValor()) {
                        derrota =  false;
                        break;
                    }
                    // Comparar con la ficha de la derecha
                    if (j < GRID_SIZE - 1 && valorActual == fichas[i][j + 1].getValor()) {
                        derrota =  false;
                        break;
                    }
                }
            }
        }

        // Si no hay espacios vacíos y no hay movimientos posibles, el juego ha terminado
        return derrota;
    }

    private void finalizarJuego(String mensaje) {
        chronometer.stop();

        new AlertDialog.Builder(this)
                .setTitle("Fin del juego")
                .setMessage(mensaje + "\n¿Quieres jugar de nuevo? \n")
                .setPositiveButton("Sí", (dialog, which) -> iniciarJuego())
                .setNegativeButton("No", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    /**
     * Muestra un diálogo para pedir el nombre del jugador.
     * Si se introduce un nombre válido, se guarda en la variable nombreJugador y se continúa.
     */

    private void AskPlayerName() {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Ingresa tu nombre");

        new AlertDialog.Builder(this)
                .setTitle("Nombre del jugador")
                .setMessage("Por favor, ingresa tu nombre para jugar 2048:")
                .setView(input)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = input.getText().toString().trim();
                        if (!nombre.isEmpty()) {
                            playerName = nombre;
                            Toast.makeText(Juego2048.this, "¡Bienvenido " + playerName + "!", Toast.LENGTH_SHORT).show();
                            // Aquí puedes continuar la inicialización del juego
                        } else {
                            Toast.makeText(Juego2048.this, "El nombre no puede estar vacío.", Toast.LENGTH_SHORT).show();
                            // Si el nombre es vacío, se vuelve a pedir el nombre
                            AskPlayerName();
                        }
                    }
                })
                .show();
    }

}