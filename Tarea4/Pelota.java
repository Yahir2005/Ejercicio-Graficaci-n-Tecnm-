package Tarea6;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; // Import necesario para la lista
import java.util.Random;
import javax.swing.*;

public class Pelota extends JFrame implements ActionListener {
    private JButton iniciar;
    private DibujoPanel panel;
    
    private Timer timerCaida;
    private Timer timerRebote;
    
    // Variables DDA
    private float ddaX, ddaY, incX, incY;
    private int pasos, contadorPasos;
    
    // Variables Rebote
    private int anguloActual;
    private int radioActual;
    private int centroReboteX;
    
    private final int SUELO_Y = 550; 
    private int inicioReboteX;       

    public Pelota() {
        super("Simulación: DDA Visible y Rebotes");
        iniciar = new JButton("Iniciar Simulación");
        iniciar.addActionListener(this);

        JPanel menu = new JPanel();
        menu.add(iniciar);

        panel = new DibujoPanel();
        panel.setCentro(40, 40); 

        this.add(menu, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);

        this.setSize(900, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == iniciar) {
            iniciarCaida();
        }
    }

    private void iniciarCaida() {
        if (timerCaida != null && timerCaida.isRunning()) timerCaida.stop();
        if (timerRebote != null && timerRebote.isRunning()) timerRebote.stop();

        // Limpiamos el rastro anterior al iniciar de nuevo
        panel.limpiarRastro();

        Random rand = new Random();
        int A = 40 + rand.nextInt(60); 

        int x1 = 40, y1 = 40;
        int x2 = A,  y2 = SUELO_Y; 

        int dx = x2 - x1;
        int dy = y2 - y1;
        pasos = Math.max(Math.abs(dx), Math.abs(dy));
        incX = (float) dx / pasos;
        incY = (float) dy / pasos;

        ddaX = x1;
        ddaY = y1;
        contadorPasos = 0;

        timerCaida = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contadorPasos <= pasos) {
                    ddaX += incX;
                    ddaY += incY;
                    
                    int nuevoX = Math.round(ddaX);
                    int nuevoY = Math.round(ddaY);

                    // VALIDACIÓN X=700
                    if (nuevoX >= 700) {
                        ((Timer)e.getSource()).stop();
                        panel.actualizarPosicion(700, nuevoY);
                        return;
                    }

                    
                    // Guardamos el punto y actualizamos la pelota
                    panel.actualizarPosicion(nuevoX, nuevoY);
                    
                    contadorPasos++;
                } else {
                    ((Timer)e.getSource()).stop();
                    Random r = new Random();
                    int radioB = 60 + r.nextInt(40); 
                    iniciarRebote(Math.round(ddaX), radioB);
                }
            }
        });
        timerCaida.start();
    }

    private void iniciarRebote(int xInicio, int radio) {
        if (radio <= 0 || xInicio >= 700) {
            return;
        }

        this.inicioReboteX = xInicio;
        this.radioActual = radio;
        this.anguloActual = 180; 
        this.centroReboteX = xInicio + radio;
        
        timerRebote = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double rad = Math.toRadians(anguloActual);
                int x = (int) Math.round(centroReboteX + radioActual * Math.cos(rad));
                int y = (int) Math.round(SUELO_Y - radioActual * Math.sin(rad));

                if (x >= 700) {
                    ((Timer)e.getSource()).stop();
                    panel.actualizarPosicion(700, y);
                    return; 
                }

                if (anguloActual >= 0) {
                    
                    panel.actualizarPosicion(x, y);
                    anguloActual -= 5; 
                } else {
                    ((Timer)e.getSource()).stop();
                    int xFinRebote = inicioReboteX + (radioActual * 2);
                    int nuevoRadio = radioActual - 10;
                    iniciarRebote(xFinRebote, nuevoRadio);
                }
            }
        });
        timerRebote.start();
    }

    public static void main(String[] args) {
        new Pelota();
    }
}

class DibujoPanel extends JPanel {
    private int cx = 40, cy = 40;
    private final int RADIO_PELOTA = 15; 
    
    // LISTA PARA GUARDAR EL RASTRO (Hacer visible el camino)
    private ArrayList<Point> rastro = new ArrayList<>();

    public void actualizarPosicion(int x, int y) {
        this.cx = x;
        this.cy = y;
        // Agregamos el punto actual al historial
        rastro.add(new Point(x, y));
        repaint();
    }
    
    public void limpiarRastro() {
        rastro.clear();
        repaint();
    }

    public void setCentro(int x, int y) {
        this.cx = x;
        this.cy = y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int lineaSuelo = 550 + RADIO_PELOTA;

        // Ejes y Marcas
        g.setColor(Color.BLACK);
        g.drawLine(20, lineaSuelo, 800, lineaSuelo); 
        g.drawLine(40, 20, 40, lineaSuelo);
        g.drawString("550", 5, lineaSuelo);

        g.setColor(Color.BLUE);
        g.drawLine(700, 20, 700, lineaSuelo);
        g.drawString("X=700", 680, 20);

        // --- DIBUJAR EL RASTRO (DDA VISIBLE) ---
        g.setColor(Color.BLACK);
        for (Point p : rastro) {
            // Dibujamos un pequeño punto o pixel en cada coordenada histórica
            g.drawLine(p.x, p.y, p.x, p.y);
        }

        // Dibujar la pelota actual encima del rastro
        g.setColor(Color.RED);
        circulo(g, cx, cy, RADIO_PELOTA);
    }

    private void circulo(Graphics g, int cx, int cy, int radio) {
        // Algoritmo [cite: 36-48]
        for (int angulo = 0; angulo <= 45; angulo++) {
            double rad = Math.toRadians(angulo);
            int x = (int) Math.round(radio * Math.cos(rad));
            int y = (int) Math.round(radio * Math.sin(rad));

            pintapunto(g, cx + x, cy + y);
            pintapunto(g, cx + y, cy + x);
            pintapunto(g, cx - y, cy + x);
            pintapunto(g, cx - x, cy + y);
            pintapunto(g, cx - x, cy - y);
            pintapunto(g, cx - y, cy - x);
            pintapunto(g, cx + y, cy - x);
            pintapunto(g, cx + x, cy - y);
        }
    }

    private void pintapunto(Graphics g, int x, int y) {
        g.drawLine(x, y, x, y);
    }
}