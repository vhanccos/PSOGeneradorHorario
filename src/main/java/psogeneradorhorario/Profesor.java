/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psogeneradorhorario;

// Representa un Profesor
class Profesor {

    private String id;
    private String nombre;
    private TipoProfesores tipo;
    private int horasMaximasSemana;
    private int horasMinimasSemana;

    public enum TipoProfesores {
        SUB_PROFESOR_A(36), // Tiempo completo 
        SUB_PROFESOR_B(12);   // Medio tiempo

        private final int horasMaximas;

        TipoProfesores(int horasMaximas) {
            this.horasMaximas = horasMaximas;
        }

        public int getHorasMaximas() {
            return horasMaximas;
        }
    }

    public Profesor(String id, String nombre, TipoProfesores tipo,
            int horasMaximasSemana, int horasMinimasSemana) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.horasMaximasSemana = horasMaximasSemana;
        this.horasMinimasSemana = horasMinimasSemana;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoProfesores getTipo() {
        return tipo;
    }

    public int getHorasMaximasSemana() {
        return horasMaximasSemana;
    }

    public int getHorasMinimasSemana() {
        return horasMinimasSemana;
    }
}
