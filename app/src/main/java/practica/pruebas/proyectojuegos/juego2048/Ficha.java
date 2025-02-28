package practica.pruebas.proyectojuegos.juego2048;

import practica.pruebas.proyectojuegos.R;

public class Ficha {

    private int valor;
    private int fila;
    private int columna;
    private boolean merged;

    public Ficha(int valor, int fila, int columna) {
        this.valor = Math.max(0, valor); // Evita valores negativos
        this.fila = fila;
        this.columna = columna;
        this.merged = false;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = Math.max(0, valor); // Evita valores negativos
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    /**
     * Fusiona esta ficha con otra, si es posible.
     * Retorna `true` si la fusión fue exitosa.
     */
    public boolean fusionar(Ficha otraFicha) {
        if (!this.merged && !otraFicha.isMerged() && this.valor == otraFicha.getValor() && this.valor > 0) {
            this.valor *= 2;
            this.merged = true;
            otraFicha.setValor(0);
            return true; // Indica que hubo fusión
        }
        return false;
    }

    public void reiniciarFusion() {
        this.merged = false;
    }

    /**
     * Devuelve el color correspondiente según el valor de la ficha.
     */
    public int getColor() {
        switch (valor) {
            case 2: return R.color.tile_2;
            case 4: return R.color.tile_4;
            case 8: return R.color.tile_8;
            case 16: return R.color.tile_16;
            case 32: return R.color.tile_32;
            case 64: return R.color.tile_64;
            case 128: return R.color.tile_128;
            case 256: return R.color.tile_256;
            case 512: return R.color.tile_512;
            case 1024: return R.color.tile_1024;
            case 2048: return R.color.tile_2048;
            default: return R.color.tile_high; // Color para valores mayores a 2048
        }
    }
}