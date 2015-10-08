//package es.uned.lsi.triminos;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Juego resuelve el juego. Esta clase contiene el algoritmo de DyV.
 * 
 * @author Miguel R. (UNED)
 * @version 1.0
 */
public class Juego
{

    /**
     * Constructor for objects of class Juego
     * 
     */
    public Juego(Tablero t) 
    {
    }

    /**
     * M�todo que realiza la resolucion por Divide y Venceras del juego. 
     * 
     * El algoritmo es el siguiente:
     * 
     *   (0) Caso trivial: 2x2 y una casilla marcada. Los otros 3 forman un trimino y solucionado.
     *   (1) Ver en que cuadrante esta la casilla marcada
     *   (2) Poner en el centro del tablero un trimino, que llamaremos trimino pivote, orientado de manera que encaje con la esquina del cuadrante que contiene la casilla marcada
     *   (3) Realizar 4 llamadas recursivas al algoritmo con los argumentos siguientes:
     *      (3.1) Al cuadrante con la casilla marcada, llamar recursivamente con dicha casilla marcada
     *      (3.2) A los otros tres cuadrantes, llamar recursivamente considerando casilla marcada la casilla del trimino pivote que interseca con dicho cuadrante
     *   (4) Para los cuatro cuadrantes soluci�n resultantes, extraer su lista de triminos y a�adirla a la lista de triminos del tablero soluci�n
     *   (5) devolver el tablero soluci�n
     *
     */
    public Tablero  resolver(Tablero t)
    {
        ArrayList<Trimino> listaTriminos = new ArrayList<Trimino>();
        Casilla c;
        Tablero cuadrante[] = new Tablero[4];
        Tablero solucion[] = new Tablero[4];  // Las soluciones parciales
        int i=0;
        int j=0;
        // DIVIDE Y VENCER�S
        if (t.getDimension() <= 2)
        {
            // CASO TRIVIAL  Paso (0)
            Trimino m = t.triminoPivote(t); 
            t.addTrimino(m); 
            return t;
        }
        else
        {
            // CASO NO TRIVIAL
            Trimino.Orientacion o = t.cuadranteCasillaMarcada(); // Paso  (1)
            Trimino m = t.triminoPivote(t); t.addTrimino(m);     // Paso  (2)
            
            // DESCOMPOSICI�N
            // Calculamos los cuatro cuadrantes. Dividimos el problema en subproblemas
            Iterator<Casilla> itr1 = m.getListaCasillas().iterator();  // Iteramos sobre las casillas del trimino pivote   
            cuadrante[0] = t.split(t,t.getCasillaMarcada(),o); // Para el paso (3.1)
            for (j=1;j<4;j++){
                c=itr1.next();
                cuadrante[j] = t.split(t,c,t.cuadranteCasilla(c)); // Para el paso (3.2)
            }
            // RECURSI�N SOBRE LOS SUBPROBLEMAS
            for (i=0;i<4;i++){
                 solucion[i] = resolver(cuadrante[i]);
            }
            // COMBINACI�N DE LAS SOLUCIONES PARCIALES Paso (4)
            // Concatenamos en t las listas de triminos de cada solucion
            for(i=0;i<4;i++) {
                listaTriminos = solucion[i].getListaTriminos();
                Iterator<Trimino> itr2 = listaTriminos.iterator();
                while(itr2.hasNext()){
                    m = itr2.next();
                    t.addTrimino(m);
                }
            }

            return t;  // Voila!  Paso (5)
        }
    }


}

