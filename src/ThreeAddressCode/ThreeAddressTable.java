/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreeAddressCode;

import ComprobacionDeTipos.CustomErrorException;
import java.util.ArrayList;
import javax.swing.tree.DefaultTreeModel;
import ComprobacionDeTipos.TypesSubTable;

/**
 *
 *
 * @author Julio Marin
 */
public class ThreeAddressTable {

    private final ArrayList<Cuadruplos> tablaCuadruplos = new ArrayList();
    private final DefaultTreeModel model;
    private int conteoTemporales;
    private int conteoEtiquetas;
    private TypesSubTable root;

    public ThreeAddressTable(DefaultTreeModel model, TypesSubTable raiz) throws CustomErrorException {
        this.model = model;
        this.conteoTemporales = 0;
        this.conteoEtiquetas = 0;
        this.root = raiz;
        iterateTree(root.treepart, null, 0, "", root.treepart);
        imprimirCuadruplos();
    }

    public ArrayList<Cuadruplos> getTablaCuadruplos() {
        return tablaCuadruplos;
    }

    //flowAllower: 0 default, 1 if, 2 while
    private String iterateTree(Object eachNode, String[] siguienteEtiqueta, int flowAllower, String switchVar, Object parentBlock) throws CustomErrorException {
        int minlimit = 0;
        int maxlimit = model.getChildCount(eachNode);
        if (flowAllower == 1) {
            minlimit = 1;
            maxlimit = maxlimit - 1;
        }
        for (int i = minlimit; i < maxlimit; i++) {
            Object child = model.getChild(eachNode, i);
            if (eachNode.equals(model.getRoot())) {
                if (child.toString().equals("MAIN")) {
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETAMAIN, child.toString().split(":")[0], "", ""));
                } else {
                    Cuadruplos t = new Cuadruplos(Operacion.ETIQUETAFUN, child.toString().split(":")[0], "", "");
                    t.setBloque(child);
                    this.tablaCuadruplos.add(t);
                }
                iterateTree(child, siguienteEtiqueta, 0, "", child);
                if (!(child.toString().equals("MAIN"))) {
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, "salida_" + child.toString().split(":")[0], "", ""));
                    Cuadruplos t = new Cuadruplos(Operacion.FINFUNCION, "salida_" + child.toString().split(":")[0], "", "");
                    t.setBloque(child);
                    this.tablaCuadruplos.add(t);
                }
            }

            if (child.toString().equals("FOR")) {
                iterateTree(child, siguienteEtiqueta, 0, "", child);
            }

            if (child.toString().contains(":ret")) {
                Cuadruplos t = new Cuadruplos(Operacion.RETURN, "", "", child.toString().split(":")[0]);
                t.setBloque(parentBlock);
                this.tablaCuadruplos.add(t);
            }

            if (child.toString().equals("SWITCH")) {
                String[] nuevasEtiquetas = new String[3];
                nuevasEtiquetas[0] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[1] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[2] = "tag" + (this.conteoEtiquetas++);

                String switchVariable = model.getChild(child, 0).toString();

                Object casesChild = model.getChild(child, 1);
                iterateTree(casesChild, nuevasEtiquetas, 0, switchVariable, child);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[2], "", ""));
            }

            if (child.toString().equals("WHILE")) {

                String[] nuevasEtiquetas = new String[3];
                nuevasEtiquetas[2] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[0] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[1] = "tag" + (this.conteoEtiquetas++);

                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[2], "", ""));

                Object expresionChild = model.getChild(child, 0);
                ExpresionsTree(expresionChild, nuevasEtiquetas, child);

                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[0], "", ""));

                iterateTree(child, nuevasEtiquetas, 2, "", child);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[1], "", ""));

            }

            if (child.toString().equals("IF")) {
                int lastChild = model.getChildCount(child) - 1;

                String[] nuevasEtiquetas = new String[3];
                nuevasEtiquetas[0] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[1] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[2] = "";

                Object expresionChild = model.getChild(child, 0);
                ExpresionsTree(expresionChild, nuevasEtiquetas, child);

                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[0], "", ""));

                if ((model.getChild(child, lastChild) == null) || (model.getChild(child, lastChild).toString().isEmpty())) {

                    iterateTree(child, nuevasEtiquetas, 1, "", child);
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[1], "", ""));

                } else {
                    nuevasEtiquetas[2] = "tag" + (this.conteoEtiquetas++);
                    iterateTree(child, nuevasEtiquetas, 1, "", child);
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, nuevasEtiquetas[2], "", ""));

                    Object elseChild = model.getChild(child, lastChild);
                    iterateTree(elseChild, nuevasEtiquetas, 0, "", child);
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[2], "", ""));
                }

            }

            if (child.toString().equals("ELSE_IF")) {
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[1], "", ""));

                siguienteEtiqueta[0] = "tag" + (this.conteoEtiquetas++);
                siguienteEtiqueta[1] = "tag" + (this.conteoEtiquetas++);

                Object expresionChild = model.getChild(child, 0);
                ExpresionsTree(expresionChild, siguienteEtiqueta, child);

                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[0], "", ""));
                Object sentenceChild = model.getChild(child, 1);
                iterateTree(sentenceChild, siguienteEtiqueta, 0, "", child);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[2], "", ""));
            }

            if (child.toString().equals("SWITCH_CASE")) {
                if (i > 0) {
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[1], "", ""));
                    siguienteEtiqueta[0] = "tag" + (this.conteoEtiquetas++);
                    siguienteEtiqueta[1] = "tag" + (this.conteoEtiquetas++);
                }

                Object expresionChild = model.getChild(child, 0);
                SubExpresionsTree(expresionChild, siguienteEtiqueta, switchVar, child);

                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[0], "", ""));
                Object sentenceChild = model.getChild(child, 1);

                iterateTree(sentenceChild, siguienteEtiqueta, 0, "", child);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[2], "", ""));
            }

            if (child.toString().equals("ELSE")) {
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[1], "", ""));
                iterateTree(child, siguienteEtiqueta, 0, "", child);
            }

            if (child.toString().equals("ASSIGN")) {
                AsignationBuild(child, parentBlock);
            }

            if (child.toString().equals("DECLR")) {
                AsignationBuild(child, parentBlock);
            }

            if (child.toString().equals("DECLR ARRAY")) {
                AsignationArray(model.getChild(child, 1).toString(), model.getChild(child, 2).toString(), model.getChild(child, 3));
            }

            if (child.toString().equals("DECLR MATRIX")) {
                AsignationArray(model.getChild(child, 1).toString(), model.getChild(child, 2).toString(), model.getChild(child, 3));
            }

            if (child.toString().equals("PRINT")) {
                int cc = model.getChildCount(child);
                for (int j = 0; j < cc; j++) {
                    String print_child = model.getChild(child, j).toString();
                    if (!print_child.equals("")) {
                        if (print_child.equals("ln")) {
                            Cuadruplos temp = new Cuadruplos(Operacion.PRINT, "", "", "\"\\n\"");
                            temp.setBloque(parentBlock);
                            this.tablaCuadruplos.add(temp);
                        } else {
                            Cuadruplos temp = new Cuadruplos(Operacion.PRINT, "", "", print_child);
                            temp.setBloque(parentBlock);
                            this.tablaCuadruplos.add(temp);
                            this.tablaCuadruplos.add(new Cuadruplos(Operacion.PRINT, "", "", "\"\\n\""));
                        }
                    }
                }
            }

            if (child.toString().contains("IN:")) {
                Cuadruplos temp = new Cuadruplos(Operacion.INPUT, "", "", child.toString().split(":")[1]);
                temp.setBloque(parentBlock);
                this.tablaCuadruplos.add(temp);
            }

            if (child.toString().contains(":fun")) {
                String t_ret = callFunction(child,child);
            }

        }

        if (flowAllower == 2) {
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[2], "", ""));
        }

        return null;

    }

    private void SubExpresionsTree(Object childNode, String[] siguienteEtiqueta, String switchVariable, Object currentBlock) throws CustomErrorException {//aqui
        if (childNode.toString().equals("=")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
//            TableRow tempTable = this.root.getID(switchVariable, currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFIGUAL, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempTable != null) {
//                tempCuadruplo.setInfoA(tempTable.offset, tempTable.type, tempTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals(">")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
//            TableRow tempTable = this.root.getID(switchVariable, currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFMAYOR, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempTable != null) {
//                tempCuadruplo.setInfoA(tempTable.offset, tempTable.type, tempTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("<")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
//            TableRow tempTable = this.root.getID(switchVariable, currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFMENOR, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempTable != null) {
//                tempCuadruplo.setInfoA(tempTable.offset, tempTable.type, tempTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("<=")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
//            TableRow tempTable = this.root.getID(switchVariable, currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFMENORIGUAL, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempTable != null) {
//                tempCuadruplo.setInfoA(tempTable.offset, tempTable.type, tempTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("=>")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
//            TableRow tempTable = this.root.getID(switchVariable, currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFMAYORIGUAL, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempTable != null) {
//                tempCuadruplo.setInfoA(tempTable.offset, tempTable.type, tempTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("!=")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
//            TableRow tempTable = this.root.getID(switchVariable, currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFDISTINTO, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempTable != null) {
//                tempCuadruplo.setInfoA(tempTable.offset, tempTable.type, tempTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("...")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            Object hijoDerecho = model.getChild(childNode, 1);
            String[] nuevasEtiquetas = new String[3];
            nuevasEtiquetas[0] = "tag" + (this.conteoEtiquetas++);
            //Parte switchVariable >= hijoIzquierdo
//            TableRow tempTable = this.root.getID(switchVariable, currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFMAYORIGUAL, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempTable != null) {
//                tempCuadruplo.setInfoA(tempTable.offset, tempTable.type, tempTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
            //Parte switchVariable >= hijoIzquierdo
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[0], "", ""));
            tempCuadruplo = new Cuadruplos(Operacion.IFMENORIGUAL, switchVariable, hijoDerecho.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempTable != null) {
//                tempCuadruplo.setInfoA(tempTable.offset, tempTable.type, tempTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
    }

    //siguienteEtiqueta[0] (Verdadero)
    //siguienteEtiqueta[1] (Falso)
    private void ExpresionsTree(Object childNode, String[] siguienteEtiqueta, Object currentBlock) throws CustomErrorException {
        if (childNode.toString().equals("AND")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            Object hijoDerecho = model.getChild(childNode, 1);
            String[] nuevaEtiqueta = new String[2];
            nuevaEtiqueta[0] = "tag" + (this.conteoEtiquetas++);
            nuevaEtiqueta[1] = siguienteEtiqueta[1];
            ExpresionsTree(hijoIzquierdo, nuevaEtiqueta, currentBlock);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevaEtiqueta[0], "", ""));
            ExpresionsTree(hijoDerecho, siguienteEtiqueta, currentBlock);
        }
        if (childNode.toString().equals("OR")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            Object hijoDerecho = model.getChild(childNode, 1);
            String[] nuevaEtiqueta = new String[2];
            nuevaEtiqueta[0] = siguienteEtiqueta[0];
            nuevaEtiqueta[1] = "tag" + (this.conteoEtiquetas++);
            ExpresionsTree(hijoIzquierdo, nuevaEtiqueta, currentBlock);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevaEtiqueta[1], "", ""));
            ExpresionsTree(hijoDerecho, siguienteEtiqueta, currentBlock);
        }
        if (childNode.toString().equals("=")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
//            TableRow tempLeftTable = this.root.getID(hijoIzquierdo.toString(), currentBlock, root);
//            TableRow tempRightTable = this.root.getID(hijoDerecho.toString(), currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFIGUAL, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempLeftTable != null) {
//                tempCuadruplo.setInfoA(tempLeftTable.offset, tempLeftTable.type, tempLeftTable.ubicacion);
//            }
//            if (tempRightTable != null) {
//                tempCuadruplo.setInfoB(tempRightTable.offset, tempRightTable.type, tempRightTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals(">")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
//            TableRow tempLeftTable = this.root.getID(hijoIzquierdo.toString(), currentBlock, root);
//            TableRow tempRightTable = this.root.getID(hijoDerecho.toString(), currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFMAYOR, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempLeftTable != null) {
//                tempCuadruplo.setInfoA(tempLeftTable.offset, tempLeftTable.type, tempLeftTable.ubicacion);
//            }
//            if (tempRightTable != null) {
//                tempCuadruplo.setInfoB(tempRightTable.offset, tempRightTable.type, tempRightTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("<")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
//            TableRow tempLeftTable = this.root.getID(hijoIzquierdo.toString(), currentBlock, root);
//            TableRow tempRightTable = this.root.getID(hijoDerecho.toString(), currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFMENOR, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempLeftTable != null) {
//                tempCuadruplo.setInfoA(tempLeftTable.offset, tempLeftTable.type, tempLeftTable.ubicacion);
//            }
//            if (tempRightTable != null) {
//                tempCuadruplo.setInfoB(tempRightTable.offset, tempRightTable.type, tempRightTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("<=")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
//            TableRow tempLeftTable = this.root.getID(hijoIzquierdo.toString(), currentBlock, root);
//            TableRow tempRightTable = this.root.getID(hijoDerecho.toString(), currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFMENORIGUAL, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempLeftTable != null) {
//                tempCuadruplo.setInfoA(tempLeftTable.offset, tempLeftTable.type, tempLeftTable.ubicacion);
//            }
//            if (tempRightTable != null) {
//                tempCuadruplo.setInfoB(tempRightTable.offset, tempRightTable.type, tempRightTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals(">=")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
//            TableRow tempLeftTable = this.root.getID(hijoIzquierdo.toString(), currentBlock, root);
//            TableRow tempRightTable = this.root.getID(hijoDerecho.toString(), currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFMAYORIGUAL, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempLeftTable != null) {
//                tempCuadruplo.setInfoA(tempLeftTable.offset, tempLeftTable.type, tempLeftTable.ubicacion);
//            }
//            if (tempRightTable != null) {
//                tempCuadruplo.setInfoB(tempRightTable.offset, tempRightTable.type, tempRightTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("!=")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
//            TableRow tempLeftTable = this.root.getID(hijoIzquierdo.toString(), currentBlock, root);
//            TableRow tempRightTable = this.root.getID(hijoDerecho.toString(), currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.IFDISTINTO, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempLeftTable != null) {
//                tempCuadruplo.setInfoA(tempLeftTable.offset, tempLeftTable.type, tempLeftTable.ubicacion);
//            }
//            if (tempRightTable != null) {
//                tempCuadruplo.setInfoB(tempRightTable.offset, tempRightTable.type, tempRightTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
    }

    private String ArithmeticTree(Object childNode, Object currentBlock) throws CustomErrorException {
        if (childNode.toString().equals("+")) {
            return ArithmeticBuild(childNode, Operacion.SUMA, currentBlock);
        } else if (childNode.toString().equals("-")) {
            if ((model.getChildCount(childNode) == 1)) {
                String temporalRetorno = "t" + (this.conteoTemporales++);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.NEGACION, model.getChild(childNode, 0).toString(), "", temporalRetorno));
                return temporalRetorno;
            } else {
                return ArithmeticBuild(childNode, Operacion.RESTA, currentBlock);
            }
        } else if (childNode.toString().equals("*")) {
            return ArithmeticBuild(childNode, Operacion.MULTIPLICACION, currentBlock);
        } else if (childNode.toString().equals("/")) {
            return ArithmeticBuild(childNode, Operacion.DIVISION, currentBlock);
        }
        if (childNode.toString().contains(":fun")) {
            String t_ret = callFunction(childNode,currentBlock);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, t_ret, "", childNode.toString()));
        } else {
            if (model.getChildCount(childNode) == 1) {
                String temporal = "t" + (this.conteoTemporales++);
                String indice = model.getChild(childNode, 0).toString();
                //Buscar ese inidice y ver el tamaño del tipo de variable
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.MULTIPLICACION, indice, "tamañoDeTipo", temporal));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNARREGLO, childNode.toString(), temporal, childNode.toString()));
            } else {
                String temporali = "t" + (this.conteoTemporales++);
                String temporali_j = "t" + (this.conteoTemporales++);
                String temporalj = "t" + (this.conteoTemporales++);
                String i = model.getChild(childNode, 0).toString();
                String j = model.getChild(childNode, 1).toString();
                //Buscar ese inidice y ver el tamaño del tipo de variable
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.MULTIPLICACION, i, "tamañoDeCol", temporali));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.SUMA, temporali, j, temporali_j));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.MULTIPLICACION, temporali_j, "TamañodeTipo", temporalj));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNARREGLO, childNode.toString(), temporalj, childNode.toString()));
            }
        }

        return null;

    }

    private void AsignationBuild(Object currentNode, Object currentBlock) throws CustomErrorException {
        Object leftChild = model.getChild(currentNode, 0);
        Object rightChild = model.getChild(currentNode, 1);
        if (currentNode.toString().equals("DECLR")) {
            leftChild = model.getChild(currentNode, model.getChildCount(currentNode) - 2);
            rightChild = model.getChild(currentNode, model.getChildCount(currentNode) - 1);
        } else {
            leftChild = model.getChild(currentNode, 0);
            rightChild = model.getChild(currentNode, 1);
        }
//        TableRow tempTable = this.root.getID(leftChild.toString(), currentBlock, root);
        if ((model.getChildCount(leftChild) == 0) && (model.getChildCount(rightChild) == 0)) {
            if (rightChild.toString().contains(":fun")) {
                String t_ret = callFunction(rightChild,currentBlock);
                Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.ASIGNACION, t_ret, "", leftChild.toString());
                tempCuadruplo.setBloque(currentBlock);
//                tempCuadruplo.setInfoRes(tempTable.offset, tempTable.type, tempTable.ubicacion);
                this.tablaCuadruplos.add(tempCuadruplo);
            } else {
                Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.ASIGNACION, rightChild.toString(), "", leftChild.toString());
                tempCuadruplo.setBloque(currentBlock);
//                tempCuadruplo.setInfoRes(tempTable.offset, tempTable.type, tempTable.ubicacion);
                this.tablaCuadruplos.add(tempCuadruplo);
            }
        } else {
            if (rightChild.toString().contains(":fun")) {
                String t_ret = callFunction(rightChild,currentBlock);
                Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.ASIGNACION, t_ret, "", leftChild.toString());
                tempCuadruplo.setBloque(currentBlock);
//                tempCuadruplo.setInfoRes(tempTable.offset, tempTable.type, tempTable.ubicacion);
                this.tablaCuadruplos.add(tempCuadruplo);
            } else if (rightChild.toString().contains("AND") || rightChild.toString().contains("OR")
                    || rightChild.toString().contains("<") || rightChild.toString().contains(">")
                    || rightChild.toString().contains("=") || rightChild.toString().contains("!=")) {
                //Creación de las Lineas de Evauación
                String[] nuevasEtiquetas = new String[2];
                nuevasEtiquetas[0] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[1] = "tag" + (this.conteoEtiquetas++);
                ExpresionsTree(rightChild, nuevasEtiquetas, currentBlock);
                //Temporal Booleano
                String temporalBool = "t" + (this.conteoTemporales++);
                //Creación de la etiqueta verdadera
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[0], "", ""));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, "true", "", temporalBool));
                //Creación de la etiqueta falsa
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[1], "", ""));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, "false", "", temporalBool));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, temporalBool, "", leftChild.toString()));
            } else if (model.getChildCount(rightChild) == 1) {
                String temporal = "t" + (this.conteoTemporales++);
                String indice = model.getChild(rightChild, 0).toString();
                //Buscar ese inidice y ver el tamaño del tipo de variable
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.MULTIPLICACION, indice, "tamañoDeTipo", temporal));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNARREGLO, rightChild.toString(), temporal, leftChild.toString()));
            } else if (rightChild.toString().contains("*") || rightChild.toString().contains("+")
                    || rightChild.toString().contains("/") || rightChild.toString().contains("%")
                    || rightChild.toString().contains("-")) {
                String temporalRetorno = ArithmeticTree(rightChild, currentBlock);
                Cuadruplos tempCuadruplo = new Cuadruplos(Operacion.ASIGNACION, temporalRetorno, "", leftChild.toString());
                tempCuadruplo.setBloque(currentBlock);
                this.tablaCuadruplos.add(tempCuadruplo);
            } else {
                String temporali = "t" + (this.conteoTemporales++);
                String temporali_j = "t" + (this.conteoTemporales++);
                String temporalj = "t" + (this.conteoTemporales++);
                String i = model.getChild(rightChild, 0).toString();
                String j = model.getChild(rightChild, 1).toString();
                //Buscar ese inidice y ver el tamaño del tipo de variable
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.MULTIPLICACION, i, "tamañoDeCol", temporali));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.SUMA, temporali, j, temporali_j));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.MULTIPLICACION, temporali_j, "TamañodeTipo", temporalj));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNARREGLO, rightChild.toString(), temporalj, leftChild.toString()));
            }
        }

    }

    private void AsignationArray(String tipo, String identificador, Object currentNode) throws CustomErrorException {
        int cc = model.getChildCount(currentNode);
        int tamanio = 0;
        switch (tipo) {
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
            if (model.getChildCount(child) != 0) {
                AsignationArray(identificador, currentNode, tamanio);
            } else {
                String temporalIndice = "t" + (this.conteoTemporales++);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.MULTIPLICACION, i + "", tamanio + "", temporalIndice));
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ARREGLOASIGN, child.toString(), temporalIndice, identificador));
            }
        }
    }

    private void AsignationArray(String identificador, Object currentNode, int tamanio) {
        int cc = model.getChildCount(currentNode);
        for (int i = 0; i < cc; i++) {
            Object child = model.getChild(currentNode, i);
            String temporalIndice = "t" + (this.conteoTemporales++);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.MULTIPLICACION, i + "", tamanio + "", temporalIndice));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ARREGLOASIGN, child.toString(), temporalIndice, identificador));
        }
    }

    private String ArithmeticBuild(Object currentNode, Operacion operacionEnum, Object currentBlock) throws CustomErrorException {
        Object leftChild = model.getChild(currentNode, 0);
        Object rightChild = model.getChild(currentNode, 1);
        String temporalRetorno;

        if ((model.getChildCount(leftChild) == 0) && (model.getChildCount(rightChild) == 0)) {
            temporalRetorno = "t" + (this.conteoTemporales++);
//            TableRow tempLeftTable = this.root.getID(leftChild.toString(), currentBlock, root);
//            TableRow tempRightTable = this.root.getID(rightChild.toString(), currentBlock, root);
            Cuadruplos tempCuadruplo = new Cuadruplos(operacionEnum, leftChild.toString(), rightChild.toString(), temporalRetorno);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempLeftTable != null) {                
//                tempCuadruplo.setInfoA(tempLeftTable.offset, tempLeftTable.type, tempLeftTable.ubicacion);
//            }
//            if (tempRightTable != null) {
//                tempCuadruplo.setInfoB(tempRightTable.offset, tempRightTable.type, tempRightTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            return temporalRetorno;
        } else {
            String temporalIzquierdo = leftChild.toString();
            String temporalDerecho = rightChild.toString();
//            TableRow tempLeftTable = null;
//            TableRow tempRightTable = null;
            if (model.getChildCount(leftChild) > 0) {
                temporalIzquierdo = ArithmeticTree(leftChild, currentBlock);
            }
//            else {
//                tempLeftTable = this.root.getID(leftChild.toString(), currentBlock, root);
//            }
            if (model.getChildCount(rightChild) > 0) {
                temporalDerecho = ArithmeticTree(rightChild, currentBlock);
            }
//            else {
//                tempRightTable = this.root.getID(rightChild.toString(), currentBlock, root);
//            }
            temporalRetorno = "t" + (this.conteoTemporales++);
            Cuadruplos tempCuadruplo = new Cuadruplos(operacionEnum, temporalIzquierdo, temporalDerecho, temporalRetorno);
            tempCuadruplo.setBloque(currentBlock);
//            if (tempLeftTable != null) {                
//                tempCuadruplo.setInfoA(tempLeftTable.offset, tempLeftTable.type, tempLeftTable.ubicacion);
//            }
//            if (tempRightTable != null) {
//                tempCuadruplo.setInfoB(tempRightTable.offset, tempRightTable.type, tempRightTable.ubicacion);
//            }
            this.tablaCuadruplos.add(tempCuadruplo);
            return temporalRetorno;
        }
    }

    private void imprimirCuadruplos() {
        int count = 1;
        for (Cuadruplos cadaCuadruplo : this.tablaCuadruplos) {
            System.out.println((count++) + ": " + cadaCuadruplo.toString());
        }
        System.out.println("================================Fin Cuadruplos===============================");
    }

    private String callFunction(Object o, Object current) {
        for (int i = 0; i < model.getChildCount(o); i++) {
            String param = model.getChild(o, i).toString();
            Cuadruplos t = new Cuadruplos(Operacion.PARAM, "", "", param);
            t.setBloque(current);
            this.tablaCuadruplos.add(t);
        }
        Cuadruplos t = new Cuadruplos(Operacion.CALL, "", "", o.toString().split(":")[0]);
        t.setBloque(current);
        this.tablaCuadruplos.add(t);
        return "RET";
    }

}
