package practica.pruebas.proyectojuegos.LaEscoba;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class Partida {
    private ArrayList<JugadorLaEscoba> jugadores;
    private Baraja baraja;
    private ArrayList<Carta> mesa;

    public Partida(ArrayList<JugadorLaEscoba> jugadores) {
        this.jugadores = jugadores;
        this.baraja = new Baraja();
        this.mesa = new ArrayList<>();
        repartirCartasIniciales();
    }

    private void repartirCartasIniciales() {
        for (JugadorLaEscoba jugadorLaEscoba : jugadores) {
            jugadorLaEscoba.recibirCartas(baraja.repartir(3));
        }

        mesa.addAll(baraja.repartir(4));
    }

    public Baraja getBaraja() {
        return baraja;
    }

    public ArrayList<Carta> getMesa() {
        return mesa;
    }

    public void setMesa(ArrayList<Carta> mesa) {
        this.mesa = mesa;
    }

    // metodo para vaciar la mesa al final de la partida
    public void vaciarMesa(JugadorLaEscoba jugador) {
        if (!this.mesa.isEmpty()) {
            jugador.setCartasGanadas(this.mesa);
            for (int i = 0; i < this.mesa.size(); i++) {
                this.mesa.removeAll(this.mesa);
            }
            this.mesa = new ArrayList<>();
        }
    }

    public ArrayList<JugadorLaEscoba> getJugadores() {
        return jugadores;
    }

    public void jugarTurno(JugadorLaEscoba jugadorLaEscoba, Carta cartaJugador, ArrayList<Carta> cartasMesaSeleccionadas) {
        if (verificarSuma15(cartaJugador, cartasMesaSeleccionadas)) {
            ArrayList<Carta> cartasGanadas = new ArrayList<>(cartasMesaSeleccionadas);
            cartasGanadas.add(cartaJugador);
            jugadorLaEscoba.ganarCartas(cartasGanadas);
            mesa.removeAll(cartasMesaSeleccionadas);
            jugadorLaEscoba.incrementarEscobas(mesa);
        } else {
            mesa.add(cartaJugador);
        }

        jugadorLaEscoba.eliminarCartaEnMano(cartaJugador);
    }

    public boolean verificarSuma15(Carta cartaJugador, ArrayList<Carta> cartasMesaSeleccionadas) {
        int suma = cartaJugador.getValor();
        for (Carta carta : cartasMesaSeleccionadas) {
            suma += carta.getValor();
        }
        return suma == 15;
    }

    public void asignarBonificacionesFinales() {
        List<JugadorLaEscoba> jugadores = getJugadores();

        // Bonificación por mayor cantidad de cartas
        JugadorLaEscoba bonusCartas = Collections.max(jugadores, Comparator.comparingInt(j -> j.getCartasGanadas().size()));
        bonusCartas.agregarPuntos(1);

        // Bonificación por mayor cantidad de oros y sietes
        JugadorLaEscoba bonusOros = null;
        JugadorLaEscoba bonusSietes = null;
        int maxOros = -1;
        int maxSietes = -1;

        for (JugadorLaEscoba jugador : jugadores) {
            int countOros = 0;
            int countSietes = 0;
            for (Carta carta : jugador.getCartasGanadas()) {
                if (carta.getPalo().equalsIgnoreCase("golden") || carta.getPalo().equalsIgnoreCase("golden")) {
                    countOros++;
                }
                if (carta.getValor() == 7) {
                    countSietes++;
                }
            }
            if (countOros > maxOros) {
                maxOros = countOros;
                bonusOros = jugador;
            }
            if (countSietes > maxSietes) {
                maxSietes = countSietes;
                bonusSietes = jugador;
            }
        }
        if (bonusOros != null) bonusOros.agregarPuntos(1);
        if (bonusSietes != null) bonusSietes.agregarPuntos(1);
    }

    private void encontrarCombinacionesHelper(List<Carta> mesa, int target, int index,
                                              List<Carta> actual, List<List<Carta>> resultados) {
        if (target == 0 && !actual.isEmpty()) {
            resultados.add(new ArrayList<>(actual));
            return;
        }
        if (target < 0 || index >= mesa.size()) {
            return;
        }

        // Opción 1: Incluir la carta actual
        actual.add(mesa.get(index));
        encontrarCombinacionesHelper(mesa, target - mesa.get(index).getValor(), index + 1, actual, resultados);
        actual.remove(actual.size() - 1);

        // Opción 2: Omitir la carta actual
        encontrarCombinacionesHelper(mesa, target, index + 1, actual, resultados);
    }

    /**
     * Metodo público que retorna todas las combinaciones de cartas de la mesa que sumen target.
     */
    public List<List<Carta>> encontrarCombinaciones(List<Carta> mesa, int target) {
        List<List<Carta>> resultados = new ArrayList<>();
        encontrarCombinacionesHelper(mesa, target, 0, new ArrayList<>(), resultados);
        return resultados;
    }

    public boolean rondaFinalizada(JugadorLaEscoba jugadorLaEscoba) {

        boolean finalizado = false;

        if (jugadores.get(0).getCartasEnMano().isEmpty() && jugadores.get(1).getCartasEnMano().isEmpty() && baraja.getBarajaCartas().isEmpty()) {
            finalizado = true;
        }

        // Si la baraja está vacía y todos los jugadores no tienen cartas, la ronda ha finalizado.
        return finalizado;
    }
}
