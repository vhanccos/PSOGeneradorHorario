package psogeneradorhorario;

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
