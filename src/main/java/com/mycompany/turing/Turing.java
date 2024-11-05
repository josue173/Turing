package com.mycompany.turing;

import java.io.IOException;
import java.util.Scanner;

public class Turing {
    public static void main(String[] args) {
        try {
            TuringMachine turing = TuringMachine.cargarDesdeArchivo("resources/datos.txt");
            turing.imprimirConfiguracion();
            turing.CrearGrafo();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                
                //Ingreso de cadenas
                System.out.println("Ingrese una cadena para verificar:");
                String cadena = scanner.nextLine().trim();
                boolean aceptada = turing.lecturaCadenas(cadena);
                boolean decidible = turing.Decidibles(cadena);

                //Si la cadena es aceptada o decidible
                if (aceptada) {
                    System.out.println("La cadena fue aceptada.");
                } else {
                    System.out.println("La cadena fue rechazada.");
                }

                if (decidible) {
                    System.out.println("La cadena es decidible.");
                } else {
                    System.out.println("La cadena no es decidible.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Se produjo un error: " + e.getMessage());
        }
    }
}

