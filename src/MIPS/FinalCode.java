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

/**
 *
 * @author Julio Marin
 */
public class FinalCode {
    
    private ArrayList<Cuadruplos> tablaCuadruplos;
    private TypesSubTable ambitoTree;
    private Instrucciones instruccion;
    private HashMap<String, String> registrosTemporales;
    
    public FinalCode(ArrayList<Cuadruplos> tablaCuadruplos, TypesSubTable ambitoTree) {
        this.tablaCuadruplos = tablaCuadruplos;
        this.ambitoTree = ambitoTree;
        this.instruccion = new Instrucciones();
        registrosTemporales = new HashMap<String, String>();
        registrosTemporales.put("$t0", "");
        registrosTemporales.put("$t1", "");
        registrosTemporales.put("$t2", "");
        registrosTemporales.put("$t3", "");
        registrosTemporales.put("$t4", "");
        registrosTemporales.put("$t5", "");
        registrosTemporales.put("$t6", "");
        registrosTemporales.put("$t7", "");
        registrosTemporales.put("$t8", "");
        registrosTemporales.put("$t9", "");
    }
    
    public void GenerarCodigoFinal() {
        String codigoMIPS = "";
        for (Cuadruplos cadaCuadruplo : this.tablaCuadruplos) {
            switch (cadaCuadruplo.getOperacion()) {
                case ETIQUETAMAIN: {
                    codigoMIPS += ".main:\n";
                } 
                case SUMA: {
                    codigoMIPS += this.instruccion.InstruccionSuma(cadaCuadruplo.getParametroA(), cadaCuadruplo.getParametroB(), cadaCuadruplo.getResultado());
                }
                case RESTA: {
                    codigoMIPS += this.instruccion.InstruccionResta(cadaCuadruplo.getParametroA(), cadaCuadruplo.getParametroB(), cadaCuadruplo.getResultado());
                }
                case MULTIPLICACION: {
                    codigoMIPS += this.instruccion.InstruccionMultiplicacion(cadaCuadruplo.getParametroA(), cadaCuadruplo.getParametroB(), cadaCuadruplo.getResultado());
                }
                case DIVISION: {
                    codigoMIPS += this.instruccion.InstruccionDivision(cadaCuadruplo.getParametroA(), cadaCuadruplo.getParametroB(), cadaCuadruplo.getResultado());
                }
                case NEGACION: {
                    
                }
                case GOTO: {
                    codigoMIPS += this.instruccion.InstruccionGOTO(cadaCuadruplo.getParametroA());
                }
                case ASIGNARREGLO: {
                    
                }
                case ETIQUETA: {
                    
                }
                case IFIGUAL: {
                    
                }
                case IFMAYOR: {
                    
                }
                case IFMENOR: {
                    
                }
                case IFMAYORIGUAL: {
                    
                }
                case IFMENORIGUAL: {
                    
                }
                case IFDISTINTO: {
                    
                }
            }
        }
        
        
    }
    
}
