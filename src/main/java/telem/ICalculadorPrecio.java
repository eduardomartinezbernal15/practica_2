package telem;

import java.time.LocalDate;

public interface ICalculadorPrecio {
    int calcularPrecioAlquiler(
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Vehiculo vehiculo
    );
}