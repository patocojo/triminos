package es.uned.lsi.triminos;

import java.util.ArrayList;
import java.util.Iterator;




/**
 * Un tromino es una L de casillas de 1x1 en sus 4 orientaciones posibles
 * 
 * @author Miguel
 * @version (a version number or a date)
 */
public class Trimino
{
    public static enum Orientacion {NO, NE, SE, SO};  // Noreste, Noroeste, Sureste, Suroeste (!!!clockwise!!!)
    // Orientación
    private Orientacion o;
    // Lista de casillas que componen el tromino
    private Casilla c1;
    private Casilla c2;
    private Casilla c3;
    private  ArrayList<Casilla> listaCasillas = new ArrayList<Casilla>();


    /**
     * Constructor for objects of class Trimino
     */
    public Trimino(int x, int y, Orientacion o)
    {
        this.o = o;
        
        c1 = new Casilla(x,y);
               
        switch (this.o) {
            case NE:  c2 = new Casilla(x,y-1);  c3 = new Casilla(x+1,y);  break;
            case NO:  c2 = new Casilla(x,y-1);  c3 = new Casilla(x-1,y);  break;
            case SE:  c2 = new Casilla(x+1,y);  c3 = new Casilla(x,y+1);  break;
            case SO:  c2 = new Casilla(x,y+1);  c3 = new Casilla(x-1,y);  break;
        }
        
        // El orden IMPORTA y debe ser en sentido horario
        listaCasillas.add(c3);
        listaCasillas.add(c1);
        listaCasillas.add(c2);
        
    }

    /**
     * Devuelve la lista de casillas que componen un trimino
     *
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y
     */
    public ArrayList<Casilla> getListaCasillas()
    {
        return this.listaCasillas;
    }

    /**
     * Escribe el tromino
     */
    public void escribe()
    {
       Casilla c;
       
        // Muestra el tromino
       //switch (this.o) {
       //     case NE: System.out.println("**\n**\n****\n****\n"+ "("+x+","+y+") NE"); break;
       //     case NO: System.out.println("  **\n  **\n****\n****\n"+ "("+x+","+y+") NO"); break;
       //     case SE: System.out.println("****\n****\n**\n**\n"+ "("+x+","+y+") SE"); break;
       //     case SO: System.out.println("****\n****\n  **\n  **\n"+ "("+x+","+y+") SO"); break;
       // }
         
       // Muestra las casilas
       Iterator<Casilla> itr = listaCasillas.iterator();
       while (itr.hasNext()) {
           c = itr.next();
           c.escribe();
       }
       System.out.println("Orientacion: "+o);
    }
}
