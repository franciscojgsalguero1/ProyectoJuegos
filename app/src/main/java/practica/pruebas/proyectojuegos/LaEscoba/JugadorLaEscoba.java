package practica.pruebas.proyectojuegos.LaEscoba;

import java.util.ArrayList;

import practica.pruebas.proyectojuegos.JugadorGeneral;

public class JugadorLaEscoba extends JugadorGeneral {
    private String nombre;
    int puntos = 0;
    private ArrayList<Carta> cartasEnMano;
    private ArrayList<Carta> cartasGanadas;

    public JugadorLaEscoba(String nombre) {
        super(nombre);
        this.cartasEnMano = new ArrayList<>();
        this.cartasGanadas = new ArrayList<>();
    }

    public ArrayList<Carta> getCartasEnMano() {
        return cartasEnMano;
    }

    public void recibirCartas(ArrayList<Carta> cartas) {
        this.cartasEnMano.addAll(cartas);
    }

    public void ganarCartas(ArrayList<Carta> cartas) {
        this.cartasGanadas.addAll(cartas);
    }

    public void eliminarCartaEnMano(Carta carta) {
        this.cartasEnMano.remove(carta);
    }

    public int calcularPuntaje() {

        // Contadores para reglas de puntuación
        int cantidadCartas = cartasGanadas.size();
        int cantidadOros = 0;
        int cantidadSietes = 0;
        boolean tieneSieteOros = false;

        for (Carta carta : cartasGanadas) {
            if (carta.getPalo().equalsIgnoreCase("Oros")) {
                cantidadOros++;
            }
            if (carta.getValor() == 7) {
                cantidadSietes++;
                if (carta.getPalo().equalsIgnoreCase("Oros")) {
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
    }

}
