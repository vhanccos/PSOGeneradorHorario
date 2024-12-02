/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psogeneradorhorario;

// Representa un Turno de Estudio
class Turno {

    private String id;
    private String nombre;
    private String horaInicio;
    private String horaFin;

    public Turno(String id, String nombre, String horaInicio, String horaFin) {
        this.id = id;
        this.nombre = nombre;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }
}
