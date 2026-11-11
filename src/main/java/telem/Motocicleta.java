package telem;
import org.json.JSONObject;

public class Motocicleta extends Vehiculo {

    int cilindrada;

    public Motocicleta(String matricula, Categoria categoria, int annio,
        int cilindrada
    ) {
        super(matricula, categoria, annio);
        this.cilindrada = cilindrada;
    }
    
    public Motocicleta(String jsonString) {
    this(new JSONObject(jsonString));
    }

    Motocicleta(JSONObject json) {
    super(json);
    this.cilindrada = json.getInt("cilindrada");
    }

    public int getCilindrada() {
        return cilindrada;
    }

    @Override
    public String getTipo() {
        return "Motocicleta";
    }

    @Override
    public int getPrecioBase() {
        switch (getCategoria()) {
            case NORMAL: return 25;
            case ESPECIAL: return 40;
            case SUPERIOR: return 50;
            default: throw new IllegalStateException("Categoría desconocida: " + getCategoria());
        }
    }
}
