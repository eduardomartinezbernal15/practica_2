package telem;
import org.json.JSONObject;

public class FactoriaVehiculos {
private FactoriaVehiculos() {
    }

    public static Vehiculo getVehiculo(String jsonString) {
        return getVehiculo(new JSONObject(jsonString));
    }

    static Vehiculo getVehiculo(JSONObject json) {
        String tipo = json.getString("tipo");

    switch (tipo) {
            case "Turismo":
            return new Turismo(json);
            case "Motocicleta":
            return new Motocicleta(json);
            case "VehiculoIndustrial":
            return new VehiculoIndustrial(json);
            default:
            throw new IllegalArgumentException("Tipo de vehículo desconocido: " + tipo);
     }
    }
}
