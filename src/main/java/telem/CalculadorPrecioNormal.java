package telem;

import java.time.LocalDate;

import java.time.temporal.ChronoUnit;


public class CalculadorPrecioNormal implements ICalculadorPrecio {
    
    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        // +1 para incluir el dia de inicio
        int dias = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        return dias * vehiculo.getPrecioBase();
    }


}