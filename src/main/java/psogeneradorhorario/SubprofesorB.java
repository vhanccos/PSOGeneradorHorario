package psogeneradorhorario;

public class SubprofesorB extends Profesor {
    public SubprofesorB(String id, String nombre, int horasMaximasSemana,
            int horasMinimasSemana) {
        super(id, nombre, TipoPprofesor.SUBPROFESOR_B,
                horasMaximasSemana, horasMinimasSemana);
    }

    // Implementación del método abstracto - siempre disponible
    @Override
    public boolean estaDisponibleEnBloque(int bloque) {
        return true;
    }
}
