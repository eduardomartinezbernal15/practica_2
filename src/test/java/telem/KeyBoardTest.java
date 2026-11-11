package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.After;
import org.junit.Test;

public class KeyBoardTest {

    private final InputStream entradaOriginal = System.in;

    private void introducir(String texto) {
        System.setIn(new ByteArrayInputStream(texto.getBytes(StandardCharsets.UTF_8)));
    }

    @After
    public void restaurarEntrada() {
        System.setIn(entradaOriginal);
    }

    @Test
    public void leeByteValidoYDevuelveErrorSiNoEsByte() {
        introducir("-12\n");
        assertEquals(-12, KeyBoard.readByte());
        introducir("128\n");
        assertEquals(KeyBoard.BYTE_ERR, KeyBoard.readByte());
    }

    @Test
    public void leeShortValidoYDevuelveErrorSiNoEsShort() {
        introducir("-1234\n");
        assertEquals(-1234, KeyBoard.readShort());
        introducir("40000\n");
        assertEquals(KeyBoard.SHORT_ERR, KeyBoard.readShort());
    }

    @Test
    public void leeIntValidoYDevuelveErrorSiNoEsEntero() {
        introducir("-123456\n");
        assertEquals(-123456, KeyBoard.readInt());
        introducir("abc\n");
        assertEquals(KeyBoard.INT_ERR, KeyBoard.readInt());
    }

    @Test
    public void leeDoubleValidoYDevuelveErrorSiNoEsNumero() {
        introducir("12.5\n");
        assertEquals(12.5, KeyBoard.readDouble(), 0.0001);
        introducir("abc\n");
        assertEquals(KeyBoard.DOUBLE_ERR, KeyBoard.readDouble(), 0);
    }

    @Test
    public void leeFloatValidoYDevuelveErrorSiNoEsNumero() {
        introducir("-3.25\n");
        assertEquals(-3.25, KeyBoard.readFloat(), 0.0001);
        introducir("abc\n");
        assertEquals(KeyBoard.FLOAT_ERR, KeyBoard.readFloat(), 0);
    }

    @Test
    public void leePrimerCaracterYDevuelveErrorAlFinalDeLaEntrada() {
        introducir("Hola\n");
        assertEquals('H', KeyBoard.readChar());
        introducir("");
        assertEquals(KeyBoard.CHAR_ERR, KeyBoard.readChar());
    }

    @Test
    public void leeLineaYDevuelveNullAlFinalDeLaEntrada() {
        introducir("Hola mundo\n");
        assertEquals("Hola mundo", KeyBoard.readString());
        introducir("");
        assertNull(KeyBoard.readString());
    }
}
