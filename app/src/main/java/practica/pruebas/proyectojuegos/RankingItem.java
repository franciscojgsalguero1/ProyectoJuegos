package practica.pruebas.proyectojuegos;

public class RankingItem {
    private int posicion;
    private String nombre;
    private int puntuacion;

    public RankingItem(int posicion, String nombre, int puntuacion) {
        this.posicion = posicion;
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }
}
