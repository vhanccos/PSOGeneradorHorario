/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psogeneradorhorario;

import java.util.List;

class Curso {

    private String id;
    private String nombre;
    private int horasSemana;
    private List<Profesor> profesoresCalificados;
    private int ciclo;
    private String turno; // A, B o C
    private int duracionMinutos;

    public Curso(String id, String nombre, int horasSemana,
            List<Profesor> profesoresCalificados,
            int ciclo, String turno) {
        this.id = id;
        this.nombre = nombre;
        this.horasSemana = horasSemana;
        this.profesoresCalificados = profesoresCalificados;
        this.ciclo = ciclo;
        this.turno = turno;
        this.duracionMinutos = horasSemana * 50; // Cada hora académica son 50 minutos
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getHorasSemana() {
        return horasSemana;
    }

    public List<Profesor> getProfesoresCalificados() {
        return profesoresCalificados;
    }

    public int getCiclo() {
        return ciclo;
    }

    public String getTurno() {
        return turno;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }
}
