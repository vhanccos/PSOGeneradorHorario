/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psogeneradorhorario;

// Representa un Curso

import java.util.List;

class Curso {

    private String id;
    private String nombre;
    private int horasSemana;
    private List<Profesor> profesoresCalificados;
    private int ciclo;
    private int duracionMinutos;
    private Turno turno; // Nuevo atributo para el turno del curso

    public Curso(String id, String nombre, int horasSemana, List<Profesor> profesoresCalificados, int ciclo) {
        this.id = id;
        this.nombre = nombre;
        this.horasSemana = horasSemana;
        this.profesoresCalificados = profesoresCalificados;
        this.ciclo = ciclo;
        this.duracionMinutos = horasSemana * 50;
    }
    
    // Nuevo constructor con turno
    public Curso(String id, String nombre, int horasSemana, List<Profesor> profesoresCalificados, int ciclo, Turno turno) {
        this.id = id;
        this.nombre = nombre;
        this.horasSemana = horasSemana;
        this.profesoresCalificados = profesoresCalificados;
        this.ciclo = ciclo;
        this.duracionMinutos = horasSemana * 50;
        this.turno = turno;
    }

    // Getters
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

    public int getDuracionMinutos() {
        return duracionMinutos;
    }
    
    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }
}
