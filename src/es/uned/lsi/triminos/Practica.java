package es.uned.lsi.triminos;

/**
 * Rellena con trominos una cuadricula de N x N, con N = 2^k y una casilla excluida.
 *
 * Uso: java es.uned.lsi.triminos.Practica x y d
 *   d     lado del tablero, potencia de dos (1, 2, 4, 8, ...)
 *   x, y  casilla excluida, con coordenadas de 1 a d
 *
 * @author Miguel
 * @version 1.1
 */
public class Practica
{
    public static Tablero t;
    public static Tablero s;

    public static void main(String[] args) {
        if (args.length != 3) {
            uso();
            System.exit(1);
        }

        int x;
        int y;
        int d;
        try {
            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            d = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            System.out.println("Los argumentos x, y y d tienen que ser enteros.");
            uso();
            System.exit(1);
            return;
        }

        String error = validar(x, y, d);
        if (error != null) {
            System.out.println(error);
            uso();
            System.exit(1);
        }

        Casilla c = new Casilla(x, y);
        try {
            t = new Tablero(1, 1, d, d, c);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            uso();
            System.exit(1);
            return;
        }

        System.out.println("\n\nResolviendo:");
        t.renderiza();
        Juego j = new Juego(t);
        s = j.resolver();
        System.out.println("\n\nSOLUCION:");
        s.renderiza();
    }

    /**
     * Devuelve null si (x, y, d) es un problema valido, o el motivo del rechazo.
     * Las coordenadas son las del programa principal: de 1 a d.
     */
    public static String validar(int x, int y, int d)
    {
        if (!Tablero.esPotenciaDeDos(d)) {
            return "d tiene que ser una potencia de dos (1, 2, 4, 8, ...), pero es " + d + ".";
        }
        if (x < 1 || x > d || y < 1 || y > d) {
            return "La casilla (" + x + "," + y + ") tiene que estar entre 1 y " + d + ".";
        }
        return null;
    }

    public static void uso()
    {
        System.out.println("Uso: java es.uned.lsi.triminos.Practica x y d");
        System.out.println("  d     lado del tablero, potencia de dos (1, 2, 4, 8, ...)");
        System.out.println("  x, y  casilla excluida, coordenadas de 1 a d");
    }
}
