package telem;

import java.time.LocalDate;

import org.json.JSONObject;

/**
 * Modela un alquiler de un vehículo por parte de un cliente, con fechas de inicio y fin.
 * Almacena los alquileres realizados en un array estático y proporciona métodos para calcular el precio del alquiler.
 * Alquiler
 */
public class Alquiler {
    
    private Vehiculo vehiculo;
    private Cliente cliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private ICalculadorPrecio calculadorPrecio = new CalculadorPrecioNormal();

    private static Alquiler[] alquileres = new Alquiler[1000];

    public static final int PRECIO_BASE = 50;
    private static int contadorAlquileres = 0;

    public Alquiler(Vehiculo vehiculo, Cliente cliente, LocalDate fechaInicio, LocalDate fechaFin) {
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Alquiler(String jsonString) {
    this(new JSONObject(jsonString));
}

    private Alquiler(JSONObject json) {
    this(
        FactoriaVehiculos.getVehiculo(json.getJSONObject("vehiculo")),
        new Cliente(json.getJSONObject("cliente").toString()),
        LocalDate.parse(json.getString("fechaInicio")),
        LocalDate.parse(json.getString("fechaFin"))
    );
    }

    public static Alquiler[] obtenerAlquileres() {
        return alquileres; 
    }

    public static void agregarAlquiler(Alquiler alquiler) {
        if (contadorAlquileres < alquileres.length) {
            alquileres[contadorAlquileres] = alquiler;
            contadorAlquileres++;
        } else {
            System.out.println("No se pueden agregar más alquileres. Capacidad máxima alcanzada.");
        }
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setCalculadorPrecio(ICalculadorPrecio calculadorPrecio) {
        if (calculadorPrecio == null) {
            throw new IllegalArgumentException("El calculador no puede ser null");
        }
        this.calculadorPrecio = calculadorPrecio;
    }

    public void setPolitica(PoliticasDePrecios politica) {
        setCalculadorPrecio(FactoriaPoliticasDePrecios.crear(politica));
    }

    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin) {
        return calculadorPrecio.calcularPrecioAlquiler(fechaInicio, fechaFin, this.vehiculo);
    }

    @Override 
    public String toString() {
        return new JSONObject(this).toString();
    }

    public static void main(String[] args) {
        // Crear instancias de Vehiculo y Cliente
        Vehiculo vehiculo1 = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        Cliente cliente1 = new Cliente("Juan Pérez", "123456789");
        LocalDate hoy = LocalDate.now();
        Alquiler alquiler1 = new Alquiler(vehiculo1, cliente1, hoy, hoy.plusDays(3));
        System.out.println(alquiler1.toString());
        System.out.println("Precio normal: " + alquiler1.calcularPrecioAlquiler(hoy, hoy.plusDays(3)));
        alquiler1.setCalculadorPrecio(new CalculadorPrecioClienteFrecuente());
        System.out.println("Precio cliente frecuente: " + alquiler1.calcularPrecioAlquiler(hoy, hoy.plusDays(3)));
        alquiler1.setPolitica(PoliticasDePrecios.Oferton);
        System.out.println("Precio ofertón: " + alquiler1.calcularPrecioAlquiler(hoy, hoy.plusDays(3)));
        System.out.println("--------------------------------------------------------");

        Vehiculo vehiculo2 = new VehiculoIndustrial("ABC123", Categoria.NORMAL, 2020, 1000, 3);
        Alquiler alquiler2 = new Alquiler(vehiculo2, cliente1, hoy, hoy.plusDays(3));

        String json1 = alquiler1.toString();
        Alquiler recuperado = new Alquiler(json1);

        System.out.println("Alquiler en JSON: " + json1);
        System.out.println("Cliente recuperado: " + recuperado.getCliente().getNombre());
        System.out.println("Tipo de vehículo recuperado: " + recuperado.getVehiculo().getTipo());
        System.out.println("Fecha de fin recuperada: " + recuperado.getFechaFin());
    }
}
