/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psogeneradorhorario;

// Representa un Aula
class Aula {

    private String id;
    private int capacidadMaxima;

    public Aula(String id, int capacidadMaxima) {
        this.id = id;
        this.capacidadMaxima = capacidadMaxima;
    }

    // Getters
    public String getId() {
        return id;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
}
