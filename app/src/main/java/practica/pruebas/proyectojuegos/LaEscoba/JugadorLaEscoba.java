package practica.pruebas.proyectojuegos.LaEscoba;

import java.util.ArrayList;

import practica.pruebas.proyectojuegos.JugadorGeneral;

public class JugadorLaEscoba extends JugadorGeneral {
    private String nombre;
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
}
