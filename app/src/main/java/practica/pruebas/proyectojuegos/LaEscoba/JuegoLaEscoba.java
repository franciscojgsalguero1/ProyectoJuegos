package practica.pruebas.proyectojuegos.LaEscoba;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import practica.pruebas.proyectojuegos.JugadorGeneral;
import practica.pruebas.proyectojuegos.R;
import practica.pruebas.proyectojuegos.database.DatabaseManager;

public class JuegoLaEscoba extends AppCompatActivity {

    private Partida partida;
    private JugadorLaEscoba jugadorLaEscobaActual;
    private LinearLayout mesaCartasLayout, cartasJugadorLayout;
    private TextView tvMesa, tvJugador, tvPuntaje;
    private Button btnJugar;

    private ArrayList<Carta> cartasSeleccionadasMesa = new ArrayList<>();
    private Carta cartaSeleccionadaMano = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //verificarConexionBD();
        setContentView(R.layout.juegolaescoba);

        Button backToMenuButton = findViewById(R.id.btn_back_to_menu);
        backToMenuButton.setOnClickListener(v -> {
            finish();
        });

        // Inicializar vistas
        tvMesa = findViewById(R.id.tvMesa);
        tvJugador = findViewById(R.id.tvJugador);
        tvPuntaje = findViewById(R.id.tvPuntaje);
        mesaCartasLayout = findViewById(R.id.mesaCartas);
        cartasJugadorLayout = findViewById(R.id.cartasJugador);
        btnJugar = findViewById(R.id.btnJugar);

        // Configurar partida
        ArrayList<JugadorLaEscoba> jugadores = new ArrayList<>();
        jugadores.add(new JugadorLaEscoba("Jugador 1 prueba"));
        jugadores.add(new JugadorLaEscoba("Jugador 2 prueba2"));
        partida = new Partida(jugadores);
        jugadorLaEscobaActual = jugadores.get(0);
        tvJugador.setText("Mano: " + jugadorLaEscobaActual.getNombre());

        // Mostrar cartas iniciales
        try {
            actualizarVista();
            actualizarPuntaje(); // Actualizar el puntaje tras una jugada
            //cambiarTurno(); cambiar turno entre jugadores
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Acción del botón Jugar
        btnJugar.setOnClickListener(v -> realizarJugadaSeleccionada());
    }

    private void actualizarVista() throws NoSuchFieldException, IllegalAccessException {
        // Limpiar vistas anteriores
        cartasJugadorLayout.removeAllViews();
        mesaCartasLayout.removeAllViews();

        int margins = 10;

        // Mostrar cartas en la mesa
        for (Carta carta : partida.getMesa()) {
            ImageView cartaView = new ImageView(this);
            cartaView.setImageResource(carta.getImageResourceId());

            // Configurar tamaño de la imagen
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250, 400);
            params.setMargins(margins, margins, margins, margins);
            cartaView.setLayoutParams(params);

            // Permitir seleccionar varias cartas de la mesa
            cartaView.setOnClickListener(v -> seleccionarCartaMesa(carta, cartaView));

            mesaCartasLayout.addView(cartaView);
        }

        // Mostrar cartas del jugador actual
        for (Carta carta : jugadorLaEscobaActual.getCartasEnMano()) {
            ImageView cartaView = new ImageView(this);
            cartaView.setImageResource(carta.getImageResourceId());

            // Configurar tamaño de la imagen
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250, 400);
            params.setMargins(margins, margins, margins, margins);
            cartaView.setLayoutParams(params);

            // Permitir seleccionar solo una carta de la mano
            cartaView.setOnClickListener(v -> seleccionarCartaMano(carta, cartaView));

            cartasJugadorLayout.addView(cartaView);
        }
    }

    /*private void realizarJugada() throws NoSuchFieldException, IllegalAccessException {
        // Ejemplo: el jugador juega la primera carta
        if (!jugadorLaEscobaActual.getCartasEnMano().isEmpty()) {
            Carta carta = jugadorLaEscobaActual.getCartasEnMano().get(0);
            partida.jugarTurno(jugadorLaEscobaActual, carta, new ArrayList<>());
            actualizarVista();

            // Cambiar al siguiente jugador
            cambiarTurno();
        }
    }*/

    private void realizarJugadaSeleccionada() {
        if (cartaSeleccionadaMano == null) {
            Toast.makeText(this, "Selecciona una carta de tu mano", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cartaSeleccionadaMano != null && cartasSeleccionadasMesa.isEmpty()) {
            partida.getMesa().add(cartaSeleccionadaMano);
            jugadorLaEscobaActual.getCartasEnMano().remove(cartaSeleccionadaMano);
            try {
                actualizarVista();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            cambiarTurno();
        } else if (partida.verificarSuma15(cartaSeleccionadaMano, cartasSeleccionadasMesa)) {
            partida.jugarTurno(jugadorLaEscobaActual, cartaSeleccionadaMano, cartasSeleccionadasMesa);
            try {
                actualizarVista();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            actualizarPuntaje();
            cambiarTurno();
        } else {
            Toast.makeText(this, "Las cartas no suman 15", Toast.LENGTH_SHORT).show();
        }

        // Reiniciar selección
        cartaSeleccionadaMano = null;
        cartasSeleccionadasMesa.clear();
    }


    private void cambiarTurno() {
        // Alternar entre los jugadores
        int indiceActual = partida.getJugadores().indexOf(jugadorLaEscobaActual);
        jugadorLaEscobaActual = partida.getJugadores().get((indiceActual + 1) % partida.getJugadores().size());
        tvJugador.setText("Mano: " + jugadorLaEscobaActual.getNombre());
    }

    private void seleccionarCarta(Carta cartaSeleccionada) throws NoSuchFieldException, IllegalAccessException {
        // Ejemplo: jugar la carta seleccionada
        partida.jugarTurno(jugadorLaEscobaActual, cartaSeleccionada, new ArrayList<>());
        actualizarVista();
        cambiarTurno();
    }

    private void seleccionarCartaMesa(Carta carta, ImageView cartaView) {
        if (cartasSeleccionadasMesa.contains(carta)) {
            cartasSeleccionadasMesa.remove(carta);
            // aqui es la opacidad seleccionar
            cartaView.setAlpha(1.0f); // Restaurar opacidad normal
        } else {
            cartasSeleccionadasMesa.add(carta);
            cartaView.setAlpha(0.5f); // Reducir opacidad para indicar selección
        }
    }

    private void seleccionarCartaMano(Carta carta, ImageView cartaView) {
        String mensaje = "Seleccionaste ";
        if(cartaSeleccionadaMano == carta) {
            cartaSeleccionadaMano = null;
            cartaView.setAlpha(1.0f); // Restaurar opacidad normal
            mensaje = "No seleccionaste ";
        } else {
            cartaSeleccionadaMano = carta;
            cartaView.setAlpha(0.5f); // Reducir opacidad para indicar selección
        }

        Toast.makeText(this, mensaje + carta.toString(), Toast.LENGTH_SHORT).show();
    }

    private void actualizarPuntaje() {
        int puntajeJ1 = partida.getJugadores().get(0).calcularPuntaje();
        int puntajeJ2 = partida.getJugadores().get(1).calcularPuntaje();

        String puntajeTexto = "Puntaje: " + partida.getJugadores().get(0).getNombre() + " - " + puntajeJ1 +
                " | " + partida.getJugadores().get(1).getNombre() + " - " + puntajeJ2;

        tvPuntaje.setText(puntajeTexto);
    }


    private void verificarConexionBD() {
        DatabaseManager dbManager = new DatabaseManager(this);
        boolean conexionExitosa = dbManager.probarConexion();

        if (conexionExitosa) {
            Toast.makeText(this, "Conexión con la base de datos exitosa", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error al conectar con la base de datos", Toast.LENGTH_LONG).show();
        }
    }

}
