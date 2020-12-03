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
        System.out.println("A");
        iterateTree(model.getRoot());
        System.out.println("Finito");
    }

    private String iterateTree(Object eachNode) throws TypeErrorException {
        int cc = model.getChildCount(eachNode);
        System.out.println("B");
        for (int i = 0; i < cc; i++) {       
            System.out.println("Ci: "+eachNode.toString());
            Object child = model.getChild(eachNode, i);
            System.out.println("Cf: "+child.toString());
            if (eachNode.equals(model.getRoot())) {
                System.out.println("D");
                //Some Code Here
                iterateTree(child);
            }
            
            if (child.toString().equals("FOR")) {
                System.out.println("E");
                //Some Code Here
                iterateTree(child);
            }
            
            if (child.toString().equals("WHILE")) {
                System.out.println("F");
                //Some Code Here
                iterateTree(child);
            }
            
            if (child.toString().equals("IF")) {
                System.out.println("G");
                //Some Code Here
                iterateTree(child);
            }
            
            if (child.toString().equals("ASSIGN")) {

                Object leftChild = model.getChild(child, 0);
                Object rightChild = model.getChild(child, 1);
                
                if ((model.getChildCount(leftChild) == 0)&&(model.getChildCount(rightChild) == 0)) {
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, rightChild.toString(), "", leftChild.toString()));
//                    return leftChild.toString();
                } else {
                    iterateTree(child);
                }
            }
            
            if (child.toString().equals("+")) {
                return ArithmeticBuild(child, Operacion.SUMA);
            }
            
            if (child.toString().equals("-")) {
                return ArithmeticBuild(child, Operacion.RESTA);
            }
            
            if (child.toString().equals("*")) {
                return ArithmeticBuild(child, Operacion.MULTIPLICACION);
            }
            
            if (child.toString().equals("/")) {
                return ArithmeticBuild(child, Operacion.DIVISION);
            }
        }
        
        return null;
        
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
                temporalIzquierdo = iterateTree(currentNode);
            }
            if (model.getChildCount(rightChild) > 0) {
                temporalDerecho = iterateTree(currentNode);
            }
            temporalRetorno = "t"+(this.conteoTemporales++);
            this.tablaCuadruplos.add(new Cuadruplos(operacionEnum, temporalIzquierdo, temporalDerecho, temporalRetorno));
            return temporalRetorno;
        }
    }

    public ArrayList<Cuadruplos> getTablaCuadruplos() {
        return tablaCuadruplos;
    }
    
    public void imprimirCuadruplos() {
        int count = 1;
        for(Cuadruplos cadaCuadruplo: this.tablaCuadruplos) {
            System.out.println((count++)+": "+cadaCuadruplo.toString());
        }
    }
    
}
