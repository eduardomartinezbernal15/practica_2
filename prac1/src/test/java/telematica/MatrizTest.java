package telematica;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


/**
 * Pruebas de los constructores de Matriz.
 */
public class MatrizTest {

    /*
     * Comprobamos el caso más sencillo:
     * el constructor recibe una matriz rectangular.
     */
    @Test
    public void constructorMatrizRectangular() {

        double[][] datos = {
            { 1.0, 2.0, 3.0 },
            { 4.0, 5.0, 6.0 }
        };

        Matriz m = new Matriz(datos);

        assertArrayEquals(
                new int[] { 2, 3 },
                m.getDimension());

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(datos[i][j], m.getElemento(i, j), 1e-9);
            }
        }        
    }

    @Test
    public void sumaMatricesElementoAElemento() {
        Matriz primera = new Matriz(new double[][] {
            { 1.0, 2.0 },
            { 3.0, 4.0 }
        });
        Matriz segunda = new Matriz(new double[][] {
            { 5.0, 6.0 },
            { 7.0, 8.0 }
        });

        Matriz resultado = primera.suma(segunda);

        assertArrayEquals(new int[] { 2, 2 }, resultado.getDimension());
        assertEquals(6.0, resultado.getElemento(0, 0), 1e-9);
        assertEquals(8.0, resultado.getElemento(0, 1), 1e-9);
        assertEquals(10.0, resultado.getElemento(1, 0), 1e-9);
        assertEquals(12.0, resultado.getElemento(1, 1), 1e-9);
    }

    @Test
    public void sumaEstaticoMatricesElementoAElemento() {
        Matriz primera = new Matriz(new double[][] {
            { 2.0, 4.0 },
            { 6.0, 8.0 }
        });
        Matriz segunda = new Matriz(new double[][] {
            { 1.0, 3.0 },
            { 5.0, 7.0 }
        });

        Matriz resultado = Matriz.suma(primera, segunda);

        assertArrayEquals(new int[] { 2, 2 }, resultado.getDimension());
        assertEquals(3.0, resultado.getElemento(0, 0), 1e-9);
        assertEquals(7.0, resultado.getElemento(0, 1), 1e-9);
        assertEquals(11.0, resultado.getElemento(1, 0), 1e-9);
        assertEquals(15.0, resultado.getElemento(1, 1), 1e-9);
    }

    @Test
    public void traspuestaIntercambiaFilasYColumnas() {
        Matriz original = new Matriz(new double[][] {
            { 1.0, 2.0, 3.0 },
            { 4.0, 5.0, 6.0 }
        });

        Matriz resultado = original.traspuesta();

        assertArrayEquals(new int[] { 3, 2 }, resultado.getDimension());
        assertEquals(1.0, resultado.getElemento(0, 0), 1e-9);
        assertEquals(4.0, resultado.getElemento(0, 1), 1e-9);
        assertEquals(2.0, resultado.getElemento(1, 0), 1e-9);
        assertEquals(5.0, resultado.getElemento(1, 1), 1e-9);
        assertEquals(3.0, resultado.getElemento(2, 0), 1e-9);
        assertEquals(6.0, resultado.getElemento(2, 1), 1e-9);
    }

    /*
     * Si las filas no tienen la misma longitud,
     * la matriz toma como número de columnas la longitud
     * de la fila más larga y completa con ceros.
     */
    @Test
    public void constructorRellenaConCeros() {

        double[][] datos = {
            { 1.0, 2.0, 3.0 },
            { 4.0, 5.0 },
            { 6.0 }
        };

        Matriz m = new Matriz(datos);

        assertArrayEquals(
                new int[] { 3, 3 },
                m.getDimension());

        assertEquals(4.0, m.getElemento(1, 0), 1e-9);
        assertEquals(5.0, m.getElemento(1, 1), 1e-9);
        assertEquals(0.0, m.getElemento(1, 2), 1e-9);

        assertEquals(6.0, m.getElemento(2, 0), 1e-9);
        assertEquals(0.0, m.getElemento(2, 1), 1e-9);
        assertEquals(0.0, m.getElemento(2, 2), 1e-9);
    }

    /*
     * El constructor debe copiar los datos recibidos.
     * Modificar posteriormente el array original no debe
     * modificar la matriz.
     */
    @Test
    public void constructorHaceUnaCopiaDeLosDatos() {

        double[][] datos = {
            { 1.0, 2.0 },
            { 3.0, 4.0 }
        };

        Matriz m = new Matriz(datos);

        datos[0][0] = 100.0;

        assertEquals(1.0, m.getElemento(0, 0), 1e-9);
    }

    /*
     * Comprobamos que el constructor lanza una excepción si recibe
     * un array null o vacío.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorMatrizNull() {
        new Matriz(null);
    }  
    
    /*
     * Comprobamos que el constructor lanza una excepción si recibe
     * un array vacío.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorMatrizVacia() {
        new Matriz(new double[0][0]);
    }

    /*
     * Comprobamos que el constructor lanza una excepción si recibe
     * todos los arrays con filas vacías.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorMatrizConFilasVacias() {
        double[][] datos = {
            { },
            { }
        };

        new Matriz(datos);
    }

    /*
     * Comprobamos que el constructor lanza una excepción si recibe
     * un array con filas null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorMatrizConFilasNull() {
        double[][] datos = {
            { 1.0, 2.0 },
            null
        };

        new Matriz(datos);
    }
    
}
