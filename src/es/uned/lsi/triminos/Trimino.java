package es.uned.lsi.triminos;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Un tromino es una L de tres casillas, en una de sus cuatro orientaciones.
 * La casilla que recibe el constructor es la esquina de la L, la que toca
 * a las otras dos.
 *
 * @author Miguel
 * @version 1.1
 */
public class Trimino
{
    /** Noroeste, noreste, sureste y suroeste, en sentido horario. */
    public static enum Orientacion {NO, NE, SE, SO};

    private Orientacion o;
    private Casilla c1;
    private Casilla c2;
    private Casilla c3;
    private ArrayList<Casilla> listaCasillas = new ArrayList<Casilla>();

    /**
     * Construye el tromino cuya esquina es (x, y) y que se abre hacia o.
     */
    public Trimino(int x, int y, Orientacion o)
    {
        if (o == null) {
            throw new IllegalArgumentException("La orientacion del tromino es nula");
        }
        this.o = o;

        c1 = new Casilla(x, y);

        switch (this.o) {
            case NE:  c2 = new Casilla(x, y - 1);  c3 = new Casilla(x + 1, y);  break;
            case NO:  c2 = new Casilla(x, y - 1);  c3 = new Casilla(x - 1, y);  break;
            case SE:  c2 = new Casilla(x + 1, y);  c3 = new Casilla(x, y + 1);  break;
            case SO:  c2 = new Casilla(x, y + 1);  c3 = new Casilla(x - 1, y);  break;
        }

        // El orden es horario: c3, esquina, c2.
        listaCasillas.add(c3);
        listaCasillas.add(c1);
        listaCasillas.add(c2);
    }

    /**
     * Devuelve una copia de las tres casillas del tromino.
     */
    public ArrayList<Casilla> getListaCasillas()
    {
        return new ArrayList<Casilla>(this.listaCasillas);
    }

    /**
     * Escribe el tromino.
     */
    public void escribe()
    {
        Iterator<Casilla> itr = listaCasillas.iterator();
        while (itr.hasNext()) {
            itr.next().escribe();
        }
        System.out.println("Orientacion: " + o);
    }
}
