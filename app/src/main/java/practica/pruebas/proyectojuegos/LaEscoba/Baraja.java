package practica.pruebas.proyectojuegos.LaEscoba;

import java.util.ArrayList;
import java.util.Collections;

public class Baraja {
    private ArrayList<Carta> cartas;

    public Baraja() {
        this.cartas = new ArrayList<>();
        String[] palos = {"Oros", "Copas", "Espadas", "Bastos"};

        for (String palo : palos) {
            for (int i = 1; i <= 10; i++) {
                cartas.add(new Carta(palo, i));
            }
        }

        Collections.shuffle(cartas);
    }

    public ArrayList<Carta> repartir(int cantidad) {
        ArrayList<Carta> mano = new ArrayList<>();
        for (int i = 0; i < cantidad && !cartas.isEmpty(); i++) {
            mano.add(cartas.remove(0));
        }
        return mano;
    }

    public Carta repartirUna() {
        return cartas.isEmpty() ? null : cartas.remove(0);
    }

    public ArrayList<Carta> getCartas() {
        return cartas;
    }
}