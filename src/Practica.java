package es.uned.lsi.triminos;



/**
 * Resuelve el problema de rellenar con trominos cuadrículas de N x N con N=2^k
 * 
 * @author Miguel 
 * @version 1.0
 */
public class Practica
{
    public static Tablero t;
    public static Tablero s;
    /**
     * main
     *
     */
     public static void main(String[] args) {
       
       if (args.length != 3){
            System.out.println("SINTAXIS: practica x y d");
        }
       else
       {
           int x = Integer.parseInt(args[0]);
           int y = Integer.parseInt(args[1]);
           int d = Integer.parseInt(args[2]);
           
           Casilla c = new Casilla(x,y);
           t = new Tablero(1,1,d,d,c);
           
           System.out.println("\n\nResolviendo:");
           t.renderiza();
           Juego j = new Juego(t);
           s = j.resolver(t);
           System.out.println("\n\nSOLUCION:");
           s.renderiza();  
       }
    }


}
