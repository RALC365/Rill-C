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
        generarMain += "    move $fp, $sp \n";
        return generarMain;
    }

    public String InstruccionEtiqueta(String etiqueta) {
        return "_" + etiqueta + " \n";
    }

    public String InstruccionCargaInmediata(String registro, String valor) {
        return "    li " + registro + ", " + valor + "\n";
    }

    public String InstruccionGuardarPalabra(String registro, int offset, String registroFuente) {
        return "    sw " + registro + ", " + offset + "(" + registroFuente + ")\n";
    }

    public String InstruccionMontaje(String registro, int offset, String registroFuente) {
        return "    lw " + registro + ", " + offset + "(" + registroFuente + ")\n";
    }

    public String InstruccionSuma(String izquierdo, String derecho, String resultado) {
        return "    add " + resultado + ", " + izquierdo + ", " + derecho + "\n";
    }

    public String InstruccionResta(String izquierdo, String derecho, String resultado) {
        return "    sub " + resultado + ", " + izquierdo + ", " + derecho + "\n";
    }

    public String InstruccionMultiplicacion(String izquierdo, String derecho, String resultado) {
        return "    mul " + resultado + ", " + izquierdo + ", " + derecho + "\n";
    }

    public String InstruccionDivision(String izquierdo, String derecho, String resultado) {
        return "    div " + resultado + ", " + izquierdo + ", " + derecho + "\n";
    }

    public String InstruccionGOTO(String etiqueta) {
        return "    b " + etiqueta + "\n";
    }

    public String InstruccionIgual(String izquierdo, String derecho, String etiqueta) {
        return "    beq Rfuente1, Rfuente2, etiqueta+\n";
    }

    public String InstruccionDistinto(String izquierdo, String derecho, String etiqueta) {
        return "    bne Rfuente1, Rfuente2, etiqueta+\n";
    }

    public String InstruccionMenor(String izquierdo, String derecho, String etiqueta) {
        return "    blt Rfuente1, Rfuente2, etiqueta+\n";
    }

    public String InstruccionMenorIgual(String izquierdo, String derecho, String etiqueta) {
        return "    ble Rfuente1, Rfuente2, etiqueta+\n";
    }

    public String InstruccionMayor(String izquierdo, String derecho, String etiqueta) {
        return "    bgt Rfuente1, Rfuente2, etiqueta+\n";
    }

    public String InstruccionMayorIgual(String izquierdo, String derecho, String etiqueta) {
        return "    bge Rfuente1, Rfuente2, etiqueta+\n";
    }

    public String InstruccionPrint(String mensajeGlobal) {
        String generarPrint = "    li $v0,4\n";
        generarPrint += "    la $a0," + mensajeGlobal + "\n";
        generarPrint += "    syscall\n";
        return generarPrint;
    }

    //PARTE ELABORADA POR WILL
    public String InstruccionInicioFuncion(int offset) {
        String ret = "";
        ret += "   sw $fp, -4($sp)";
        ret += "   sw $ra, -8($sp)";
        ret += "   move $fp,$sp";
        return ret;
    }

    public String InstruccionFinFuncion(int offset) {
        String ret = "";
        ret += "    move $sp, $fp";
        ret += "    lw $fp, -4($sp)";
        ret += "    lw $ra, -8($sp)";
        ret += "    jr $ra";
        return ret;
    }

    public String InstruccionInputInt(String variable_en_memoria, int offset) {
        String ins = "";
        ins += "    li $v0, 5	# read int \n";
        ins += "    syscall		# print it \n";
        ins += InstruccionGuardarPalabra("$v0", offset, variable_en_memoria);
        return ins;
    }

    public String InstruccionInputString(String variable_en_memoria, int offset) {
        String ins = "";
        ins += "    la $a0, buffer #load byte space into address";
        ins += "    li $a1, 20 # allot the byte space for string";
        ins += "    move $t0,$a0 #save string to t0";
        ins += "    syscall";
        ins += InstruccionGuardarPalabra("$t0", offset, variable_en_memoria);
        return ins;
    }
}


/*
|Codigo de Prueba|
()(main:
	int size.,
	size: 5*(6/9+1/90*7)+6*7.,
	(pp: size:).,
	int a.,
	int b.,
	suma(a,b).,
:)

(int a, int b)suma(fun:nll
	int x:a+b.,
:)
 */
