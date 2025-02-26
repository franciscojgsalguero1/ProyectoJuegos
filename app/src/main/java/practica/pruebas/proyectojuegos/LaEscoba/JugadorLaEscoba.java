package practica.pruebas.proyectojuegos.LaEscoba;

import java.util.ArrayList;
import practica.pruebas.proyectojuegos.resources.View;

public class JugadorLaEscoba {
    private String playerName;
    private ArrayList<Carta> cartasEnMano;
    private ArrayList<Carta> cartasGanadas;
    private int cantidadOros;
    private int cantidadSietes;
    private int escobas;
    private boolean baza;
    int score;

    public JugadorLaEscoba(String playerName) {
        this.playerName = playerName;
        this.cartasEnMano = new ArrayList<>();
        this.cartasGanadas = new ArrayList<>();
        this.cantidadOros = 0;
        this.cantidadSietes = 0;
        this.escobas = 0;
        this.baza = false;
        this.score = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return this.score;
    }

    public ArrayList<Carta> getCartasEnMano() {
        return cartasEnMano;
    }

    public ArrayList<Carta> getCartasGanadas() {
        return cartasGanadas;
    }

    public void setCartasGanadas(ArrayList<Carta> cartas) {
        this.cartasGanadas.addAll(cartas);
    }

    public int getCantidadOros() {
        return cantidadOros;
    }

    public int getCantidadSietes() {
        return cantidadSietes;
    }

    public boolean getBaza() {
        return this.baza;
    }

    public void setCantidadOros(int cantidadOros) {
        this.cantidadOros = cantidadOros;
    }

    public void setCantidadSietes(int cantidadSietes) {
        this.cantidadSietes = cantidadSietes;
    }

    public void setBaza(boolean baza) {
        this.baza = baza;
    }

    public void recibirCartas(ArrayList<Carta> cartas) {
        this.cartasEnMano.addAll(cartas);
    }

    public void ganarCartas(ArrayList<Carta> cartas) {
        this.cartasGanadas.addAll(cartas);
        this.baza = true;
    }

    public void eliminarCartaEnMano(Carta carta) {
        cartasEnMano.removeIf(c -> c.getPalo().equals(carta.getPalo()) && c.getValor() == carta.getValor());
    }

    public int getescobas() { return escobas; }

    public void incrementarEscobas(ArrayList<Carta> mesa) {
        if (mesa.isEmpty()) {
            this.escobas++;
            View.showMessage(this.getPlayerName() + " hizo una escoba!");
        }
    }

    // Permite agregar puntos directos (por bonificaciones comparativas)
    public void agregarPuntos(int score) {
        this.score += score;
    }

    /**
     * Calcula la puntuación individual del jugador basada en:
     * - Puntos por escobas (cada escoba vale 1 punto)
     * - 1 punto por capturar el 7 de oros
     * La bonificación por mayor cantidad de cartas, oros y sietes se asignará al final de la ronda.
     */

    public int calcularPuntaje() {

        // Contadores para reglas de puntuación
        int cantidadCartas = cartasGanadas.size();
        int cantidadOros = 0;
        int cantidadSietes = 0;
        int puntos = 0;

        puntos += this.escobas;

        for (Carta carta : cartasGanadas) {
            if (carta.getPalo().equalsIgnoreCase("golden")) {
                cantidadOros++;
                if (carta.getValor() == 7) {
                    cantidadSietes++;
                    puntos += 1; // ✅ 1 punto si tiene el 7 de oro
                }
            }
        }

        // **Reglas de puntuación**:
        if (cantidadSietes > 2) puntos += 1; // ✅ 1 punto si tiene más sietes
        if (cantidadOros > 5) puntos += 1; // ✅ 1 punto si tiene más cartas de oros
        if (cantidadCartas > 20) puntos += 1; // ✅ 1 punto si capturó más cartas

        return puntos;
    }
}
