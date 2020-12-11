/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreeAddressCode;

import ComprobacionDeTipos.TypeErrorException;
import ThreeAddressCode.Operacion;
import java.util.ArrayList;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * 
 * @author Julio Marin
 */
public class ThreeAddressTable { 
    
    private ArrayList<Cuadruplos> tablaCuadruplos = new ArrayList();
    private final DefaultTreeModel model;
    private int conteoTemporales;
    
    public ThreeAddressTable(DefaultTreeModel model) throws TypeErrorException {
        this.model = model;
        this.conteoTemporales = 0;
        iterateTree(model.getRoot());
        imprimirCuadruplos();
    }

    private String iterateTree(Object eachNode) throws TypeErrorException {
        int cc = model.getChildCount(eachNode);
        for (int i = 0; i < cc; i++) {       
            Object child = model.getChild(eachNode, i);
            if (eachNode.equals(model.getRoot())) {
                iterateTree(child);
            }
            
            if (child.toString().equals("FOR")) {
                iterateTree(child);
            }
            
            if (child.toString().equals("WHILE")) {
                iterateTree(child);
            }
            
            if (child.toString().equals("IF")) {
                iterateTree(child);
            }
            
            if (child.toString().equals("ASSIGN")) {
                AsignationBuild(child);
            }
            
            if (child.toString().equals("DECLR ARRAY")) {
                AsignationArray(model.getChild(child, 1).toString(), model.getChild(child, 2).toString(), model.getChild(child, 3));
            }
            
            if (child.toString().equals("DECLR MATRIX")) {
            
            }
            
        }
        
        return null;
        
    }
    
    private String ArithmeticTree(Object childNode) throws TypeErrorException {
        
        if (childNode.toString().equals("+")) {
            return ArithmeticBuild(childNode, Operacion.SUMA);
        }

        if (childNode.toString().equals("-")) {
            if ((model.getChildCount(childNode) == 1)) {
                String temporalRetorno = "t"+(this.conteoTemporales++);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.NEGACION, model.getChild(childNode, 0).toString(), "", temporalRetorno));
                return temporalRetorno;
            } else {
                return ArithmeticBuild(childNode, Operacion.RESTA);
            }
        }

        if (childNode.toString().equals("*")) {
            return ArithmeticBuild(childNode, Operacion.MULTIPLICACION);
        }

        if (childNode.toString().equals("/")) {
            return ArithmeticBuild(childNode, Operacion.DIVISION);
        }
        
        return null;
        
    }
    
    private void AsignationBuild(Object currentNode) throws TypeErrorException {
        
        Object leftChild = model.getChild(currentNode, 0);
        Object rightChild = model.getChild(currentNode, 1);

        if ((model.getChildCount(leftChild) == 0)&&(model.getChildCount(rightChild) == 0)) {
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, rightChild.toString(), "", leftChild.toString()));
        } else {
            String temporalRetorno = ArithmeticTree(rightChild);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, temporalRetorno, "", leftChild.toString()));
        }
        
    }
    
    private void AsignationArray(String tipo, String identificador, Object currentNode) throws TypeErrorException {
        
        int cc = model.getChildCount(currentNode);
        int tamanio = 0;
        
        switch(tipo) {
            case "chr":
                tamanio = 2;
                break;
            case "int": 
                tamanio = 4;
                break;
            case "bln": 
                tamanio = 1;
                break;
        }
        
        for (int i = 0; i < cc; i++) {
            Object child = model.getChild(currentNode, i);
            String temporalIndice = "t"+(this.conteoTemporales++);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.MULTIPLICACION, i+"", tamanio+"", temporalIndice));
            if (i == 0) {
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, temporalIndice, "", identificador));
            }
            String temporalRetorno = "t"+(this.conteoTemporales++);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNARREGLO, child.toString(), temporalIndice, temporalRetorno));
        }
    }

    private String ArithmeticBuild(Object currentNode, Operacion operacionEnum) throws TypeErrorException {
        
        Object leftChild = model.getChild(currentNode, 0);
        Object rightChild = model.getChild(currentNode, 1);
        String temporalRetorno;

        if ((model.getChildCount(leftChild) == 0)&&(model.getChildCount(rightChild) == 0)) {
            temporalRetorno = "t"+(this.conteoTemporales++);
            this.tablaCuadruplos.add(new Cuadruplos(operacionEnum, leftChild.toString(), rightChild.toString(), temporalRetorno));
            return temporalRetorno;
        } else {
            String temporalIzquierdo = leftChild.toString();
            String temporalDerecho = rightChild.toString();
            if (model.getChildCount(leftChild) > 0) {
                temporalIzquierdo = ArithmeticTree(leftChild);
            }
            if (model.getChildCount(rightChild) > 0) {
                temporalDerecho = ArithmeticTree(rightChild);
            }
            temporalRetorno = "t"+(this.conteoTemporales++);
            this.tablaCuadruplos.add(new Cuadruplos(operacionEnum, temporalIzquierdo, temporalDerecho, temporalRetorno));
            return temporalRetorno;
        }
    }

    public ArrayList<Cuadruplos> getTablaCuadruplos() {
        return tablaCuadruplos;
    }
    
    private void imprimirCuadruplos() {
        int count = 1;
        for(Cuadruplos cadaCuadruplo: this.tablaCuadruplos) {
            System.out.println((count++)+": "+cadaCuadruplo.toString());
        }
    }
    
}
