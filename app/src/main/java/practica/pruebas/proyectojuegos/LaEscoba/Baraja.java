package practica.pruebas.proyectojuegos.LaEscoba;

import java.util.ArrayList;
import java.util.Collections;

public class Baraja {
    private ArrayList<Carta> barajaCartas;

    public Baraja() {
        this.barajaCartas = new ArrayList<>();
        String[] palos = {"clubs", "goblets", "golden", "swords"};

        for (String palo : palos) {
            for (int i = 1; i <= 12; i++) {
                if (i != 8 && i != 9) {
                    barajaCartas.add(new Carta(palo, i));
                }
            }
        }
        Collections.shuffle(barajaCartas);
    }

    public ArrayList<Carta> repartir(int cantidad) {
        ArrayList<Carta> repartirCartas = new ArrayList<>();
        for (int i = 0; i < cantidad && !barajaCartas.isEmpty(); i++) {
            repartirCartas.add(barajaCartas.remove(0));
        }
        return repartirCartas;
    }

    public Carta repartirUna() {
        return barajaCartas.isEmpty() ? null : barajaCartas.remove(0);
    }

    public ArrayList<Carta> getBarajaCartas() {
        return barajaCartas;
    }
}