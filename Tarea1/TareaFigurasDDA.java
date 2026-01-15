import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TareaFigurasDDA extends JFrame implements ActionListener {

    private JButton btnTriangulo, btnCuadrado, btnTrapecio;
    private DibujoPanel panel;
    private int x1, y1, x2, y2; // Coordenadas base

    public TareaFigurasDDA() {
        super("Tarea Figuras con Algoritmo DDA");

        // ---------- Pedir coordenadas ----------
        try {
            x1 = Integer.parseInt(JOptionPane.showInputDialog("Ingrese X1 (por ejemplo 200):"));
            y1 = Integer.parseInt(JOptionPane.showInputDialog("Ingrese Y1 (por ejemplo 400):"));
            x2 = Integer.parseInt(JOptionPane.showInputDialog("Ingrese X2 (por ejemplo 400):"));
            y2 = Integer.parseInt(JOptionPane.showInputDialog("Ingrese Y2 (por ejemplo 400):"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
            "Coordenadas no válidas. Se usarán valores por defecto.");
            x1 = 200; y1 = 400; x2 = 400; y2 = 400;
        }

        // ---------- Configurar interfaz ----------
        btnTriangulo = new JButton("Triángulo");
        btnCuadrado = new JButton("Cuadrado");
        btnTrapecio = new JButton("Trapecio");

        btnTriangulo.addActionListener(this);
        btnCuadrado.addActionListener(this);
        btnTrapecio.addActionListener(this);

        JPanel menu = new JPanel();
        menu.add(btnTriangulo);
        menu.add(btnCuadrado);
        menu.add(btnTrapecio);

        panel = new DibujoPanel();

        this.add(menu, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);

        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTriangulo) panel.setFigura("triangulo", x1, y1, x2, y2);
        if (e.getSource() == btnCuadrado) panel.setFigura("cuadrado", x1, y1, x2, y2);
        if (e.getSource() == btnTrapecio) panel.setFigura("trapecio", x1, y1, x2, y2);
        panel.repaint();
    }

    public static void main(String[] args) {
        new TareaFigurasDDA();
    }
}

// =================== PANEL DE DIBUJO ===================
class DibujoPanel extends JPanel {
    private String figura = "";
    private int x1, y1, x2, y2;

    public void setFigura(String figura, int x1, int y1, int x2, int y2) {
        this.figura = figura;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        switch (figura) {
            case "triangulo":
                dibujarTriangulo(g);
                break;
            case "cuadrado":
                dibujarCuadrado(g);
                break;
            case "trapecio":
                dibujarTrapecio(g);
                break;
        }
    }

    // ---------- Algoritmo DDA ----------
    private void lineaDDA(Graphics g, int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        int pasos = Math.max(Math.abs(dx), Math.abs(dy));

        float incX = (float) dx / pasos;
        float incY = (float) dy / pasos;

        float x = x1;
        float y = y1;

        for (int i = 0; i <= pasos; i++) {
            g.drawLine(Math.round(x), 
            Math.round(y), 
            Math.round(x), 
            Math.round(y));
            x += incX;
            y += incY;
        }
    }

    // ---------- Figuras ----------
    private void dibujarTriangulo(Graphics g) {
        int baseX1 = x1, baseY1 = y1;
        int baseX2 = x2, baseY2 = y2;
        int altura = 150;

        int medioX = (baseX1 + baseX2) / 2;
        int picoY = baseY1 - altura;

        lineaDDA(g, baseX1, baseY1, baseX2, baseY2);
        lineaDDA(g, baseX1, baseY1, medioX, picoY);
        lineaDDA(g, baseX2, baseY2, medioX, picoY);
    }

    private void dibujarCuadrado(Graphics g) {
        int baseX1 = x1, baseY1 = y1;
        int baseX2 = x2, baseY2 = y2;
        int lado = baseX2 - baseX1;
        int arribaY = baseY1 - lado;

        lineaDDA(g, baseX1, baseY1, baseX2, baseY2);  // base inferior
        lineaDDA(g, baseX1, baseY1, baseX1, arribaY);  // lado izq
        lineaDDA(g, baseX2, baseY2, baseX2, arribaY);  // lado der
        lineaDDA(g, baseX1, arribaY, baseX2, arribaY); // base superior
    }

    private void dibujarTrapecio(Graphics g) {
        int baseX1 = x1, baseY1 = y1;
        int baseX2 = x2, baseY2 = y2;
        int altura = 120;
        int margen = 60;

        int arribaX1 = baseX1 + margen;
        int arribaX2 = baseX2 - margen;
        int arribaY = baseY1 - altura;

        lineaDDA(g, baseX1, baseY1, baseX2, baseY2);       // base inferior
        lineaDDA(g, baseX1, baseY1, arribaX1, arribaY);     // lado izq
        lineaDDA(g, baseX2, baseY2, arribaX2, arribaY);     // lado der
        lineaDDA(g, arribaX1, arribaY, arribaX2, arribaY);  // base superior
    }
}
