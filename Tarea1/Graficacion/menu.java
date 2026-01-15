package Graficacion;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
public class menu {
    public static void main(String[] args) {
        int menu=0;
        float p1=0,p2=0,p3=0,p4=0;
        triangulo objTriangulo = new triangulo();
        trapecio objTrapecio = new trapecio();
        cuadrado objCuadrado = new cuadrado();
        do {
            menu = Integer.parseInt(JOptionPane.showInputDialog("\\n" + 
                                "MENU\\n" + 
                                " 1.-Triangulo\\n" + 
                                " 2.-Trapecio \\n" + 
                                " 3.-Cuadrado \\n" +
                                " 4.-Salir"));
            switch (menu) {
                case 1:
                    p1=Integer.parseInt(JOptionPane.showInputDialog("Ingrese la coordenada uno"));
                    p2=Integer.parseInt(JOptionPane.showInputDialog("Ingrese la coordenada dos"));
                    p3=Integer.parseInt(JOptionPane.showInputDialog("Ingrese la coordenada tres"));
                    
                    objTriangulo.opcTriangulo(p1, p2, p3);
                break;

                case 2:
                    p1=Integer.parseInt(JOptionPane.showInputDialog("Ingrese la coordenada uno"));
                    p2=Integer.parseInt(JOptionPane.showInputDialog("Ingrese la coordenada dos "));
                    p3=Integer.parseInt(JOptionPane.showInputDialog("Ingrese la coordenada tres"));
                    p4=Integer.parseInt(JOptionPane.showInputDialog("Ingrese la coordenada cuatro"));

                    objTrapecio.opcTrapecio(p1, p2, p3, p4);
                break;

                case 3:
                    p1=Integer.parseInt(JOptionPane.showInputDialog("Ingrese lado uno"));
                    p2=Integer.parseInt(JOptionPane.showInputDialog("Ingrese lado dos"));
                    p3=Integer.parseInt(JOptionPane.showInputDialog("Ingrese lado tres"));
                    p4=Integer.parseInt(JOptionPane.showInputDialog("Ingrese lado cuatro"));

                    objCuadrado.opcCuadrado(p1, p2, p3, p4);
                break;
                
                case 4:
                JOptionPane.showMessageDialog(null, "Saliendo ....");
                break;

                default:
                JOptionPane.showMessageDialog(null, "No existe tal opcion");
                    break;
            }
        } while (menu != 4);
    }
}
