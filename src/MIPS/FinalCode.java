/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIPS;

import ComprobacionDeTipos.TypesSubTable;
import ThreeAddressCode.Cuadruplos;
import ThreeAddressCode.Operacion;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 *
 */
public class FinalCode {

    private final ArrayList<Cuadruplos> tablaCuadruplos;
    private final TypesSubTable ambitoTree;
    private final Instrucciones instruccion;
    private int conteoMensaje = 0;
    private int bandera_etiqueta = 0;
    private Stack<ArrayList> paramsStack = new Stack<>();
    private ArrayList<String> params_per_call = new ArrayList();

    public FinalCode(ArrayList<Cuadruplos> tablaCuadruplos, TypesSubTable ambitoTree) {
        this.tablaCuadruplos = tablaCuadruplos;
        this.ambitoTree = ambitoTree;
        this.instruccion = new Instrucciones(ambitoTree);
    }

    public String GenerarCodigoFinal() {
        String cargaGlobal = "       .data\n";
        String codigoMIPS = "       .text\n"
                + "     .globl main\n";
        for (Cuadruplos c : this.tablaCuadruplos) {
            switch (c.getOperacion()) {
                case ETIQUETAMAIN: {
                    codigoMIPS += this.instruccion.InstruccionMain();
                }
                break;
                case ASIGNACION: {
                    codigoMIPS += instruccion.Asignacion(c.getParametroA(), c.getResultado(), c.getBloque());
                }
                break;
                case SUMA: {
                    codigoMIPS += instruccion.OperacionAritmetica(Operacion.SUMA, c.getParametroA(), c.getParametroB(), c.getResultado(), c.getBloque());
                }
                break;
                case GLOBAL: {
                    cargaGlobal += c.getResultado() + ":    .word 0\n";
                }
                break;
                case RESTA: {
                    codigoMIPS += instruccion.OperacionAritmetica(Operacion.RESTA, c.getParametroA(), c.getParametroB(), c.getResultado(), c.getBloque());
                }
                break;
                case MULTIPLICACION: {
                    codigoMIPS += instruccion.OperacionAritmetica(Operacion.MULTIPLICACION, c.getParametroA(), c.getParametroB(), c.getResultado(), c.getBloque());
                }
                break;
                case DIVISION: {
                    codigoMIPS += instruccion.OperacionAritmetica(Operacion.DIVISION, c.getParametroA(), c.getParametroB(), c.getResultado(), c.getBloque());
                }
                break;
                case NEGACION: {
                }
                break;
                case GOTO: {
                    codigoMIPS += instruccion.Salto(c.getParametroA());
                }
                break;
                case ASIGNARREGLO: {
                }
                break;
                case ETIQUETAFUN: {
                    if (bandera_etiqueta == 0) {
                        codigoMIPS += "    li $v0,10\n";
                        codigoMIPS += "    syscall\n\n";
                        bandera_etiqueta++;
                    }
                    codigoMIPS += "_fun_" + c.getParametroA() + ":\n";
                    codigoMIPS += instruccion.InicioFuncion(params_per_call, c.getBloque());
                }
                break;
                case ETIQUETA: {
                    if (bandera_etiqueta == 0) {
                        codigoMIPS += "    li $v0,10\n";
                        codigoMIPS += "    syscall\n\n";
                        bandera_etiqueta++;
                    }
                    codigoMIPS += "_" + c.getParametroA() + ":\n";
                }
                break;
                case PARAM: {
                    params_per_call.add(c.getResultado());
                }
                break;
                case CALL: {
                    paramsStack.push(params_per_call);
                    codigoMIPS += instruccion.PreinicioFuncion(params_per_call, c.getBloque(), c.getResultado());
                }
                break;
                case RETURN: {
                    codigoMIPS += instruccion.Ret(c.getResultado(), c.getBloque());
                }
                break;
                case FINFUNCION: {
                    ArrayList<String> ps = paramsStack.pop();
                    codigoMIPS += instruccion.FinFuncion(ps, c.getBloque(), "_" + c.getParametroA());
                }
                break;
                case IFIGUAL: {
                    codigoMIPS += instruccion.SaltoCondicional(Operacion.IFIGUAL, c.getParametroA(), c.getParametroB(), c.getResultado(), c.getBloque());
                }
                break;
                case IFMAYOR: {
                    codigoMIPS += instruccion.SaltoCondicional(Operacion.IFMAYOR, c.getParametroA(), c.getParametroB(), c.getResultado(), c.getBloque());
                }
                break;
                case IFMENOR: {
                    codigoMIPS += instruccion.SaltoCondicional(Operacion.IFMENOR, c.getParametroA(), c.getParametroB(), c.getResultado(), c.getBloque());
                }
                break;
                case IFMAYORIGUAL: {
                    codigoMIPS += instruccion.SaltoCondicional(Operacion.IFMAYORIGUAL, c.getParametroA(), c.getParametroB(), c.getResultado(), c.getBloque());
                }
                break;
                case IFMENORIGUAL: {
                    codigoMIPS += instruccion.SaltoCondicional(Operacion.IFMENORIGUAL, c.getParametroA(), c.getParametroB(), c.getResultado(), c.getBloque());
                }
                break;
                case IFDISTINTO: {
                    codigoMIPS += instruccion.SaltoCondicional(Operacion.IFDISTINTO, c.getParametroA(), c.getParametroB(), c.getResultado(), c.getBloque());
                }
                break;
                case PRINT: {
                    String mensaje = "_msg" + (conteoMensaje++);
                    if (c.getResultado().contains("\"")) {
                        cargaGlobal += mensaje + ": .asciiz " + c.getResultado() + "\n";
                        codigoMIPS += this.instruccion.Print(mensaje);
                    } else {
                        codigoMIPS += this.instruccion.PrintVariable(c.getResultado(), c.getBloque());
                    }
                }
                break;
                case INPUT: {
                    codigoMIPS += instruccion.InputInt(c.getResultado(), c.getBloque());
                }
                break;
            }
        }
        if (bandera_etiqueta == 0) {
            codigoMIPS += "    li $v0,10\n";
            codigoMIPS += "    syscall\n";
            bandera_etiqueta++;
        }
        System.out.println(cargaGlobal + codigoMIPS);
        return cargaGlobal + codigoMIPS;
    }
}
