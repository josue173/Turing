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
    private String estadoFinal;
    private Set<String> estadosAceptacion;
    private Set<String> estadosIntermedios;
    private Map<String, Transicion> mapaTransiciones;
    private String[][] partes;
    private String[] estados;
    private int partesCount = 0;

    public TuringMachine(String estadoInicial, String estadoFinal, Set<String> estadosIntermedios, Set<String> estadosAceptacion, Map<String, Transicion> mapaTransiciones) {
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
        this.estadosIntermedios = estadosIntermedios;
        this.estadosAceptacion = estadosAceptacion;
        this.mapaTransiciones = mapaTransiciones;
        this.partes = new String[100][5];

        // Inicializar el arreglo de estados con el tamaño adecuado
        int totalEstados = 1 + 1 + estadosAceptacion.size() + estadosIntermedios.size(); // estadoInicial + estadoFinal + estados de aceptación + estados intermedios
        this.estados = new String[totalEstados];
    }

    public void imprimirConfiguracion() {
        System.out.println("Estado Inicial: " + this.estadoInicial);
        System.out.println("Estado Final: " + this.estadoFinal);
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

        // Leer estado de aceptación
        String estadoInicial = lector.readLine();

        // Leer estado final
        String estadoFinal = lector.readLine();

        // Leer estados intermedios
        Set<String> estadosIntermedios = new HashSet<>(Arrays.asList(lector.readLine().split(",")));

        // Mapa de transiciones
        Set<String> estadosAceptacion = new HashSet<>();
        HashMap<String, Transicion> mapaTransiciones = new HashMap<>();
        TuringMachine machine = new TuringMachine(estadoInicial, estadoFinal, estadosIntermedios, estadosAceptacion, mapaTransiciones);

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

        this.estados[n++] = estadoFinal;
        
        for(String estado: estados ){
            grafo.addNode(estado);
        }

        // Agregar nodos y aristas al grafo
        for (String[] parte : partes) {
            if (parte.length < 5) continue;

            String estadoActual = parte[0];
            String siguienteEstado = parte[3];

            if (estadoActual != null && grafo.getNode(estadoActual) == null) {
                grafo.addNode(estadoActual).setAttribute("ui.label", "Estado " + estadoActual);
            }
            if (siguienteEstado != null && !estadoActual.equals(siguienteEstado) && grafo.getNode(siguienteEstado) == null) {
                grafo.addNode(siguienteEstado).setAttribute("ui.label", "Estado " + siguienteEstado);
            }

            if (estadoActual != null && siguienteEstado != null) {
                String edgeId = estadoActual + "->" + siguienteEstado;
                if (grafo.getEdge(edgeId) == null) {
                    Edge arista = grafo.addEdge(edgeId, estadoActual, siguienteEstado, true);
                    arista.setAttribute("ui.label", "Transición: " + estadoActual + " a " + siguienteEstado);
                }
            }
        }

        grafo.setAttribute("ui.stylesheet",
            "node { fill-color: blue; size: 20px; text-alignment: at-right; } " +
            "edge { fill-color: black; text-size: 14px; shape: cubic-curve; arrow-size: 8px, 8px; }");
        grafo.setAttribute("ui.title", "Grafo de la Máquina de Turing");

        grafo.display();
    }
}
