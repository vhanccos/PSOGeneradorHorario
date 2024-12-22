/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psogeneradorhorario;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

class Particula {

    private Map<String, SlotHorario[]> horario;
    private double fitness;

    public Particula() {
        // Inicializar horario con 16 bloques por día
        horario = new HashMap<>();
        for (String dia : Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes")) {
            horario.put(dia, new SlotHorario[16]);
        }
    }

    // Metodo para evaluar fitness
    public void evaluarFitness() {
        // Inicializar fitness en 0
        double fitness = 1000.0; // Base alta para penalizar violaciones

        // Validar restricciones fuertes (cada violación reduce el fitness)
        if (!validarRestriccionesFuertes()) {
            fitness -= 500.0;
        }

        // Criterios de evaluación
        fitness += balancearCargaProfesor();
        fitness += optimizarDistribucionCursos();

        this.fitness = fitness;
    }

    // Métodos para validar restricciones
    private boolean validarRestriccionesFuertes() {
        // RF1: Un curso a lo más en un docente, periodo y aula
        // RF2: Un docente a lo más en un curso y aula en un periodo
        // RF3: Un aula a lo más con un curso y docente en un periodo
        // RF4: Respetar disponibilidad de horas del profesor
        // RF5: Asignar cursos solo a profesores calificados
        // RF6: Un curso solo una vez por turno
        // RF7: Dividir cursos de 4+ horas en dos turnos
        return true;
    }

    private double balancearCargaProfesor() {
        double bonus = 0.0;
        Map<Profesor, Integer> horasPorProfesor = new HashMap<>();

        // Contar horas asignadas por profesor
        for (String dia : horario.keySet()) {
            for (SlotHorario slot : horario.get(dia)) {
                if (slot != null) {
                    Profesor profesor = slot.getProfesor();
                    horasPorProfesor.put(profesor,
                            horasPorProfesor.getOrDefault(profesor, 0) + 1);
                }
            }
        }

        // Bonus para profesores Tipo A con preferencias de bloques
        for (Profesor profesor : horasPorProfesor.keySet()) {
            if (profesor instanceof SubprofesorA) {
                SubprofesorA profA = (SubprofesorA) profesor;
                Set<Integer> bloquesPreferidos = profA.getBloquesDisponibles();

                int horasAsignadas = horasPorProfesor.get(profesor);
                int horasCorrectas = 0;

                // Verificar que las horas asignadas estén en bloques preferidos
                for (String dia : horario.keySet()) {
                    for (int bloque = 0; bloque < horario.get(dia).length; bloque++) {
                        SlotHorario slot = horario.get(dia)[bloque];
                        if (slot != null && slot.getProfesor().equals(profesor)
                                && bloquesPreferidos.contains(bloque)) {
                            horasCorrectas++;
                        }
                    }
                }

                // Bonus proporcional a la asignación en bloques preferidos
                double porcentajeCorrectas = (double) horasCorrectas / horasAsignadas;
                bonus += 40.0 * porcentajeCorrectas;
            }
        }

        return bonus;
    }

    private double optimizarDistribucionCursos() {
        double bonus = 0.0;
        Map<String, Integer> cursosPorDia = new HashMap<>();

        // Contar cursos por día
        for (String dia : horario.keySet()) {
            Set<String> cursosDiferentes = new HashSet<>();

            for (SlotHorario slot : horario.get(dia)) {
                if (slot != null) {
                    cursosDiferentes.add(slot.getCurso().getId());
                }
            }

            cursosPorDia.put(dia, cursosDiferentes.size());
        }

        // Calcular varianza de cursos por día para buscar distribución uniforme
        double mediaCursos = cursosPorDia.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        double varianza = cursosPorDia.values().stream()
                .mapToDouble(x -> Math.pow(x - mediaCursos, 2))
                .average()
                .orElse(0.0);

        // Bonus inversamente proporcional a la varianza
        // Mientras más uniforme, mayor bonus
        bonus = 20.0 / (1 + varianza);

        return bonus;
    }

    // Añadir este método getter
    public Map<String, SlotHorario[]> getHorario() {
        return horario;
    }

    // Método para obtener fitness
    public double getFitness() {
        return fitness;
    }
}
