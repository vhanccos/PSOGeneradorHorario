/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psogeneradorhorario;

import java.util.List;

public abstract class Profesor {

    private String id;
    private String nombre;
    private TipoPprofesor tipo;
    private int horasMaximasSemana;
    private int horasMinimasSemana;

    public enum TipoPprofesor {
        SUBPROFESOR_A(36), // 36 horas semanales
        SUBPROFESOR_B(12); // 12 horas semanales

        private final int horasMaximas;

        TipoPprofesor(int horasMaximas) {
            this.horasMaximas = horasMaximas;
        }

        public int getHorasMaximas() {
            return horasMaximas;
        }
    }

    public Profesor(String id, String nombre, TipoPprofesor tipo,
            int horasMaximasSemana, int horasMinimasSemana) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.horasMaximasSemana = horasMaximasSemana;
        this.horasMinimasSemana = horasMinimasSemana;
    }

    public static Profesor buscarProfesorPorId(String id, List<Profesor> profesores) {
        for (Profesor profesor : profesores) {
            if (profesor.getId().equals(id)) {
                return profesor;
            }
        }
        return null;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoPprofesor getTipo() {
        return tipo;
    }

    public int getHorasMaximasSemana() {
        return horasMaximasSemana;
    }

    public int getHorasMinimasSemana() {
        return horasMinimasSemana;
    }

    // Método abstracto para verificar disponibilidad de bloques
    public abstract boolean estaDisponibleEnBloque(int bloque);

}
