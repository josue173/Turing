/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.turing;

/**
 *
 * @author josue
 */
import java.io.IOException;

public class Turing {
   public static void main(String[] var0) {
      try {
         TuringMachine var1 = TuringMachine.cargarDesdeArchivo("resources/datos.txt");
         var1.imprimirConfiguracion();
         var1.CrearGrafo();
      } catch (IOException var2) {
         System.err.println("Error al leer el archivo: " + var2.getMessage());
      }

   }
}