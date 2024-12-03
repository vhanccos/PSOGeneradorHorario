/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psogeneradorhorario;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Particula {

    private Map<String, SlotHorario[]> horario;
    private double fitness;

    public Particula() {
        // Inicializar horario con 16 bloques por día
        horario = new HashMap<>();
        for (String dia : Arrays.asList("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")) {
            horario.put(dia, new SlotHorario[16]);
        }
    }

    // Método para evaluar fitness

    public void evaluarFitness() {
        int penalizacion = 0;
        int horasOciosas = 0;
        int bloquesOcupados = 0;

        for (String dia : horario.keySet()) {
            SlotHorario[] bloques = horario.get(dia);
            boolean bloqueAnteriorOcupado = false;
            int bloquesConCurso = 0;

            for (int i = 0; i < bloques.length; i++) {
                if (bloques[i] != null) {
                    bloquesOcupados++;
                    bloquesConCurso++;
                    bloqueAnteriorOcupado = true;

                    // Validar restricciones fuertes
                    if (!validarRestriccionesFuertes()) {
                        penalizacion += 20; // Penalización por cada violación de restricciones
                    }
                } else if (bloqueAnteriorOcupado) {
                    horasOciosas++; // Contar horas ociosas si hay un hueco entre bloques ocupados
                    bloqueAnteriorOcupado = false;
                }
            }

            // Penalizar si un curso ocupa más de un turno (e.g., mañana y tarde)
            if (bloquesConCurso > 8) {
                penalizacion += 10;
            }
        }

        // Calcular fitness final
        fitness = (double) bloquesOcupados / (horasOciosas + 1) - penalizacion;

        // Asegurarse de que el fitness no sea negativo
        if (fitness < 0) {
            fitness = 0;
        }
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

    // Añadir este método getter
    public Map<String, SlotHorario[]> getHorario() {
        return horario;
    }

    // Método para obtener fitness
    public double getFitness() {
        return fitness;
    }
}
