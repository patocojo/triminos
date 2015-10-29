//package es.uned.lsi.triminos;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Un tablero contiene las coordenadas y la lista de triminos que lo rellenan
 * 
 *
 */
public class Tablero
{
    // Definicion de la clase
    private int si_X, si_Y, id_X, id_Y; // Coordenadas superior izquierda (si) e inferior derecha  (id)
    private Casilla casillaMarcada;
    private  ArrayList<Trimino> listaTriminos; // Un tablero contiene triminos
    private int dimension;
   
    /**
     * Construccion de un tablero a partir de sus coordenadas
     */
    public Tablero(int a,int b, int x,int y, Casilla c)
    {
       // inicializa el objeto con las coordenadas
       si_X = a;
       si_Y = b;
       id_X = x;
       id_Y = y;
       dimension = id_X - si_X + 1; // El lado del cuadrado 
       
       // la casilla que hace de casilla exclu�da del juego de rellenar trominos
       casillaMarcada = c;
        
       // La lista (vac�a) de trominos
       listaTriminos = new ArrayList<Trimino>();
        
    }
    
     /**
     * Construccion de un tablero a partir de la dimension y de la casilla marcada
     */
    public Tablero(int d, Casilla c)
    {
       // inicializa el objeto con las coordenadas
       si_X = 0;
       si_Y = 0;
       id_X = d-1;
       id_Y = d-1;
       dimension = d; // El lado del cuadrado 
       // la casilla que hace de casilla exclu�da del juego de rellenar trominos
       casillaMarcada = c;
       // La lista (vacía) de trominos
       listaTriminos = new ArrayList<Trimino>();
        
    }
    
    /**
     * Construccion de un tablero a partir de sus casilla superior izquierda e inferior derecha.
     * La Casilla c es la casilla marcada
     */
    public Tablero(Casilla a,Casilla b, Casilla c)
    {
       // inicializa el objeto con las coordenadas de las casillas
       si_X = a.get_x();
       si_Y = a.get_y();
       id_X = b.get_x();
       id_Y = b.get_y();
       
       dimension = id_X - si_X + 1; // El lado del cuadrado 
       
       // la casilla que hace de casilla excluída del juego de rellenar trominos
       casillaMarcada = c;
        
       // La lista (vacía) de trominos
       listaTriminos = new ArrayList<Trimino>();
        
    }
    
    /**
     * En qué cuadrante del tablero está la casilla c
     */
    public Trimino.Orientacion cuadranteCasilla(Casilla c)
    {
        int bx = si_X + (dimension/2) - 1; // bisectriz vertical
        int by = si_Y + (dimension/2) - 1; // bisectriz horizontal
        Trimino.Orientacion o = Trimino.Orientacion.NO; 
        
        if (c.get_x() <= bx && c.get_y() <= by ) {o = Trimino.Orientacion.NO;}
        if (c.get_x() > bx && c.get_y() <= by ) o = Trimino.Orientacion.NE;
        if (c.get_x() > bx && c.get_y() > by ) o = Trimino.Orientacion.SE;
        if (c.get_x() <= bx && c.get_y() > by ) o = Trimino.Orientacion.SO;
        
        return o;
    }
    
    /**
     * En qu� cuadrante del tablero est� la casilla marcada
     */
    public Trimino.Orientacion cuadranteCasillaMarcada()
    {
        int bx = si_X + (dimension/2) - 1; // bisectriz vertical
        int by = si_Y + (dimension/2) - 1; // bisectriz horizontal
        Trimino.Orientacion o = Trimino.Orientacion.NO; 
        
        if (casillaMarcada.get_x() <= bx && casillaMarcada.get_y() <= by ) {o = Trimino.Orientacion.NO;}
        if (casillaMarcada.get_x() > bx && casillaMarcada.get_y() <= by ) o = Trimino.Orientacion.NE;
        if (casillaMarcada.get_x() > bx && casillaMarcada.get_y() > by ) o = Trimino.Orientacion.SE;
        if (casillaMarcada.get_x() <= bx && casillaMarcada.get_y() > by ) o = Trimino.Orientacion.SO;
        
        return o;
    }
    
    /**
     * Anade un Trimino al tablero
     *
     */
    public void addTrimino(Trimino m)
    {
        // Anade un Trimino al tablero. 
        this.listaTriminos.add(m);
    }

    /**
     * Devuelve la casilla superior izquierda del tablero
     *
     *
     */
    public Casilla getCasillaSuperiorIzquierda()
    {
        Casilla c = new Casilla(si_X,si_Y);
        return c;
    }

    /**
     * Devuelve la casilla inferior derecha del tablero
     *
     *
     */
    public Casilla getCasillaInferiorDerecha()
    {
        Casilla c = new Casilla(id_X,id_Y);
        return c;
    }
    
     /**
     * Devuelve la casilla marcada del tablero
     *
     *
     */
    public Casilla getCasillaMarcada()
    {
        return this.casillaMarcada;
    }
    
    /**
     * Devuelve la dimension del tablero
     *
     *
     */
    public int getDimension()
    {
        return this.dimension;
    }
    
    /**
     * Dado un tablero, devuelve el trimino pivote en funci�n de la orientaci�n de la casilla marcada
     *
     */
    public Trimino triminoPivote(Tablero t)
    {
        Trimino.Orientacion o = t.cuadranteCasillaMarcada();
        int x=0;
        int y=0;
        
         switch (o){
            case NO: x = si_X + (dimension/2);   y = si_Y + (dimension/2);     break;
            case NE: x = si_X + (dimension/2)-1; y = si_Y + (dimension/2);     break;
            case SE: x = si_X + (dimension/2)-1; y = si_Y + (dimension/2)-1;   break;
            case SO: x = si_X + (dimension/2);   y = si_Y + (dimension/2)-1;   break;
        }
        
        Trimino m = new Trimino(x,y,o);
        return m;
    }

    /**
     * Realiza un split al cuadrante indicado, devolviendo un tablero con la casilla marcada indicada en el argumento
     *
     */
    public Tablero split(Tablero t, Casilla c, Trimino.Orientacion o)
    {
        // P(x) La casilla debe pertenecer al cuadrante que se crea, sino...!BOOM!
        int x=0;
        int y=0;
               
         switch (o){
            case NO: x = si_X;                   y = si_Y;  break;
            case NE: x = si_X +(dimension/2);    y = si_Y;  break;
            case SE: x = si_X +(dimension/2);    y = si_Y + (dimension/2);  break;
            case SO: x = si_X;                   y = si_Y + (dimension/2);  break;
        }
        
        Casilla a = new Casilla(x,y);
        Casilla b = new Casilla(x+(dimension/2)-1,y+(dimension/2)-1);
        
        Tablero nuevoTablero = new Tablero(a,b,c);
        return nuevoTablero;
    }

    /**
     * Devuelve la lista de triminos que contiene el tablero
     *
     */
    public ArrayList<Trimino> getListaTriminos()
    {
        return this.listaTriminos;
    }

    /**
     * Renderiza un tablero en forma de tabla
     *
     */
    public void renderiza()
    {
       char[][] tabla = new char[this.dimension][this.dimension];
       String simbolos = "1234567890"; 
       //String simbolos = "o+"; 
       ArrayList<Casilla> listaCasillas = new ArrayList<Casilla>();
       Casilla c;
       int i=0;
       int x=0;
       int y=0;
       Trimino m;
       
       //Representamos la "casilla marcada" con #
       x=this.casillaMarcada.get_x() - this.si_X; 
       y=this.casillaMarcada.get_y() - this.si_Y; 
       tabla[x][y] = '#';
       Iterator<Trimino> itr_m = listaTriminos.iterator();
	// Iteramos sobre los triminos del tablero
       while (itr_m.hasNext()) {
           m = itr_m.next();
           listaCasillas = m.getListaCasillas();
           // Iteramos sobre las 3 casillas de cada trimino
           Iterator<Casilla> itr_c = listaCasillas.iterator();
           while (itr_c.hasNext()) {
                    c = itr_c.next();
                    x=c.get_x()  - this.si_X;
                    y=c.get_y()  - this.si_Y;
                    tabla[x][y] = simbolos.charAt(i);
           }
           i=(i+1) % simbolos.length();     // Incremento modular
        }
       
       // Escribimos la tabla
       for(y=0;y<dimension;y++){
            for(x=0;x<dimension;x++){
                if(tabla[x][y]=='\u0000')
                    System.out.print("+ ");   
                else {
                    System.out.print(tabla[x][y]);
                    System.out.print(' ');
                }
            }
            System.out.println();
       }
    }


    
    /**
     * Escribe los triminos que contiene un tablero
     * 
     */
    public void escribe()
    {
       Trimino m;
       Iterator<Trimino> itr = listaTriminos.iterator();
       
       System.out.print("\nTABLERO: (" + si_X + "," + si_Y + ") (" + id_X + "," + id_Y + ")\nLISTA TRIMINOS:");

       while (itr.hasNext()) {
           m = itr.next();
           m.escribe();
        }
       System.out.println("FIN LISTA TRIMINOS");
       System.out.print("Casilla marcada:"); this.casillaMarcada.escribe();
       System.out.println("Dimension:"+this.dimension); 
       System.out.println("FIN TABLERO\n");
    }
}
