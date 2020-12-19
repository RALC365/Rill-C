/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreeAddressCode;

import ComprobacionDeTipos.CustomErrorException;
import java.util.ArrayList;
import javax.swing.tree.DefaultTreeModel;

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

    public ThreeAddressTable(DefaultTreeModel model) throws CustomErrorException {
        this.model = model;
        this.conteoTemporales = 0;
        this.conteoEtiquetas = 0;
        iterateTree(model.getRoot(), null, 0, "");
        imprimirCuadruplos();
    }

    //flowAllower: 0 default, 1 if, 2 while
    private String iterateTree(Object eachNode, String[] siguienteEtiqueta, int flowAllower, String switchVar) throws CustomErrorException {
        int minlimit = 0;
        int maxlimit = model.getChildCount(eachNode);
        if (flowAllower == 1) {
            minlimit = 1;
            maxlimit = maxlimit - 1;
        }
        for (int i = minlimit; i < maxlimit; i++) {
            Object child = model.getChild(eachNode, i);
            if (eachNode.equals(model.getRoot())) {
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, eachNode.toString(), "", ""));
                iterateTree(child, siguienteEtiqueta, 0, "");
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.FINFUNCION, "", "", ""));
            }

            if (child.toString().equals("FOR")) {
                iterateTree(child, siguienteEtiqueta, 0, "");
            }

            if (child.toString().equals("SWITCH")) {
                String[] nuevasEtiquetas = new String[3];
                nuevasEtiquetas[0] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[1] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[2] = "tag" + (this.conteoEtiquetas++);

                String switchVariable = model.getChild(child, 0).toString();

                Object casesChild = model.getChild(child, 1);
                iterateTree(casesChild, nuevasEtiquetas, 0, switchVariable);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[2], "", ""));
            }

            if (child.toString().equals("WHILE")) {

                String[] nuevasEtiquetas = new String[3];
                nuevasEtiquetas[2] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[0] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[1] = "tag" + (this.conteoEtiquetas++);

                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[2], "", ""));

                Object expresionChild = model.getChild(child, 0);
                ExpresionsTree(expresionChild, nuevasEtiquetas);

                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[0], "", ""));

                iterateTree(child, nuevasEtiquetas, 2, "");
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[1], "", ""));

            }

            if (child.toString().equals("IF")) {
                int lastChild = model.getChildCount(child) - 1;

                String[] nuevasEtiquetas = new String[3];
                nuevasEtiquetas[0] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[1] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[2] = "";

                Object expresionChild = model.getChild(child, 0);
                ExpresionsTree(expresionChild, nuevasEtiquetas);

                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[0], "", ""));

                if ((model.getChild(child, lastChild) == null) || (model.getChild(child, lastChild).toString().isEmpty())) {

                    iterateTree(child, nuevasEtiquetas, 1, "");
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[1], "", ""));

                } else {
                    nuevasEtiquetas[2] = "tag" + (this.conteoEtiquetas++);
                    iterateTree(child, nuevasEtiquetas, 1, "");
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, nuevasEtiquetas[2], "", ""));

                    Object elseChild = model.getChild(child, lastChild);
                    iterateTree(elseChild, nuevasEtiquetas, 0, "");
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[2], "", ""));
                }

            }

            if (child.toString().equals("ELSE_IF")) {
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[1], "", ""));

                siguienteEtiqueta[0] = "tag" + (this.conteoEtiquetas++);
                siguienteEtiqueta[1] = "tag" + (this.conteoEtiquetas++);

                Object expresionChild = model.getChild(child, 0);
                ExpresionsTree(expresionChild, siguienteEtiqueta);

                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[0], "", ""));
                Object sentenceChild = model.getChild(child, 1);
                iterateTree(sentenceChild, siguienteEtiqueta, 0, "");
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[2], "", ""));
            }

            if (child.toString().equals("SWITCH_CASE")) {
                if (i > 0) {
                    this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[1], "", ""));
                    siguienteEtiqueta[0] = "tag" + (this.conteoEtiquetas++);
                    siguienteEtiqueta[1] = "tag" + (this.conteoEtiquetas++);
                }

                Object expresionChild = model.getChild(child, 0);
                SubExpresionsTree(expresionChild, siguienteEtiqueta, switchVar);

                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[0], "", ""));
                Object sentenceChild = model.getChild(child, 1);

                iterateTree(sentenceChild, siguienteEtiqueta, 0, "");
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[2], "", ""));
            }

            if (child.toString().equals("ELSE")) {
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, siguienteEtiqueta[1], "", ""));
                iterateTree(child, siguienteEtiqueta, 0, "");
            }

            if (child.toString().equals("ASSIGN")) {
                AsignationBuild(child);
            }

            if (child.toString().equals("DECLR")) {
                AsignationBuild(child);
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
                            this.tablaCuadruplos.add(new Cuadruplos(Operacion.PRINT, "", "", "\\n"));
                        } else {
                            this.tablaCuadruplos.add(new Cuadruplos(Operacion.PRINT, "", "", print_child));
                        }
                    }
                }
            }

            if (child.toString().contains("IN:")) {
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.INPUT, "", "", child.toString().split(":")[1]));
            }

            if (child.toString().contains(":fun")) {
                String t_ret = callFunction(child);
            }

        }

        if (flowAllower == 2) {
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[2], "", ""));
        }

        return null;

    }

    private void SubExpresionsTree(Object childNode, String[] siguienteEtiqueta, String switchVariable) throws CustomErrorException {
        if (childNode.toString().equals("=")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFIGUAL, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals(">")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMAYOR, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("<")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMENOR, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("<=")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMENORIGUAL, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("=>")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMAYORIGUAL, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("!=")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFDISTINTO, switchVariable, hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("...")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            Object hijoDerecho = model.getChild(childNode, 1);
            String[] nuevasEtiquetas = new String[3];
            nuevasEtiquetas[0] = "tag" + (this.conteoEtiquetas++);
            //Parte switchVariable >= hijoIzquierdo
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMAYORIGUAL, switchVariable, hijoIzquierdo.toString(), nuevasEtiquetas[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
            //Parte switchVariable >= hijoIzquierdo
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevasEtiquetas[0], "", ""));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMENORIGUAL, switchVariable, hijoDerecho.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
    }

    //siguienteEtiqueta[0] (Verdadero)
    //siguienteEtiqueta[1] (Falso)
    private void ExpresionsTree(Object childNode, String[] siguienteEtiqueta) throws CustomErrorException {
        if (childNode.toString().equals("AND")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            Object hijoDerecho = model.getChild(childNode, 1);
            String[] nuevaEtiqueta = new String[2];
            nuevaEtiqueta[0] = "tag" + (this.conteoEtiquetas++);
            nuevaEtiqueta[1] = siguienteEtiqueta[1];
            ExpresionsTree(hijoIzquierdo, nuevaEtiqueta);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevaEtiqueta[0], "", ""));
            ExpresionsTree(hijoDerecho, siguienteEtiqueta);
        }
        if (childNode.toString().equals("OR")) {
            Object hijoIzquierdo = model.getChild(childNode, 0);
            Object hijoDerecho = model.getChild(childNode, 1);
            String[] nuevaEtiqueta = new String[2];
            nuevaEtiqueta[0] = siguienteEtiqueta[0];
            nuevaEtiqueta[1] = "tag" + (this.conteoEtiquetas++);
            ExpresionsTree(hijoIzquierdo, nuevaEtiqueta);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.ETIQUETA, nuevaEtiqueta[1], "", ""));
            ExpresionsTree(hijoDerecho, siguienteEtiqueta);
        }
        if (childNode.toString().equals("=")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFIGUAL, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals(">")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMAYOR, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("<")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMENOR, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("<=")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMENORIGUAL, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals(">=")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFMAYORIGUAL, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
        if (childNode.toString().equals("!=")) {
            Object hijoDerecho = model.getChild(childNode, 0);
            Object hijoIzquierdo = model.getChild(childNode, 1);
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.IFDISTINTO, hijoDerecho.toString(), hijoIzquierdo.toString(), siguienteEtiqueta[0]));
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.GOTO, siguienteEtiqueta[1], "", ""));
        }
    }

    private String ArithmeticTree(Object childNode) throws CustomErrorException {

        if (childNode.toString().equals("+")) {
            return ArithmeticBuild(childNode, Operacion.SUMA);
        } else if (childNode.toString().equals("-")) {
            if ((model.getChildCount(childNode) == 1)) {
                String temporalRetorno = "t" + (this.conteoTemporales++);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.NEGACION, model.getChild(childNode, 0).toString(), "", temporalRetorno));
                return temporalRetorno;
            } else {
                return ArithmeticBuild(childNode, Operacion.RESTA);
            }
        } else if (childNode.toString().equals("*")) {
            return ArithmeticBuild(childNode, Operacion.MULTIPLICACION);
        } else if (childNode.toString().equals("/")) {
            return ArithmeticBuild(childNode, Operacion.DIVISION);
        }
        if (childNode.toString().contains(":fun")) {
            String t_ret = callFunction(childNode);
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

    private void AsignationBuild(Object currentNode) throws CustomErrorException {
        Object leftChild = model.getChild(currentNode, 0);
        Object rightChild = model.getChild(currentNode, 1);
        if (currentNode.toString().equals("DECLR")) {
            leftChild = model.getChild(currentNode, model.getChildCount(currentNode) - 2);
            rightChild = model.getChild(currentNode, model.getChildCount(currentNode) - 1);
        } else {
            leftChild = model.getChild(currentNode, 0);
            rightChild = model.getChild(currentNode, 1);
        }
        if ((model.getChildCount(leftChild) == 0) && (model.getChildCount(rightChild) == 0)) {
            if (rightChild.toString().contains(":fun")) {
                String t_ret = callFunction(rightChild);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, t_ret, "", leftChild.toString()));
            } else {
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, rightChild.toString(), "", leftChild.toString()));
            }
        } else {
            if (rightChild.toString().contains(":fun")) {
                String t_ret = callFunction(rightChild);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, t_ret, "", leftChild.toString()));
            } else if (rightChild.toString().contains("AND") || rightChild.toString().contains("OR")
                    || rightChild.toString().contains("<") || rightChild.toString().contains(">")
                    || rightChild.toString().contains("=") || rightChild.toString().contains("!=")) {
                //Creación de las Lineas de Evauación
                String[] nuevasEtiquetas = new String[2];
                nuevasEtiquetas[0] = "tag" + (this.conteoEtiquetas++);
                nuevasEtiquetas[1] = "tag" + (this.conteoEtiquetas++);
                ExpresionsTree(rightChild, nuevasEtiquetas);
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
                String temporalRetorno = ArithmeticTree(rightChild);
                this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, temporalRetorno, "", leftChild.toString()));
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
            if (model.getChildCount(child)!=0) {
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

    private String ArithmeticBuild(Object currentNode, Operacion operacionEnum) throws CustomErrorException {
        Object leftChild = model.getChild(currentNode, 0);
        Object rightChild = model.getChild(currentNode, 1);
        String temporalRetorno;

        if ((model.getChildCount(leftChild) == 0) && (model.getChildCount(rightChild) == 0)) {
            temporalRetorno = "t" + (this.conteoTemporales++);
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
            temporalRetorno = "t" + (this.conteoTemporales++);
            this.tablaCuadruplos.add(new Cuadruplos(operacionEnum, temporalIzquierdo, temporalDerecho, temporalRetorno));
            return temporalRetorno;
        }
    }

    public ArrayList<Cuadruplos> getTablaCuadruplos() {
        return tablaCuadruplos;
    }

    private void imprimirCuadruplos() {
        int count = 1;
        for (Cuadruplos cadaCuadruplo : this.tablaCuadruplos) {
            System.out.println((count++) + ": " + cadaCuadruplo.toString());
        }
    }

    private String callFunction(Object o) {
        for (int i = 0; i < model.getChildCount(o); i++) {
            String param = model.getChild(o, i).toString();
            this.tablaCuadruplos.add(new Cuadruplos(Operacion.PARAM, "", "", param));
        }
        this.tablaCuadruplos.add(new Cuadruplos(Operacion.CALL, "", "", o.toString().split(":")[0]));
        String t_ret = "t" + (this.conteoTemporales++);
        this.tablaCuadruplos.add(new Cuadruplos(Operacion.ASIGNACION, "RET", "", t_ret));
        return t_ret;
    }

}
