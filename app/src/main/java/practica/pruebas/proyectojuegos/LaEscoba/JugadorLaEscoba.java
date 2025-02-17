package practica.pruebas.proyectojuegos.LaEscoba;

import java.util.ArrayList;

import practica.pruebas.proyectojuegos.JugadorGeneral;

public class JugadorLaEscoba extends JugadorGeneral {
    private String nombre;
    private ArrayList<Carta> cartasEnMano;
    private ArrayList<Carta> cartasGanadas;
    private int puntos;
    private int escobas;

    public JugadorLaEscoba(String nombre) {
        super(nombre);
        this.cartasEnMano = new ArrayList<>();
        this.cartasGanadas = new ArrayList<>();
        this.puntos = 0;
        this.escobas = 0;
    }

    public ArrayList<Carta> getCartasEnMano() {
        return cartasEnMano;
    }

    public ArrayList<Carta> getCartasGanadas() {
        return cartasGanadas;
    }

    public void recibirCartas(ArrayList<Carta> cartas) {
        this.cartasEnMano.addAll(cartas);
    }

    public void ganarCartas(ArrayList<Carta> cartas) {
        this.cartasGanadas.addAll(cartas);
    }

    public void eliminarCartaEnMano(Carta carta) {
        cartasEnMano.removeIf(c -> c.getPalo().equals(carta.getPalo()) && c.getValor() == carta.getValor());
    }

    public void incrementarEscobas() {
        this.escobas++;
    }

    // Permite agregar puntos directos (por bonificaciones comparativas)
    public void agregarPuntos(int puntos) {
        this.puntos += puntos;
    }

    /**
     * Calcula la puntuación individual del jugador basada en:
     * - Puntos por escobas (cada escoba vale 1 punto)
     * - 1 punto por capturar el 7 de oros (o 7 de golden, según la nomenclatura)
     *
     * La bonificación por mayor cantidad de cartas, oros y sietes se asignará al final de la ronda.
     */
    public int calcularPuntaje() {
        int puntosCalculados = 0;

        // Sumar puntos por escobas
        puntosCalculados += escobas;

        // Verificar si capturó el 7 de oros
        boolean tieneSieteOros = false;
        for (Carta carta : cartasGanadas) {
            if (carta.getValor() == 7 && (carta.getPalo().equalsIgnoreCase("oros") || carta.getPalo().equalsIgnoreCase("golden"))) {
                tieneSieteOros = true;
                break;
            }
        }
        if (tieneSieteOros) {
            puntosCalculados += 1;
        }

        // Sumar los puntos que ya se hayan agregado (por bonificaciones comparativas)
        puntosCalculados += this.puntos;

        return puntosCalculados;
    }

    /*public int calcularPuntaje() {

        // Contadores para reglas de puntuación
        int cantidadCartas = cartasGanadas.size();
        int cantidadOros = 0;
        int cantidadSietes = 0;
        boolean tieneSieteOros = false;

        for (Carta carta : cartasGanadas) {
            if (carta.getPalo().equalsIgnoreCase("golden")) {
                cantidadOros++;
            }
            if (carta.getValor() == 7) {
                cantidadSietes++;
                if (carta.getPalo().equalsIgnoreCase("golden")) {
                    tieneSieteOros = true;
                }
            }
        }

        // **Reglas de puntuación**:
        if (tieneSieteOros) this.puntos += 1;  // ✅ 1 punto si capturó el 7 de Oros
        if (cantidadSietes > 0) this.puntos += 1; // ✅ 1 punto si tiene más sietes
        if (cantidadOros > 0) this.puntos += 1; // ✅ 1 punto si tiene más cartas de oros
        if (cantidadCartas > 0) this.puntos += 1; // ✅ 1 punto si capturó más cartas

        return this.puntos;
    }*/

}
