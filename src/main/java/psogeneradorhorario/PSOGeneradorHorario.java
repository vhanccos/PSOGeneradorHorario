package psogeneradorhorario;

import java.util.*;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;

// Clase que representa un Curso
// Clase que representa un Profesor
// Clase que representa un Aula
// Clase que representa un Slot de Horario
// Clase de Partícula para PSO
// Clase principal de PSO para Generación de Horarios
class GeneradorHorariosPSO {

    private List<Curso> cursos;
    private List<Profesor> profesores;
    private List<Aula> aulas;
    private int numeroPArticulas;
    private int iteraciones;

    public GeneradorHorariosPSO(List<Curso> cursos, List<Profesor> profesores, List<Aula> aulas) {
        this.cursos = cursos;
        this.profesores = profesores;
        this.aulas = aulas;
        this.numeroPArticulas = 50; // Valor por defecto
        this.iteraciones = 100; // Valor por defecto
    }

    public Particula generarMejorHorario() {
        // Inicializar población de partículas
        List<Particula> poblacion = new ArrayList<>();
        for (int i = 0; i < numeroPArticulas; i++) {
            poblacion.add(generarParticulaInicial());
        }

        // Proceso de optimización por enjambre de partículas
        Particula mejorParticula = null;
        for (int iteracion = 0; iteracion < iteraciones; iteracion++) {
            for (Particula particula : poblacion) {
                particula.evaluarFitness();
                System.out.println("Fines "+iteracion+" :"+particula.getFitness());

                // Actualizar la mejor partícula global
                if (mejorParticula == null
                        || particula.getFitness() > mejorParticula.getFitness()) {
                    mejorParticula = particula;
                }
            }

            // Actualizar velocidad y posición de partículas
            actualizarPoblacion(poblacion, mejorParticula);
        }
        
        System.out.println("Mejor PARTICULA: "+mejorParticula.getFitness());

        return mejorParticula;
    }

    public Particula generarParticulaInicial() {
        Particula particula = new Particula();
        Random random = new Random();

        // Dividir los cursos por turnos
        List<Curso> cursosTurnoA = cursos.stream().filter(c -> "A".equals(c.getTurno())).collect(Collectors.toList());
        List<Curso> cursosTurnoB = cursos.stream().filter(c -> "B".equals(c.getTurno())).collect(Collectors.toList());
        List<Curso> cursosTurnoC = cursos.stream().filter(c -> "C".equals(c.getTurno())).collect(Collectors.toList());

        // Combinar en el orden de prioridad: A, B, luego C
        List<Curso> cursosShuffle = new ArrayList<>();
        cursosShuffle.addAll(cursosTurnoA);
        cursosShuffle.addAll(cursosTurnoB);
        Collections.shuffle(cursosShuffle);
        Collections.shuffle(cursosTurnoC);
        cursosShuffle.addAll(cursosTurnoC);

        for (Curso curso : cursosShuffle) {
            int bloquesRestantes = calcularBloquesNecesarios(curso);
            Profesor profesorSeleccionado = seleccionarProfesorCalificado(curso);
            if (profesorSeleccionado == null) {
                System.out.println("No hay profesores disponibles para el curso: " + curso.getNombre());
                continue;
            }

            Aula aulaSeleccionada = seleccionarAulaAdecuada(curso);
            if (aulaSeleccionada == null) {
                System.out.println("No hay aulas disponibles para el curso: " + curso.getNombre());
                continue;
            }

            // División específica para diferentes duraciones
            int[] divisiones;
            if (bloquesRestantes == 5) {
                divisiones = new int[] { 3, 2 }; // 3 y 2 bloques
            } else if (bloquesRestantes == 4) {
                divisiones = new int[] { 2, 2 }; // 2 y 2 bloques
            } else {
                divisiones = new int[] { bloquesRestantes }; // Sin división si es menos de 4
            }

            // Conjunto para rastrear días usados
            Set<String> diasUsados = new HashSet<>();

            // Asignar cada división en un día diferente
            for (int division : divisiones) {
                boolean divisionAsignada = false;

                // Intentar 100 veces encontrar un día diferente
                for (int intentos = 0; intentos < 100 && !divisionAsignada; intentos++) {
                    String dia = seleccionarDiaAleatorio();

                    // Verificar que el día no ha sido usado previamente
                    if (diasUsados.contains(dia)) {
                        continue;
                    }

                    int[] bloquesDisponibles = encontrarBloquesConsecutivosDisponibles(
                            particula, dia, division, curso);

                    if (bloquesDisponibles == null) {
                        continue;
                    }

                    // Asignar bloques al día
                    for (int i = 0; i < division; i++) {
                        SlotHorario slot = new SlotHorario(
                                curso,
                                profesorSeleccionado,
                                aulaSeleccionada,
                                dia,
                                bloquesDisponibles[i],
                                bloquesDisponibles[i] + 1);
                        particula.getHorario().get(dia)[bloquesDisponibles[i]] = slot;
                    }

                    // Marcar día como usado y división como asignada
                    diasUsados.add(dia);
                    divisionAsignada = true;
                    bloquesRestantes -= division;
                }

                // Si no se pudo asignar la división, reportar error
                if (!divisionAsignada) {
                    System.out.println("No se pudo asignar todas las horas del curso: " + curso.getNombre());
                    break;
                }
            }

            // Asignar bloques restantes si hay alguno
            while (bloquesRestantes > 0) {
                boolean bloqueAsignado = false;

                for (int intentos = 0; intentos < 100 && !bloqueAsignado; intentos++) {
                    String dia = seleccionarDiaAleatorio();

                    // Verificar que el día no ha sido usado previamente
                    if (diasUsados.contains(dia)) {
                        continue;
                    }

                    int bloquesPorAsignar = Math.min(bloquesRestantes, 2);

                    int[] bloquesDisponibles = encontrarBloquesConsecutivosDisponibles(
                            particula, dia, bloquesPorAsignar, curso);

                    if (bloquesDisponibles == null) {
                        continue;
                    }

                    // Asignar bloques disponibles
                    for (int i = 0; i < bloquesPorAsignar; i++) {
                        SlotHorario slot = new SlotHorario(
                                curso,
                                profesorSeleccionado,
                                aulaSeleccionada,
                                dia,
                                bloquesDisponibles[i],
                                bloquesDisponibles[i] + 1);
                        particula.getHorario().get(dia)[bloquesDisponibles[i]] = slot;
                    }

                    diasUsados.add(dia);
                    bloquesRestantes -= bloquesPorAsignar;
                    bloqueAsignado = true;
                }

                if (!bloqueAsignado) {
                    System.out.println("No se pudo asignar todas las horas del curso: " + curso.getNombre());
                    break;
                }
            }
        }

        return particula;
    }

    // Método auxiliar para calcular bloques necesarios
    private int calcularBloquesNecesarios(Curso curso) {
        return curso.getHorasSemana();
    }

    // Método para seleccionar día aleatorio
    private String seleccionarDiaAleatorio() {
        String[] dias = { "Lunes", "Martes", "Miercoles", "Jueves", "Viernes" };
        return dias[new Random().nextInt(dias.length)];
    }

    // Método para seleccionar profesor calificado
    private Profesor seleccionarProfesorCalificado(Curso curso) {
        List<Profesor> profesoresCalificados = curso.getProfesoresCalificados();

        if (profesoresCalificados.isEmpty()) {
            return null;
        }

        // Filtrar profesores con horas disponibles
        List<Profesor> profesoresDisponibles = profesoresCalificados.stream()
                .filter(p -> p.getHorasMinimasSemana() > 0)
                .collect(Collectors.toList());

        return profesoresDisponibles.isEmpty()
                ? null
                : profesoresDisponibles.get(new Random().nextInt(profesoresDisponibles.size()));
    }

    // Método para seleccionar aula adecuada
    private Aula seleccionarAulaAdecuada(Curso curso) {
        // Filtrar aulas que pueden albergar el curso
        List<Aula> aulasApropiadas = aulas.stream()
                .filter(a -> a.getCapacidadMaxima() >= 30) // Ejemplo de filtro, ajustar según necesidad
                .collect(Collectors.toList());

        return aulasApropiadas.isEmpty()
                ? null
                : aulasApropiadas.get(new Random().nextInt(aulasApropiadas.size()));
    }

    // Método para encontrar bloques consecutivos disponibles
    private int[] encontrarBloquesConsecutivosDisponibles(
            Particula particula, String dia, int bloquesNecesarios, Curso curso) {

        // Determinar rango de bloques según el turno
        int inicioRango, finRango;
        if (curso.getTurno().equals("A")) {
            inicioRango = 0;
            finRango = 7; // Turno A: bloques de 0 a 7
        } else if (curso.getTurno().equals("B")) {
            inicioRango = 6;
            finRango = 15; // Turno B: bloques de 6 a 15
        } else if (curso.getTurno().equals("C")) {
            inicioRango = 0;
            finRango = 15; // Turno C: bloques de 0 a 15
        } else {
            throw new IllegalArgumentException("Turno no válido para el curso: " + curso.getNombre());
        }

        SlotHorario[] slotsDia = particula.getHorario().get(dia);

        // Iterar sobre los bloques dentro del rango permitido por el turno
        for (int inicio = inicioRango; inicio <= finRango - bloquesNecesarios + 1; inicio++) {
            boolean bloquesLibres = true;

            // Verificar si los bloques consecutivos están disponibles
            for (int i = 0; i < bloquesNecesarios; i++) {
                if (slotsDia[inicio + i] != null) { // Bloque ocupado
                    bloquesLibres = false;
                    break;
                }
            }

            // Si se encontraron bloques consecutivos disponibles
            if (bloquesLibres) {
                int[] bloques = new int[bloquesNecesarios];
                for (int i = 0; i < bloquesNecesarios; i++) {
                    bloques[i] = inicio + i;
                }
                return bloques;
            }
        }

        return null; // No se encontraron bloques consecutivos disponibles
    }

    // Método para validar restricciones de turno
    private boolean validarRestriccionesTurno(Curso curso, String dia, int[] bloques) {
        // Lógica para validar restricciones de turno
        // Por ejemplo, verificar que inicie a las 7 am para turno A y 2 pm para turno B
        if (curso.getTurno().equals("A") && bloques[0] != 0) {
            return false;
        }
        if (curso.getTurno().equals("B") && bloques[0] != 7) {
            return false;
        }

        return true;
    }


  private void actualizarPoblacion(List<Particula> poblacion, Particula mejorGlobal) {
    double w = 0.7;  // Factor de inercia
    double c1 = 1.4; // Factor cognitivo
    double c2 = 1.8; // Factor social (aumentado para dar más peso al mejor global)
    Random random = new Random();

    for (Particula particula : poblacion) {
        // Obtener lista de slots con profesores tipo A
        List<SlotInfo> slotsProfesorA = obtenerSlotsConProfesorA(particula);
        List<SlotInfo> slotsMejorGlobal = obtenerSlotsConProfesorA(mejorGlobal);

        // Calcular velocidad y actualizar posición para cada slot
        for (SlotInfo slotActual : slotsProfesorA) {
            // Encontrar el slot correspondiente en el mejor global
            SlotInfo slotMejorGlobal = encontrarSlotCorrespondiente(slotActual, slotsMejorGlobal);
            
            if (slotMejorGlobal != null) {
                // Calcular probabilidad de movimiento basada en PSO
                double velocidad = w * random.nextDouble() +
                                 c1 * random.nextDouble() * (distanciaEntrePosiciones(slotActual, slotMejorGlobal)) +
                                 c2 * random.nextDouble() * (distanciaEntrePosiciones(slotActual, slotMejorGlobal));

                if (random.nextDouble() < Math.abs(velocidad)) {
                    moverSlot(particula, slotActual, slotMejorGlobal);
                }
            }
        }
    }
}

private List<SlotInfo> obtenerSlotsConProfesorA(Particula particula) {
    List<SlotInfo> slots = new ArrayList<>();
    
    for (String dia : particula.getHorario().keySet()) {
        SlotHorario[] slotsDelDia = particula.getHorario().get(dia);
        for (int bloque = 0; bloque < slotsDelDia.length; bloque++) {
            if (slotsDelDia[bloque] != null && 
                slotsDelDia[bloque].getProfesor() instanceof SubprofesorA) {
                slots.add(new SlotInfo(
                    dia,
                    bloque,
                    slotsDelDia[bloque].getCurso(),
                    slotsDelDia[bloque].getProfesor(),
                    slotsDelDia[bloque].getAula()
                ));
            }
        }
    }
    return slots;
}

private SlotInfo encontrarSlotCorrespondiente(SlotInfo slotBuscado, List<SlotInfo> slots) {
    return slots.stream()
        .filter(s -> s.getCurso().equals(slotBuscado.getCurso()))
        .findFirst()
        .orElse(null);
}

private double distanciaEntrePosiciones(SlotInfo slot1, SlotInfo slot2) {
    // Calcular distancia normalizada entre dos posiciones
    int distanciaDias = Math.abs(
        obtenerIndiceDia(slot1.getDia()) - obtenerIndiceDia(slot2.getDia())
    );
    int distanciaBloques = Math.abs(slot1.getBloque() - slot2.getBloque());
    
    // Normalizar distancia a un valor entre 0 y 1
    return (distanciaDias + distanciaBloques) / 20.0; // 20 es un factor de normalización
}

private int obtenerIndiceDia(String dia) {
    String[] dias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};
    return Arrays.asList(dias).indexOf(dia);
}

private void moverSlot(Particula particula, SlotInfo slotOrigen, SlotInfo slotDestino) {
    Map<String, SlotHorario[]> horario = particula.getHorario();
    
    // Verificar si el destino está libre
    if (horario.get(slotDestino.getDia())[slotDestino.getBloque()] == null) {
        // Limpiar posición original
        horario.get(slotOrigen.getDia())[slotOrigen.getBloque()] = null;
        
        // Crear nuevo slot en la posición destino
        horario.get(slotDestino.getDia())[slotDestino.getBloque()] = new SlotHorario(
            slotOrigen.getCurso(),
            slotOrigen.getProfesor(),
            slotOrigen.getAula(),
            slotDestino.getDia(),
            slotDestino.getBloque(),
            slotDestino.getBloque() + 1
        );
    }
}

// Clase auxiliar para manejar la información de los slots
private static class SlotInfo {
    private final String dia;
    private final int bloque;
    private final Curso curso;
    private final Profesor profesor;
    private final Aula aula;

    public SlotInfo(String dia, int bloque, Curso curso, Profesor profesor, Aula aula) {
        this.dia = dia;
        this.bloque = bloque;
        this.curso = curso;
        this.profesor = profesor;
        this.aula = aula;
    }

    public String getDia() { return dia; }
    public int getBloque() { return bloque; }
    public Curso getCurso() { return curso; }
    public Profesor getProfesor() { return profesor; }
    public Aula getAula() { return aula; }
}


}

// Clase principal para probar el generador
public class PSOGeneradorHorario {

    public static void main(String[] args) {
        // // Profesores
        // Profesor profesor1 = new Profesor("PROF001", "Antonio Arroyo Paz",
        // Profesor.TipoPprofesor.SUBPROFESOR_A, 36,
        // 20);
        // Profesor profesor2 = new Profesor("PROF002", "Lucy Angela Delgado Barra",
        // Profesor.TipoPprofesor.SUBPROFESOR_A,
        // 30, 18);
        // Profesor profesor3 = new Profesor("PROF003", "Leticia Marisol Laura Ochoa",
        // Profesor.TipoPprofesor.SUBPROFESOR_B, 15, 10);
        // Profesor profesor4 = new Profesor("PROF004", "William Milton Bornas Rios",
        // Profesor.TipoPprofesor.SUBPROFESOR_A,
        // 36, 22);
        // Profesor profesor5 = new Profesor("PROF005", "Aníbal Sardón",
        // Profesor.TipoPprofesor.SUBPROFESOR_B, 20, 12);
        // Profesor profesor6 = new Profesor("PROF006", "Guevara Puente de la Vega",
        // Profesor.TipoPprofesor.SUBPROFESOR_A,
        // 30, 18);
        // Profesor profesor7 = new Profesor("PROF007", "Karim",
        // Profesor.TipoPprofesor.SUBPROFESOR_B, 15, 10);
        // Profesor profesor8 = new Profesor("PROF008", "Olha Sharhorodska",
        // Profesor.TipoPprofesor.SUBPROFESOR_A, 36, 22);
        // Profesor profesor9 = new Profesor("PROF009", "Juan Carlos Juarez Bueno",
        // Profesor.TipoPprofesor.SUBPROFESOR_A,
        // 40, 25);
        //
        // // Aulas
        // Aula aula1 = new Aula("AULA201", 40);
        // Aula aula2 = new Aula("AULA202", 35);
        // Aula aula3 = new Aula("AULA203", 45);
        // Aula aula4 = new Aula("AULA301", 30);
        // Aula aula5 = new Aula("AULA302", 30);
        // Aula aula6 = new Aula("AULA303", 30);
        //
        // // Cursos con Turno A, B y C
        // Curso cursoConstruccionSoftwareA = new Curso(
        // "CURSO001A", "Construcción de Software (Turno A)", 2,
        // Arrays.asList(profesor1), 1, "A");
        // Curso cursoConstruccionSoftwareB = new Curso(
        // "CURSO001B", "Construcción de Software (Turno B)", 2,
        // Arrays.asList(profesor1), 2, "B");
        // Curso cursoConstruccionSoftwareC = new Curso(
        // "CURSO001C", "Construcción de Software (Turno C)", 2,
        // Arrays.asList(profesor1), 3, "C");
        //
        // Curso cursoRedesA = new Curso(
        // "CURSO002A", "Redes y Comunicación de Datos (Turno A)", 4,
        // Arrays.asList(profesor2, profesor3), 1, "A");
        // Curso cursoRedesB = new Curso(
        // "CURSO002B", "Redes y Comunicación de Datos (Turno B)", 4,
        // Arrays.asList(profesor2, profesor3), 2, "B");
        // Curso cursoRedesC = new Curso(
        // "CURSO002C", "Redes y Comunicación de Datos (Turno C)", 4,
        // Arrays.asList(profesor2, profesor3), 3, "C");
        //
        // Curso cursoTeoriaObjetosA = new Curso(
        // "CURSO003A", "Teoría de Objetos (Turno A)", 4,
        // Arrays.asList(profesor4, profesor5), 1, "A");
        // Curso cursoTeoriaObjetosB = new Curso(
        // "CURSO003B", "Teoría de Objetos (Turno B)", 4,
        // Arrays.asList(profesor4, profesor5), 2, "B");
        // Curso cursoTeoriaObjetosC = new Curso(
        // "CURSO003C", "Teoría de Objetos (Turno C)", 4,
        // Arrays.asList(profesor4, profesor5), 3, "C");
        //
        // Curso cursoSistemasOperativosA = new Curso(
        // "CURSO004A", "Sistemas Operativos (Turno A)", 4,
        // Arrays.asList(profesor1, profesor6, profesor7), 1, "A");
        // Curso cursoSistemasOperativosB = new Curso(
        // "CURSO004B", "Sistemas Operativos (Turno B)", 4,
        // Arrays.asList(profesor1, profesor6, profesor7), 2, "B");
        // Curso cursoSistemasOperativosC = new Curso(
        // "CURSO004C", "Sistemas Operativos (Turno C)", 4,
        // Arrays.asList(profesor1, profesor6, profesor7), 3, "C");
        //
        // Curso cursoMetodosNumericosA = new Curso(
        // "CURSO005A", "Métodos Numéricos (Turno A)", 3,
        // Arrays.asList(profesor8), 1, "A");
        // Curso cursoMetodosNumericosB = new Curso(
        // "CURSO005B", "Métodos Numéricos (Turno B)", 3,
        // Arrays.asList(profesor8), 2, "B");
        // Curso cursoMetodosNumericosC = new Curso(
        // "CURSO005C", "Métodos Numéricos (Turno C)", 3,
        // Arrays.asList(profesor8), 3, "C");
        //
        // Curso cursoFundamentosSistemasA = new Curso(
        // "CURSO006A", "Fundamentos de Sistemas de Información (Turno A)", 5,
        // Arrays.asList(profesor9), 1, "A");
        // Curso cursoFundamentosSistemasB = new Curso(
        // "CURSO006B", "Fundamentos de Sistemas de Información (Turno B)", 5,
        // Arrays.asList(profesor9), 2, "B");
        // Curso cursoFundamentosSistemasC = new Curso(
        // "CURSO006C", "Fundamentos de Sistemas de Información (Turno C)", 5,
        // Arrays.asList(profesor9), 3, "C");
        //
        // // Crear lista con todos los turnos
        // List<Curso> cursos = Arrays.asList(
        // cursoConstruccionSoftwareA, cursoConstruccionSoftwareB,
        // cursoConstruccionSoftwareC,
        // cursoRedesA, cursoRedesB, cursoRedesC,
        // cursoTeoriaObjetosA, cursoTeoriaObjetosB, cursoTeoriaObjetosC,
        // cursoSistemasOperativosA, cursoSistemasOperativosB, cursoSistemasOperativosC,
        // cursoMetodosNumericosA, cursoMetodosNumericosB, cursoMetodosNumericosC,
        // cursoFundamentosSistemasA, cursoFundamentosSistemasB,
        // cursoFundamentosSistemasC);
        // List<Profesor> profesores = Arrays.asList(
        // profesor1, profesor2, profesor3, profesor4, profesor5,
        // profesor6, profesor7, profesor8, profesor9);
        // List<Aula> aulas = Arrays.asList(
        // aula1, aula2, aula3, aula4, aula5, aula6);

        // Cargar los datos desde los archivos CSV
        List<Profesor> profesores = LectorCSV.cargarProfesoresDesdeCSV("src/main/java/psogeneradorhorario/profesores.csv");
        List<Aula> aulas = LectorCSV.cargarAulasDesdeCSV("src/main/java/psogeneradorhorario/aulas.csv");
        List<Curso> cursos = LectorCSV.cargarCursosDesdeCSV("src/main/java/psogeneradorhorario/cursos.csv",
                new ArrayList<>(profesores));
        //List<Profesor> profesores = LectorCSV.cargarProfesoresDesdeCSV("psogeneradorhorario/profesores.csv");
        //List<Aula> aulas = LectorCSV.cargarAulasDesdeCSV("psogeneradorhorario/aulas.csv");
        //List<Curso> cursos = LectorCSV.cargarCursosDesdeCSV("psogeneradorhorario/cursos.csv",
        //        new ArrayList<>(profesores));

        // Verificar los datos cargados
        System.out.println("Profesores cargados: " + profesores.size());
        System.out.println("Aulas cargadas: " + aulas.size());
        System.out.println("Cursos cargados: " + cursos.size());

        // Crear el generador de horarios
        GeneradorHorariosPSO generador = new GeneradorHorariosPSO(cursos, profesores, aulas);

        // Generar un horario (partícula)
        Particula horarioGenerado = generador.generarParticulaInicial();

        // Imprimir los detalles del horario generado
        imprimirDetallesParticula(horarioGenerado);
        SwingUtilities.invokeLater(() -> {
            HorarioVisualizer visualizer = new HorarioVisualizer(horarioGenerado);
            visualizer.setVisible(true);
        });
        horarioGenerado.evaluarFitness();
        System.out.println("Fitnes antes de actualizar 1: " + horarioGenerado.getFitness());
        
        Particula horarioGenerado2 = generador.generarMejorHorario();
        imprimirDetallesParticula(horarioGenerado2);
        horarioGenerado2.evaluarFitness();
        System.out.println("Fitnes antes de actualizar 1: " + horarioGenerado2.getFitness());

    }

    // Método para imprimir los detalles de la partícula
    private static void imprimirDetallesParticula(Particula particula) {
        if (particula == null) {
            System.out.println("No se pudo generar la partícula.");
            return;
        }

        System.out.println("Detalles de la Partícula Generada:");

        // Iterar por cada día
        for (Map.Entry<String, SlotHorario[]> diaEntry : particula.getHorario().entrySet()) {
            String dia = diaEntry.getKey();
            SlotHorario[] slots = diaEntry.getValue();

            System.out.println("\nDía: " + dia);

            // Imprimir slots ocupados
            for (int i = 0; i < slots.length; i++) {
                if (slots[i] != null) {
                    System.out.printf("Bloque %d: Curso %s, Profesor %s, Aula %s%n",
                            i,
                            slots[i].getCurso().getNombre(),
                            slots[i].getProfesor().getNombre(),
                            slots[i].getAula().getId());
                }
            }
        }
    }
}
