package practica.pruebas.proyectojuegos.LaEscoba;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
            this.mesa.clear();
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

    public JugadorLaEscoba asignarBonificacionesFinales() {
        List<JugadorLaEscoba> jugadores = getJugadores();
        JugadorLaEscoba ganador = jugadores.get(0);

        for (JugadorLaEscoba jugador: jugadores) {
            for (Carta carta: jugador.getCartasGanadas()) {
                if (carta.getPalo() == "golden"){
                    jugador.setCantidadOros(jugador.getCantidadOros()+1);
                    if (carta.getValor() == 7) {
                        jugador.agregarPuntos(1);
                    }
                }
                if (carta.getValor() == 7) {
                    jugador.setCantidadSietes(jugador.getCantidadSietes()+1);
                }
            }
        }

        Collections.max(jugadores, Comparator.comparingInt(JugadorLaEscoba::getCantidadOros)).agregarPuntos(1);
        Collections.max(jugadores, Comparator.comparingInt(JugadorLaEscoba::getCantidadSietes)).agregarPuntos(1);

        /*for (JugadorLaEscoba jugador: jugadores) {
            JugadorLaEscoba jugadorGanador = jugador;
        }*/

        for (JugadorLaEscoba jugador: jugadores) {
            if (ganador.getPuntos() < jugador.getPuntos()) {
                ganador = jugador;
            }
        }
        return ganador;
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

    public boolean rondaFinalizada() {

        boolean finalizado = false;
        int contador = 0;

        // por si implementamos la posibilidad de que haya más jugadores
        for (int i= 0 ; i < jugadores.size(); i++) {
            if (jugadores.get(i).getCartasEnMano().isEmpty()) {
                contador++;
            }
        }

        if (jugadores.size() == 2 && contador == 1 && baraja.getBarajaCartas().isEmpty() && mesa.isEmpty()) {
            for (int i = 0; i < jugadores.size(); i++) {
                mesa.addAll(jugadores.get(i).getCartasEnMano());
            }
        }

        if (contador == jugadores.size() && baraja.getBarajaCartas().isEmpty()) {
            finalizado = true;
        }

        // Si la baraja está vacía y todos los jugadores no tienen cartas, la ronda ha finalizado.
        return finalizado;
    }
}
