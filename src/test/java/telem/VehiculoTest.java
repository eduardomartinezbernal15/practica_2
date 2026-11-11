package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Test;

public class VehiculoTest {

    @Test
    public void preciosBasePorTipoYCategoria() {
        Categoria[] categorias = {Categoria.NORMAL, Categoria.ESPECIAL, Categoria.SUPERIOR};
        int[] preciosTurismo = {50, 80, 100};
        int[] preciosMoto = {25, 40, 50};
        int[] preciosIndustrial = {75, 120, 150};

        for (int i = 0; i < categorias.length; i++) {
            assertEquals(preciosTurismo[i], new Turismo("T" + i, categorias[i], 2020, 5, 400).getPrecioBase());
            assertEquals(preciosMoto[i], new Motocicleta("M" + i, categorias[i], 2021, 125).getPrecioBase());
            assertEquals(preciosIndustrial[i], new VehiculoIndustrial("I" + i, categorias[i], 2022, 1000, 3).getPrecioBase());
        }
    }

    @Test
    public void turismoConservaDatosDisponibilidadYJson() {
        Turismo turismo = new Turismo("1234ABC", Categoria.ESPECIAL, 2020, 5, 450.5);
        assertEquals("1234ABC", turismo.getMatricula());
        assertEquals(Categoria.ESPECIAL, turismo.getCategoria());
        assertEquals(2020, turismo.getAnnio());
        assertEquals(5, turismo.getPlazas());
        assertEquals(450.5, turismo.getCapacidadMaletero(), 0.001);
        assertEquals("Turismo", turismo.getTipo());
        assertFalse(turismo.isDisponible());

        turismo.setDisponible(true);
        assertTrue(turismo.isDisponible());

        JSONObject json = new JSONObject(turismo.toString());
        assertEquals("Turismo", json.getString("tipo"));
        assertEquals("1234ABC", json.getString("matricula"));
        assertEquals("ESPECIAL", json.getString("categoria"));
        assertEquals(2020, json.getInt("annio"));
        assertEquals(5, json.getInt("plazas"));
        assertEquals(450.5, json.getDouble("capacidadMaletero"), 0.001);
        assertEquals(80, json.getInt("precioBase"));
        assertTrue(json.getBoolean("disponible"));
    }

    @Test
    public void motocicletaConservaDatosYLosSerializa() {
        Motocicleta moto = new Motocicleta("5678DEF", Categoria.NORMAL, 2021, 125);
        assertEquals("5678DEF", moto.getMatricula());
        assertEquals(Categoria.NORMAL, moto.getCategoria());
        assertEquals(2021, moto.getAnnio());
        assertEquals(125, moto.getCilindrada());
        assertEquals("Motocicleta", moto.getTipo());

        JSONObject json = new JSONObject(moto.toString());
        assertEquals("Motocicleta", json.getString("tipo"));
        assertEquals("5678DEF", json.getString("matricula"));
        assertEquals(125, json.getInt("cilindrada"));
        assertEquals(25, json.getInt("precioBase"));
    }

    @Test
    public void vehiculoIndustrialConservaDatosYLosSerializa() {
        VehiculoIndustrial industrial = new VehiculoIndustrial("9012GHI", Categoria.SUPERIOR, 2022, 1000, 3);
        assertEquals("9012GHI", industrial.getMatricula());
        assertEquals(Categoria.SUPERIOR, industrial.getCategoria());
        assertEquals(2022, industrial.getAnnio());
        assertEquals(1000, industrial.getCapacidadCarga());
        assertEquals(3, industrial.getNumeroEjes());
        assertEquals("VehiculoIndustrial", industrial.getTipo());

        JSONObject json = new JSONObject(industrial.toString());
        assertEquals("VehiculoIndustrial", json.getString("tipo"));
        assertEquals("9012GHI", json.getString("matricula"));
        assertEquals(1000, json.getInt("capacidadCarga"));
        assertEquals(3, json.getInt("numeroEjes"));
        assertEquals(150, json.getInt("precioBase"));
    }
}
