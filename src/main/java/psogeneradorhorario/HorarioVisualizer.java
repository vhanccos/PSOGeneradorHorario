package psogeneradorhorario;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class HorarioVisualizer extends JFrame {

    public HorarioVisualizer(Particula particula) {
        setTitle("Mejor Horario Generado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Crear el modelo de tabla
        DefaultTableModel model = new DefaultTableModel();

        // Obtener el horario de la particula
        Map<String, SlotHorario[]> horario = particula.getHorario();

        // Ordenar los días en el orden deseado
        String[] diasSemana = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};
        Map<String, SlotHorario[]> horarioOrdenado = new LinkedHashMap<>();
        for (String dia : diasSemana) {
            horarioOrdenado.put(dia, horario.getOrDefault(dia, new SlotHorario[16]));
        }

        // Configurar las columnas
        model.addColumn("Hora / Día");
        for (String dia : diasSemana) {
            model.addColumn(dia);
        }

        // Determinar el número máximo de bloques (16 en este caso)
        int numBloques = 16;

        // Generar las horas correspondientes a los bloques
        String[] horas = generarHoras(numBloques);

        // Poblar la tabla con datos
        for (int i = 0; i < numBloques; i++) {
            Object[] row = new Object[diasSemana.length + 1];
            row[0] = horas[i]; // Primera columna con la hora correspondiente
            int diaIndex = 1; // Índice de la columna correspondiente al día

            for (String dia : diasSemana) {
                SlotHorario[] slots = horarioOrdenado.get(dia);
                if (slots != null && i < slots.length && slots[i] != null) {
                    SlotHorario slot = slots[i];
                    String turno = slot.getCurso().getTurno();  // Obtener el turno del curso (Turno A, Turno B)

                    // Formato de presentación con un solo turno
                    row[diaIndex] = String.format(
                            "<html><b>%s (%s)</b><br>(%s, %s)</html>",
                            slot.getCurso().getNombre(),
                            turno,  // Solo se muestra un turno
                            slot.getProfesor().getNombre(),
                            slot.getAula().getId()
                    );
                } else {
                    row[diaIndex] = "<html><i>Libre</i></html>"; // Indicar slot libre
                }
                diaIndex++;
            }
            model.addRow(row);
        }

        // Crear la tabla
        JTable table = new JTable(model);
        table.setRowHeight(60); // Ajustar altura de las filas
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value != null ? value.toString() : "", JLabel.CENTER);
                label.setOpaque(true);
                label.setFont(new Font("SansSerif", Font.PLAIN, 12));
                label.setVerticalAlignment(JLabel.TOP);

                if (isSelected) {
                    label.setBackground(new Color(184, 207, 229));
                } else {
                    label.setBackground(Color.WHITE);
                }

                return label;
            }
        });

        // Añadir la tabla a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Método para generar las horas de los bloques
    private String[] generarHoras(int numBloques) {
        String[] horas = new String[numBloques];
        int inicioHora = 7;
        int inicioMinuto = 0;

        for (int i = 0; i < numBloques; i++) {
            int finHora = inicioHora;
            int finMinuto = inicioMinuto + 50;
            if (finMinuto >= 60) {
                finHora++;
                finMinuto -= 60;
            }
            horas[i] = String.format("%02d:%02d-%02d:%02d", inicioHora, inicioMinuto, finHora, finMinuto);

            // Incrementar tiempo para el siguiente bloque
            inicioMinuto += 50;
            if (inicioMinuto >= 60) {
                inicioHora++;
                inicioMinuto -= 60;
            }

            // Agregar 10 minutos cada dos bloques
            if ((i + 1) % 2 == 0) {
                inicioMinuto += 10;
                if (inicioMinuto >= 60) {
                    inicioHora++;
                    inicioMinuto -= 60;
                }
            }
        }
        return horas;
    }
}
