/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosinterface;

/**
 *
 * @author arles
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvManager {
    private final String filePath;

    public CsvManager(String filePath) {
        this.filePath = filePath;
    }

    // Leer datos del CSV
    public List<String[]> readCsv() throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        }
        return data;
    }

    // Escribir datos al CSV
    public void writeCsv(List<String[]> data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        }
    }
}
