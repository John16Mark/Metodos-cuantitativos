package def;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class VariableTableExample extends JFrame {
    private JPanel contentPane;
    private JTextField textField;
    private JButton generateButton;
    private JButton botonEjecutar;
    private JTable table;
    private JScrollPane scrollPane;

    Color fondo = new Color(61, 97, 128);
    Color encabezado = new Color (66, 139, 203);
    Color celdaImpar = new Color(178, 232, 244);
    Color celdaPar = new Color(215, 239, 244);
    Color celdaBloqueada = new Color(244, 244, 244);
    
    public VariableTableExample() {
        // Configuración básica de la ventana
        setTitle("Variable Table Example");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        contentPane = new JPanel();
		contentPane.setBackground(fondo);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panelFondo = new JPanel();
		panelFondo.setOpaque(false);
		contentPane.add(panelFondo, BorderLayout.SOUTH);
        botonEjecutar = new JButton("Ejecutar");
        botonEjecutar.setEnabled(false);
		panelFondo.add(botonEjecutar);
        
        // Panel para ingresar filas y columnas
        JPanel panel = new JPanel();
		panel.setOpaque(false);
		contentPane.add(panel, BorderLayout.NORTH);
		
        JLabel lblNewLabel = new JLabel("New label");
		panel.add(lblNewLabel);
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		
        generateButton = new JButton("New button");
		panel.add(generateButton);

        // Inicializar tabla y agregar al JScrollPane
        table = new JTable();
        table.setBackground(fondo);

        scrollPane = new JScrollPane(table);
		//panel_1.setOpaque(false);
        scrollPane.getViewport().setBackground(fondo);
		contentPane.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        /*scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.setBackground(fondo);*/
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setBackground(encabezado);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setBorder(null); // Borde rojo para la tabla
        table.getTableHeader().setBorder(null); // Borde azul para el encabezado
        table.setIntercellSpacing(new Dimension(0,0));

        // Acción del botón para generar la tabla
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTable();
            }
        });

        botonEjecutar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutar();
            }
        });

        // Mostrar la ventana
        setVisible(true);
    }

    public void generateTable() {
        try {
            int rows = Integer.parseInt(textField.getText());

            Object[][] data = new Object[rows][rows + 1]; // +1 para la columna de nombres de filas
            String[] columnNames = new String[rows + 1];
            columnNames[0] = "";

            for (int i = 1; i <= rows; i++) {
                columnNames[i] = "Nodo " + (i);
            }
            for (int i = 0; i < rows; i++) {
                data[i][0] = "Nodo " + (i + 1);
            }

            // Crear un modelo de tabla personalizado
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Deshabilitar la celda (1,1) - filas y columnas son 0-indexed
                    if (column <= row+1) {
                        return false;
                    }
                    if (column == 0) {
                        return false;
                    }
                    return true;
                }
            };
            table.setModel(model);

            // Renderizador personalizado para cambiar el color de las celdas deshabilitadas
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    Border border = BorderFactory.createLineBorder(Color.RED); // Borde negro interno
                    ((JComponent) c).setBorder(border);

                    if(column == 0) {
                        c.setBackground(encabezado);
                        c.setForeground(Color.WHITE);
                        setHorizontalAlignment(JLabel.CENTER);
                        border = BorderFactory.createLineBorder(encabezado); // Borde negro interno
                        ((JComponent) c).setBorder(border);
                        return c;
                    }
                    if (!model.isCellEditable(row, column)) {
                        c.setBackground(celdaBloqueada);
                    } else {
                        c.setForeground(Color.BLACK);
                        setHorizontalAlignment(JLabel.LEFT);
                        if(row % 2 == 0)
                            c.setBackground(celdaImpar);
                        else
                            c.setBackground(celdaPar);
                    }
                    return c;
                }
            });

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos para filas y columnas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        botonEjecutar.setEnabled(true);
    }

    public void generateTable(int tam) {
        try {
            Object[][] data = new Object[tam][tam];
            String[] columnNames = new String[tam];

            for (int i = 0; i < tam; i++) {
                columnNames[i] = "Columna " + (i + 1);
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos para filas y columnas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void ejecutar() {

        // Obtener los datos de la tabla
        String salida = "";
        for (int i=0; i<table.getRowCount(); i++) {
            for (int j=i+1; j<table.getRowCount(); j++) {
                Object cellValue = table.getValueAt(i, j+1);
                if(cellValue != null && cellValue != "") {
                    System.out.println(i+", "+(j+1)+"= "+ cellValue);
                    salida += (i+1)+","+(j+1)+","+cellValue+"\n";
                }
                
            }
        }

        // Escribir los datos de la tabla en un archivo comma separated values
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(".\\src\\python\\rutas.csv"))) {
            writer.write(salida);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + e.getMessage());
        }

        // Ejecutar el script de python
        String[] command = {"python", "src/python/ruta.py"};
		ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();

            String line;
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // Esperar a que el proceso termine y obtener el código de salida
            int exitCode = process.waitFor();
            System.out.println("El script Python terminó con código: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
