import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class TimbiricheGUI extends JFrame {

    private Tablero tablero;
    private JTable tabla;
    private DefaultTableModel modelo;
    private Random random = new Random();

    public TimbiricheGUI() {
        setTitle("Timbiriche (Colchones)");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // -------- Panel superior --------
        JPanel top = new JPanel();
        JLabel lbl = new JLabel("Número de jugadas:");
        JTextField txtJugadas = new JTextField(5);
        JButton btn = new JButton("Simular");

        top.add(lbl);
        top.add(txtJugadas);
        top.add(btn);
        add(top, BorderLayout.NORTH);

        // -------- Tablero --------
        tablero = new Tablero();
        add(tablero, BorderLayout.CENTER);

        // -------- Tabla --------
        modelo = new DefaultTableModel(
                new String[]{"Jugada", "num", "num2", "num3", "Columna", "Fila", "Tipo"}, 0);
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.EAST);

        // -------- Acción --------
        btn.addActionListener(e -> {
            modelo.setRowCount(0);
            tablero.reset();
            int jugadas = Integer.parseInt(txtJugadas.getText());
            simular(jugadas);
        });
    }

    // ===============================
    // SIMULACIÓN SEGÚN PDF
    // ===============================
    private void simular(int jugadas) {
        for (int j = 1; j <= jugadas; j++) {

            int num = random.nextInt(100);
            int num2 = random.nextInt(100);
            int num3 = random.nextInt(100);

            if (num % 2 == 0) {
                int col = num2 % 10;
                int fila = num3 % 11;
                tablero.addLinea(col, fila, col + 1, fila);
                modelo.addRow(new Object[]{j, num, num2, num3, col, fila, "Horizontal"});
            } else {
                int col = num2 % 11;
                int fila = num3 % 10;
                tablero.addLinea(col, fila, col, fila + 1);
                modelo.addRow(new Object[]{j, num, num2, num3, col, fila, "Vertical"});
            }
        }
        tablero.repaint();
    }

    // ===============================
    // TABLERO GRÁFICO
    // ===============================
    class Tablero extends JPanel {

        private final int TAM = 40;
        private Set<String> lineas = new HashSet<>();
        private Set<String> cuadros = new HashSet<>();

        void reset() {
            lineas.clear();
            cuadros.clear();
        }

        void addLinea(int x1, int y1, int x2, int y2) {
            lineas.add(x1 + "," + y1 + "," + x2 + "," + y2);
            verificarCuadros();
        }

        private void verificarCuadros() {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    String arriba = x + "," + y + "," + (x + 1) + "," + y;
                    String abajo = x + "," + (y + 1) + "," + (x + 1) + "," + (y + 1);
                    String izq = x + "," + y + "," + x + "," + (y + 1);
                    String der = (x + 1) + "," + y + "," + (x + 1) + "," + (y + 1);

                    if (lineas.contains(arriba) && lineas.contains(abajo)
                            && lineas.contains(izq) && lineas.contains(der)) {
                        cuadros.add(x + "," + y);
                    }
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Dibujar cuadros cerrados
            g.setColor(new Color(173, 216, 230));
            for (String c : cuadros) {
                String[] p = c.split(",");
                int x = Integer.parseInt(p[0]);
                int y = Integer.parseInt(p[1]);
                g.fillRect(x * TAM + 52, y * TAM + 52, TAM - 4, TAM - 4);
            }

            // Dibujar puntos
            g.setColor(Color.BLACK);
            for (int i = 0; i <= 11; i++) {
                for (int j = 0; j <= 11; j++) {
                    g.fillOval(i * TAM + 50, j * TAM + 50, 5, 5);
                }
            }

            // Dibujar líneas con DDA
            g.setColor(Color.BLUE);
            for (String l : lineas) {
                String[] p = l.split(",");
                dda(g,
                        Integer.parseInt(p[0]) * TAM + 50,
                        Integer.parseInt(p[1]) * TAM + 50,
                        Integer.parseInt(p[2]) * TAM + 50,
                        Integer.parseInt(p[3]) * TAM + 50);
            }
        }

        // ===============================
        // DDA
        // ===============================
        private void dda(Graphics g, int x1, int y1, int x2, int y2) {
            int dx = x2 - x1;
            int dy = y2 - y1;
            int pasos = Math.max(Math.abs(dx), Math.abs(dy));

            float xInc = dx / (float) pasos;
            float yInc = dy / (float) pasos;

            float x = x1;
            float y = y1;

            for (int i = 0; i <= pasos; i++) {
                g.fillOval(Math.round(x), Math.round(y), 3, 3);
                x += xInc;
                y += yInc;
            }
        }
    }

    // ===============================
    // MAIN
    // ===============================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TimbiricheGUI().setVisible(true));
    }
}
