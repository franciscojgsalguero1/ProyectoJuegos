package practica.pruebas.proyectojuegos.resources;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class JugadorGeneral {
    private String nombre;
    private int puntuacion;

    // Constructor sin puntuación
    public JugadorGeneral() {
        this.nombre = "";
        this.puntuacion = 0;
    }

    // Constructor con puntuación predeterminada
    public JugadorGeneral(String nombre) {
        this.nombre = nombre;
        this.puntuacion = 0;
    }

    // Getter y Setter para el nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }

    // Getter y Setter para la puntuación
    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        if (puntuacion < 0) {
            throw new IllegalArgumentException("La puntuación no puede ser negativa");
        }
        this.puntuacion = puntuacion;
    }

    // Metodo para añadir puntos
    public void agregarPuntos(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("Los puntos añadidos no pueden ser negativos");
        }
        this.puntuacion += puntos;
    }

    // Metodo para reiniciar la puntuación
    public void reiniciarPuntuacion() {
        this.setPuntuacion(0);
    }

    // Metodo estático para pedir el nombre del jugador
    public static void pedirNombre(Context context, NombreCallback callback) {

        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Introduce tu nombre");

        new AlertDialog.Builder(context)
                .setTitle("Nombre del jugador")
                .setMessage("Introduce tu nombre para comenzar:")
                .setView(input)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    String nombre = input.getText().toString().trim();
                    if (!nombre.isEmpty()) {
                        callback.onNombreIntroducido(new JugadorGeneral(nombre)); // Devolver el jugador
                    } else {
                        Toast.makeText(context, "El nombre no puede estar vacío.", Toast.LENGTH_SHORT).show();
                        pedirNombre(context, callback); // Volver a preguntar
                    }
                })
                .setCancelable(false)
                .show();
    }

    // Metodo toString para mostrar la información del jugador
    @Override
    public String toString() {
        return "Jugador: " + this.nombre + ", Puntuación: " + this.puntuacion;
    }

    // Callback para devolver el jugador creado
    public interface NombreCallback {
        void onNombreIntroducido(JugadorGeneral jugadorGeneral);
    }
}
