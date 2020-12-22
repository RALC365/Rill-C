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

/**
 *
 * @author Julio Marin
 */
public class FinalCode {

    private final ArrayList<Cuadruplos> tablaCuadruplos;
    private final TypesSubTable ambitoTree;
    private final Instrucciones instruccion;
    private int conteoMensaje = 0;

    public FinalCode(ArrayList<Cuadruplos> tablaCuadruplos, TypesSubTable ambitoTree) {
        this.tablaCuadruplos = tablaCuadruplos;
        this.ambitoTree = ambitoTree;
        this.instruccion = new Instrucciones(ambitoTree);
    }

    public void GenerarCodigoFinal() {
        String cargaGlobal = "      .data\n";
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
                }
                break;
                case ASIGNARREGLO: {
                }
                break;
                case ETIQUETA: {
                }
                break;
                case IFIGUAL: {
                }
                break;
                case IFMAYOR: {
                }
                break;
                case IFMENOR: {
                }
                break;
                case IFMAYORIGUAL: {
                }
                break;
                case IFMENORIGUAL: {
                }
                break;
                case IFDISTINTO: {
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
            }
        }
        codigoMIPS += "    li $v0,10\n";
        codigoMIPS += "    syscall\n";
        System.out.println(cargaGlobal + codigoMIPS);
    }
}
