package practica.pruebas.proyectojuegos.LaEscoba;

import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> cartasEnMano;
    private ArrayList<Carta> cartasGanadas;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.cartasEnMano = new ArrayList<>();
        this.cartasGanadas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
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
}
