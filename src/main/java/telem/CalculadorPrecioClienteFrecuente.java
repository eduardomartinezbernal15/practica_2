package telem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculadorPrecioClienteFrecuente implements ICalculadorPrecio {

    @Override
    public int calcularPrecioAlquiler(LocalDate inicio, LocalDate fin, Vehiculo vehiculo) {
        int dias = (int) ChronoUnit.DAYS.between(inicio, fin) + 1;
        int primerosTres = Math.min(dias, 3);
        int restantes = dias - primerosTres;
        int precioDiario = vehiculo.getPrecioBase();

        double total = primerosTres * precioDiario * 0.8
                     + restantes * precioDiario * 0.6;
        return (int) Math.round(total);
    }
}
