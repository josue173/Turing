package com.mycompany.turing;

/**
 *
 * @author josue
 */
class Transicion {
   char simboloLeido;
   char simboloEscrito;
   String siguienteEstado;
   char movimiento;

   public Transicion(char var1, char var2, String var3, char var4) {
      this.simboloLeido = var1;
      this.simboloEscrito = var2;
      this.siguienteEstado = var3;
      this.movimiento = var4;
   }

   public String toString() {
      return this.simboloLeido + " -> " + this.simboloEscrito + ", " + this.siguienteEstado + ", " + this.movimiento;
   }
}
