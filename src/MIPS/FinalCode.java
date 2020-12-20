/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIPS;

import ComprobacionDeTipos.TypesSubTable;
import MIPS.Instrucciones;
import ThreeAddressCode.Cuadruplos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Julio Marin
 */
public class FinalCode {
    
    private ArrayList<Cuadruplos> tablaCuadruplos;
    private TypesSubTable ambitoTree;
    private Instrucciones instruccion;
    private HashMap<String, String> registrosTemporales;
    private HashMap<String, String> argumentos;
    private String[] usoRegistrosTemporales;
    private final String registroZero = "$zero";
    private final String registroFP = "$fp";
    private int conteoMensaje = 0;
    
    public FinalCode(ArrayList<Cuadruplos> tablaCuadruplos, TypesSubTable ambitoTree) {
        this.tablaCuadruplos = tablaCuadruplos;
        this.ambitoTree = ambitoTree;
        this.instruccion = new Instrucciones();
        registrosTemporales = new HashMap<String, String>();
        argumentos = new HashMap<String, String>();
        usoRegistrosTemporales = new String[10];
        for (int i = 0; i < 10; i++) {
            usoRegistrosTemporales[i] = "";
        }
        argumentos.put("$a0", "");
        argumentos.put("$a1", "");
        argumentos.put("$a2", "");
        argumentos.put("$a3", "");
        
    }
    
    public void GenerarCodigoFinal() {
        String cargaGlobal = ".text\n";
        String codigoMIPS = ".globl main\n";
        for (Cuadruplos cadaCuadruplo : this.tablaCuadruplos) {
            switch (cadaCuadruplo.getOperacion()) {
                case ETIQUETAMAIN: {
                    codigoMIPS += this.instruccion.InstruccionMain();
                } break;
                case ASIGNACION: {//?
                    String registroLibre = getRegistroVacio();
                    String resultado = cadaCuadruplo.getResultado();
                    codigoMIPS += this.instruccion.InstruccionCargaInmediata(registroLibre, cadaCuadruplo.getParametroA());
                    //codigoMIPS += this.instruccion.InstruccionSuma(registroZero, cadaCuadruplo.getParametroA(), registroLibre);
                    codigoMIPS += this.instruccion.InstruccionGuardarPalabra(registroLibre, 0, registroFP);
                    LiberarRegistro("", resultado);
                } break;
                case SUMA: {
                    String registroLibre = "$t"+getRegistroVacio();
                    boolean banderaDeracha = false;
                    boolean banderaIzquierda = false;
                    registrosTemporales.put(cadaCuadruplo.getResultado(), registroLibre);
                    String izquierdo = cadaCuadruplo.getParametroA();
                    String izquierdoPrevio = izquierdo;
                    String derecho = cadaCuadruplo.getParametroB();
                    String derechoPrevio = derecho;
                    if (encontrarRegistro(izquierdo) == 2) {
                        banderaIzquierda = true;
                        izquierdo = registrosTemporales.get(izquierdo);
                    } else {
                        //3
                    }
                    if (encontrarRegistro(derecho) == 2) {
                        banderaDeracha = true;
                        derecho = registrosTemporales.get(derecho);
                    } else {
                        //3
                    }
                    codigoMIPS += this.instruccion.InstruccionSuma(derecho, izquierdo, registroLibre);
                    if (banderaIzquierda) {
                        LiberarRegistro(izquierdoPrevio, izquierdo);
                    }
                    if (banderaDeracha) {
                        LiberarRegistro(derechoPrevio, derecho);
                    }
                } break;
                case RESTA: {
                    codigoMIPS += this.instruccion.InstruccionResta(cadaCuadruplo.getParametroA(), cadaCuadruplo.getParametroB(), cadaCuadruplo.getResultado());
                } break;
                case MULTIPLICACION: {
                    codigoMIPS += this.instruccion.InstruccionMultiplicacion(cadaCuadruplo.getParametroA(), cadaCuadruplo.getParametroB(), cadaCuadruplo.getResultado());
                } break;
                case DIVISION: {
                    codigoMIPS += this.instruccion.InstruccionDivision(cadaCuadruplo.getParametroA(), cadaCuadruplo.getParametroB(), cadaCuadruplo.getResultado());
                } break;
                case NEGACION: {
                    
                } break;
                case GOTO: {
                    codigoMIPS += this.instruccion.InstruccionGOTO(cadaCuadruplo.getParametroA());
                } break;
                case ASIGNARREGLO: {
                    
                } break;
                case ETIQUETA: {
                    
                } break;
                case IFIGUAL: {
                    
                } break;
                case IFMAYOR: {
                    
                } break;
                case IFMENOR: {
                    
                } break;
                case IFMAYORIGUAL: {
                    
                } break;
                case IFMENORIGUAL: {
                    
                } break;
                case IFDISTINTO: {
                    
                } break;
                case PRINT: {
                    String mensaje = "_msg"+(conteoMensaje++);
                    cargaGlobal += mensaje+": .asciiz "+""+cadaCuadruplo.getResultado()+""+"\n";
                    codigoMIPS += this.instruccion.InstruccionPrint(mensaje);
                } break;
            }
        }
        codigoMIPS += "li $v0,10\n";
        codigoMIPS += "syscall\n";
        System.out.println(cargaGlobal+codigoMIPS);
    }
    
    private String getRegistroVacio() {
        for (int i = 0; i < 10; i++) {
            if (usoRegistrosTemporales[i].isEmpty()) {
                return (usoRegistrosTemporales[i] = "$t"+i);
            }
        }
        return "";
    }
    
    private void LiberarRegistro(String llave, String valor) {
        for (int i = 0; i < 10; i++) {
            if (usoRegistrosTemporales[i].equals(valor)) {
                usoRegistrosTemporales[i] = "";
                if (registrosTemporales.containsKey(llave)) {
                    registrosTemporales.remove(llave);
                }
                break;
            }
        }
    }
    
    private int encontrarRegistro(String entrada) {
        if ((entrada == null)||(entrada.isEmpty())) {
            return 0;
        } else {
            try {
                Integer.parseInt(entrada);
                return 1;
            } catch (NumberFormatException e) {
                if (registrosTemporales.containsKey(entrada)) {
                    return 2;
                } else {
                    return 3;
                }
            }
        }
    }
    
}
