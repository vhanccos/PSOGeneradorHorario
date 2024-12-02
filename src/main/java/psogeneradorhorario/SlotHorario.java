/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psogeneradorhorario;

// Representa un Slot de Horario
class SlotHorario {

    private Curso curso;
    private Profesor profesor;
    private Aula aula;
    private Turno turno;
    private int horaInicio; // Hora de inicio en minutos desde medianoche
    private int horaFin;    // Hora de fin en minutos desde medianoche
    private int duracionMinutos;
    private String dia;

    public SlotHorario(Curso curso, Profesor profesor, Aula aula,
            Turno turno, int horaInicio, int duracionMinutos, String dia) {
        this.curso = curso;
        this.profesor = profesor;
        this.aula = aula;
        this.turno = turno;
        this.horaInicio = horaInicio;
        this.duracionMinutos = duracionMinutos;
        this.horaFin = horaInicio + duracionMinutos;
        this.dia = dia;
    }

    // Método para convertir hora en formato HH:MM a minutos desde medianoche
    public static int convertirAMinutos(String horaTexto) {
        String[] partes = horaTexto.split(":");
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);
        return horas * 60 + minutos;
    }

    // Método para convertir minutos desde medianoche a formato HH:MM
    public static String convertirAHoraTexto(int minutos) {
        int horas = minutos / 60;
        int minutosRestantes = minutos % 60;
        return String.format("%02d:%02d", horas, minutosRestantes);
    }

    // Getters
    public Curso getCurso() {
        return curso;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public Aula getAula() {
        return aula;
    }

    public Turno getTurno() {
        return turno;
    }

    // Devolver la hora de inicio como texto para compatibilidad
    public String getHoraInicio() {
        return convertirAHoraTexto(horaInicio);
    }
    public int getHoraInicioI() {
        return horaInicio;
    }

    public String getHoraFin() {
        return convertirAHoraTexto(horaFin);
    }
    
    public int getHoraFinI() {
        return horaFin;
    }

    public int getHoraInicioMinutos() {
        return horaInicio;
    }

    public int getHoraFinMinutos() {
        return horaFin;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public String getDia() {
        return dia;
    }

    public int getDiaI() {
        // Definir el arreglo con los días de la semana
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};

        // Buscar el índice del día en el arreglo
        for (int i = 0; i < dias.length; i++) {
            if (dias[i].equals(this.dia)) {  // Usar "this.dia" para acceder al atributo de la clase
                return i;  // Devuelve el índice correspondiente al día
            }
        }

        // Si no se encuentra el día, devolver -1 (indicar un valor no válido)
        return -1;
    }
}