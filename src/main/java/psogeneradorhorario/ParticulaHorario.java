
package psogeneradorhorario;

// Partícula que representa una solución de horario

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ParticulaHorario {

    private HorarioTridimensional horarioTridimensional;
    private double aptitud;
    private HorarioTridimensional mejorHorarioPersonal;
    private double mejorAptitudPersonal;

    public ParticulaHorario(List<SlotHorario> slotsHorario) {
        // Crear un nuevo horario tridimensional
        this.horarioTridimensional = new HorarioTridimensional();

        // Agregar cada slot al horario tridimensional
        for (SlotHorario slot : slotsHorario) {
            int diaIndex = slot.getDiaI();
            this.horarioTridimensional.agregarSlot(diaIndex, slot);
        }

        // Calcular aptitud
        this.aptitud = calcularAptitud();

        // Guardar una copia del mejor horario personal inicial
        this.mejorHorarioPersonal = copiarHorarioTridimensional(this.horarioTridimensional);
        this.mejorAptitudPersonal = aptitud;
    }

    // Método para crear una copia profunda del horario tridimensional
    private HorarioTridimensional copiarHorarioTridimensional(HorarioTridimensional original) {
        HorarioTridimensional copia = new HorarioTridimensional();
        for (int dia = 0; dia < 5; dia++) {
            for (SlotHorario slot : original.getSlotsDia(dia)) {
                copia.agregarSlot(dia, slot);
            }
        }
        return copia;
    }

    private double calcularAptitud() {
        // Cálculo de aptitud basado en:
        // 1. Minimizar tiempos ociosos para profesores y estudiantes
        // 2. Maximizar ocupación de aulas
        // 3. Verificar restricciones fuertes

        double aptitud = 0.0;

        // Verificar restricciones fuertes
        if (!validarRestriccionesFuertes()) {
            return Double.MIN_VALUE; // Horario inválido
        }

        // Calcular penalización por tiempo ocioso
        aptitud -= calcularPenalizacionTiempoOcioso();

        // Calcular utilización de aulas
        aptitud += calcularUtilizacionAulas();

        return aptitud;
    }

    private boolean validarRestriccionesFuertes() {
        // Implementar las restricciones RF1-RF7 mencionadas en el documento original
        // Las principales incluyen:
        // - Un curso solo asignado a un profesor en un tiempo específico
        // - Un profesor solo en un curso a la vez
        // - Un aula solo ocupada por un curso a la vez
        // - Respetar disponibilidad de profesores
        // - Profesor calificado para el curso
        // - Solo un curso por turno
        // - Cursos de 4+ horas divididos en dos turnos

        Set<String> turnosProfesores = new HashSet<>();
        Set<String> turnosAulas = new HashSet<>();

        return true; // Placeholder
    }

    private double calcularPenalizacionTiempoOcioso() {
        // Calcular tiempo ocioso entre clases
        return 0.0; // Placeholder
    }

    private double calcularUtilizacionAulas() {
        // Calcular uso uniforme de aulas
        return 0.0; // Placeholder
    }

    // Getters
    public HorarioTridimensional getHorarioTridimensional() {
        return horarioTridimensional;
    }

    public double getAptitud() {
        return aptitud;
    }

    public HorarioTridimensional getMejorHorarioPersonal() {
        return mejorHorarioPersonal;
    }

    public double getMejorAptitudPersonal() {
        return mejorAptitudPersonal;
    }
}

