/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreeAddressCode;

import ComprobacionDeTipos.TypesSubTable;

/**
 *
 * @author Julio Marin
 */
public class Cuadruplos {

    private Operacion operacion;
    private String parametroA;
    private String parametroB;
    private String resultado;
    private Object bloque;
//    private int offsetA;
//    private String typeA;
//    private String ubicacionA;
//    private int offsetB;
//    private String typeB;
//    private String ubicacionB;
//    private int offsetRes;
//    private String typeRes;
//    private String ubicacionRes;

    public Cuadruplos(Operacion operacion, String parametroA, String parametroB, String resultado) {
        this.operacion = operacion;
        this.parametroA = parametroA;
        this.parametroB = parametroB;
        this.resultado = resultado;
//        this.offsetA = -1;
//        this.typeA = "";
//        this.ubicacionA = "";
//        this.offsetB = -1;
//        this.typeB = "";
//        this.ubicacionB = "";
//        this.offsetRes = -1;
//        this.typeRes = "";
//        this.ubicacionRes = "";
    }

//    public void setInfoA(int offsetA, String typeA, String ubicacionA) {
//        this.offsetA = offsetA;
//        this.typeA = typeA;
//        this.ubicacionA = ubicacionA;
//    }
//    
//    public void setInfoB(int offsetB, String typeB, String ubicacionB) {
//        this.offsetB = offsetB;
//        this.typeB = typeB;
//        this.ubicacionB = ubicacionB;
//    }
//    
//    public void setInfoRes(int offsetRes, String typeRes, String ubicacionRes) {
//        this.offsetRes = offsetRes;
//        this.typeRes = typeRes;
//        this.ubicacionRes = ubicacionRes;
//    }
//
//    public int getOffsetA() {
//        return offsetA;
//    }
//
//    public String getTypeA() {
//        return typeA;
//    }
//
//    public String getUbicacionA() {
//        return ubicacionA;
//    }
//
//    public int getOffsetB() {
//        return offsetB;
//    }
//
//    public String getTypeB() {
//        return typeB;
//    }
//
//    public String getUbicacionB() {
//        return ubicacionB;
//    }
//
//    public int getOffsetRes() {
//        return offsetRes;
//    }
//
//    public String getTypeRes() {
//        return typeRes;
//    }
//
//    public String getUbicacionRes() {
//        return ubicacionRes;
//    }
    public Object getBloque() {
        return bloque;
    }

    public void setBloque(Object bloque) {
        this.bloque = bloque;
    }

    public Operacion getOperacion() {
        return operacion;
    }

    public String getParametroA() {
        return parametroA;
    }

    public String getParametroB() {
        return parametroB;
    }

    public String getResultado() {
        return resultado;
    }

    @Override
    public String toString() {
        if (bloque != null) {
            return "(Op: " + this.operacion + ", ParA: " + this.parametroA + ", ParB: " + this.parametroB + ", Res: " + this.resultado + ")" + ", Ambito: " + this.bloque.toString();
        }
        return "(Op: " + this.operacion + ", ParA: " + this.parametroA + ", ParB: " + this.parametroB + ", Res: " + this.resultado + ")" + ", Ambito: NULL";
    }

}
