/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIPS;

import ComprobacionDeTipos.TableRow;
import ComprobacionDeTipos.TypesSubTable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Julio Marin
 */
public class Instrucciones {

    private ArrayList<String> registros = new ArrayList<>(Arrays.asList("$zero", "$at",
            "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3",
            "$t4", "$t5", "$t6", "$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6",
            "$s7", "$t8", "$t9", "$k0", "$k1", "$gp", "$sp", "$fp", "$ra"));

    private TypesSubTable root = null;

    public Instrucciones(TypesSubTable root) {
        this.root = root;
    }

    private String isValueORegister(String id) {
        try {
            Integer.parseInt(id);
            return "int";
        } catch (Exception e) {
            if (id.equals("false")) {
                return "bln";
            } else if (id.equals("true")) {
                return "bln";
            } else if (id.contains("'")) {
                return "chr";
            } else if (registros.contains(id)) {
                return "registro";
            } else {
                return "";
            }
        }
    }

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

    public String InstruccionGuardarPalabra(String registro, String registroFuente) {
        return "    sw " + registro + ", " + registroFuente + "\n";
    }

    public String InstruccionMontaje(String registro, int offset, String registroFuente) {
        return "    lw " + registro + ", " + offset + "(" + registroFuente + ")\n";
    }

    public String InstruccionMontaje(String registro, String registroFuente) {
        return "    lw " + registro + ", " + registroFuente + "\n";
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
    public String InstruccionInicioFuncion(String[] params, Object bloqueActual) {
        String ret = "";
        ret += "   sw $fp, -4($sp) \n";
        ret += "   sw $fp, -4($sp) \n";
        ret += "   sw $ra, -8($sp) \n";
        ret += "\n";
        ret += "   #Guardar los parametros \n";
        for (String param : params) {
            if (isValueORegister(ret).equals("")) {
                TableRow id = root.getID(param, bloqueActual, root);
                ret += "   sw $s0, " + id.ubicacion + " \n";
            } else {
                ret += "   Todavía no sé esta parte \n";
            }
        }
        ret += "\n";
        //Como se mandan en A debe tambien moverse los a a s
        ret += "   #mover los parámetros recibido a las S \n";
        ret += "   move $s0,$a0 \n";
        ret += "\n";
        ret += "   #Mover Apuntadores \n";
        ret += "   mov $fp, $sp \n";
        ret += "   sub $sp,$sp, SUMA_OFFETS \n";
        ret += "\n";
        ret += "   #Código dentro de la función \n";
        return ret;
    }

    public String InstruccionFinFuncion(String[] Params) {
        String ret = "";
        ret += "   _salida_fun: \n";
        ret += "   mov $sp, $fp \n";
        ret += "   lw $fp, -4($sp) \n";
        ret += "   lw $ra, -8($sp) \n";
        ret += "\n";
        ret += "   #Restaurar los parametros \n";
        ret += "   lw $s0,-12($sp) \n";
        ret += "   jr $ra \n";
        return ret;

    }

    public String InstructionSaltoAFuncion(String etq_salto) {
        return "    " + etq_salto + "\n";
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
