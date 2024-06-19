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
    private JTextField textField, tfOrigen, tfDestino;
    private JButton generateButton;
    private JButton botonEjecutar;
    private JTable table;
    private JScrollPane scrollPane;

    public static final int width = 900;
    public static final int height = 600;

    Color gris = new Color(30, 30, 40);
    Color fondo = new Color(61, 97, 128);
    Color encabezado = new Color (66, 139, 203);
    Color celdaImpar = new Color(178, 232, 244);
    Color celdaPar = new Color(215, 239, 244);
    Color celdaBloqueada = new Color(244, 244, 244);
    
    public VariableTableExample() {
        // Configuración básica de la ventana
        setTitle("Variable Table Example");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        contentPane = new JPanel();
		contentPane.setBackground(fondo);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
        
        // Panel para ingresar número de nodos
        JPanel panel = new JPanel();
		panel.setOpaque(false);
		contentPane.add(panel, BorderLayout.NORTH);
		
        JLabel lblNewLabel = new JLabel("Número de nodos");
		lblNewLabel.setForeground(gris);
        panel.add(lblNewLabel);
		textField = new JTextField();
        textField.setText("5");
		panel.add(textField);
		textField.setColumns(10);
		
        generateButton = new JButton("Generar Tabla");
		panel.add(generateButton);

        // Panel para ingresar nodo de origen y destino
        JPanel panelSouth = new JPanel();
		panelSouth.setOpaque(false);
		contentPane.add(panelSouth, BorderLayout.SOUTH);

        JLabel lblOrigen = new JLabel("Nodo Origen: ");
        lblOrigen.setForeground(gris);
		panelSouth.add(lblOrigen);
		tfOrigen = new JTextField();
		panelSouth.add(tfOrigen);
		tfOrigen.setColumns(10);

        JLabel lblDestino = new JLabel("Nodo Destino: ");
        lblDestino.setForeground(gris);
		panelSouth.add(lblDestino);
		tfDestino = new JTextField();
		panelSouth.add(tfDestino);
		tfDestino.setColumns(10);

        botonEjecutar = new JButton("Ejecutar");
        botonEjecutar.setEnabled(false);
		panelSouth.add(botonEjecutar);

        // Inicializar tabla y agregar al JScrollPane
        table = new JTable();
        table.setBackground(fondo);

        scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(fondo);
		contentPane.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setBackground(encabezado);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setBorder(null);
        table.getTableHeader().setBorder(null);
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
                data[i-1][0] = "Nodo " + (i);
            }

            /*for (int i = 0; i < rows; i++)
                data[i][0] = "Nodo " + (i + 1);*/

            // Crear un modelo de tabla personalizado
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Deshabilitar la celda (1,1) - filas y columnas son 0-indexed
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
                    /*Border border = BorderFactory.createLineBorder(Color.RED); // Borde negro interno
                    ((JComponent) c).setBorder(border);*/

                    if(column == 0) {
                        c.setBackground(encabezado);
                        c.setForeground(Color.WHITE);
                        setHorizontalAlignment(JLabel.CENTER);
                        /*border = BorderFactory.createLineBorder(encabezado); // Borde negro interno
                        ((JComponent) c).setBorder(border);*/
                        return c;
                    }else {
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

        if(tfOrigen.getText().isEmpty() || tfDestino.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese los nodos origen y destino.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los datos de la tabla
        String salida = "";
        for (int i=0; i<table.getRowCount(); i++) {
            for (int j=i+1; j<table.getRowCount(); j++) {
                Object cellValue = table.getValueAt(i, j+1);
                if(cellValue != null && cellValue != "")
                    salida += (i+1)+","+(j+1)+","+cellValue+"\n";
            }
        }

        // Escribir los datos de la tabla en un archivo comma separated values
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(".\\src\\python\\rutas.csv"))) {
            writer.write(salida);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo de rutas: " + e.getMessage());
        }

        // Escribir los datos de la tabla en un archivo comma separated values
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(".\\src\\python\\origen.csv"))) {
            writer.write("origen,"+tfOrigen.getText()+"\ndestino,"+tfDestino.getText());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo de nodos: " + e.getMessage());
        }

        // Ejecutar el script de python
        String[] command = {"python", "src/python/ruta.py",textField.getText()};
        System.out.println(command[1]);
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
