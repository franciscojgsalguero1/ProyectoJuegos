package practica.pruebas.proyectojuegos.LaEscoba;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import practica.pruebas.proyectojuegos.R;

public class JuegoLaEscoba extends AppCompatActivity {

    private Partida partida;
    private JugadorLaEscoba jugadorLaEscobaActual;
    private LinearLayout mesaCartasLayout, cartasJugadorLayout;
    private TextView tvMesa, tvJugador;
    private Button btnJugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juegolaescoba);

        Button backToMenuButton = findViewById(R.id.btn_back_to_menu);
        backToMenuButton.setOnClickListener(v -> {
            finish();
        });

        // Inicializar vistas
        tvMesa = findViewById(R.id.tvMesa);
        tvJugador = findViewById(R.id.tvJugador);
        mesaCartasLayout = findViewById(R.id.mesaCartas);
        cartasJugadorLayout = findViewById(R.id.cartasJugador);
        btnJugar = findViewById(R.id.btnJugar);

        // Configurar partida
        ArrayList<JugadorLaEscoba> jugadores = new ArrayList<>();
        jugadores.add(new JugadorLaEscoba("Jugador 1"));
        jugadores.add(new JugadorLaEscoba("Jugador 2"));
        partida = new Partida(jugadores);
        jugadorLaEscobaActual = jugadores.get(0);

        // Mostrar cartas iniciales
        try {
            actualizarVista();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Acción del botón Jugar
        btnJugar.setOnClickListener(view -> {
            try {
                realizarJugada();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void actualizarVista() throws NoSuchFieldException, IllegalAccessException {
        // Limpiar vistas anteriores
        cartasJugadorLayout.removeAllViews();
        mesaCartasLayout.removeAllViews();

        // Mostrar cartas en la mesa
        for (Carta carta : partida.getMesa()) {
            ImageView cartaView = new ImageView(this);
            cartaView.setImageResource(carta.getImageResourceId());

            // Configurar tamaño de la imagen
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 200);
            params.setMargins(10, 10, 10, 10);
            cartaView.setLayoutParams(params);

            mesaCartasLayout.addView(cartaView);
        }

        // Mostrar cartas del jugador actual
        for (Carta carta : jugadorLaEscobaActual.getCartasEnMano()) {
            ImageView cartaView = new ImageView(this);
            cartaView.setImageResource(carta.getImageResourceId());

            // Configurar tamaño de la imagen
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 200);
            params.setMargins(10, 10, 10, 10);
            cartaView.setLayoutParams(params);

            // Hacer que la carta sea seleccionable
            cartaView.setOnClickListener(v -> {
                try {
                    seleccionarCarta(carta);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });

            cartasJugadorLayout.addView(cartaView);
        }
    }

    private void realizarJugada() throws NoSuchFieldException, IllegalAccessException {
        // Ejemplo: el jugador juega la primera carta
        if (!jugadorLaEscobaActual.getCartasEnMano().isEmpty()) {
            Carta carta = jugadorLaEscobaActual.getCartasEnMano().get(0);
            partida.jugarTurno(jugadorLaEscobaActual, carta, new ArrayList<>());
            actualizarVista();

            // Cambiar al siguiente jugador
            cambiarTurno();
        }
    }

    private void cambiarTurno() {
        // Alternar entre los jugadores
        int indiceActual = partida.getJugadores().indexOf(jugadorLaEscobaActual);
        jugadorLaEscobaActual = partida.getJugadores().get((indiceActual + 1) % partida.getJugadores().size());
    }

    private void seleccionarCarta(Carta cartaSeleccionada) throws NoSuchFieldException, IllegalAccessException {
        // Ejemplo: jugar la carta seleccionada
        partida.jugarTurno(jugadorLaEscobaActual, cartaSeleccionada, new ArrayList<>());
        actualizarVista();
        cambiarTurno();
    }


}
