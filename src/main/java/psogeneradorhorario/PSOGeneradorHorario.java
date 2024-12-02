package psogeneradorhorario;

import java.util.*;

// Nueva clase para representar un horario tridimensional
class HorarioTridimensional {

    // Estructura para almacenar horario: [Día][Slots del día]
    private List<List<SlotHorario>> horarioPorDia;

    public HorarioTridimensional() {
        // Inicializar con 5 días (Lunes a Viernes)
        horarioPorDia = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            horarioPorDia.add(new ArrayList<>());
        }
    }

    // Método para agregar un slot a un día específico
    public void agregarSlot(int dia, SlotHorario slot) {
        if (dia >= 0 && dia < horarioPorDia.size()) {
            horarioPorDia.get(dia).add(slot);
        }
    }

    // Método para obtener slots de un día específico
    public List<SlotHorario> getSlotsDia(int dia) {
        if (dia >= 0 && dia < horarioPorDia.size()) {
            return horarioPorDia.get(dia);
        }
        return new ArrayList<>();
    }

    // Obtener todos los slots de todos los días
    public List<SlotHorario> getTodosLosSlots() {
        List<SlotHorario> todosSlots = new ArrayList<>();
        for (List<SlotHorario> diasSlots : horarioPorDia) {
            todosSlots.addAll(diasSlots);
        }
        return todosSlots;
    }
    public boolean hayConflicto(int diaIndex, Turno turno, Curso curso, int horaInicio, int duracion) {
    for (SlotHorario slot : getSlotsDia(diaIndex)) {
        if (slot.getTurno().equals(turno)) {
            // Verificar si el mismo curso ya está asignado en este turno
            if (slot.getCurso().equals(curso)) {
                return true;
            }
            // Verificar si el horario se solapa
            if ((horaInicio < slot.getHoraFinI() && horaInicio >= slot.getHoraInicioI()) ||
                (horaInicio + duracion > slot.getHoraInicioI() && horaInicio + duracion <= slot.getHoraFinI())) {
                return true;
            }
        }
    }
    return false;
}

    // Método para imprimir horario tridimensional
    public void imprimirHorario() {
        String[] diasNombres = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};

        for (int i = 0; i < horarioPorDia.size(); i++) {
            System.out.println("\n" + diasNombres[i] + ":");

            // Ordenar slots del día por hora de inicio
            List<SlotHorario> slotsDia = horarioPorDia.get(i);
            slotsDia.sort(Comparator.comparing(SlotHorario::getHoraInicioMinutos));

            for (SlotHorario slot : slotsDia) {
                System.out.printf("- %s: %s-%s (%d mins) | Profesor: %s | Aula: %s | Turno: %s\n",
                        slot.getCurso().getNombre(),
                        slot.getHoraInicio(),
                        slot.getHoraFin(),
                        slot.getDuracionMinutos(),
                        slot.getProfesor().getNombre(),
                        slot.getAula().getId(),
                        slot.getTurno().getNombre()
                );
            }
        }
    }
}




// Implementación del Algoritmo de Enjambre de Partículas (PSO)
class OptimizadorHorariosPSO {

    private List<Curso> cursos;
    private List<Profesor> profesores;
    private List<Aula> aulas;
    private List<Turno> turnos;
    private int tamañoPoblacion;
    private int maximoIteraciones;
    private double pesosCognitivo;
    private double pesosSocial;
    private List<ParticulaHorario> poblacion;
    private ParticulaHorario mejorHorarioGlobal;

    public OptimizadorHorariosPSO(List<Curso> cursos, List<Profesor> profesores,
            List<Aula> aulas, List<Turno> turnos,
            int tamañoPoblacion, int maximoIteraciones) {
        this.cursos = cursos;
        this.profesores = profesores;
        this.aulas = aulas;
        this.turnos = turnos;
        this.tamañoPoblacion = tamañoPoblacion;
        this.maximoIteraciones = maximoIteraciones;
        this.pesosCognitivo = 2.0;
        this.pesosSocial = 2.0;
        this.poblacion = new ArrayList<>();
    }

    public ParticulaHorario optimizar() {
        // Inicializar población
        inicializarPoblacion();

        for (int iteracion = 0; iteracion < maximoIteraciones; iteracion++) {
            // Evaluar aptitud de cada partícula
            actualizarAptitudyMejoresHorarios();

            // Actualizar velocidades y posiciones de partículas
            actualizarParticulas();
        }

        return mejorHorarioGlobal;
    }

    private void inicializarPoblacion() {
        for (int i = 0; i < tamañoPoblacion; i++) {
            List<SlotHorario> horarioAleatorio = generarHorarioAleatorioValido();
            ParticulaHorario particula = new ParticulaHorario(horarioAleatorio);
            poblacion.add(particula);

            // Actualizar mejor horario global
            if (mejorHorarioGlobal == null
                    || particula.getAptitud() > mejorHorarioGlobal.getAptitud()) {
                mejorHorarioGlobal = particula;
            }
        }
    }

    List<SlotHorario> generarHorarioAleatorioValido() {
    HorarioTridimensional horarioTridimensional = new HorarioTridimensional();
    Random aleatorio = new Random();
    String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};

    for (Curso curso : cursos) {
        // Verificar si el curso tiene profesores calificados
        if (curso.getProfesoresCalificados().isEmpty()) {
            continue; // Saltar si no hay profesores calificados
        }

        // Asignar el profesor de manera aleatoria de la lista de profesores calificados
        Profesor profesorAsignado = curso.getProfesoresCalificados().get(
                aleatorio.nextInt(curso.getProfesoresCalificados().size())
        );

        // Asignar el aula de manera aleatoria
        Aula aulaAsignada = aulas.get(aleatorio.nextInt(aulas.size()));

        // Usar el turno especificado por el curso
        Turno turnoAsignado = curso.getTurno();
        int horaBaseMinutos = SlotHorario.convertirAMinutos(turnoAsignado.getHoraInicio());
        System.out.println("Hora baseMinu: "+horaBaseMinutos+" "+curso.getNombre());
        int horaFinTurno = SlotHorario.convertirAMinutos(turnoAsignado.getHoraFin());

        int horasRestantes = curso.getHorasSemana();
        int diaIndex = aleatorio.nextInt(dias.length);
        String diaAsignado = dias[diaIndex];

        while (horasRestantes > 0) {
            // Calcular la duración del slot (máximo 3 horas académicas, es decir, 150 minutos)
            int duracionSlot = Math.min(horasRestantes, 3) * 50; // Duración en minutos
            int horaInicioMinutos = horaBaseMinutos;

            // Verificar si hay conflicto de horario para este día, turno y tiempo
            while (horarioTridimensional.hayConflicto(diaIndex, turnoAsignado, curso, horaInicioMinutos, duracionSlot)) {
                horaInicioMinutos += 10; // Aumentar 10 minutos para buscar el siguiente espacio libre
                if (horaInicioMinutos + duracionSlot > horaFinTurno) {
                    // Si no hay más espacio en este día, pasar al siguiente día
                    diaIndex = (diaIndex + 1) % dias.length;
                    diaAsignado = dias[diaIndex];
                    horaInicioMinutos = SlotHorario.convertirAMinutos(turnoAsignado.getHoraInicio());
                }
                
                horaInicioMinutos +=10;
            }

            // Crear el slot y agregarlo al horario tridimensional
            SlotHorario slot = new SlotHorario(
                    curso,
                    profesorAsignado,
                    aulaAsignada,
                    turnoAsignado,
                    horaInicioMinutos,
                    duracionSlot,
                    diaAsignado
            );
            horarioTridimensional.agregarSlot(diaIndex, slot);
            System.out.println("Fin hora slot: "+slot.getHoraFin());

            // Actualizar la hora base y las horas restantes
            horaBaseMinutos = slot.getHoraFinI() + 10; // Añadir 10 minutos de intervalo entre slots
            horasRestantes -= duracionSlot / 50; // Restar las horas ya asignadas
        }
    }

    return horarioTridimensional.getTodosLosSlots();
}




    private void actualizarAptitudyMejoresHorarios() {
        // Actualizar mejores horarios personales y globales
    }

    private void actualizarParticulas() {
        // Actualizar partículas usando lógica de PSO
    }

    // Método para imprimir el mejor horario
    public void imprimirMejorHorario() {
        System.out.println("Aptitud del Mejor Horario: " + mejorHorarioGlobal.getAptitud());
        // Imprimir detalles del horario
    }
    // Método para imprimir el horario con más detalle

    // Método para imprimir el horario usando la nueva estructura
    public void imprimirHorario(List<SlotHorario> horario) {
        HorarioTridimensional horarioTridimensional = new HorarioTridimensional();

        // Reconstruir el horario tridimensional a partir de los slots
        for (SlotHorario slot : horario) {
            int diaIndex = getDiaIndex(slot.getDia());
            horarioTridimensional.agregarSlot(diaIndex, slot);
        }

        // Imprimir usando el nuevo método de impresión
        horarioTridimensional.imprimirHorario();
    }

    // Método auxiliar para obtener el índice del día
    private int getDiaIndex(String dia) {
        return switch (dia) {
            case "Lunes" ->
                0;
            case "Martes" ->
                1;
            case "Miércoles" ->
                2;
            case "Jueves" ->
                3;
            case "Viernes" ->
                4;
            default ->
                0;
        };
    }
}

// Clase principal para demostrar el generador de horarios
public class PSOGeneradorHorario {

    public static void main(String[] args) {
        // Crear datos de ejemplo
        List<Profesor> profesores = Arrays.asList(
                new Profesor("P001", "Juan Pérez", Profesor.TipoProfesores.SUB_PROFESOR_A, 36, 20),
                new Profesor("P002", "María García", Profesor.TipoProfesores.SUB_PROFESOR_B, 12, 6),
                new Profesor("P003", "Carlos Rodríguez", Profesor.TipoProfesores.SUB_PROFESOR_A, 36, 20),
                new Profesor("P004", "Laura López", Profesor.TipoProfesores.SUB_PROFESOR_B, 24, 10),
                new Profesor("P005", "Luis Hernández", Profesor.TipoProfesores.SUB_PROFESOR_B, 12, 25)
        );
        List<Turno> turnos = Arrays.asList(
                new Turno("A", "Mañana", "7:00", "13:10"),
                new Turno("B", "Tarde", "14:00", "20:10")   
        );

       // Crear cursos y asignar turnos específicos
        List<Curso> cursos = Arrays.asList(
                new Curso("C001", "Matemáticas", 4,
                        Arrays.asList(profesores.get(0), profesores.get(1)), 1, turnos.get(0)), // Mañana
                new Curso("C001", "Matemáticas", 4,
                        Arrays.asList(profesores.get(0), profesores.get(1)), 1, turnos.get(1)), // Mañana
                new Curso("C002", "Física", 3,
                        Arrays.asList(profesores.get(1), profesores.get(2)), 2, turnos.get(1)), // Tarde
                new Curso("C003", "Programación", 5,
                        Arrays.asList(profesores.get(0), profesores.get(2)), 3, turnos.get(0)), // Mañana
                new Curso("C004", "Química", 4,
                        Arrays.asList(profesores.get(2), profesores.get(3)), 4, turnos.get(1)), // Tarde
                new Curso("C005", "Historia", 2,
                        Arrays.asList(profesores.get(3), profesores.get(4)), 5, turnos.get(0)), // Mañana
                new Curso("C006", "Literatura", 3,
                        Arrays.asList(profesores.get(4), profesores.get(0)), 6, turnos.get(1)), // Tarde
                new Curso("C007", "Biología", 5,
                        Arrays.asList(profesores.get(1), profesores.get(3)), 7, turnos.get(0))  // Mañana
        );

        List<Aula> aulas = Arrays.asList(
                new Aula("301", 30),
                new Aula("302", 25),
                new Aula("303", 35),
                new Aula("304", 40),
                new Aula("305", 20)
        );

        

        // Configurar optimizador de horarios
        OptimizadorHorariosPSO optimizador = new OptimizadorHorariosPSO(
                cursos, profesores, aulas, turnos, 10, 50
        );

        // Generar y mostrar horario aleatorio
        List<SlotHorario> horarioAleatorio = optimizador.generarHorarioAleatorioValido();
        optimizador.imprimirHorario(horarioAleatorio);
        System.out.println("\n===== HORARIO GENERADO =====");
        System.out.println("Total slots generados: " + horarioAleatorio.size());

        // Opcional: Ejecutar optimización
        // ParticulaHorario mejorHorario = optimizador.optimizar();
        // optimizador.imprimirHorario(mejorHorario.getSlotsHorario());
    }
}

