package com.mycompany.turing;

import java.io.IOException;

public class Turing {
    public static void main(String[] var0) {
        try {
            TuringMachine turing = TuringMachine.cargarDesdeArchivo("resources/datos.txt");
            turing.imprimirConfiguracion();
            turing.CrearGrafo();
        } catch (IOException var2) {
            System.err.println("Error al leer el archivo: " + var2.getMessage());
        }
    }
}
