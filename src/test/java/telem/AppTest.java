package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.json.JSONObject;
import org.junit.Test;

public class AppTest {

    @Test
    public void vehicleTypesExposeTheirAttributesAndAvailability() {
        Turismo turismo = new Turismo("TUR123", Categoria.NORMAL, 2020, 5, 420.5);
        Motocicleta motocicleta = new Motocicleta("MOT123", Categoria.ESPECIAL, 2021, 500);
        VehiculoIndustrial industrial =
            new VehiculoIndustrial("IND123", Categoria.SUPERIOR, 2022, 12000, 4);

        assertVehicleAttributes(turismo, "TUR123", Categoria.NORMAL, 2020);
        assertEquals(5, turismo.getPlazas());
        assertEquals(420.5, turismo.getCapacidadMaletero(), 0.001);

        assertVehicleAttributes(motocicleta, "MOT123", Categoria.ESPECIAL, 2021);
        assertEquals(500, motocicleta.getCilindrada());

        assertVehicleAttributes(industrial, "IND123", Categoria.SUPERIOR, 2022);
        assertEquals(12000, industrial.getCapacidadCarga());
        assertEquals(4, industrial.getNumeroEjes());

        assertFalse(turismo.isDisponible());
        turismo.setDisponible(true);
        assertTrue(turismo.isDisponible());
    }

    @Test
    public void eachVehicleTypeUsesThePriceForItsCategory() {
        Categoria[] categorias = {
            Categoria.NORMAL, Categoria.ESPECIAL, Categoria.SUPERIOR
        };
        int[][] preciosEsperados = {
            {50, 80, 100},
            {25, 40, 50},
            {75, 120, 150}
        };
        String[] tiposEsperados = {"Turismo", "Motocicleta", "VehiculoIndustrial"};

        for (int indiceTipo = 0; indiceTipo < preciosEsperados.length; indiceTipo++) {
            for (int indiceCategoria = 0; indiceCategoria < categorias.length; indiceCategoria++) {
                Vehiculo vehiculo = crearVehiculo(indiceTipo, categorias[indiceCategoria]);

                assertEquals(tiposEsperados[indiceTipo], vehiculo.getTipo());
                assertEquals(
                    vehiculo.getTipo() + " / " + categorias[indiceCategoria],
                    preciosEsperados[indiceTipo][indiceCategoria],
                    vehiculo.getPrecioBase()
                );
            }
        }
    }

    @Test
    public void rentalExposesItsDataAndCalculatesInclusiveRentalDays() {
        Vehiculo vehiculo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        Cliente cliente = new Cliente("Ana García", "12345678A");
        LocalDate inicio = LocalDate.of(2026, 5, 10);
        LocalDate fin = LocalDate.of(2026, 5, 13);
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, fin);

        assertEquals(vehiculo, alquiler.getVehiculo());
        assertEquals(cliente, alquiler.getCliente());
        assertEquals(inicio, alquiler.getFechaInicio());
        assertEquals(fin, alquiler.getFechaFin());
        assertEquals(50, alquiler.calcularPrecioAlquiler(inicio, inicio));
        assertEquals(200, alquiler.calcularPrecioAlquiler(inicio, fin));
    }

    @Test
    public void rentalCanBeAddedToTheSharedRegistry() {
        Alquiler[] alquileres = Alquiler.obtenerAlquileres();
        int indiceDisponible = 0;

        while (indiceDisponible < alquileres.length && alquileres[indiceDisponible] != null) {
            indiceDisponible++;
        }
        assertTrue("La lista de alquileres debe tener capacidad disponible",
            indiceDisponible < alquileres.length);

        Alquiler alquiler = new Alquiler(
            new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0),
            new Cliente("Ana García", "12345678A"),
            LocalDate.of(2026, 5, 10),
            LocalDate.of(2026, 5, 11)
        );
        Alquiler.agregarAlquiler(alquiler);

        assertSame(alquiler, alquileres[indiceDisponible]);
    }

    @Test
    public void clientAndVehicleCanBeSerializedAsJson() {
        Cliente cliente = new Cliente("Ana García", "12345678A");
        JSONObject clienteJson = new JSONObject(cliente.toString());

        assertEquals("Ana García", clienteJson.getString("nombre"));
        assertEquals("12345678A", clienteJson.getString("dni"));

        Turismo turismo = new Turismo("ABC123", Categoria.ESPECIAL, 2020, 5, 400.0);
        JSONObject vehiculoJson = new JSONObject(turismo.toString());

        assertEquals("Turismo", vehiculoJson.getString("tipo"));
        assertEquals("ABC123", vehiculoJson.getString("matricula"));
        assertEquals("ESPECIAL", vehiculoJson.getString("categoria"));
        assertEquals(2020, vehiculoJson.getInt("annio"));
        assertFalse(vehiculoJson.getBoolean("disponible"));
        assertEquals(5, vehiculoJson.getInt("plazas"));
        assertEquals(400.0, vehiculoJson.getDouble("capacidadMaletero"), 0.001);

        VehiculoIndustrial industrial =
            new VehiculoIndustrial("IND123", Categoria.SUPERIOR, 2022, 12000, 4);
        JSONObject industrialJson = new JSONObject(industrial.toString());
        assertEquals("VehiculoIndustrial", industrialJson.getString("tipo"));
        assertEquals(12000, industrialJson.getInt("capacidadCarga"));
        assertEquals(4, industrialJson.getInt("numeroEjes"));
    }

    @Test
    public void rentalCanBeSerializedWithItsAssociatedEntities() {
        Vehiculo vehiculo = new Motocicleta("MOT123", Categoria.NORMAL, 2021, 125);
        Cliente cliente = new Cliente("Luis Pérez", "87654321B");
        LocalDate inicio = LocalDate.of(2026, 6, 1);
        LocalDate fin = LocalDate.of(2026, 6, 2);
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, fin);

        JSONObject alquilerJson = new JSONObject(alquiler.toString());

        assertEquals("Luis Pérez", alquilerJson.getJSONObject("cliente").getString("nombre"));
        assertEquals("MOT123", alquilerJson.getJSONObject("vehiculo").getString("matricula"));
        assertEquals(inicio.toString(), alquilerJson.getString("fechaInicio"));
        assertEquals(fin.toString(), alquilerJson.getString("fechaFin"));
    }

    @Test
    public void vehicleFactoryRecreatesEachVehicleTypeFromJson() {
        Turismo turismo = new Turismo("TUR123", Categoria.NORMAL, 2020, 5, 420.5);
        turismo.setDisponible(true);
        Vehiculo turismoRestaurado = FactoriaVehiculos.getVehiculo(turismo.toString());

        assertTrue(turismoRestaurado instanceof Turismo);
        assertVehicleAttributes(turismoRestaurado, "TUR123", Categoria.NORMAL, 2020);
        assertTrue(turismoRestaurado.isDisponible());
        assertEquals(5, ((Turismo) turismoRestaurado).getPlazas());
        assertEquals(420.5, ((Turismo) turismoRestaurado).getCapacidadMaletero(), 0.001);

        Motocicleta motocicleta = new Motocicleta("MOT123", Categoria.ESPECIAL, 2021, 500);
        Vehiculo motocicletaRestaurada = FactoriaVehiculos.getVehiculo(motocicleta.toString());

        assertTrue(motocicletaRestaurada instanceof Motocicleta);
        assertVehicleAttributes(motocicletaRestaurada, "MOT123", Categoria.ESPECIAL, 2021);
        assertEquals(500, ((Motocicleta) motocicletaRestaurada).getCilindrada());

        VehiculoIndustrial industrial =
            new VehiculoIndustrial("IND123", Categoria.SUPERIOR, 2022, 12000, 4);
        Vehiculo industrialRestaurado = FactoriaVehiculos.getVehiculo(industrial.toString());

        assertTrue(industrialRestaurado instanceof VehiculoIndustrial);
        assertVehicleAttributes(industrialRestaurado, "IND123", Categoria.SUPERIOR, 2022);
        assertEquals(12000, ((VehiculoIndustrial) industrialRestaurado).getCapacidadCarga());
        assertEquals(4, ((VehiculoIndustrial) industrialRestaurado).getNumeroEjes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void vehicleFactoryRejectsUnknownVehicleTypes() {
        FactoriaVehiculos.getVehiculo("{\"tipo\":\"Barco\"}");
    }

    @Test
    public void rentalManagerFiltersVehiclesByType() {
        GestorAlquileres gestor = new GestorAlquileres();

        Vehiculo[] turismos = gestor.obtenerVehiculos("Turismo");

        assertEquals(3, turismos.length);
        for (Vehiculo vehiculo : turismos) {
            assertEquals("Turismo", vehiculo.getTipo());
        }
        assertEquals(0, gestor.obtenerVehiculos("Barco").length);
    }

    @Test
    public void rentalManagerStoresAddedRentals() {
        GestorAlquileres gestor = new GestorAlquileres();
        Vehiculo vehiculo = gestor.obtenerVehiculos("Turismo")[0];
        Cliente cliente = new Cliente("Clara Videncia", "001");
        Alquiler alquiler = new Alquiler(
            vehiculo,
            cliente,
            LocalDate.of(2026, 7, 1),
            LocalDate.of(2026, 7, 3)
        );

        gestor.agregarAlquiler(alquiler);

        assertSame(alquiler, gestor.obtenerAlquileres()[0]);
    }

    @Test
    public void rentalManagerPersistsAndRestoresRentalsAndTheirVehicles() throws Exception {
        Path fichero = Files.createTempFile("gestor-alquileres", ".json");
        try {
            GestorAlquileres gestor = new GestorAlquileres();
            Vehiculo vehiculo = gestor.obtenerVehiculos("Turismo")[0];
            Cliente cliente = new Cliente("Clara Videncia", "001");
            LocalDate inicio = LocalDate.of(2026, 7, 1);
            LocalDate fin = LocalDate.of(2026, 7, 3);
            gestor.agregarAlquiler(new Alquiler(vehiculo, cliente, inicio, fin));

            gestor.guardarDatos(fichero.toString());

            GestorAlquileres gestorRestaurado = new GestorAlquileres();
            gestorRestaurado.cargarDatos(fichero.toString());
            Alquiler alquilerRestaurado = gestorRestaurado.obtenerAlquileres()[0];

            assertEquals(1, countRentals(gestorRestaurado.obtenerAlquileres()));
            assertEquals("Clara Videncia", alquilerRestaurado.getCliente().getNombre());
            assertEquals("001", alquilerRestaurado.getCliente().getDni());
            assertEquals(inicio, alquilerRestaurado.getFechaInicio());
            assertEquals(fin, alquilerRestaurado.getFechaFin());
            assertEquals(vehiculo.getMatricula(), alquilerRestaurado.getVehiculo().getMatricula());
            assertEquals(vehiculo.getTipo(), alquilerRestaurado.getVehiculo().getTipo());
            assertSame(
                findVehicleByPlate(
                    gestorRestaurado.obtenerVehiculos(vehiculo.getTipo()),
                    vehiculo.getMatricula()
                ),
                alquilerRestaurado.getVehiculo()
            );
        } finally {
            Files.deleteIfExists(fichero);
        }
    }

    private int countRentals(Alquiler[] alquileres) {
        int cantidad = 0;
        for (Alquiler alquiler : alquileres) {
            if (alquiler != null) {
                cantidad++;
            }
        }
        return cantidad;
    }

    private Vehiculo findVehicleByPlate(Vehiculo[] vehiculos, String matricula) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getMatricula().equals(matricula)) {
                return vehiculo;
            }
        }
        return null;
    }

    private void assertVehicleAttributes(
        Vehiculo vehiculo, String matricula, Categoria categoria, int annio
    ) {
        assertEquals(matricula, vehiculo.getMatricula());
        assertEquals(categoria, vehiculo.getCategoria());
        assertEquals(annio, vehiculo.getAnnio());
    }

    private Vehiculo crearVehiculo(int tipo, Categoria categoria) {
        switch (tipo) {
            case 0:
                return new Turismo("ABC123", categoria, 2020, 5, 400.0);
            case 1:
                return new Motocicleta("ABC123", categoria, 2020, 125);
            case 2:
                return new VehiculoIndustrial("ABC123", categoria, 2020, 1000, 3);
            default:
                throw new IllegalArgumentException("Tipo de vehículo no soportado: " + tipo);
        }
    }
}
