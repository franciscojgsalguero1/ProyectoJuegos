package practica.pruebas.proyectojuegos.LaEscoba;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import practica.pruebas.proyectojuegos.R;

public class JuegoLaEscoba extends AppCompatActivity {

    private Partida partida;
    private Jugador jugadorActual;
    private LinearLayout mesaCartasLayout, cartasJugadorLayout;
    private TextView tvMesa, tvJugador;
    private Button btnJugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juegolaescoba);

        // Inicializar vistas
        tvMesa = findViewById(R.id.tvMesa);
        tvJugador = findViewById(R.id.tvJugador);
        mesaCartasLayout = findViewById(R.id.mesaCartas);
        cartasJugadorLayout = findViewById(R.id.cartasJugador);
        btnJugar = findViewById(R.id.btnJugar);

        // Configurar partida
        ArrayList<Jugador> jugadores = new ArrayList<>();
        jugadores.add(new Jugador("Jugador 1"));
        jugadores.add(new Jugador("Jugador 2"));
        partida = new Partida(jugadores);
        jugadorActual = jugadores.get(0);

        // Mostrar cartas iniciales
        actualizarVista();

        // Acción del botón Jugar
        btnJugar.setOnClickListener(view -> realizarJugada());
    }

    private void actualizarVista() {
        // Mostrar cartas en la mesa
        mesaCartasLayout.removeAllViews();
        for (Carta carta : partida.getMesa()) {
            TextView cartaView = new TextView(this);
            cartaView.setText(carta.toString());
            mesaCartasLayout.addView(cartaView);
        }

        // Mostrar cartas del jugador actual
        cartasJugadorLayout.removeAllViews();
        for (Carta carta : jugadorActual.getCartasEnMano()) {
            TextView cartaView = new TextView(this);
            cartaView.setText(carta.toString());
            cartasJugadorLayout.addView(cartaView);
        }
    }

    private void realizarJugada() {
        // Ejemplo: el jugador juega la primera carta
        if (!jugadorActual.getCartasEnMano().isEmpty()) {
            Carta carta = jugadorActual.getCartasEnMano().get(0);
            partida.jugarTurno(jugadorActual, carta, new ArrayList<>());
            actualizarVista();

            // Cambiar al siguiente jugador
            cambiarTurno();
        }
    }

    private void cambiarTurno() {
        // Alternar entre los jugadores
        int indiceActual = partida.getJugadores().indexOf(jugadorActual);
        jugadorActual = partida.getJugadores().get((indiceActual + 1) % partida.getJugadores().size());
    }
}
