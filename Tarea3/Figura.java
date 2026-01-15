import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Figura extends JFrame implements ActionListener{
    private JButton inicio;
    private DibujoPanel panel;
    private int x1 = 0, y1 = 10, x2 = 20, y2 = 10; //coordenadas de la figura

    public Figura() {
        /*Interfaz Panel */
        super("Figura Rombo ");
        inicio = new JButton("Caer");
        inicio.addActionListener(this);

        JPanel menu = new JPanel();

        menu.add(inicio);

        panel = new DibujoPanel();

        panel.setFigura(x1, y1, x2, y2);

        this.add(menu, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);

        this.setSize(1600, 900);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == inicio) {
            caida(); // Si se presiona el botón, inicia la caída
        }
    }

    /*Metodo de caída */
    public void caida() {

    int destinoX = (int) (Math.random() * (100 - 40) + 40); 
    
    //Donde va a caer la pelota
    int sueloY = 448; 

    int ancho = x2 - x1;
    int alto = y2 - y1;

    float pasosTotales = (sueloY - y1) / 5.0f; // Número de pasos hasta el suelo
    float avanceX = (destinoX - x1) / pasosTotales; // Movimiento horizontal por paso
    
    final float[] xFlotante = {x1};

    
    Timer timer = new Timer(30, new ActionListener() { //Retraso de 30 ms
        @Override
        public void actionPerformed(ActionEvent e) {
        
            y1 += 5; // Baja 5 píxeles en cada paso
            
            xFlotante[0] += avanceX;
            x1 = (int) xFlotante[0];

            x2 = x1 + ancho;
            y2 = y1 + alto;

            panel.setFigura(x1, y1, x2, y2);
            panel.repaint();

            if (y1 >= sueloY) { // Verificar si tocó el suelo

                ((Timer)e.getSource()).stop(); //Detener caida
                System.out.println("Cayó en la posición X aleatoria: " + x1);
                iniciarSecuenciaRebote(); // Inicia rebote manda a llamar al metodo de rebote
                }
            }
        });
        timer.start();
    }


    public void iniciarSecuenciaRebote() {
    
    int radioB = (int) (Math.random() * (100 - 60) + 60); 
    
    System.out.println("Iniciando secuencia con Radio Aleatorio: " + radioB);
    
    ejecutarRebote(radioB);
    }

    
    public void ejecutarRebote(int radioActual) {

    int cx = x1 + radioActual;

    
    if (cx >= 700) { // Límite en X
        System.out.println("Simulación terminada: El centro alcanzó el límite X=700");
        return; 
    }

    
    if (radioActual <= 10) { 
        System.out.println("Simulación terminada: Radio muy pequeño.");
        return; 
    }
    
    int cy = y1; // Base (suelo)
    int ancho = x2 - x1; 
    int alto = y2 - y1;

    final int[] angulo = {180}; // empieza en 180 grados inicia al lado izquierdo del arco

    Timer timer = new Timer(30, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            double radianes = Math.toRadians(angulo[0]);


            //Paramentros de la circunferencia
            int radioX = (int) (radioActual * Math.cos(radianes)); //Desplazamiento horizontal
            int radioY = (int) (radioActual * Math.sin(radianes)); //Desplazamiento vertical

            // Calcula una nueva posición
            x1 = cx + radioX; 
            y1 = cy - radioY; 

            x2 = x1 + ancho;
            y2 = y1 + alto;

            panel.setFigura(x1, y1, x2, y2); //Envia las nuevas coordenadas de la figura 
            panel.repaint(); //Vuelve a pintar el circulo

            angulo[0] -= 5;

            //Cuando el arco llega a 0 grados
            if (angulo[0] < 0) { 
                ((Timer)e.getSource()).stop(); 
                
                
                y1 = cy;
                x1 = cx + radioActual;
                x2 = x1 + ancho;
                y2 = y1 + alto;
                panel.setFigura(x1, y1, x2, y2);
                panel.repaint();

                
                int siguienteRadio = radioActual - 10; //Se reduce el radio  y se llama así mismo "Recursividad"
                
                
                ejecutarRebote(siguienteRadio);
                }
            }
        });

        timer.start();
    }


    public static void main(String[] args) {
        new Figura();
    }
}

// =================== PANEL DE DIBUJO ===================
class DibujoPanel extends JPanel {
    private int x1, y1, x2, y2;

    public void setFigura(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int lineaSuelo = 460 ;

        //Pinta el suelo y la linea vertical
        g.setColor(Color.BLACK);
        g.drawLine(20, lineaSuelo, 800, lineaSuelo); 
        g.drawLine(40, 20, 40, lineaSuelo);
        g.drawString("440", 5, lineaSuelo);

        //Pinta la linea linea vertical del limite que es de x=700
        g.setColor(Color.BLUE);
        g.drawLine(700, 20, 700, lineaSuelo);
        g.drawString("X=700", 680, 20);

        //Dibuja al circulo
        g.setColor(Color.red);
        dibujarCirculo(g);
    }

    //Pinta el circulo 
    private void dibujarCirculo(Graphics g) {
        
        int cx = (x1 + x2) / 2;
        int cy = (y1 + y2) / 2;

        
        int dx = x2 - x1;
        int dy = y2 - y1;
        int radio = (int)Math.sqrt(dx*dx + dy*dy) / 2;

        
        for (int angulo = 0; angulo <= 45; angulo++) {
            double rad = Math.toRadians(angulo);
            int x = (int)(radio * Math.cos(rad));
            int y = (int)(radio * Math.sin(rad));

            
            g.drawLine(cx + x, cy + y, cx + x, cy + y);
            g.drawLine(cx + y, cy + x, cx + y, cy + x);
            g.drawLine(cx - y, cy + x, cx - y, cy + x);
            g.drawLine(cx - x, cy + y, cx - x, cy + y);
            g.drawLine(cx - x, cy - y, cx - x, cy - y);
            g.drawLine(cx - y, cy - x, cx - y, cy - x);
            g.drawLine(cx + y, cy - x, cx + y, cy - x);
            g.drawLine(cx + x, cy - y, cx + x, cy - y);
        }
    }
}
