package practica.pruebas.proyectojuegos.LaEscoba;

import java.util.ArrayList;

import practica.pruebas.proyectojuegos.JugadorGeneral;

public class JugadorLaEscoba extends JugadorGeneral {
    //private String nombre;
    private ArrayList<Carta> cartasEnMano;
    private ArrayList<Carta> cartasGanadas;
    private int puntos;
    private int escobas;
    private boolean baza;

    public JugadorLaEscoba(String nombre) {
        super(nombre);
        this.cartasEnMano = new ArrayList<>();
        this.cartasGanadas = new ArrayList<>();
        this.puntos = 0;
        this.escobas = 0;
        this.baza = false;
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

    public boolean getBaza() {
        return baza;
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

    public void incrementarEscobas(ArrayList<Carta> mesa) {
        if (mesa.isEmpty()) {
            this.escobas++;
            System.out.println(this.getNombre() + " hizo una escoba!");
        }
    }

    // Permite agregar puntos directos (por bonificaciones comparativas)
    public void agregarPuntos(int puntos) {
        this.puntos += puntos;
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

        for (Carta carta : cartasGanadas) {
            if (carta.getPalo().equalsIgnoreCase("golden")) {
                cantidadOros++;
            }
            if (carta.getValor() == 7) {
                cantidadSietes++;
                if (carta.getPalo().equalsIgnoreCase("golden")) {
                    this.puntos += 1; // ✅ 1 punto si tiene el 7 de oro
                }
            }
        }

        // **Reglas de puntuación**:
        if (cantidadSietes > 0) this.puntos += 1; // ✅ 1 punto si tiene más sietes
        if (cantidadOros > 0) this.puntos += 1; // ✅ 1 punto si tiene más cartas de oros
        if (cantidadCartas > 0) this.puntos += 1; // ✅ 1 punto si capturó más cartas

        return this.puntos;
    }

}
