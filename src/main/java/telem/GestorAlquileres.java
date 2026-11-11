package telem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;

public class GestorAlquileres {
    
    private int contadorAlquileres = 0;
    private Alquiler[] alquileres = new Alquiler[1000];

    // Para simplificar, vamos a definir un array de clientes y 
    // otro de vehículos dentro del gestor.
    // En una aplicación real estarían en una base de datos.
    private Cliente[] clientes = new Cliente[10];
    private Vehiculo[] vehiculos = new Vehiculo[100];


    public GestorAlquileres() {
        inicializarClientes();
        inicializarVehiculos();
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // INICIALIZACIÓN DE CLIENTES Y VEHÍCULOS DE EJEMPLO

    // Definimos métodos privados para inicializar clientes y vehículos de ejemplo
    private void inicializarClientes() {
        clientes[0] = new Cliente("Clara Videncia", "001");
        clientes[1] = new Cliente("Aitor Menta", "002");
        clientes[2] = new Cliente("Lola Mento", "003");
        clientes[3] = new Cliente("Armando Guerra", "004");
        clientes[4] = new Cliente("Ana Lítica", "005");
        clientes[5] = new Cliente("Carmelo Cotón", "006");
    }

    private void inicializarVehiculos() {
        vehiculos[0] = new Turismo("TNA2020", Categoria.NORMAL, 2020, 5, 450.0);
        vehiculos[1] = new Motocicleta("MNA2021", Categoria.NORMAL, 2021, 200);
        vehiculos[2] = new VehiculoIndustrial("CNA2019", Categoria.NORMAL, 2019, 1400, 2);
        vehiculos[3] = new Turismo("TSA2024", Categoria.SUPERIOR, 2024, 5, 550.0);
        vehiculos[4] = new Motocicleta("MSA2021", Categoria.SUPERIOR, 2021, 500);
        vehiculos[5] = new VehiculoIndustrial("CSA2025", Categoria.SUPERIOR, 2025, 2400, 3);
        vehiculos[6] = new Turismo("TEA2026", Categoria.ESPECIAL, 2026, 7, 650.0);
        vehiculos[7] = new Motocicleta("MEA2026", Categoria.ESPECIAL, 2026, 1000);
        vehiculos[8] = new VehiculoIndustrial("CEA2025", Categoria.ESPECIAL, 2025, 4400, 5);
        
        for (Vehiculo vehiculo : vehiculos) {
        if (vehiculo != null) {
        vehiculo.setDisponible(true);
        }
    }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTODOS PARA GESTIONAR VEHÍCULOS

     public Vehiculo[] obtenerVehiculos(String tipo) {
        
        Vehiculo[] vehiculosFiltrados = new Vehiculo[vehiculos.length];
        int contador = 0;

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo != null) {
                if (vehiculo.getTipo().equals(tipo)) {
                    vehiculosFiltrados[contador++] = vehiculo;
                }
            }
        }

        // Ajustar el tamaño del array para que solo contenga los vehículos encontrados
        Vehiculo[] resultado = new Vehiculo[contador];
        System.arraycopy(vehiculosFiltrados, 0, resultado, 0, contador);
        return resultado;
    }   

    /**
     * Devuelve un array con los vehículos del tipo y categoría especificados.
     * @param tipo
     * @param categoria
     * @return lista de vehículos del tipo y categoría especificados
     */
    public Vehiculo[] obtenerVehiculos(String tipo, Categoria categoria) {
    Vehiculo[] delTipo = obtenerVehiculos(tipo);
    Vehiculo[] filtrados = new Vehiculo[delTipo.length];
    int contador = 0;

    for (Vehiculo vehiculo : delTipo) {
    if (vehiculo.getCategoria() == categoria) {
    filtrados[contador] = vehiculo;
    contador++;
        }
    }

    Vehiculo[] resultado = new Vehiculo[contador];
    System.arraycopy(filtrados, 0, resultado, 0, contador);
    return resultado;
}

    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTODOS PARA GESTIONAR ALQUILERES

    /**
     * Agrega un nuevo alquiler al array de alquileres.
     * @param alquiler
     */
    public void agregarAlquiler(Alquiler alquiler) {
        if (contadorAlquileres < alquileres.length) {
            alquileres[contadorAlquileres] = alquiler;
            contadorAlquileres++;
        } else {
            System.out.println("No se pueden agregar más alquileres. Capacidad máxima alcanzada.");
        }
    }

    /**
     * Devuelve un array con todos los alquileres registrados en el gestor.
     * @return
     */
    public Alquiler[] obtenerAlquileres() {
        return alquileres; 
    }
    public Alquiler[] obtenerAlquileres(Cliente cliente) {
    Alquiler[] filtrados = new Alquiler[contadorAlquileres];
    int contador = 0;

    for (int i = 0; i < contadorAlquileres; i++) {
    Alquiler alquiler = alquileres[i];

    if (alquiler.getCliente().getDni().equals(cliente.getDni())) {
    filtrados[contador] = alquiler;
    contador++;
        }
    }

    Alquiler[] resultado = new Alquiler[contador];
    System.arraycopy(filtrados, 0, resultado, 0, contador);
    return resultado;
}

    private boolean esClienteFrecuente(Cliente cliente, int umbral) {
    return obtenerAlquileres(cliente).length >= umbral;
}
  
    public void guardarDatos(String nombreFichero) throws IOException {
    JSONObject datos = new JSONObject();
    datos.put("clientes", crearArrayJson(clientes));
    datos.put("vehiculos", crearArrayJson(vehiculos));
    datos.put("alquileres", crearArrayJson(alquileres));

    Files.writeString(Path.of(nombreFichero), datos.toString(2));
}

    private JSONArray crearArrayJson(Object[] objetos) {
    JSONArray resultado = new JSONArray();

    for (Object objeto : objetos) {
        if (objeto != null) {
            resultado.put(new JSONObject(objeto.toString()));
        }
    }

    return resultado;
}

    public void cargarDatos(String nombreFichero) throws IOException {
    String texto = Files.readString(Path.of(nombreFichero));
    JSONObject datos = new JSONObject(texto);

    JSONArray jsonClientes = datos.getJSONArray("clientes");
    JSONArray jsonVehiculos = datos.getJSONArray("vehiculos");
    JSONArray jsonAlquileres = datos.getJSONArray("alquileres");

    if (jsonClientes.length() > clientes.length
            || jsonVehiculos.length() > vehiculos.length
            || jsonAlquileres.length() > alquileres.length) {
        throw new IllegalArgumentException("El fichero supera la capacidad del gestor.");
    }

    Cliente[] clientesLeidos = new Cliente[clientes.length];
    Vehiculo[] vehiculosLeidos = new Vehiculo[vehiculos.length];
    Alquiler[] alquileresLeidos = new Alquiler[alquileres.length];

    for (int i = 0; i < jsonClientes.length(); i++) {
        clientesLeidos[i] = new Cliente(jsonClientes.getJSONObject(i).toString());
    }

    for (int i = 0; i < jsonVehiculos.length(); i++) {
        String json = jsonVehiculos.getJSONObject(i).toString();
        vehiculosLeidos[i] = FactoriaVehiculos.getVehiculo(json);
    }

    for (int i = 0; i < jsonAlquileres.length(); i++) {
        Alquiler leido = new Alquiler(jsonAlquileres.getJSONObject(i).toString());

        Cliente cliente = buscarCliente(clientesLeidos, leido.getCliente().getDni());
        Vehiculo vehiculo = buscarVehiculo(
            vehiculosLeidos, leido.getVehiculo().getMatricula()
        );

        if (cliente == null || vehiculo == null) {
            throw new IllegalArgumentException("Falta el cliente o el vehículo de un alquiler.");
        }

        alquileresLeidos[i] = new Alquiler(
            vehiculo, cliente, leido.getFechaInicio(), leido.getFechaFin()
        );
    }

        clientes = clientesLeidos;
        vehiculos = vehiculosLeidos;
        alquileres = alquileresLeidos;
        contadorAlquileres = jsonAlquileres.length();
    }

        private Cliente buscarCliente(Cliente[] lista, String dni) {
        for (Cliente cliente : lista) {
        if (cliente != null && cliente.getDni().equals(dni)) {
            return cliente;
        }
        }
    return null;
    }   

        private Vehiculo buscarVehiculo(Vehiculo[] lista, String matricula) {
        for (Vehiculo vehiculo : lista) {
        if (vehiculo != null && vehiculo.getMatricula().equals(matricula)) {
            return vehiculo;
        }
        }
    return null;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // INTERFAZ DE USUARIO (CONSOLA) Y LÓGICA DE NEGOCIO

    // La interfaz de usuario y la lógica de negocio suelen implementarse por separado.
    // Aquí se integran ambas por simplicidad, pero en una aplicación real hay que separarlas.


    public void menuPrincipal(){
        
        boolean salir = false;
        
        while(!salir){

            // Se da oportunidad de salir inmediatamente de la app.
            System.out.println("Gestíon de Alquileres");
            System.out.println("¿Desea salir de la app (S/N)?");

            char s = KeyBoard.readChar();
            if (s == 's' || s == 'S'){
                System.out.println("Bye ...");
                return;
            }
            System.out.println("(1) gestionar alquileres");
            System.out.println("(4) guardar datos");
            System.out.println("(5) recuperar datos");

            int accion = KeyBoard.readInt();

            if (accion == 4 || accion == 5) {
            System.out.println("Introduzca el nombre del fichero:");
            String fichero = KeyBoard.readString();

          try {
          if (accion == 4) {
            guardarDatos(fichero);
            System.out.println("Datos guardados.");
             } else {
            cargarDatos(fichero);
            System.out.println("Datos recuperados.");
        }
    } catch (IOException | IllegalArgumentException | org.json.JSONException
            | java.time.format.DateTimeParseException e) {
        System.out.println("Error al guardar o cargar: " + e.getMessage());
    }

    continue;
}

        if (accion != 1) {
        System.out.println("Opción inválida.");
         continue;
    }
            // Primero se identifica al cliente:
            Cliente cliente = identificarCliente();

            // Ahora el cliente puede elegir una opción o salir de la aplicación.
            if (cliente != null) {
                System.out.println("Sr " + cliente.getNombre() + " Introduzca una opción");
                System.out.println("(1) seleccionar coche para alquilar");
                System.out.println("(2) entregar un coche alquilado");
                System.out.println("(3) listar su histórico de alquileres");

                int opcion = KeyBoard.readInt();
                System.out.println("opción escogida: " + opcion);

        switch (opcion) {
        case 1:
        alquilarVehiculo(cliente);
        break;

        case 2:
        devolverVehiculo(cliente);
        break;

        case 3:
        Alquiler[] historial = obtenerAlquileres(cliente);

        if (historial.length == 0) {
        System.out.println("No tiene alquileres registrados.");
        }

        for (Alquiler alquiler : historial) {
        System.out.println(alquiler);
        }
        break;

        default:
        System.out.println("Opción inválida.");
        break;
}
            }
            else{
                System.out.println("Debe identificarse correctamente o salir de la app");
            }
        }
    }

    public Cliente identificarCliente(){

        boolean seleccionado = false;
        Cliente cliente = null;

        while (!seleccionado) {
        
            System.out.println("Bienvenido al Gestor de Alquileres");
            for (int i = 0; i < clientes.length; i++) {
                if (clientes[i] != null) {
                    System.out.println(i + " ---> " + clientes[i].getNombre() + " - " + clientes[i].getDni());
                }
            }
            System.out.println("Identífiquese como cliente con su número de cliente (-1 para salir):");
            int numeroCliente = KeyBoard.readInt();
            if (numeroCliente == -1) {
                return null;
            }
            else if (numeroCliente < 0 || numeroCliente >= clientes.length || clientes[numeroCliente] == null) {
                System.out.println("identificarCliente:Número de cliente inválido.");
                return null;
            }
            else{
                seleccionado = true;
                cliente = clientes[numeroCliente];
            }
        }
        return cliente;
    }

    public Vehiculo seleccionarVehiculo(Cliente cliente){
        boolean seleccionado = false;
        Vehiculo vehiculo = null;

        while (!seleccionado) {
        
            System.out.println("Estimado Sr./Sra. " + cliente.getNombre());
            System.out.println("Introduzca el número correspondiente al tipo de vehículo que desea alquilar:");
            System.out.println("1. Turismo, 2. Motocicleta, 3. Vehículo Industrial: ");

            int tipoVehiculo = KeyBoard.readInt();
            String tipo = "";
            Vehiculo [] elegibles = new Vehiculo[100];

            switch (tipoVehiculo) {
                case 1:
                    System.out.println("Ha seleccionado Turismo.");
                    tipo = "Turismo";
                    break;
                case 2:
                    System.out.println("Ha seleccionado Motocicleta.");
                    tipo = "Motocicleta";
                    break;
                case 3:
                    System.out.println("Ha seleccionado Vehículo Industrial.");
                    tipo = "VehiculoIndustrial";
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione un tipo de vehículo válido.");
                    continue; // Volver a pedir la selección
            }

            System.out.println("Vehiculos disponibles");

            System.out.println("Estimado Sr./Sra. " + cliente.getNombre() + "\n seleccione categoría.");
            System.out.println("Introduzca el número correspondiente a la categoría que desea alquilar:");
            System.out.println("1. Normal, 2. Superior, 3. Especial, 4.Cualquiera: ");

            int categoriaVehiculo = KeyBoard.readInt();

            switch (categoriaVehiculo) {
                case 1:
                    System.out.println("Ha seleccionado Categoría Normal.");
                    elegibles = obtenerVehiculos(tipo, Categoria.NORMAL);
                    break;
                case 2:
                    System.out.println("Ha seleccionado Categoría Superior.");
                    elegibles = obtenerVehiculos(tipo, Categoria.SUPERIOR);
                    break;
                case 3:
                    System.out.println("Ha seleccionado Categoría Especial.");
                    elegibles = obtenerVehiculos(tipo, Categoria.ESPECIAL);
                    break;
                case 4:
                    elegibles = obtenerVehiculos(tipo);
                    break;
                    default:
                    System.out.println("Categoría inválida.");
                    continue;
            }

        boolean hayDisponibles = false;

        for (int i = 0; i < elegibles.length; i++) {
        if (elegibles[i].isDisponible()) {
        System.out.println(i + " ---> " + elegibles[i]);
        hayDisponibles = true;
    }
}

        if (!hayDisponibles) {
        System.out.println("No hay vehículos disponibles de ese tipo y categoría.");
        return null;
}

        System.out.println("Introduzca el número del vehículo (-1 para cancelar):");
        int seleccion = KeyBoard.readInt();

        if (seleccion == -1) {
        return null;
}

        if (seleccion < 0 || seleccion >= elegibles.length|| !elegibles[seleccion].isDisponible()) {
        System.out.println("Seleccione un vehículo disponible.");
        continue;
}

        vehiculo = elegibles[seleccion];
        seleccionado = true;
        }
        return vehiculo;
    }
        private void alquilarVehiculo(Cliente cliente) {
        if (contadorAlquileres == alquileres.length) {
        System.out.println("No se pueden registrar más alquileres.");
        return;
    }

        Vehiculo vehiculo = seleccionarVehiculo(cliente);
        if (vehiculo == null) {
        return;
    }

        System.out.println("¿Cuántos días desea alquilar el vehículo?");
        int dias = KeyBoard.readInt();

        if (dias <= 0 || dias == KeyBoard.INT_ERR) {
        System.out.println("Introduzca un número válido de días.");
        return;
    }

    LocalDate inicio = LocalDate.now();
    LocalDate fin = inicio.plusDays(dias - 1);
    Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, fin);

    boolean frecuente = esClienteFrecuente(cliente, 3);
    alquiler.setPolitica(frecuente
        ? PoliticasDePrecios.ClienteFrecuente
        : PoliticasDePrecios.Normal);
    int precio = alquiler.calcularPrecioAlquiler(inicio, fin);
    String tarifa = frecuente ? "cliente frecuente" : "normal";

    agregarAlquiler(alquiler);
    vehiculo.setDisponible(false);
    System.out.printf("Alquiler realizado. Tarifa %s. Precio: %.2f EUR%n", tarifa, (double) precio);
}

            private void devolverVehiculo(Cliente cliente) {
            System.out.println("Introduzca la matrícula del vehículo:");
            String matricula = KeyBoard.readString();

            for (int i = contadorAlquileres - 1; i >= 0; i--) {
            Alquiler alquiler = alquileres[i];
            Vehiculo vehiculo = alquiler.getVehiculo();

            if (vehiculo.getMatricula().equalsIgnoreCase(matricula)) {
            if (vehiculo.isDisponible()) {
            System.out.println("Ese vehículo ya está disponible.");
            } else if (!alquiler.getCliente().getDni().equals(cliente.getDni())) {
            System.out.println("Ese vehículo está alquilado por otro cliente.");
            } else {
            vehiculo.setDisponible(true);
            System.out.println("Vehículo devuelto.");
            }
            return;
        }
    }

            System.out.println("No se encontró un alquiler para esa matrícula.");
}

    public void listarAlquileres (){
        System.out.println("Listado de alquileres");
        for (Alquiler alq: alquileres){
            System.out.println(alq);
        }
        System.out.println();
    }

    public void listarVehiculos(){
        System.out.println("Listado de vehículos");
        for (Vehiculo veh: vehiculos){
            System.out.println(veh);
        }
        System.out.println();
    }


    public static void main(String [] args){
        GestorAlquileres app = new GestorAlquileres();
        app.menuPrincipal();
    }
}
