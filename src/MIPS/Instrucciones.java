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
    
    public String SearchInstruccion(Cuadruplos lineaCuadruplo) {
        Operacion op = lineaCuadruplo.getOperacion();
        String izquierdo = lineaCuadruplo.getParametroA();
        String derecho = lineaCuadruplo.getParametroB();
        String resultado = lineaCuadruplo.getResultado();
        switch (op) {
            case SUMA: {
                return InstruccionSuma(izquierdo, derecho, resultado);
            }
            case RESTA: {
                return InstruccionResta(izquierdo, derecho, resultado);
            }
            case MULTIPLICACION: {
                return InstruccionMultiplicacion(izquierdo, derecho, resultado);
            }
            case DIVISION: {
                return InstruccionDivision(izquierdo, derecho, resultado);
            }
            case ASIGNACION: {
                return InstruccionSuma(izquierdo, derecho, resultado);
            }
            case NEGACION: {
                return InstruccionSuma(izquierdo, derecho, resultado);
            }
            case GOTO: {
                return InstruccionGOTO(izquierdo);
            }
            case ASIGNARREGLO: {
                return InstruccionSuma(izquierdo, derecho, resultado);
            }
            case ETIQUETA: {
                return InstruccionSuma(izquierdo, derecho, resultado);
            }
            case IFIGUAL: {
                return InstruccionSuma(izquierdo, derecho, resultado);
            }
            case IFMAYOR: {
                return InstruccionSuma(izquierdo, derecho, resultado);
            }
            case IFMENOR: {
                return InstruccionSuma(izquierdo, derecho, resultado);
            }
            case IFMAYORIGUAL: {
                return InstruccionSuma(izquierdo, derecho, resultado);
            }
            case IFMENORIGUAL: {
                return InstruccionSuma(izquierdo, derecho, resultado);
            }
            case IFDISTINTO: {
                return InstruccionSuma(izquierdo, derecho, resultado);
            }
        }
        return "";
    }
    
    private String InstruccionSuma(String izquierdo, String derecho, String resultado) {
        return "add Rdestino, Rfuente1, Rfuente2";
    }
    
    private String InstruccionResta(String izquierdo, String derecho, String resultado) {
        return "sub Rdestino, Rfuente1, Rfuente2";
    }
    
    private String InstruccionMultiplicacion(String izquierdo, String derecho, String resultado) {
        return "mul Rdestino, Rfuente1, Rfuente2";
    }
    
    private String InstruccionDivision(String izquierdo, String derecho, String resultado) {
        return "div Rdestino, Rfuente1, Rfuente2";
    }
    
    private String InstruccionGOTO(String etiqueta) {
        return "b etiqueta";
    }
    
    private String InstruccionIgual(String izquierdo, String derecho, String etiqueta) {
        return "beq Rfuente1, Rfuente2, etiqueta";
    }
    
    private String InstruccionDistinto(String izquierdo, String derecho, String etiqueta) {
        return "bne Rfuente1, Rfuente2, etiqueta";
    }
    
    private String InstruccionMenor(String izquierdo, String derecho, String etiqueta) {
        return "blt Rfuente1, Rfuente2, etiqueta";
    }
    
    private String InstruccionMenorIgual(String izquierdo, String derecho, String etiqueta) {
        return "ble Rfuente1, Rfuente2, etiqueta";
    }
    
    private String InstruccionMayor(String izquierdo, String derecho, String etiqueta) {
        return "bgt Rfuente1, Rfuente2, etiqueta";
    }
    
    private String InstruccionMayorIgual(String izquierdo, String derecho, String etiqueta) {
        return "bge Rfuente1, Rfuente2, etiqueta";
    }
    
}
