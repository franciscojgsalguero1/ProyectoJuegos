package practica.pruebas.proyectojuegos.LaEscoba;

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

    public void asignarBonificacionesFinales() {
        List<JugadorLaEscoba> jugadores = getJugadores();

        // Bonus por mayor cantidad de cartas ganadas
        int maxCartas = -1;
        JugadorLaEscoba bonusCartas = null;
        for (JugadorLaEscoba jugador : jugadores) {
            int cantidadCartas = jugador.getCartasGanadas().size();
            if (cantidadCartas > maxCartas) {
                maxCartas = cantidadCartas;
                bonusCartas = jugador;
            }
        }
        if (bonusCartas != null) {
            bonusCartas.agregarPuntos(1);
        }

        // Bonus por mayor cantidad de oros.
        // Asumiremos que las cartas de oros son aquellas cuyo palo es "golden" (puedes ajustarlo si usas otro término)
        int maxOros = -1;
        JugadorLaEscoba bonusOros = null;
        for (JugadorLaEscoba jugador : jugadores) {
            int countOros = 0;
            for (Carta carta : jugador.getCartasGanadas()) {
                if (carta.getPalo().equalsIgnoreCase("golden")) {
                    countOros++;
                }
            }
            if (countOros > maxOros) {
                maxOros = countOros;
                bonusOros = jugador;
            }
        }
        if (bonusOros != null) {
            bonusOros.agregarPuntos(1);
        }

        // Bonus por mayor cantidad de sietes
        int maxSietes = -1;
        JugadorLaEscoba bonusSietes = null;
        for (JugadorLaEscoba jugador : jugadores) {
            int countSietes = 0;
            for (Carta carta : jugador.getCartasGanadas()) {
                if (carta.getValor() == 7) {
                    countSietes++;
                }
            }
            if (countSietes > maxSietes) {
                maxSietes = countSietes;
                bonusSietes = jugador;
            }
        }
        if (bonusSietes != null) {
            bonusSietes.agregarPuntos(1);
        }
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
