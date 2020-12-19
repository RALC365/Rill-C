/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIPS;

import ThreeAddressCode.Cuadruplos;
import ThreeAddressCode.Operacion;

/**
 *
 * @author Julio Marin
 */
public class Instrucciones {
    
    public String InstruccionEtiqueta(String etiqueta) {
        return ".etiqueta: ";
    }
    
    public String Montaje(String registro, String identificador) {
        return "lw "+registro+", "+identificador;
    }
    
    public String InstruccionSuma(String izquierdo, String derecho, String resultado) {
        return "add "+resultado+", "+izquierdo+", "+derecho+" \n";
    }
    
    public String InstruccionResta(String izquierdo, String derecho, String resultado) {
        return "sub Rdestino, Rfuente1, Rfuente2";
    }
    
    public String InstruccionMultiplicacion(String izquierdo, String derecho, String resultado) {
        return "mul Rdestino, Rfuente1, Rfuente2";
    }
    
    public String InstruccionDivision(String izquierdo, String derecho, String resultado) {
        return "div Rdestino, Rfuente1, Rfuente2";
    }
    
    public String InstruccionGOTO(String etiqueta) {
        return "b etiqueta";
    }
    
    public String InstruccionIgual(String izquierdo, String derecho, String etiqueta) {
        return "beq Rfuente1, Rfuente2, etiqueta";
    }
    
    public String InstruccionDistinto(String izquierdo, String derecho, String etiqueta) {
        return "bne Rfuente1, Rfuente2, etiqueta";
    }
    
    public String InstruccionMenor(String izquierdo, String derecho, String etiqueta) {
        return "blt Rfuente1, Rfuente2, etiqueta";
    }
    
    public String InstruccionMenorIgual(String izquierdo, String derecho, String etiqueta) {
        return "ble Rfuente1, Rfuente2, etiqueta";
    }
    
    public String InstruccionMayor(String izquierdo, String derecho, String etiqueta) {
        return "bgt Rfuente1, Rfuente2, etiqueta";
    }
    
    public String InstruccionMayorIgual(String izquierdo, String derecho, String etiqueta) {
        return "bge Rfuente1, Rfuente2, etiqueta";
    }
    
}
