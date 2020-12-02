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
    
    public ThreeAddressTable(DefaultTreeModel model) throws TypeErrorException {
        this.model = model;
        System.out.println("A");
        iterateTree(model.getRoot());
        System.out.println("Finito");
    }

    private void iterateTree(Object eachNode) throws TypeErrorException {
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
                System.out.println("H");
                Object leftChild = model.getChild(child, 0);
                Object rightChild = model.getChild(child, 1);
                
                if ((model.getChildCount(rightChild) == 0)&&(model.getChildCount(rightChild) == 0)) {
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, rightChild.toString(), "", leftChild.toString()));
                } else {
                    iterateTree(child);
                }
            }
            
            if (child.toString().equals("+")) {
                System.out.println("I");
                Object leftChild = model.getChild(child, 0);
                Object rightChild = model.getChild(child, 1);
                
                if ((model.getChildCount(rightChild) == 0)&&(model.getChildCount(rightChild) == 0)) {
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.SUMA, leftChild.toString(), rightChild.toString(), ""));
                } else {
                    iterateTree(child);
                }
            }
            
            if (child.toString().equals("-")) {
                System.out.println("I");
                Object leftChild = model.getChild(child, 0);
                Object rightChild = model.getChild(child, 1);
                
                if ((model.getChildCount(rightChild) == 0)&&(model.getChildCount(rightChild) == 0)) {
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.RESTA, leftChild.toString(), rightChild.toString(), ""));
                } else {
                    iterateTree(child);
                }
            }
            
            if (child.toString().equals("*")) {
                System.out.println("I");
                Object leftChild = model.getChild(child, 0);
                Object rightChild = model.getChild(child, 1);
                
                if ((model.getChildCount(rightChild) == 0)&&(model.getChildCount(rightChild) == 0)) {
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.MULTIPLICACION, leftChild.toString(), rightChild.toString(), ""));
                } else {
                    iterateTree(child);
                }
            }
            
            if (child.toString().equals("/")) {
                System.out.println("I");
                Object leftChild = model.getChild(child, 0);
                Object rightChild = model.getChild(child, 1);
                
                if ((model.getChildCount(rightChild) == 0)&&(model.getChildCount(rightChild) == 0)) {
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.DIVISION, leftChild.toString(), rightChild.toString(), ""));
                } else {
                    iterateTree(child);
                }
            }
        }
    }

}
