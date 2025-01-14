package practica.pruebas.proyectojuegos.LaEscoba;

import java.util.ArrayList;

public class Partida {
    private ArrayList<Jugador> jugadores;
    private Baraja baraja;
    private ArrayList<Carta> mesa;

    public Partida(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.baraja = new Baraja();
        this.mesa = new ArrayList<>();
        repartirCartasIniciales();
    }

    private void repartirCartasIniciales() {
        for (Jugador jugador : jugadores) {
            jugador.recibirCartas(baraja.repartir(3));
        }
        for (int i = 0; i < 4; i++) {
            mesa.add(baraja.repartirUna());
        }
    }

    public ArrayList<Carta> getMesa() {
        return mesa;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void jugarTurno(Jugador jugador, Carta cartaJugador, ArrayList<Carta> cartasMesaSeleccionadas) {
        if (verificarSuma15(cartaJugador, cartasMesaSeleccionadas)) {
            ArrayList<Carta> cartasGanadas = new ArrayList<>(cartasMesaSeleccionadas);
            cartasGanadas.add(cartaJugador);
            jugador.ganarCartas(cartasGanadas);
            mesa.removeAll(cartasMesaSeleccionadas);

            if (mesa.isEmpty()) {
                System.out.println(jugador.getNombre() + " hizo una escoba!");
            }
        } else {
            mesa.add(cartaJugador);
        }

        jugador.getCartasEnMano().remove(cartaJugador);
    }

    private boolean verificarSuma15(Carta cartaJugador, ArrayList<Carta> cartasMesaSeleccionadas) {
        int suma = cartaJugador.getValor();
        for (Carta carta : cartasMesaSeleccionadas) {
            suma += carta.getValor();
        }
        return suma == 15;
    }
}
