package com.mycompany.turing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

class TuringMachine {
    private String estadoInicial;
    private Set<String> estadosAceptacion;
    private Set<String> estadosIntermedios;
    private Map<String, Transicion> mapaTransiciones;
    private String[][] partes;
    private String[] estados;
    private int partesCount = 0;

    public TuringMachine(String estadoInicial, Set<String> estadosIntermedios, Set<String> estadosAceptacion, Map<String, Transicion> mapaTransiciones) {
        this.estadoInicial = estadoInicial;
        this.estadosIntermedios = estadosIntermedios;
        this.estadosAceptacion = estadosAceptacion;
        this.mapaTransiciones = mapaTransiciones;
        this.partes = new String[100][5];

        // Inicializar el arreglo de estados con el tamaño adecuado
        int totalEstados = 1 + estadosAceptacion.size() + estadosIntermedios.size(); // estadoInicial + estados de aceptación + estados intermedios
        this.estados = new String[totalEstados];
    }

    public void imprimirConfiguracion() {
        System.out.println("Estado Inicial: " + this.estadoInicial);
        System.out.println("Estados de Aceptación: " + String.join(", ", this.estadosAceptacion));
        System.out.println("Estados intermedios: " + String.join(", ", this.estadosIntermedios));
        System.out.println("Tabla de transición de estados:");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s%n", "Estado Inicial", "Símbolo Leído", "Nuevo Estado", "Símbolo Escrito", "Movimiento");
        System.out.println("---------------------------------------------------------------------");

        for (Map.Entry<String, Transicion> entradaTransicion : this.mapaTransiciones.entrySet()) {
            String estadoActual = entradaTransicion.getKey().split(",")[0];
            char simboloLeido = entradaTransicion.getValue().simboloLeido;
            String nuevoEstado = entradaTransicion.getValue().siguienteEstado;
            char simboloEscrito = entradaTransicion.getValue().simboloEscrito;
            char movimiento = entradaTransicion.getValue().movimiento;
            System.out.printf("%-15s %-15s %-15s %-15s %-15s%n", estadoActual, simboloLeido, nuevoEstado, simboloEscrito, movimiento);
        }
    }

    public static TuringMachine cargarDesdeArchivo(String ruta) throws IOException {
        BufferedReader lector = new BufferedReader(new FileReader(ruta));

        // Leer estado inicial
        String estadoInicial = lector.readLine();

        // Leer estados de aceptación
        Set<String> estadosAceptacion = new HashSet<>(Arrays.asList(lector.readLine().split(",")));

        // Leer estados intermedios
        Set<String> estadosIntermedios = new HashSet<>(Arrays.asList(lector.readLine().split(",")));

        // Mapa de transiciones
        HashMap<String, Transicion> mapaTransiciones = new HashMap<>();
        TuringMachine machine = new TuringMachine(estadoInicial, estadosIntermedios, estadosAceptacion, mapaTransiciones);

        String linea;
        while ((linea = lector.readLine()) != null) {
            String[] partesLinea = linea.split(",");
            if (partesLinea.length < 5) {
                System.out.println("Línea inválida en el archivo: " + linea);
                continue;
            }

            for (int i = 0; i < partesLinea.length; i++) {
                machine.partes[machine.partesCount][i] = partesLinea[i];
            }
            machine.partesCount++;

            String estadoActual = partesLinea[0];
            char simboloLeido = partesLinea[1].charAt(0);
            char simboloEscrito = partesLinea[2].charAt(0);
            String siguienteEstado = partesLinea[3];
            char movimiento = partesLinea[4].charAt(0);
            mapaTransiciones.put(estadoActual + "," + simboloLeido, new Transicion(simboloLeido, simboloEscrito, siguienteEstado, movimiento));
        }

        lector.close();
        return machine;
    }

    public void CrearGrafo() {
    System.setProperty("org.graphstream.ui", "swing");
    Graph grafo = new SingleGraph("Máquina de Turing");

    // Agregar estados al arreglo 'estados'
    int n = 0;
    this.estados[n++] = estadoInicial;
    
    for (String estado : estadosAceptacion) {
        this.estados[n++] = estado;
    }

    for (String estado : estadosIntermedios) {
        this.estados[n++] = estado;
    }
    
    // Crear nodos para cada estado
    for (String estado : estados) {
        if (estado != null) {
            grafo.addNode(estado).setAttribute("ui.label", "Estado " + estado);
        }
    }

    // Crear aristas (transiciones) a partir de mapaTransiciones
    for (Map.Entry<String, Transicion> entrada : mapaTransiciones.entrySet()) {
        String estadoActual = entrada.getKey().split(",")[0];
        String siguienteEstado = entrada.getValue().siguienteEstado;
        
        // Agregar arista entre estadoActual y siguienteEstado si no existe
        String edgeId = estadoActual + "->" + siguienteEstado;
        if (grafo.getEdge(edgeId) == null) {
            Edge arista = grafo.addEdge(edgeId, estadoActual, siguienteEstado, true);
            arista.setAttribute("ui.label", "Transición: " + estadoActual + " a " + siguienteEstado);
        }
    }

    // Estilo del grafo
    grafo.setAttribute("ui.stylesheet",
        "node { fill-color: blue; size: 20px; text-alignment: at-right; } " +
        "edge { fill-color: black; text-size: 14px; shape: cubic-curve; arrow-size: 8px, 8px; }");
    grafo.setAttribute("ui.title", "Grafo de la Máquina de Turing");

    grafo.display();
}


    public boolean lecturaCadenas(String cadena) {
        // Lectura de cadenas
        char[] cinta = (cadena + "    ").toCharArray();
        int posicion = 0;
        String estadoActual = this.estadoInicial;
        System.out.println("Cadena a procesar: " + cadena);
        System.out.println("Cinta inicial: " + String.valueOf(cinta));

        while (!estadosAceptacion.contains(estadoActual)) {
            char simboloLeido = cinta[posicion];
            String clave = estadoActual + "," + simboloLeido;
            System.out.println("Leyendo símbolo: " + simboloLeido + " en estado: " + estadoActual);
            Transicion transicion = this.mapaTransiciones.get(clave);
            if (transicion == null) {
                System.out.println("No hay transición para: " + clave);
                return false;
            }

            cinta[posicion] = transicion.simboloEscrito;
            estadoActual = transicion.siguienteEstado;

            if (transicion.movimiento == 'R') {
                posicion++;
            } else if (transicion.movimiento == 'L') {
                posicion--;
            }

            if (posicion < 0 || posicion >= cinta.length) {
                System.out.println("La cabeza de lectura se salió de los límites.");
                return false;
            }

            // Impresión de la posición de la cadena
            System.out.println("Estado actual: " + estadoActual);
            System.out.println("Cinta actual: " + String.valueOf(cinta));
            System.out.println("Posición: " + posicion);
        }
        
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Cadena aceptada. Estado de aceptación alcanzado: " + estadoActual);
        return true;
    }

    public boolean Decidibles(String cadena) {
        char[] cinta = (cadena + "    ").toCharArray();
        int posicion = 0;
        String estadoActual = this.estadoInicial;
        Set<String> estadosRecorridos = new HashSet<>();

        while (!estadosAceptacion.contains(estadoActual)) {
            if (estadosRecorridos.contains(estadoActual + "," + posicion)) {
                System.out.println("Cadena no decidible. Se detectó un bucle infinito.");
                return false;
            }
            estadosRecorridos.add(estadoActual + "," + posicion);

            char simboloLeido = cinta[posicion];
            String clave = estadoActual + "," + simboloLeido;
            Transicion transicion = this.mapaTransiciones.get(clave);
            if (transicion == null) {
                System.out.println("Cadena no decidible. No hay transición para: " + clave);
                return false;
            }

            // Movimientos R y L 
            cinta[posicion] = transicion.simboloEscrito;
            estadoActual = transicion.siguienteEstado;

            if (transicion.movimiento == 'R') {
                posicion++;
            } else if (transicion.movimiento == 'L') {
                posicion--;
            }

            if (posicion < 0 || posicion >= cinta.length) {
                System.out.println("Cadena no decidible.");
                return false;
            }
        }

        System.out.println("Cadena decidible y aceptada. Estado de aceptación alcanzado: " + estadoActual);
        return true;
    }
}
