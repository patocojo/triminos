package es.uned.lsi.triminos;

/**
 * Una casilla es un par de coordenadas
 * 
 * @author Miguel R.
 * @version 1.0
 */
public class Casilla
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;

    /**
     * Constructor for objects of class Casilla
     */
    public Casilla(int x,int y)
    {
        // initialise instance variables
        this.x = x;
        this.y = y;
        
    }

    /**
     * Coordenada x
     *
     */
    public int get_x()
    {
        // put your code here
        return this.x;
    }

    /**
     * Coordenada y
     *
     */
    public int get_y()
    {
        // put your code here
        return this.y;
    }
    
    /**
     * Escribe la casilla
     * 
     * @param  x   Coordenada x
     * @param  y   Coordenada y
     * 
     */
    public void escribe()
    {
        // put your code here
        System.out.println("Casilla: ("+x+","+y+")");
    }
}
