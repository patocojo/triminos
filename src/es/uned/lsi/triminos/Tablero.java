package es.uned.lsi.triminos;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Un tablero cuadrado de lado 2^k y la lista de trominos que lo rellenan.
 * Las coordenadas son inclusivas: la esquina superior izquierda es (si_X, si_Y)
 * y la inferior derecha es (id_X, id_Y).
 *
 * @author Miguel R.
 * @version 1.1
 */
public class Tablero
{
    private int si_X, si_Y, id_X, id_Y;
    private Casilla casillaMarcada;
    private ArrayList<Trimino> listaTriminos;
    private int dimension;

    /**
     * Tablero de coordenadas inclusivas (a, b) a (x, y).
     * El lado horizontal y el vertical tienen que coincidir y ser potencia de dos.
     * La casilla c es la casilla excluida.
     */
    public Tablero(int a, int b, int x, int y, Casilla c)
    {
        si_X = a;
        si_Y = b;
        id_X = x;
        id_Y = y;
        inicializar(c);
    }

    /**
     * Tablero de lado d con coordenadas de 0 a d-1.
     * El programa principal usa el otro constructor, con coordenadas de 1 a d.
     */
    public Tablero(int d, Casilla c)
    {
        si_X = 0;
        si_Y = 0;
        id_X = d - 1;
        id_Y = d - 1;
        inicializar(c);
    }

    /**
     * Tablero delimitado por las casillas a (superior izquierda) y b
     * (inferior derecha). La casilla c es la casilla excluida.
     */
    public Tablero(Casilla a, Casilla b, Casilla c)
    {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Las esquinas del tablero son nulas");
        }
        si_X = a.get_x();
        si_Y = a.get_y();
        id_X = b.get_x();
        id_Y = b.get_y();
        inicializar(c);
    }

    /**
     * Cierto si n es 1, 2, 4, 8, 16, ...
     */
    public static boolean esPotenciaDeDos(int n)
    {
        return n > 0 && (n & (n - 1)) == 0;
    }

    private void inicializar(Casilla c)
    {
        int ancho = id_X - si_X + 1;
        int alto = id_Y - si_Y + 1;
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException(
                "El tablero tiene que tener lados positivos. Esquinas ("
                + si_X + "," + si_Y + ") (" + id_X + "," + id_Y + ")");
        }
        if (ancho != alto) {
            throw new IllegalArgumentException(
                "El tablero tiene que ser cuadrado, pero mide " + ancho + "x" + alto);
        }
        if (!esPotenciaDeDos(ancho)) {
            throw new IllegalArgumentException(
                "El lado tiene que ser una potencia de dos (1, 2, 4, 8, ...), pero es " + ancho);
        }
        if (c == null) {
            throw new IllegalArgumentException("La casilla marcada es nula");
        }
        if (c.get_x() < si_X || c.get_x() > id_X || c.get_y() < si_Y || c.get_y() > id_Y) {
            throw new IllegalArgumentException(
                "La casilla marcada (" + c.get_x() + "," + c.get_y()
                + ") queda fuera del tablero (" + si_X + "," + si_Y
                + ")-(" + id_X + "," + id_Y + ")");
        }
        dimension = ancho;
        casillaMarcada = c;
        listaTriminos = new ArrayList<Trimino>();
    }

    /**
     * Cuadrante del tablero en el que cae la casilla c.
     */
    public Trimino.Orientacion cuadranteCasilla(Casilla c)
    {
        if (c == null) {
            throw new IllegalArgumentException("La casilla es nula");
        }
        return cuadrante(c.get_x(), c.get_y());
    }

    /**
     * Cuadrante del tablero en el que cae la casilla marcada.
     */
    public Trimino.Orientacion cuadranteCasillaMarcada()
    {
        return cuadrante(casillaMarcada.get_x(), casillaMarcada.get_y());
    }

    private Trimino.Orientacion cuadrante(int x, int y)
    {
        int bx = si_X + (dimension / 2) - 1;
        int by = si_Y + (dimension / 2) - 1;
        if (x <= bx && y <= by) return Trimino.Orientacion.NO;
        if (x > bx && y <= by) return Trimino.Orientacion.NE;
        if (x > bx && y > by) return Trimino.Orientacion.SE;
        return Trimino.Orientacion.SO;
    }

    /**
     * Añade un tromino al tablero.
     */
    public void addTrimino(Trimino m)
    {
        if (m == null) {
            throw new IllegalArgumentException("El tromino es nulo");
        }
        this.listaTriminos.add(m);
    }

    public Casilla getCasillaSuperiorIzquierda()
    {
        return new Casilla(si_X, si_Y);
    }

    public Casilla getCasillaInferiorDerecha()
    {
        return new Casilla(id_X, id_Y);
    }

    public Casilla getCasillaMarcada()
    {
        return this.casillaMarcada;
    }

    public int getDimension()
    {
        return this.dimension;
    }

    /**
     * Tromino central, colocado de forma que tapa los tres cuadrantes
     * que no contienen la casilla marcada.
     */
    public Trimino triminoPivote()
    {
        Trimino.Orientacion o = cuadranteCasillaMarcada();
        int x = 0;
        int y = 0;

        switch (o) {
            case NO: x = si_X + (dimension / 2);     y = si_Y + (dimension / 2);     break;
            case NE: x = si_X + (dimension / 2) - 1; y = si_Y + (dimension / 2);     break;
            case SE: x = si_X + (dimension / 2) - 1; y = si_Y + (dimension / 2) - 1; break;
            case SO: x = si_X + (dimension / 2);     y = si_Y + (dimension / 2) - 1; break;
        }

        return new Trimino(x, y, o);
    }

    /**
     * Subtablero del cuadrante o, con c como casilla excluida.
     * c tiene que caer dentro de ese cuadrante.
     */
    public Tablero split(Casilla c, Trimino.Orientacion o)
    {
        if (c == null || o == null) {
            throw new IllegalArgumentException("El cuadrante o la casilla son nulos");
        }
        int x = 0;
        int y = 0;

        switch (o) {
            case NO: x = si_X;                y = si_Y;                  break;
            case NE: x = si_X + (dimension / 2); y = si_Y;               break;
            case SE: x = si_X + (dimension / 2); y = si_Y + (dimension / 2); break;
            case SO: x = si_X;                y = si_Y + (dimension / 2); break;
        }

        Casilla a = new Casilla(x, y);
        Casilla b = new Casilla(x + (dimension / 2) - 1, y + (dimension / 2) - 1);
        if (c.get_x() < a.get_x() || c.get_x() > b.get_x()
                || c.get_y() < a.get_y() || c.get_y() > b.get_y()) {
            throw new IllegalArgumentException(
                "La casilla (" + c.get_x() + "," + c.get_y()
                + ") no pertenece al cuadrante " + o);
        }
        return new Tablero(a, b, c);
    }

    /**
     * Copia de la lista de trominos. Modificarla no cambia el tablero.
     */
    public ArrayList<Trimino> getListaTriminos()
    {
        return new ArrayList<Trimino>(this.listaTriminos);
    }

    /**
     * Dibuja el tablero. La casilla excluida es #. Cada tromino usa su
     * numero, de 1 en adelante, repetido en sus tres casillas. Las casillas
     * todavia vacias se dibujan con +.
     */
    public void renderiza()
    {
        int n = this.dimension;
        int cantidad = listaTriminos.size();
        int ancho = Math.max(1, String.valueOf(Math.max(cantidad, 1)).length());
        String[][] tabla = new String[n][n];

        int mx = this.casillaMarcada.get_x() - this.si_X;
        int my = this.casillaMarcada.get_y() - this.si_Y;
        tabla[mx][my] = celda("#", ancho);

        int id = 1;
        Iterator<Trimino> itr_m = this.listaTriminos.iterator();
        while (itr_m.hasNext()) {
            Trimino m = itr_m.next();
            String etiqueta = celda(Integer.toString(id), ancho);
            Iterator<Casilla> itr_c = m.getListaCasillas().iterator();
            while (itr_c.hasNext()) {
                Casilla c = itr_c.next();
                int x = c.get_x() - this.si_X;
                int y = c.get_y() - this.si_Y;
                if (x < 0 || y < 0 || x >= n || y >= n) {
                    throw new IllegalArgumentException(
                        "El tromino " + id + " se sale del tablero en ("
                        + c.get_x() + "," + c.get_y() + ")");
                }
                if (tabla[x][y] != null) {
                    throw new IllegalArgumentException(
                        "El tromino " + id + " pisa la casilla ("
                        + c.get_x() + "," + c.get_y() + ")");
                }
                tabla[x][y] = etiqueta;
            }
            id++;
        }

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if (tabla[x][y] == null) {
                    System.out.print(celda("+", ancho));
                } else {
                    System.out.print(tabla[x][y]);
                }
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    private static String celda(String texto, int ancho)
    {
        return String.format("%" + ancho + "s", texto);
    }

    /**
     * Escribe los trominos que contiene el tablero.
     */
    public void escribe()
    {
        Iterator<Trimino> itr = listaTriminos.iterator();

        System.out.print("\nTABLERO: (" + si_X + "," + si_Y + ") (" + id_X + "," + id_Y + ")\nLISTA TRIMINOS:");
        while (itr.hasNext()) {
            itr.next().escribe();
        }
        System.out.println("FIN LISTA TRIMINOS");
        System.out.print("Casilla marcada:");
        this.casillaMarcada.escribe();
        System.out.println("Dimension:" + this.dimension);
        System.out.println("FIN TABLERO\n");
    }
}
