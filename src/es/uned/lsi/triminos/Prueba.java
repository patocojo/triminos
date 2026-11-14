package es.uned.lsi.triminos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Comprueba el cubrimiento y las entradas que antes se aceptaban mal.
 * No forma parte de la resolucion: se ejecuta con
 * java es.uned.lsi.triminos.Prueba
 */
public class Prueba
{
    private static int fallos = 0;

    public static void main(String[] args) throws Exception {
        for (int n : new int[] {1, 2, 4, 8, 16, 32}) {
            barrer("1-based", n, 1);
            barrer("0-based", n, 0);
        }
        desplazado();
        listasIndependientes();
        rechazos();
        dibujoSinRepetir();
        salidaConocida();
        lineaDeComandos();
        if (fallos > 0) {
            System.out.println("FALLOS " + fallos);
            System.exit(1);
        }
        System.out.println("PRUEBA OK");
    }

    private static void barrer(String etiqueta, int n, int origen)
    {
        int mal = 0;
        String primero = null;
        for (int y = origen; y < origen + n; y++) {
            for (int x = origen; x < origen + n; x++) {
                Tablero t = origen == 0
                    ? new Tablero(n, new Casilla(x, y))
                    : new Tablero(origen, origen, origen + n - 1, origen + n - 1, new Casilla(x, y));
                new Juego(t).resolver();
                String detalle = cubrir(t, origen, origen, n, x, y);
                if (detalle != null) {
                    mal++;
                    if (primero == null) primero = "(" + x + "," + y + ") " + detalle;
                }
            }
        }
        if (mal == 0) {
            System.out.println("OK " + etiqueta + " n=" + n);
        } else {
            fallos++;
            System.out.println("MAL " + etiqueta + " n=" + n + " " + mal + " primero=" + primero);
        }
    }

    private static void desplazado()
    {
        Tablero t = new Tablero(5, 5, 8, 8, new Casilla(6, 7));
        String detalle = cubrir(new Juego(t).resolver(), 5, 5, 4, 6, 7);
        if (detalle == null && t.getListaTriminos().size() == 5) {
            System.out.println("OK desplazado");
        } else {
            fallos++;
            System.out.println("MAL desplazado " + detalle);
        }
    }

    /**
     * Devuelve null si el tablero ya resuelto cubre el cuadrado, o el motivo.
     */
    static String cubrir(Tablero t, int siX, int siY, int n, int mx, int my)
    {
        if (t.getListaTriminos().size() != (n * n - 1) / 3) {
            return "trominos=" + t.getListaTriminos().size();
        }
        int[][] cover = new int[n][n];
        for (Trimino m : t.getListaTriminos()) {
            ArrayList<Casilla> cells = m.getListaCasillas();
            if (cells.size() != 3) return "tromino sin 3 casillas";
            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
            for (int i = 0; i < cells.size(); i++) {
                for (int j = 0; j < i; j++) {
                    if (cells.get(i).get_x() == cells.get(j).get_x()
                            && cells.get(i).get_y() == cells.get(j).get_y()) {
                        return "casillas repetidas en un tromino";
                    }
                }
                int x = cells.get(i).get_x();
                int y = cells.get(i).get_y();
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
                int lx = x - siX;
                int ly = y - siY;
                if (lx < 0 || ly < 0 || lx >= n || ly >= n) return "tromino fuera";
                cover[lx][ly]++;
            }
            if (maxX - minX != 1 || maxY - minY != 1) return "tromino que no es una L";
        }
        for (int ly = 0; ly < n; ly++) {
            for (int lx = 0; lx < n; lx++) {
                int esperado = (siX + lx == mx && siY + ly == my) ? 0 : 1;
                if (cover[lx][ly] != esperado) {
                    return "cobertura (" + (siX + lx) + "," + (siY + ly) + ")=" + cover[lx][ly];
                }
            }
        }
        return null;
    }

    private static void listasIndependientes()
    {
        Tablero t = new Tablero(1, 1, 4, 4, new Casilla(1, 1));
        new Juego(t).resolver();
        int antes = t.getListaTriminos().size();
        t.getListaTriminos().clear();
        Trimino m = t.getListaTriminos().get(0);
        m.getListaCasillas().clear();
        if (t.getListaTriminos().size() == antes && m.getListaCasillas().size() == 3) {
            System.out.println("OK listas copiadas");
        } else {
            fallos++;
            System.out.println("MAL listas copiadas");
        }
    }

    private static void rechazos()
    {
        esperar("no cuadrado", true, new Accion() {
            public void run() { new Tablero(1, 1, 4, 2, new Casilla(1, 1)); }
        });
        esperar("lado 5", true, new Accion() {
            public void run() { new Tablero(1, 1, 5, 5, new Casilla(1, 1)); }
        });
        esperar("lado 0", true, new Accion() {
            public void run() { new Tablero(1, 1, 0, 0, new Casilla(1, 1)); }
        });
        esperar("casilla fuera", true, new Accion() {
            public void run() { new Tablero(1, 1, 4, 4, new Casilla(0, 0)); }
        });
        esperar("juego nulo", true, new Accion() {
            public void run() { new Juego(null); }
        });
        if (Practica.validar(1, 1, 4) != null) fallo("4 deberia valer");
        if (Practica.validar(1, 1, 5) == null) fallo("5 no deberia valer");
        if (Practica.validar(0, 0, 4) == null) fallo("0,0 no deberia valer");
        if (Practica.validar(1, 1, 1) != null) fallo("1 deberia valer");
        else System.out.println("OK validar");
    }

    private interface Accion { void run(); }

    private static void esperar(String nombre, boolean excepcion, Accion accion)
    {
        boolean lanzo = false;
        try {
            accion.run();
        } catch (IllegalArgumentException ex) {
            lanzo = ex.getMessage() != null && ex.getMessage().length() > 0;
        }
        if (lanzo == excepcion) {
            System.out.println("OK rechazo " + nombre);
        } else {
            fallos++;
            System.out.println("MAL rechazo " + nombre);
        }
    }

    private static void fallo(String mensaje)
    {
        fallos++;
        System.out.println("MAL " + mensaje);
    }

    private static void dibujoSinRepetir()
    {
        Tablero t = new Tablero(1, 1, 16, 16, new Casilla(1, 1));
        new Juego(t).resolver();
        String texto = capturar(t);
        Set<String> etiquetas = new HashSet<String>();
        int almohadillas = 0;
        int vacias = 0;
        for (String token : texto.split("\\s+")) {
            if (token.isEmpty()) continue;
            if (token.equals("#")) almohadillas++;
            else if (token.equals("+")) vacias++;
            else etiquetas.add(token);
        }
        int esperados = t.getListaTriminos().size();
        if (almohadillas == 1 && vacias == 0 && etiquetas.size() == esperados && esperados == 85) {
            System.out.println("OK dibujo n=16 etiquetas=" + etiquetas.size());
        } else {
            fallos++;
            System.out.println("MAL dibujo n=16 hash=" + almohadillas
                + " vacias=" + vacias + " etiquetas=" + etiquetas.size() + " trominos=" + esperados);
        }
    }

    private static void salidaConocida()
    {
        String n2 = capturarResuelto(2, 1, 1);
        String n4 = capturarResuelto(4, 1, 1);
        String esperado2 = "# 1 \n1 1 \n";
        String esperado4 = "# 2 5 5 \n2 2 1 5 \n3 1 1 4 \n3 3 4 4 \n";
        if (n2.equals(esperado2) && n4.equals(esperado4)) {
            System.out.println("OK dibujo conocido n=2 y n=4");
        } else {
            fallos++;
            System.out.println("MAL dibujo conocido\n[" + n2 + "]\n[" + n4 + "]");
        }
        String n1 = capturarResuelto(1, 1, 1);
        if (n1.equals("# \n") && new Tablero(1, 1, 1, 1, new Casilla(1, 1)).getListaTriminos().isEmpty()) {
            // El tablero de arriba no esta resuelto; el resuelto de lado 1 no tiene trominos.
            Tablero uno = new Tablero(1, 1, 1, 1, new Casilla(1, 1));
            new Juego(uno).resolver();
            if (uno.getListaTriminos().isEmpty()) System.out.println("OK n=1 sin trominos");
            else fallo("n=1 ha colocado trominos");
        } else {
            fallo("dibujo n=1 [" + n1 + "]");
        }
    }

    private static String capturarResuelto(int n, int x, int y)
    {
        Tablero t = new Tablero(1, 1, n, n, new Casilla(x, y));
        new Juego(t).resolver();
        return capturar(t);
    }

    private static String capturar(Tablero t)
    {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(bos, true, "UTF-8");
            java.io.PrintStream viejo = System.out;
            try {
                System.setOut(ps);
                t.renderiza();
            } finally {
                System.setOut(viejo);
            }
            return bos.toString("UTF-8");
        } catch (java.io.UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static void lineaDeComandos() throws Exception
    {
        Proceso ok = ejecutar("1", "1", "4");
        Proceso n5 = ejecutar("1", "1", "5");
        Proceso fuera = ejecutar("0", "0", "4");
        Proceso texto = ejecutar("a", "b", "c");
        Proceso pocos = ejecutar("1", "1");
        Proceso uno = ejecutar("1", "1", "1");
        if (ok.codigo == 0 && ok.texto.contains("SOLUCION:") && ok.texto.contains("# 2 5 5")) {
            System.out.println("OK cli 1 1 4");
        } else {
            fallo("cli 1 1 4 codigo=" + ok.codigo);
        }
        if (n5.codigo == 1 && n5.texto.contains("potencia de dos") && !n5.texto.contains("SOLUCION:")) {
            System.out.println("OK cli rechaza n=5");
        } else {
            fallo("cli n=5 codigo=" + n5.codigo + " " + n5.texto);
        }
        if (fuera.codigo == 1 && fuera.texto.contains("entre 1 y 4")) {
            System.out.println("OK cli rechaza 0 0 4");
        } else {
            fallo("cli 0 0 4 " + fuera.texto);
        }
        if (texto.codigo == 1 && texto.texto.contains("enteros")) {
            System.out.println("OK cli rechaza texto");
        } else {
            fallo("cli texto " + texto.texto);
        }
        if (pocos.codigo == 1 && pocos.texto.contains("Uso:")) {
            System.out.println("OK cli argumentos de menos");
        } else {
            fallo("cli argumentos " + pocos.codigo);
        }
        if (uno.codigo == 0 && uno.texto.contains("SOLUCION:") && uno.texto.contains("# ")) {
            System.out.println("OK cli n=1");
        } else {
            fallo("cli n=1 " + uno.codigo + " " + uno.texto);
        }
    }

    private static Proceso ejecutar(String... args) throws Exception
    {
        String java = System.getProperty("java.home") + "/bin/java";
        List<String> cmd = new ArrayList<String>();
        cmd.add(java);
        cmd.add("-cp");
        cmd.add(System.getProperty("java.class.path"));
        cmd.add("es.uned.lsi.triminos.Practica");
        cmd.addAll(Arrays.asList(args));
        Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
        String linea;
        while ((linea = br.readLine()) != null) {
            sb.append(linea).append('\n');
        }
        int codigo = p.waitFor();
        Proceso r = new Proceso();
        r.codigo = codigo;
        r.texto = sb.toString();
        return r;
    }

    private static final class Proceso {
        int codigo;
        String texto;
    }
}
