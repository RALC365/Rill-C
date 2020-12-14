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
    private int conteoEtiquetas;
    
    public ThreeAddressTable(DefaultTreeModel model) throws TypeErrorException {
        this.model = model;
        this.conteoTemporales = 0;
        this.conteoEtiquetas = 0;
        iterateTree(model.getRoot(), null, 0);
        imprimirCuadruplos();
    }

    //flowAllower: 0 default, 1 if, 2 while
    private String iterateTree(Object eachNode, String[] siguienteEtiqueta, int flowAllower) throws TypeErrorException {
        int minlimit = 0;
        int maxlimit = model.getChildCount(eachNode);
        switch(flowAllower) {
            case 1:
                minlimit = 1;
                maxlimit = maxlimit - 1;
                break;
        }
        for (int i = minlimit; i < maxlimit; i++) {
            Object child = model.getChild(eachNode, i);
            if (eachNode.equals(model.getRoot())) {
                iterateTree(child, siguienteEtiqueta, 0);
            }
            
            if (child.toString().equals("FOR")) {
                iterateTree(child, siguienteEtiqueta, 0);
            }
            
            if (child.toString().equals("WHILE")) {
                String[] nuevasEtiquetas = new String[3];
                nuevasEtiquetas[2] = "tag"+(this.conteoEtiquetas++);
                nuevasEtiquetas[0] = "tag"+(this.conteoEtiquetas++);
                nuevasEtiquetas[1] = "tag"+(this.conteoEtiquetas++);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[2], "", ""));
                ExpresionsTree(child, nuevasEtiquetas, 1);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[0], "", ""));
                iterateTree(child, nuevasEtiquetas, 2);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[1], "", ""));
            }
            
            if (child.toString().equals("IF")) {
                int lastChild = model.getChildCount(child) - 1;
                
                String[] nuevasEtiquetas = new String[3];
                nuevasEtiquetas[0] = "tag"+(this.conteoEtiquetas++);
                nuevasEtiquetas[1] = "tag"+(this.conteoEtiquetas++);
                nuevasEtiquetas[2] = "";
                
                ExpresionsTree(child, nuevasEtiquetas, 1);
                
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[0], "", ""));
                
                if ((model.getChild(child, lastChild) == null)||(model.getChild(child, lastChild).toString().isEmpty())) {
                    iterateTree(child, nuevasEtiquetas, 1);
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[1], "", ""));
                } else {
                    nuevasEtiquetas[2] = "tag"+(this.conteoEtiquetas++);
                    iterateTree(child, nuevasEtiquetas, 1);
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, nuevasEtiquetas[2], "", ""));
                    Object elseChild = model.getChild(child, lastChild);
                    iterateTree(elseChild, nuevasEtiquetas, 0);
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[2], "", ""));
                }
                
            }
            
            if (child.toString().equals("ELSE_IF")) {
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[1], "", ""));
                
                siguienteEtiqueta[0] = "tag"+(this.conteoEtiquetas++);
                siguienteEtiqueta[1] = "tag"+(this.conteoEtiquetas++);
                
                ExpresionsTree(child, siguienteEtiqueta, 1);
                
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[0], "", ""));
                Object sentenceChild = model.getChild(child, 1);
                iterateTree(sentenceChild, siguienteEtiqueta, 0);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[2], "", ""));
                
            }
            
            if (child.toString().equals("ELSE")) {
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[1], "", ""));
                iterateTree(child, siguienteEtiqueta, 0);
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
        
        if (flowAllower == 2) {
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[2], "", ""));
        }
        
        return null;
        
    }
    
    private void ExpresionsTree(Object childNode, String[] siguienteEtiqueta, int flowAllower) throws TypeErrorException {
        for (int i = 0; i < flowAllower; i++) {
            Object child = model.getChild(childNode, i);
            if (child.toString().equals("=")) {
                Object hijoDerecho = model.getChild(child, 0);
                Object hijoIzquierdo = model.getChild(child, 1);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFIGUAL, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
            }
            if (child.toString().equals(">")) {
                Object hijoDerecho = model.getChild(child, 0);
                Object hijoIzquierdo = model.getChild(child, 1);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMAYOR, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
            }
            if (child.toString().equals("<")) {
                Object hijoDerecho = model.getChild(child, 0);
                Object hijoIzquierdo = model.getChild(child, 1);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMENOR, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
            }
            if (child.toString().equals("<=")) {
                Object hijoDerecho = model.getChild(child, 0);
                Object hijoIzquierdo = model.getChild(child, 1);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMENORIGUAL, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
            }
            if (child.toString().equals("=>")) {
                Object hijoDerecho = model.getChild(child, 0);
                Object hijoIzquierdo = model.getChild(child, 1);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMAYORIGUAL, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
            }
            if (child.toString().equals("!=")) {
                Object hijoDerecho = model.getChild(child, 0);
                Object hijoIzquierdo = model.getChild(child, 1);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFDISTINTO, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
            }
            //        int maxlimit = model.getChildCount(childNode);
        }
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
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNARREGLO, child.toString(), temporalIndice, identificador));
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
