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
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class CrudInterface extends JFrame {
    private final CsvManager csvManager;
    private final DefaultTableModel tableModel;
    private final JTextField idField;
    private final JTextField nombreField;
    private final JComboBox<String> turnoCombo;
    private final JTextField profesoresField;
    private final JTextField horasField;
    private final JTextField cicloField;
    private final JTextField duracionField;

    public CrudInterface(String csvFilePath) {
        csvManager = new CsvManager(csvFilePath);
        setTitle("Gestor de CSV - CRUD");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Márgenes generales
        add(mainPanel);

        // Tabla y scroll
        tableModel = new DefaultTableModel(new String[]{
                "ID", "Nombre", "TurnoId", "Profesores_ids", "HorasSemana", "Ciclo", "DuracionMinutos"
        }, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de entrada de datos
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Datos de Materias"));
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Crear campos de entrada
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField();
        idField.setPreferredSize(new Dimension(200, 25)); // Aumentar el tamaño del TextField
        idField.setEnabled(true); // Habilitado por defecto

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField();
        nombreField.setPreferredSize(new Dimension(200, 25));

        JLabel turnoLabel = new JLabel("Turno:");
        turnoCombo = new JComboBox<>(new String[]{"1", "2", "3"});
        turnoCombo.setPreferredSize(new Dimension(200, 25));

        JLabel profesoresLabel = new JLabel("Profesores:");
        profesoresField = new JTextField();
        profesoresField.setPreferredSize(new Dimension(200, 25));

        JLabel horasLabel = new JLabel("Horas Semana:");
        horasField = new JTextField();
        horasField.setPreferredSize(new Dimension(200, 25));

        JLabel cicloLabel = new JLabel("Ciclo:");
        cicloField = new JTextField();
        cicloField.setPreferredSize(new Dimension(200, 25));

        JLabel duracionLabel = new JLabel("Duración (Min):");
        duracionField = new JTextField();
        duracionField.setPreferredSize(new Dimension(200, 25));

        // Añadir componentes al panel de entrada
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(idLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(nombreLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(nombreField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(turnoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; inputPanel.add(turnoCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(profesoresLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; inputPanel.add(profesoresField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(horasLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; inputPanel.add(horasField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; inputPanel.add(cicloLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5; inputPanel.add(cicloField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; inputPanel.add(duracionLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 6; inputPanel.add(duracionField, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton addButton = new JButton("Agregar");
        JButton updateButton = new JButton("Actualizar");
        JButton deleteButton = new JButton("Eliminar");
        JButton loadButton = new JButton("Mostrar");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(loadButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Eventos de botones
        addButton.addActionListener(e -> {
            String newId = idField.getText();
            if (isIdInUse(newId)) {
                JOptionPane.showMessageDialog(this, "Este ID ya está en uso. Elija otro ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String[] row = {
                        idField.getText(),
                        nombreField.getText(),
                        (String) turnoCombo.getSelectedItem(),
                        profesoresField.getText(),
                        horasField.getText(),
                        cicloField.getText(),
                        duracionField.getText()
                };
                tableModel.addRow(row);
                saveCsv();
                clearFields();
            }
        });

        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.setValueAt(idField.getText(), selectedRow, 0);
                tableModel.setValueAt(nombreField.getText(), selectedRow, 1);
                tableModel.setValueAt(turnoCombo.getSelectedItem(), selectedRow, 2);
                tableModel.setValueAt(profesoresField.getText(), selectedRow, 3);
                tableModel.setValueAt(horasField.getText(), selectedRow, 4);
                tableModel.setValueAt(cicloField.getText(), selectedRow, 5);
                tableModel.setValueAt(duracionField.getText(), selectedRow, 6);
                saveCsv();
                clearFields();
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.removeRow(selectedRow);
                saveCsv();
                clearFields();
            }
        });

        loadButton.addActionListener(e -> loadCsv());

        // Evento para cargar datos de la fila seleccionada
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    nombreField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    turnoCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
                    profesoresField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    horasField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    cicloField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                    duracionField.setText(tableModel.getValueAt(selectedRow, 6).toString());

                    // Deshabilitar el campo ID para edición
                    idField.setEnabled(false);
                }
            }
        });

        loadCsv();
    }

    private void loadCsv() {
        try {
            tableModel.setRowCount(0); // Limpiar tabla
            List<String[]> data = csvManager.readCsv();
            for (String[] row : data) {
                tableModel.addRow(row);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar el archivo CSV: " + ex.getMessage());
        }
    }

    private void saveCsv() {
        try {
            List<String[]> data = new Vector<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Vector<String> row = new Vector<>();
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    row.add((String) tableModel.getValueAt(i, j));
                }
                data.add(row.toArray(new String[0]));
            }
            csvManager.writeCsv(data);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo CSV: " + ex.getMessage());
        }
    }

    private boolean isIdInUse(String id) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private void clearFields() {
        idField.setText("");
        nombreField.setText("");
        turnoCombo.setSelectedIndex(0);
        profesoresField.setText("");
        horasField.setText("");
        cicloField.setText("");
        duracionField.setText("");
        idField.setEnabled(true); // Habilitar el campo ID nuevamente
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CrudInterface("materias.csv").setVisible(true));
    }
}
