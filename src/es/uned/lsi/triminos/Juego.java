package es.uned.lsi.triminos;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Resuelve el tablero por divide y venceras.
 *
 * @author Miguel R. (UNED)
 * @version 1.1
 */
public class Juego
{
    private final Tablero tablero;

    /**
     * Guarda el tablero que resolvera {@link #resolver()}.
     */
    public Juego(Tablero t)
    {
        if (t == null) {
            throw new IllegalArgumentException("El tablero es nulo");
        }
        this.tablero = t;
    }

    /**
     * Resuelve el tablero recibido en el constructor.
     */
    public Tablero resolver()
    {
        return resolver(this.tablero);
    }

    /**
     * Resuelve t por divide y venceras.
     *
     * (0) Si el lado es 1, la unica casilla es la excluida y no hay trominos.
     *     Si el lado es 2, las otras tres casillas forman el tromino.
     * (1) Ver en que cuadrante esta la casilla marcada.
     * (2) Poner en el centro un tromino pivote, orientado hacia el cuadrante
     *     que contiene la casilla marcada.
     * (3) Cuatro llamadas recursivas:
     *     (3.1) al cuadrante de la casilla marcada, con esa casilla;
     *     (3.2) a los otros tres, tomando como marcada la casilla del pivote
     *           que cae en ese cuadrante.
     * (4) Reunir los trominos de los cuatro cuadrantes en el tablero.
     * (5) Devolver el tablero.
     */
    public Tablero resolver(Tablero t)
    {
        if (t == null) {
            throw new IllegalArgumentException("El tablero es nulo");
        }
        if (t.getDimension() == 1) {
            return t;
        }
        if (t.getDimension() == 2) {
            t.addTrimino(t.triminoPivote());
            return t;
        }

        Trimino.Orientacion o = t.cuadranteCasillaMarcada();
        Trimino m = t.triminoPivote();
        t.addTrimino(m);

        Tablero[] cuadrante = new Tablero[4];
        Tablero[] solucion = new Tablero[4];
        Iterator<Casilla> itr1 = m.getListaCasillas().iterator();
        cuadrante[0] = t.split(t.getCasillaMarcada(), o);
        for (int j = 1; j < 4; j++) {
            Casilla c = itr1.next();
            cuadrante[j] = t.split(c, t.cuadranteCasilla(c));
        }

        for (int i = 0; i < 4; i++) {
            solucion[i] = resolver(cuadrante[i]);
        }

        for (int i = 0; i < 4; i++) {
            Iterator<Trimino> itr2 = solucion[i].getListaTriminos().iterator();
            while (itr2.hasNext()) {
                t.addTrimino(itr2.next());
            }
        }
        return t;
    }
}
