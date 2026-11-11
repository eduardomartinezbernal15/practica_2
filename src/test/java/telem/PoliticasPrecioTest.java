package telem;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import org.junit.Test;

public class PoliticasPrecioTest {

    private final Vehiculo vehiculo = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 400);
    private final LocalDate inicio = LocalDate.of(2026, 1, 1);

    @Test
    public void calculaLasCuatroPoliticas() {
        ICalculadorPrecio normal = new CalculadorPrecioNormal();
        ICalculadorPrecio alta = new CalculadorPrecioTemporadaAlta();
        ICalculadorPrecio frecuente = new CalculadorPrecioClienteFrecuente();
        ICalculadorPrecio oferton = new CalculadorPrecioOferton();

        assertEquals(400, normal.calcularPrecioAlquiler(inicio, inicio.plusDays(7), vehiculo));
        assertEquals(800, alta.calcularPrecioAlquiler(inicio, inicio.plusDays(7), vehiculo));
        assertEquals(120, frecuente.calcularPrecioAlquiler(inicio, inicio.plusDays(2), vehiculo));
        assertEquals(150, frecuente.calcularPrecioAlquiler(inicio, inicio.plusDays(3), vehiculo));
        assertEquals(245, oferton.calcularPrecioAlquiler(inicio, inicio.plusDays(6), vehiculo));
        assertEquals(252, oferton.calcularPrecioAlquiler(inicio, inicio.plusDays(7), vehiculo));
    }

    @Test
    public void alquilerDelegaElCalculoEnLaPoliticaElegida() {
        Alquiler alquiler = new Alquiler(
            vehiculo, new Cliente("Ana", "12345678A"), inicio, inicio.plusDays(3));

        assertEquals(200, alquiler.calcularPrecioAlquiler(inicio, inicio.plusDays(3)));
        alquiler.setCalculadorPrecio(new CalculadorPrecioClienteFrecuente());
        assertEquals(150, alquiler.calcularPrecioAlquiler(inicio, inicio.plusDays(3)));
    }

    @Test
    public void factoriaPermiteElegirLasCuatroPoliticas() {
        Alquiler alquiler = new Alquiler(
            vehiculo, new Cliente("Ana", "12345678A"), inicio, inicio.plusDays(7));

        alquiler.setPolitica(PoliticasDePrecios.Normal);
        assertEquals(400, alquiler.calcularPrecioAlquiler(inicio, inicio.plusDays(7)));

        alquiler.setPolitica(PoliticasDePrecios.TemporadaAlta);
        assertEquals(800, alquiler.calcularPrecioAlquiler(inicio, inicio.plusDays(7)));

        alquiler.setPolitica(PoliticasDePrecios.ClienteFrecuente);
        assertEquals(270, alquiler.calcularPrecioAlquiler(inicio, inicio.plusDays(7)));

        alquiler.setPolitica(PoliticasDePrecios.Oferton);
        assertEquals(252, alquiler.calcularPrecioAlquiler(inicio, inicio.plusDays(7)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void factoriaRechazaUnaPoliticaNula() {
        FactoriaPoliticasDePrecios.crear(null);
    }
}
