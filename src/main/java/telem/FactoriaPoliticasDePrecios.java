package telem;

public class FactoriaPoliticasDePrecios {

    public static ICalculadorPrecio crear(PoliticasDePrecios politica) {
        if (politica == null) {
            throw new IllegalArgumentException("La política no puede ser null");
        }

        switch (politica) {
            case Normal:
                return new CalculadorPrecioNormal();
            case TemporadaAlta:
                return new CalculadorPrecioTemporadaAlta();
            case ClienteFrecuente:
                return new CalculadorPrecioClienteFrecuente();
            case Oferton:
                return new CalculadorPrecioOferton();
            default:
                throw new IllegalArgumentException("Política desconocida: " + politica);
        }
    }
}
