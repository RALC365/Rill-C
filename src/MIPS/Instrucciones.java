/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIPS;

/**
 *
 * @author Julio Marin
 */
public class Instrucciones {
    
    public String InstruccionMain() {
        String generarMain = "main: \n";
               generarMain += "move $fp, $sp \n";
        return generarMain;
    }
    
    public String InstruccionEtiqueta(String etiqueta) {
        return ".etiqueta: \n";
    }
    
    public String InstruccionCargaInmediata(String registro, String valor) {
        return "li "+registro+", "+valor+"\n";
    }
    
    public String InstruccionGuardarPalabra(String registro, int offset, String registroFuente) {
        return "sw "+registro+", "+offset+"("+registroFuente+")\n";
    }
    
    public String Montaje(String registro, String identificador) {
        return "lw "+registro+", "+identificador+"\n";
    }
    
    public String InstruccionSuma(String izquierdo, String derecho, String resultado) {
        return "add "+resultado+", "+izquierdo+", "+derecho+"\n";
    }
    
    public String InstruccionResta(String izquierdo, String derecho, String resultado) {
        return "sub Rdestino, Rfuente1, Rfuente2+\n";
    }
    
    public String InstruccionMultiplicacion(String izquierdo, String derecho, String resultado) {
        return "mul Rdestino, Rfuente1, Rfuente2+\n";
    }
    
    public String InstruccionDivision(String izquierdo, String derecho, String resultado) {
        return "div Rdestino, Rfuente1, Rfuente2+\n";
    }
    
    public String InstruccionGOTO(String etiqueta) {
        return "b etiqueta+\n";
    }
    
    public String InstruccionIgual(String izquierdo, String derecho, String etiqueta) {
        return "beq Rfuente1, Rfuente2, etiqueta+\n";
    }
    
    public String InstruccionDistinto(String izquierdo, String derecho, String etiqueta) {
        return "bne Rfuente1, Rfuente2, etiqueta+\n";
    }
    
    public String InstruccionMenor(String izquierdo, String derecho, String etiqueta) {
        return "blt Rfuente1, Rfuente2, etiqueta+\n";
    }
    
    public String InstruccionMenorIgual(String izquierdo, String derecho, String etiqueta) {
        return "ble Rfuente1, Rfuente2, etiqueta+\n";
    }
    
    public String InstruccionMayor(String izquierdo, String derecho, String etiqueta) {
        return "bgt Rfuente1, Rfuente2, etiqueta+\n";
    }
    
    public String InstruccionMayorIgual(String izquierdo, String derecho, String etiqueta) {
        return "bge Rfuente1, Rfuente2, etiqueta+\n";
    }
    
    public String InstruccionPrint(String mensajeGlobal) {
        String generarPrint = "li $v0,4\n";
        generarPrint += "la $a0,"+mensajeGlobal+"\n";
        generarPrint += "syscall\n";
        return generarPrint;
    }
    
    public String InstruccionInicioFuncion(int offset) {
        //sw $fp, -4($sp)
        //sw $ra, -8($sp)
        //move $fp,$sp
        
        return "";
    }
    
}
