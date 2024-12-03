package psogeneradorhorario;

class SlotHorario {

    private Curso curso;
    private Profesor profesor;
    private Aula aula;
    private String dia;
    private int bloqueInicio;
    private int bloqueFin;

    public SlotHorario(Curso curso, Profesor profesor, Aula aula,
            String dia, int bloqueInicio, int bloqueFin) {
        this.curso = curso;
        this.profesor = profesor;
        this.aula = aula;
        this.dia = dia;
        this.bloqueInicio = bloqueInicio;
        this.bloqueFin = bloqueFin;
    }

    public Curso getCurso() {
        return curso;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public Aula getAula() {
        return aula;
    }

    public String getDia() {
        return dia;
    }

    public int getBloqueInicio() {
        return bloqueInicio;
    }

    public int getBloqueFin() {
        return bloqueFin;
    }
}
