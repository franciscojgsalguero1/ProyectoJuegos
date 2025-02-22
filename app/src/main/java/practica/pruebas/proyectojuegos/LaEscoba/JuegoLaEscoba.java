package practica.pruebas.proyectojuegos.LaEscoba;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

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
    private DatabaseManager dbManager; // Referencia al DatabaseManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializamos dbManager
        dbManager = DatabaseManager.getInstance(this);
        //verificarConexionBD();
        setContentView(R.layout.juegolaescoba);

        // Inicializar vistas
        tvMesa = findViewById(R.id.tvMesa);
        tvJugador = findViewById(R.id.tvJugador);
        tvPuntaje = findViewById(R.id.tvPuntaje);
        tvDeckSize = findViewById(R.id.tvDeckSize);
        mesaCartasLayout = findViewById(R.id.mesaCartas);
        cartasJugadorLayout = findViewById(R.id.cartasJugador);
        btnJugar = findViewById(R.id.btnJugar);
        Button backToMenuButton = findViewById(R.id.btn_back_to_menu);
        backToMenuButton.setOnClickListener(v -> {finish();});

        // Configurar partida
        ArrayList<JugadorLaEscoba> jugadores = new ArrayList<>();
        int numeroJugadores = 2;
        for (int i = 1; i < numeroJugadores+1; i++) {
            // Pedir nombre al jugador
            JugadorLaEscoba jugador = new JugadorLaEscoba("nuevo jugador La Escoba" + i);
                jugadores.add(jugador);
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
        boolean cambiarturnobooleano = true;

        if (cartaSeleccionadaMano == null) {
            // si el jugador no selecciona ninguna carta, se le pide que seleccione una carta de su mano
            jugadorLaEscobaActual.setBaza(false);
            this.mensajeToast("Selecciona una carta de tu mano");
            cambiarturnobooleano = false;
        } else if (cartaSeleccionadaMano != null && cartasSeleccionadasMesa.isEmpty()) {
            // si el jugador selecciona una carta de su mano y no ha seleccionado ninguna carta de la mesa, se añade la carta a la mesa
            jugadorLaEscobaActual.setBaza(false);
            partida.getMesa().add(cartaSeleccionadaMano);
            jugadorLaEscobaActual.eliminarCartaEnMano(cartaSeleccionadaMano);
        } else if (cartaSeleccionadaMano != null && partida.verificarSuma15(cartaSeleccionadaMano, cartasSeleccionadasMesa)) {
            // si el jugador selecciona una carta de su mano y ha seleccionado cartas de la mesa, se realiza la jugada
            jugadorLaEscobaActual.setBaza(true);
            partida.jugarTurno(jugadorLaEscobaActual, cartaSeleccionadaMano, cartasSeleccionadasMesa);
        } else {
            // si la jugada no es válida, se pide que seleccione otra jugada
            this.mensajeToast("Las cartas no suman 15");
            cambiarturnobooleano = false;
        }

        if (cambiarturnobooleano) {
            // antes del cambio de turno se mira la condición de finalización de la ronda
            if (partida.rondaFinalizada()) {
                JugadorLaEscoba jugadorbaza = jugadorLaEscobaActual;
                for (int i = 0; i < partida.getJugadores().size(); i++) {
                    if(partida.getJugadores().get(i).getBaza()) {
                        jugadorbaza = partida.getJugadores().get(i);
                    }
                }
                partida.vaciarMesa(jugadorbaza);
                JugadorLaEscoba ganador = partida.asignarBonificacionesFinales();
                dbManager.insertarPuntuacion(ganador.getNombre(), ganador.getPuntos());
            }
            cambiarTurno();
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

        repartirAntesCanvioTurno();

        // Alternar entre los jugadores
        int indiceActual = partida.getJugadores().indexOf(jugadorLaEscobaActual);
        jugadorLaEscobaActual = partida.getJugadores().get((indiceActual + 1) % partida.getJugadores().size());
        tvJugador.setText("Mano: " + jugadorLaEscobaActual.getNombre());

        // Actualizar la vista
        try {
            actualizarVista();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // actualizamos el puntuaje de los jugadores
        actualizarPuntaje();

        // Actualizar la vista: tamaño de la baraja
        actualizarTamañoBaraja();

        // Verificar si hay jugada disponible
        if (!existeJugadaDisponibleParaJugador(jugadorLaEscobaActual, partida.getMesa())) {
            this.mensajeToast("Jugada NO disponible");
        }
    }

    public void repartirAntesCanvioTurno() {

        // Antes de cambiar de turno, verificamos si el jugador actual tiene cartas en la mano y si hay cartas en la mesa.
        boolean necesitaRepartirJugador = false;
        int contador = 0;
        //boolean jugadores = partida.getJugadores().get(0).getCartasEnMano().isEmpty();
        List<JugadorLaEscoba> jugadores = partida.getJugadores();
        boolean necesitaRepartirMesa = partida.getMesa().isEmpty();

        // comprobamos que ningún jugador tiene cartas
        for (JugadorLaEscoba jugador: jugadores) {
            if (jugador.getCartasEnMano().isEmpty()) {
                contador++;
            }

            if (contador == jugadores.size()) {
                necesitaRepartirJugador = true;
            }
        }

        // Si los jugadores tienen cartas, se le reparten 3 cartas a cada uno
        if (necesitaRepartirJugador && !partida.getBaraja().getBarajaCartas().isEmpty()) {

            int msj = 3;

            if (partida.getBaraja().getBarajaCartas().size() < 6) {
                msj = partida.getBaraja().getBarajaCartas().size()/2;
            }
            this.mensajeToast("Se reparten " +  msj + " cartas a cada jugador");

            for (int i = 0; i < 3; i++) {
                for (JugadorLaEscoba jugador : jugadores) {
                    jugador.recibirCartas(partida.getBaraja().repartir(1));
                }
            }
        }

        // Si la mesa está vacía, se reparten 4 cartas para la mesa
        if (necesitaRepartirMesa && !partida.getBaraja().getBarajaCartas().isEmpty()) {
            for (int i = 0; i < 4; i++) {
                Carta carta = partida.getBaraja().repartirUna();
                if (carta != null) {
                    partida.getMesa().add(carta);
                }
            }
            this.mensajeToast("Se reparten 4 cartas a la mesa");
        }

    }

    public boolean existeJugadaDisponibleParaJugador(JugadorLaEscoba jugador, List<Carta> mesa) {
        List<Carta> mano = jugador.getCartasEnMano();
        boolean jugada = false;

        // Para cada carta en la mano, se busca una combinación en la mesa.
        for (Carta cartaMano : mano) {
            int target = 15 - cartaMano.getValor();
            List<List<Carta>> combinacionesValidas = partida.encontrarCombinaciones(mesa, target);
            if (!combinacionesValidas.isEmpty()) {
                System.out.println("Jugada encontrada para: " + cartaMano);
                jugada = true;
            }
        }
        return jugada;
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
        if(cartaSeleccionadaMano == carta) {
            cartaSeleccionadaMano = null;
            cartaView.setAlpha(1.0f); // Restaurar opacidad normal
        } else {
            cartaSeleccionadaMano = carta;
            cartaView.setAlpha(0.5f); // Reducir opacidad para indicar selección
        }
    }

    private void actualizarPuntaje() {

        String textoPuntaje = "Puntaje: \n ";

        jugadorLaEscobaActual.calcularPuntaje();

        for (JugadorLaEscoba jugador : partida.getJugadores()) {
            textoPuntaje += jugador.getNombre() + " - " + jugador.calcularPuntaje() + " | ";
        }

        tvPuntaje.setText(textoPuntaje);
    }

    private void actualizarTamañoBaraja() {
        // Actualizar el tamaño de la baraja
        int deckSize = partida.getBaraja().getBarajaCartas().size();
        tvDeckSize.setText("Baraja: " + deckSize);
    }

    public void mensajeToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}