package psogeneradorhorario;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SubprofesorA extends Profesor {
    // Nuevos atributos específicos para SUBPROFESOR_A
    private Set<Integer> bloquesDisponibles;
    private Set<String> diasDisponibles; // Nuevo atributo para días disponibles

    public SubprofesorA(String id, String nombre, int horasMaximasSemana,
            int horasMinimasSemana, Set<Integer> bloquesDisponibles, Set<String> diasDisponibles) {
        super(id, nombre, TipoPprofesor.SUBPROFESOR_A,
                horasMaximasSemana, horasMinimasSemana);
        this.bloquesDisponibles = bloquesDisponibles != null
                ? bloquesDisponibles
                : new HashSet<>();
            
        this.diasDisponibles = diasDisponibles != null
            ? diasDisponibles
            : new HashSet<>(Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes")); // Por defecto todos los días
    }

    // Implementación del método abstracto
    @Override
    public boolean estaDisponibleEnBloque(int bloque) {
        return bloquesDisponibles.contains(bloque);
    }

    // Nuevo método para verificar disponibilidad completa
    public boolean estaDisponible(String dia, int bloque) {
        return diasDisponibles.contains(dia) && bloquesDisponibles.contains(bloque);
    }

    // Getters
    public Set<Integer> getBloquesDisponibles() {
        return bloquesDisponibles;
    }

    public Set<String> getDiasDisponibles() {
        return diasDisponibles;
    }

    // Métodos para modificar disponibilidad
    public void addBloqueDisponible(int bloque) {
        bloquesDisponibles.add(bloque);
    }

    public void addDiaDisponible(String dia) {
        diasDisponibles.add(dia);
    }

    public void removeDiaDisponible(String dia) {
        diasDisponibles.remove(dia);
    }

    // Método para verificar si un día está disponible
    public boolean isDiaDisponible(String dia) {
        return diasDisponibles.contains(dia);
    }
}

