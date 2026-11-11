package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.time.LocalDate;
import org.json.JSONObject;
import org.junit.Test;

public class AlquilerTest {

    private final Vehiculo vehiculo = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 400);
    private final Cliente cliente = new Cliente("Ana", "12345678A");
    private final LocalDate inicio = LocalDate.of(2026, 10, 9);

    @Test
    public void conservaLasReferenciasYFechas() {
        LocalDate fin = inicio.plusDays(3);
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, fin);

        assertSame(vehiculo, alquiler.getVehiculo());
        assertSame(cliente, alquiler.getCliente());
        assertEquals(inicio, alquiler.getFechaInicio());
        assertEquals(fin, alquiler.getFechaFin());
    }

    @Test
    public void calculaPrecioIncluyendoDiaInicialYFinal() {
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio);

        assertEquals(50, alquiler.calcularPrecioAlquiler(inicio, inicio));
        assertEquals(200, alquiler.calcularPrecioAlquiler(inicio, inicio.plusDays(3)));
        assertEquals(100, alquiler.calcularPrecioAlquiler(LocalDate.of(2026, 12, 31), LocalDate.of(2027, 1, 1)));
    }

    @Test
    public void serializaLosObjetosAnidadosYLasFechas() {
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio.plusDays(3));
        JSONObject json = new JSONObject(alquiler.toString());

        assertEquals("2026-10-09", json.getString("fechaInicio"));
        assertEquals("2026-10-12", json.getString("fechaFin"));
        assertEquals("Ana", json.getJSONObject("cliente").getString("nombre"));
        assertEquals("12345678A", json.getJSONObject("cliente").getString("dni"));
        assertEquals("Turismo", json.getJSONObject("vehiculo").getString("tipo"));
        assertEquals("1234ABC", json.getJSONObject("vehiculo").getString("matricula"));
    }

    @Test
    public void agregaAlquilerAlRegistro() {
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio);
        Alquiler[] registro = Alquiler.obtenerAlquileres();
        int posicion = 0;
        while (posicion < registro.length && registro[posicion] != null) {
            posicion++;
        }

        Alquiler.agregarAlquiler(alquiler);

        assertSame(alquiler, registro[posicion]);
    }
}
