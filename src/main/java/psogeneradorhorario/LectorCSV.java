package psogeneradorhorario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import psogeneradorhorario.Profesor.TipoPprofesor;

public class LectorCSV {

  public static List<String[]> leerCSV(String filePath) throws IOException {
    List<String[]> registros = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String linea;

      // Leer líneas del archivo
      while ((linea = br.readLine()) != null) {
        // Dividir línea en columnas
        String[] datos = linea.split(",");
        registros.add(datos);
      }
    }

    return registros;
  }

  public static List<Profesor> cargarProfesoresDesdeCSV(String filePath) {
    List<Profesor> profesores = new ArrayList<>();

    try {
      List<String[]> registros = LectorCSV.leerCSV(filePath);

      for (String[] registro : registros) {
        // Crear instancia de Profesor desde columnas del CSV
        String id = registro[0];
        String nombre = registro[1];
        String tipo = registro[2];
        int horasMaximasSemana = Integer.parseInt(registro[3]);
        int horasMinimasSemana = Integer.parseInt(registro[4]);

        Profesor.TipoPprofesor tipoProfesor = Profesor.TipoPprofesor.SUBPROFESOR_A;
        if (tipo.equals("A")) {
          tipoProfesor = Profesor.TipoPprofesor.SUBPROFESOR_A;
        } else if (tipo.equals("B")) {
          tipoProfesor = Profesor.TipoPprofesor.SUBPROFESOR_B;
        }

        Profesor profesor = new Profesor(id, nombre, tipoProfesor, horasMaximasSemana,
            horasMinimasSemana);
        profesores.add(profesor);
      }
    } catch (IOException e) {
      System.err.println("Error al leer el archivo CSV de profesores: " + e.getMessage());
    }

    return profesores;
  }

  public static List<Aula> cargarAulasDesdeCSV(String filePath) {
    List<Aula> aulas = new ArrayList<>();

    try {
      List<String[]> registros = LectorCSV.leerCSV(filePath);

      for (String[] registro : registros) {
        // Crear instancia de Aula desde columnas del CSV
        String id = registro[0];
        int capacidad = Integer.parseInt(registro[1]);

        Aula aula = new Aula(id, capacidad);
        aulas.add(aula);
      }
    } catch (IOException e) {
      System.err.println("Error al leer el archivo CSV de aulas: " + e.getMessage());
    }

    return aulas;
  }

  public static List<Curso> cargarCursosDesdeCSV(String filePath, ArrayList<Profesor> docentes) {
    List<Curso> cursos = new ArrayList<>();

    try {
      List<String[]> registros = LectorCSV.leerCSV(filePath);

      for (String[] registro : registros) {
        // Crear instancia de Curso desde columnas del CSV
        String id = registro[0];
        String nombre = registro[1];
        int horasSemanales = Integer.parseInt(registro[2]);
        List<String> IdsDocentesCalificados = List.of(registro[3].split(";"));
        int ciclo = Integer.parseInt(registro[4]);
        String turno = registro[5];

        // buscar en la lista de todos los docentes el id y colocarlos en una lista de
        // docentesCalificados
        List<Profesor> docentesCalificados = IdsDocentesCalificados.stream().map(docenteId -> {
          Profesor docente = Profesor.buscarProfesorPorId(docenteId, docentes);
          return docente;
        }).toList();

        Curso curso = new Curso(id, nombre, horasSemanales, docentesCalificados, ciclo, turno);
        cursos.add(curso);
      }
    } catch (IOException e) {
      System.err.println("Error al leer el archivo CSV de cursos: " + e.getMessage());
    }

    return cursos;
  }

}
