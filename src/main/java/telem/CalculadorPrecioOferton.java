package telem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculadorPrecioOferton implements ICalculadorPrecio {

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        // +1 para incluir el dia de inicio
        int dias = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;

        double total = dias * vehiculo.getPrecioBase() * 0.7;

            if (dias > 7) {
                total = total * 0.9;
            }

        return (int) Math.round(total);
    }




}
