package telem;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Test;

public class ClienteTest {

    @Test
    public void conservaLosDatosYLosSerializa() {
        Cliente cliente = new Cliente("Ana Pérez", "12345678A");

        assertEquals("Ana Pérez", cliente.getNombre());
        assertEquals("12345678A", cliente.getDni());

        JSONObject json = new JSONObject(cliente.toString());
        assertEquals("Ana Pérez", json.getString("nombre"));
        assertEquals("12345678A", json.getString("dni"));
    }
}
