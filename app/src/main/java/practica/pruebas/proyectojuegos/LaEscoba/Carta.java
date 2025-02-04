package practica.pruebas.proyectojuegos.LaEscoba;

import practica.pruebas.proyectojuegos.R;

public class Carta {
    private String palo;
    private int valor;

    public Carta(String palo, int valor) {
        this.palo = palo;
        this.valor = valor;
    }

    public String getPalo() {
        return palo;
    }

    public int getValor() {
        return valor;
    }

    // Metodo para obtener el ID del recurso de la imagen
    public int getImageResourceId() throws NoSuchFieldException, IllegalAccessException {
        String nombreImagen = "card_" + valor + "_" + palo.toLowerCase(); // Ejemplo: card_1_golden
        return R.drawable.class.getField(nombreImagen).getInt(null);
    }

    @Override
    public String toString() {
        return valor + " de " + palo;
    }
}
