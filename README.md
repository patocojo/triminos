# triminos

A Trimino Puzzle Solver.

Práctica de programación orientada a objetos: rellenar una cuadrícula N×N con trominós en L, por divide y vencerás. N tiene que ser una potencia de dos (1, 2, 4, 8, …) y una casilla queda excluida.

## Compilar y ejecutar

Desde la raíz del proyecto, con JDK 8 o posterior:

```bash
javac -encoding UTF-8 -d bin src/es/uned/lsi/triminos/*.java
java -cp bin es.uned.lsi.triminos.Practica 1 1 4
java -cp bin es.uned.lsi.triminos.Prueba
```

`Practica` recibe `x y d`:

- `d` es el lado del tablero y tiene que ser potencia de dos.
- `x` e `y` son la casilla excluida, con coordenadas de 1 a `d`.

El constructor `Tablero(int d, Casilla)` usa el otro convenio: coordenadas de 0 a `d - 1`.

`Prueba` recorre los tableros de lado 1, 2, 4, 8, 16 y 32 y comprueba que la solución cubre el cuadrado sin solapes. También rechaza los lados que no son potencia de dos, los tableros que no son cuadrados y las casillas fuera de rango.

En Eclipse el directorio fuente es `src` y la salida es `bin`. Las clases están en el paquete `es.uned.lsi.triminos`.
