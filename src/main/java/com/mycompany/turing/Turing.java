package com.mycompany.turing;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Turing {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            TuringMachine turing;

            System.out.println("Seleccione el modo de carga de la máquina de Turing:");
            System.out.println("1. Cargar desde archivo");
            System.out.println("2. Ingresar manualmente");
            int opcion = Integer.parseInt(scanner.nextLine().trim());

            if (opcion == 1) {
                // Cargar desde archivo
                turing = TuringMachine.cargarDesdeArchivo("resources/datos.txt");
            } else if (opcion == 2) {
                // Ingresar manualmente
                turing = cargarManual(scanner);
            } else {
                System.out.println("Opción no válida.");
                return;
            }

            turing.imprimirConfiguracion();
            turing.CrearGrafo();

            while (true) {
                // Ingreso de cadenas
                System.out.println("Ingrese una cadena para verificar:");
                String cadena = scanner.nextLine().trim();
                boolean aceptada = turing.lecturaCadenas(cadena);
                boolean decidible = turing.Decidibles(cadena);

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

    public static TuringMachine cargarManual(Scanner scanner) {
         TuringMachine turing;
    System.out.println("Ingrese el estado inicial:");
    String estadoInicial = scanner.nextLine().trim();

    System.out.println("Ingrese los estados de aceptación (separados por comas):");
    Set<String> estadosAceptacion = new HashSet<>();
    for (String estado : scanner.nextLine().split(",")) {
        estadosAceptacion.add(estado.trim());
    }

    System.out.println("Ingrese los estados intermedios (separados por comas):");
    Set<String> estadosIntermedios = new HashSet<>();
    for (String estado : scanner.nextLine().split(",")) {
        estadosIntermedios.add(estado.trim());
    }

    Map<String, Transicion> mapaTransiciones = new HashMap<>();
    System.out.println("Ingrese las transiciones en el formato especificado (escriba 'fin' para terminar):");

    while (true) {
        System.out.println("Estado actual:");
        String estadoActual = scanner.nextLine().trim();
        if (estadoActual.equalsIgnoreCase("fin")) break;

        System.out.println("Estado siguiente:");
        String siguienteEstado = scanner.nextLine().trim();
        if (siguienteEstado.equalsIgnoreCase("fin")) break;

        System.out.println("Función de transición (formato a:b,c):");
        String funcionTransicion = scanner.nextLine().trim();
        if (funcionTransicion.equalsIgnoreCase("fin")) break;

        String[] partesTransicion = funcionTransicion.split(":|,");
        if (partesTransicion.length == 3) {
            char simboloLeido = partesTransicion[0].charAt(0);
            char simboloEscrito = partesTransicion[1].charAt(0);
            char movimiento = partesTransicion[2].charAt(0);

            // Añadimos la transición al mapa
            mapaTransiciones.put(estadoActual + "," + simboloLeido,
                new Transicion(simboloLeido, simboloEscrito, siguienteEstado, movimiento));
        } else {
            System.out.println("Formato incorrecto. Intente nuevamente.");
        }
    }

    return new TuringMachine(estadoInicial, estadosIntermedios, estadosAceptacion, mapaTransiciones);
    }
}