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
    private TextView tvMesa, tvJugador, tvPuntaje, tvDeckSize;
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
        tvDeckSize = findViewById(R.id.tvDeckSize);
        mesaCartasLayout = findViewById(R.id.mesaCartas);
        cartasJugadorLayout = findViewById(R.id.cartasJugador);
        btnJugar = findViewById(R.id.btnJugar);

        // Configurar partida
        ArrayList<JugadorLaEscoba> jugadores = new ArrayList<>();
        int numeroJugadores = 2;
        for (int i = 0; i < numeroJugadores; i++) {
            String nombreJugador = "Jugador " + (i + 1);
            jugadores.add(new JugadorLaEscoba(nombreJugador));
        }
        partida = new Partida(jugadores);
        jugadorLaEscobaActual = jugadores.get(0);
        tvJugador.setText("Mano: " + jugadorLaEscobaActual.getNombre());

        // Mostrar cartas iniciales
        try {
            actualizarVista();
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
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(220, 400);
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

        actualizarPuntaje(); // Actualizar el puntaje tras una jugada
        actualizarTamañoBaraja(); // Enseñamos el tamaño de la baraja al jugador
    }

    private void realizarJugadaSeleccionada() {
        if (cartaSeleccionadaMano == null) {
            Toast.makeText(this, "Selecciona una carta de tu mano", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cartaSeleccionadaMano != null && cartasSeleccionadasMesa.isEmpty()) {
            partida.getMesa().add(cartaSeleccionadaMano);
            jugadorLaEscobaActual.eliminarCartaEnMano(cartaSeleccionadaMano);
            cambiarTurno();
        } else if (partida.verificarSuma15(cartaSeleccionadaMano, cartasSeleccionadasMesa)) {
            partida.jugarTurno(jugadorLaEscobaActual, cartaSeleccionadaMano, cartasSeleccionadasMesa);
            try {
                actualizarVista();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            cambiarTurno();
        } else {
            Toast.makeText(this, "Las cartas no suman 15", Toast.LENGTH_SHORT).show();
        }

        // Reiniciar selección
        try {
            actualizarVista();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        cartaSeleccionadaMano = null;
        cartasSeleccionadasMesa.clear();
    }


    private void cambiarTurno() {

        // Antes de cambiar de turno, verificamos si el jugador actual tiene cartas en la mano y si hay cartas en la mesa.
        boolean necesitaRepartirJugador = jugadorLaEscobaActual.getCartasEnMano().isEmpty();
        boolean necesitaRepartirMesa = partida.getMesa().isEmpty();

        // Actualizar la vista: puntajes
        actualizarPuntaje();

        // Si el jugador actual no tiene cartas, se le reparten 3 cartas
        if (necesitaRepartirJugador && !partida.getBaraja().getCartas().isEmpty()) {
            jugadorLaEscobaActual.recibirCartas(partida.getBaraja().repartir(3));
            Toast.makeText(this, "Se reparten 3 cartas al jugador", Toast.LENGTH_SHORT).show();
        }

        // Si la mesa está vacía, se reparten 4 cartas para la mesa
        if (necesitaRepartirMesa && !partida.getBaraja().getCartas().isEmpty()) {
            for (int i = 0; i < 4; i++) {
                Carta carta = partida.getBaraja().repartirUna();
                if (carta != null) {
                    partida.getMesa().add(carta);
                }
            }
            Toast.makeText(this, "Se reparten 4 cartas a la mesa", Toast.LENGTH_SHORT).show();
        }

        // Alternar entre los jugadores
        int indiceActual = partida.getJugadores().indexOf(jugadorLaEscobaActual);
        jugadorLaEscobaActual = partida.getJugadores().get((indiceActual + 1) % partida.getJugadores().size());
        tvJugador.setText("Mano: " + jugadorLaEscobaActual.getNombre());

        // Actualizar la vista: tamaño de la baraja
        actualizarTamañoBaraja();

        // Verificar si hay jugada disponible
        boolean jugadaDisponible = partida.jugadaDisponible(jugadorLaEscobaActual);
        if (jugadaDisponible) {
            Toast.makeText(this, "Jugada disponible", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Jugada NO disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void seleccionarCarta(Carta cartaSeleccionada) throws NoSuchFieldException, IllegalAccessException {
        // jugar la carta seleccionada
        partida.jugarTurno(jugadorLaEscobaActual, cartaSeleccionada, new ArrayList<>());
        actualizarVista();
        cambiarTurno();
    }

    private void seleccionarCartaMesa(Carta carta, ImageView cartaView) {
        if (cartasSeleccionadasMesa.contains(carta)) {
            cartasSeleccionadasMesa.remove(carta);
            // aqui se pone la logica para la opacidad de seleccionar
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

        String textoPuntaje = "Puntaje: \n ";

        for (JugadorLaEscoba jugador : partida.getJugadores()) {
            textoPuntaje += jugador.getNombre() + " - " + jugador.calcularPuntaje() + " | ";
        }

        tvPuntaje.setText(textoPuntaje);

    }

    private void actualizarTamañoBaraja() {
        // Actualizar el tamaño de la baraja
        int deckSize = partida.getBaraja().getCartas().size();
        tvDeckSize.setText("Baraja: " + deckSize);
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
