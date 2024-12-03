package psogeneradorhorario;

import java.util.HashSet;
import java.util.Set;

public class SubprofesorA extends Profesor {
    // Nuevos atributos específicos para SUBPROFESOR_A
    private Set<Integer> bloquesDisponibles;

    public SubprofesorA(String id, String nombre, int horasMaximasSemana,
            int horasMinimasSemana, Set<Integer> bloquesDisponibles) {
        super(id, nombre, TipoPprofesor.SUBPROFESOR_A,
                horasMaximasSemana, horasMinimasSemana);
        this.bloquesDisponibles = bloquesDisponibles != null
                ? bloquesDisponibles
                : new HashSet<>();
    }

    // Implementación del método abstracto
    @Override
    public boolean estaDisponibleEnBloque(int bloque) {
        return bloquesDisponibles.contains(bloque);
    }

    // Getter para bloques disponibles
    public Set<Integer> getBloquesDisponibles() {
        return bloquesDisponibles;
    }

    // Método para añadir bloques disponibles
    public void addBloqueDisponible(int bloque) {
        bloquesDisponibles.add(bloque);
    }
}
