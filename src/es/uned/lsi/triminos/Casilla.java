package es.uned.lsi.triminos;

/**
 * Una casilla es un par de coordenadas.
 *
 * @author Miguel R.
 * @version 1.0
 */
public class Casilla
{
    private int x;
    private int y;

    /**
     * Constructor de una casilla.
     */
    public Casilla(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Coordenada x.
     */
    public int get_x()
    {
        return this.x;
    }

    /**
     * Coordenada y.
     */
    public int get_y()
    {
        return this.y;
    }

    /**
     * Escribe la casilla.
     */
    public void escribe()
    {
        System.out.println("Casilla: (" + x + "," + y + ")");
    }
}
