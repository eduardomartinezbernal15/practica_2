package telem;

import org.json.JSONObject;

public class VehiculoIndustrial extends Vehiculo {

    private int capacidadCarga;
    private int numeroEjes;

    public VehiculoIndustrial(String matricula, Categoria categoria, int annio,
        int capacidadCarga, int numeroEjes
    ) {
        super(matricula, categoria, annio);
        this.capacidadCarga = capacidadCarga;
        this.numeroEjes = numeroEjes;
    }

    public int getCapacidadCarga() {
        return capacidadCarga;
    }

    public int getNumeroEjes() {
        return numeroEjes;
    }

    @Override
    public String getTipo() {
        return "VehiculoIndustrial";
    }

    @Override
    public int getPrecioBase() {
        switch (getCategoria()) {
            case NORMAL: return 75;
            case ESPECIAL: return 120;
            case SUPERIOR: return 150;
            default: throw new IllegalStateException("Categoría desconocida: " + getCategoria());
        }
    }

    @Override 
    public String toString() {
        return new JSONObject(this).toString();
    }
}
