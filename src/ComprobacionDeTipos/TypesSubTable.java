/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComprobacionDeTipos;

import CUP.InstructionCode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Will
 */
public class TypesSubTable {

    public final Object treepart;//actual
    private final DefaultTreeModel model;
    private final String functionName;
    private final String functionType;
    private final LinkedHashMap<String, TableRow> ids = new LinkedHashMap();//Tabla de sibolos
    private final TypesSubTable parent;
    public final LinkedHashMap<String, TypesSubTable> children = new LinkedHashMap();//Arbol de Simbolos
    private int offsetActual;
    public ArrayList<String> errors = new ArrayList();

    public TypesSubTable(String functionName, String functionType, DefaultTreeModel model, Object treepart, TypesSubTable parent, int offsetActual) {
        this.functionName = functionName;
        this.functionType = functionType;
        this.treepart = treepart;
        this.model = model;
        this.parent = parent;
        this.offsetActual = offsetActual;
    }

    //Verifica que si el ID existe en el programa y el ambito de ese ID es 
    //disponible desde el ambito actual y lo devuelve
    private TableRow checkIDExistence(String id) {
        TypesSubTable x = this;
        if (x.ids.containsKey(id)) {
            return x.ids.get(id);
        } else {
            while (x.parent != null) {
                x = x.parent;
                if (x.ids.containsKey(id)) {
                    return x.ids.get(id);
                }
            }
            return null;
        }
    }

    //Agreg a la Subtabla el ID verificando si ese ID no habia sido declarado antes
    private boolean addIDToSubTable(TableRow t) {//?
        if (checkIDExistence(t.id) != null) {
            return false;
        }
        ids.put(t.id, t);
        return true;
    }

    //Agregar Declaraciones a las subtabla y creacion de más subtablas
    //Para la creación del ámbito
    public void getDeclarations(Object a) {
        int cc = model.getChildCount(a);
        //Agregan el ID del FOR a su tabla de IDs
        if (a.toString().equals("FOR")) {
            checkForParameters(a);
        }

        //Recoree los Hijo del nodo Actual 
        for (int i = 0; i < cc; i++) {
            Object o = model.getChild(a, i);

            if (o.toString().contains(":fun")) {
                String fun = checkFunction(o.toString().replace(":fun", ""));
                if (!fun.equals("")) {
                    if (!checkFunctionParams(fun.split(" -> ")[0], o)) {
                        addError(o, "No hay función '" + o.toString().replace(":fun", "") + "' con esos tipos de parámetros. ");
                    }
                } else {
                    addError(o, "La función '" + o.toString().replace(":fun", "") + " no existe");
                }
            }

            if (o.toString().equals("PRINT")) {
                int p_cc = model.getChildCount(o);
                for (int j = 0; j < p_cc; j++) {
                    if (!model.getChild(o, j).toString().contains("\"") && !model.getChild(o, j).toString().equals("ln") && !model.getChild(o, j).toString().equals("")) {
                        checkTypeofValue(model.getChild(o, j));
                    }
                }
            }

            if (o.toString().contains("IN:")) {
                TableRow id = checkIDExistence(o.toString().split(":")[1].substring(1));
                if (id == null) {
                    addError(o, "No hay variable de nombre: '" + o.toString().split(":")[1].substring(1) + "' accesible desde este ámbito actual. ");
                }
            }

            //Revisar si la variable de retorno del tipo correcto
            if (o.toString().contains(":ret")) {
                String id = o.toString().replace(":ret", "");
                String ret_type = "";
                if (id.equals("false") || id.equals("true")) {
                    ret_type = "bln";
                } else if (id.equals("nll")) {
                    ret_type = "nll";
                } else if (isNumeric(id)) {
                    ret_type = "int";
                } else if (id.contains("'") || id.contains("‘") || id.contains("’")) {
                    ret_type = "chr";
                } else {
                    TableRow id1 = checkIDExistence(id);
                    if (id1 != null) {
                        ret_type = id1.type;
                    } else {
                        addError(o, "No hay variable de nombre: '" + id + "' accesible desde este ámbito actual. ");
                        ret_type = "nll";
                    }
                }
                String fun_ret_type = (functionType.split("->")[1]).substring(1);
                if (!ret_type.equals(fun_ret_type)) {
                    if (ret_type.contains("array")) {
                        if (((ret_type.replace("..", "#")).split("#").length == 3 && fun_ret_type.contains("mtx"))
                                && (ret_type.contains(fun_ret_type.split(" ")[1]))) {
                            //Son del mismo tipo
                        } else if (((ret_type.replace("..", "#")).split("#").length == 2 && fun_ret_type.contains("arr"))
                                && (ret_type.contains(fun_ret_type.split(" ")[1]))) {
                            //Son del mismo tipo
                        } else {
                            addError(o, "El valor de retorno: '" + id + "' no es del tipo esperado.");
                        }
                    } else {
                        addError(o, "El valor de retorno: '" + id + "' no es del tipo esperado.");
                    }
                }
            }

            //PARAMETERS
            if (o.toString().equals("PARAMETERS")) {
                String[] pos = {"$a0", "$a1", "$a2", "$a3", "$a4",
                    "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$s8", "$s9",};
                for (int j = 0; j < model.getChildCount(o); j++) {
                    String[] p = model.getChild(o, j).toString().split(" ");
                    if (p[0].contains("mtx")) {
                        TableRow row = new TableRow(p[2], "array(0..0,array(0..0," + p[1] + "))", offsetActual, pos[j]);
                        addIDToSubTable(row);
                    } else if (p[0].contains("arr")) {
                        TableRow row = new TableRow(p[2], "array(0..0," + p[1] + ")", offsetActual, pos[j]);
                        addIDToSubTable(row);
                    } else {
                        TableRow row = new TableRow(p[1], p[0], offsetActual, pos[j]);
                        if (p[0].equals("int")) {
                            offsetActual += 4;
                        }
                        if (p[0].equals("bln")) {
                            offsetActual += 1;
                        }
                        if (p[0].equals("chr")) {
                            offsetActual += 1;
                        }
                        addIDToSubTable(row);
                    }
                }
            }

            //FOR
            if (o.toString().equals("FOR")) {
                TypesSubTable st = new TypesSubTable("FOR", "nll", model, o, this, offsetActual);
                this.children.put("FOR " + this.children.size(), st);
                st.getDeclarations(o);
                this.errors.addAll(st.errors);
            }

            //WHILE
            if (o.toString().equals("WHILE")) {
                if (checkBlnExpression(model.getChild(o, 0))) {
                    TypesSubTable st = new TypesSubTable("WHILE", "nll", model, o, this, offsetActual);
                    this.children.put("WHILE " + this.children.size(), st);
                    st.getDeclarations(o);
                    this.errors.addAll(st.errors);
                }
            }

            //IF
            if (o.toString().equals("IF")) {
                if (checkBlnExpression(model.getChild(o, 0))) {
                    TypesSubTable st = new TypesSubTable("IF", "nll", model, o, this, offsetActual);
                    this.children.put("IF " + this.children.size(), st);
                    st.getDeclarations(o);
                    this.errors.addAll(st.errors);
                }
            }

            //ELSE_IFS
            if (o.toString().equals("ELSE_IFS")) {
                getDeclarations(o);
            }

            //SENTENCES
            if (o.toString().equals("SENTENCES")) {
                getDeclarations(o);
            }

            //ELSE_IF
            if (o.toString().equals("ELSE_IF")) {
                if (checkBlnExpression(model.getChild(o, 0))) {
                    TypesSubTable st = new TypesSubTable("ELSE_IF", "nll", model, o, this, offsetActual);
                    this.children.put("ELSE_IF " + this.children.size(), st);
                    st.getDeclarations(o);
                    this.errors.addAll(st.errors);
                }
            }

            //ELSE_IF
            if (o.toString().equals("ELSE")) {
                TypesSubTable st = new TypesSubTable("ELSE", "nll", model, o, this, offsetActual);
                this.children.put("ELSE " + this.children.size(), st);
                st.getDeclarations(o);
                this.errors.addAll(st.errors);
            }

            //SWITCH
            if (o.toString().equals("SWITCH")) {
                TableRow id = checkIDExistence(model.getChild(o, 0).toString());
                if (id != null) {
                    TypesSubTable st = new TypesSubTable("SWITCH", "nll", model, o, this, offsetActual);
                    this.children.put("SWITCH " + this.children.size(), st);
                    st.getDeclarations(o);
                    this.errors.addAll(st.errors);
                } else {
                    addError(o, "La variable de nombre: '" + model.getChild(o, 0).toString() + "' no fue encontrada en el ámbito actual. ");
                }
            }

            //CASES
            if (o.toString().equals("CASES")) {
                getDeclarations(o);
            }

            //SWITCH_CASE
            if (o.toString().equals("SWITCH_CASE")) {
                if (checkBlnExpressionSWITCH(model.getChild(o, 0))) {
                    TypesSubTable st = new TypesSubTable("CASE", "nll", model, o, this, offsetActual);
                    this.children.put("CASE " + this.children.size(), st);
                    st.getDeclarations(o);
                    this.errors.addAll(st.errors);
                }
            }

            //Declaraciones sin valores
            if (o.toString().equals("DECLR NEST/NO_VAL")) {
                if (checkIDExistence(model.getChild(o, 1).toString()) != null) {
                    addError(o, "La variable: " + model.getChild(o, 1).toString() + " ya existe en el ámbito");
                } else {
                    TableRow row = new TableRow(model.getChild(o, 1).toString(), model.getChild(o, 0).toString(), offsetActual);
                    addIDToSubTable(row);
                    //Sumar al Offset Actual
                    if (model.getChild(o, 0).toString().equals("int")) {
                        offsetActual += 4;
                    }
                    if (model.getChild(o, 0).toString().equals("bln")) {
                        offsetActual += 1;
                    }
                    if (model.getChild(o, 0).toString().equals("chr")) {
                        offsetActual += 1;
                    }
                }
                int ccc = model.getChildCount(o);
                if (ccc > 2) {
                    for (int j = 2; j < ccc; j++) {
                        if (checkIDExistence(model.getChild(o, j).toString()) != null) {
                            addError(o, "La variable: " + model.getChild(o, j).toString() + " ya existe en el ámbito");
                        } else {
                            TableRow row = new TableRow(model.getChild(o, j).toString(), model.getChild(o, 0).toString(), offsetActual);
                            addIDToSubTable(row);
                            //Sumar al Offset Actual
                            if (model.getChild(o, 0).toString().equals("int")) {
                                offsetActual += 4;
                            }
                            if (model.getChild(o, 0).toString().equals("bln")) {
                                offsetActual += 1;
                            }
                            if (model.getChild(o, 0).toString().equals("chr")) {
                                offsetActual += 1;
                            }
                        }
                    }
                }
            }

            //Declaraciones con Valor (Y análisis de Tipo)
            if (o.toString().equals("DECLR")) {
                String id = model.getChild(o, 1).toString();
                String type = model.getChild(o, 0).toString();
                String val = model.getChild(o, 2).toString();
                checkDeclr(id, type, val, o);
            }

            //Declaraciones de Matrices Vacías
            if (o.toString().equals("DECLR MATRIX EMPTY")) {
                if (checkIDExistence(model.getChild(o, 2).toString()) != null) {
                    addError(o, "La variable: " + model.getChild(o, 1).toString() + " ya existe en el ámbito. ");
                } else {
                    String id = model.getChild(o, 2).toString();

                    String filas = getSize(model.getChild(model.getChild(o, 0), 0).toString(), o);
                    String columnas = getSize(model.getChild(model.getChild(o, 0), 1).toString(), o);

                    String type = "array(0.." + filas
                            + ",array(0.." + columnas + "," + model.getChild(o, 1).toString() + "))";
                    TableRow row = new TableRow(id, type, offsetActual);
                    addIDToSubTable(row);
                    //Sumar al Offset Actual
                    if (isNumeric(filas) && isNumeric(columnas)) {
                        if (model.getChild(o, 1).toString().equals("int")) {
                            offsetActual += 4 * Integer.parseInt(filas) * Integer.parseInt(columnas);
                        }
                        if (model.getChild(o, 1).toString().equals("bln")) {
                            offsetActual += 1 * Integer.parseInt(filas) * Integer.parseInt(columnas);
                        }
                        if (model.getChild(o, 1).toString().equals("chr")) {
                            offsetActual += 1 * Integer.parseInt(filas) * Integer.parseInt(columnas);
                        }
                    }
                }
            }

            //Declaraciones de Matrices
            if (o.toString().equals("DECLR MATRIX")) {
                if (checkIDExistence(model.getChild(o, 2).toString()) != null) {
                    addError(o, " La variable: " + model.getChild(o, 2).toString() + " ya existe en el ámbito. ");
                } else {
                    String id = model.getChild(o, 2).toString();
                    int[] size = checkMatrix(model.getChild(o, 1).toString(), model.getChild(o, 3));
                    String type = "array(0.." + size[0]
                            + ",array(0.." + size[1] + "," + model.getChild(o, 1).toString() + "))";
                    TableRow row = new TableRow(id, type, offsetActual);
                    addIDToSubTable(row);
                    //Sumar al Offset Actual
                    if (model.getChild(o, 1).toString().equals("int")) {
                        offsetActual += 4 * size[0] * size[1];
                    }
                    if (model.getChild(o, 1).toString().equals("bln")) {
                        offsetActual += 1 * size[0] * size[1];
                    }
                    if (model.getChild(o, 1).toString().equals("chr")) {
                        offsetActual += 1 * size[0] * size[1];
                    }
                }
            }

            //Declaraciones de Arreglos Vacía
            if (o.toString().equals("DECLR ARRAY EMPTY")) {
                if (checkIDExistence(model.getChild(o, 2).toString()) != null) {
                    addError(o, " La variable: " + model.getChild(o, 1).toString() + " ya existe en el ámbito.. ");
                } else {
                    String id = model.getChild(o, 2).toString();
                    String els = getSize(model.getChild(model.getChild(o, 0), 0).toString(), o);
                    String type = "array(0.." + els + "," + model.getChild(o, 1).toString() + ")";
                    TableRow row = new TableRow(id, type, offsetActual);
                    addIDToSubTable(row);
                    //Sumar al Offset Actual
                    if (isNumeric(els)) {
                        if (model.getChild(o, 1).toString().equals("int")) {
                            offsetActual += 4 * Integer.parseInt(els);
                        }
                        if (model.getChild(o, 1).toString().equals("bln")) {
                            offsetActual += 1 * Integer.parseInt(els);
                        }
                        if (model.getChild(o, 1).toString().equals("chr")) {
                            offsetActual += 1 * Integer.parseInt(els);
                        }
                    }
                }
            }

            //Declaraciones de Arreglos
            if (o.toString().equals("DECLR ARRAY")) {
                if (checkIDExistence(model.getChild(o, 2).toString()) != null) {
                    addError(o, "La variable: " + model.getChild(o, 1).toString() + " ya existe en el ámbito.. ");
                } else {
                    String id = model.getChild(o, 2).toString();
                    int els = checkArray(model.getChild(o, 1).toString(), model.getChild(o, 3));
                    String type = "array(0.." + els + "," + model.getChild(o, 1).toString() + ")";
                    TableRow row = new TableRow(id, type, offsetActual);
                    addIDToSubTable(row);
                    //Sumar al Offset Actual
                    if (model.getChild(o, 1).toString().equals("int")) {
                        offsetActual += 4 * els;
                    }
                    if (model.getChild(o, 1).toString().equals("bln")) {
                        offsetActual += 1 * els;
                    }
                    if (model.getChild(o, 1).toString().equals("chr")) {
                        offsetActual += 1 * els;
                    }
                }
            }

            //Asignaciones
            if (o.toString().equals("ASSIGN")) {
                String id = model.getChild(o, 0).toString();
                String value = model.getChild(o, 1).toString();
                checkAssing(id, value, o);
            }

            //Afinacion de tipo id[][] = X
            if (o.toString().equals("ASSIGN MATRIX")) {
                String id = model.getChild(o, 0).toString();
                TableRow x = checkIDExistence(id);
                if (x != null) {
                    String de_type = checkExpression(o, 1);
                    //Ver si realmente es matriz porque se le puede poner los [][] a cualquier varuable 
                    if ((x.type.replace("..", "#")).split("#").length != 3) {
                        addError(o, "La variable de nombre '" + id + "' no es de tipo mtx. ");
                        return;
                    }
                    if (de_type.contains("array")) {
                        if ((x.type.replace("..", "#")).split("#").length != 3) {
                            addError(o, "La expresion de lado derecho de '" + id + "' no es del tipos esperado. ");
                            return;
                        }
                    }
                    if ((de_type.contains("int") && x.type.contains("int"))
                            || (de_type.contains("bln") && x.type.contains("bln"))
                            || (de_type.contains("chr") && x.type.contains("chr"))) {
                    } else {
                        addError(o, "La expresion de lado derecho de '" + id + "' no es del tipo esperado. ");
                    }
                } else {
                    addError(o, "No se encuentra el nombre: '" + id + "' accesible desde este ámbito. ");
                }
            }

            //Para asignacions de tipo id[]=X
            if (o.toString().equals("ASSIGN ARRAY")) {
                String id = model.getChild(o, 0).toString();
                TableRow x = checkIDExistence(id);
                if (x != null) {
                    String de_type = checkExpression(o, 1);
                    //Ver si realmente es matriz porque se le puede poner los [][] a cualquier varuable 
                    if ((x.type.replace("..", "#")).split("#").length != 2) {
                        addError(o, "La variable de nombre '" + id + "' no es de tipo arr. ");
                        return;
                    }
                    if (de_type.contains("array")) {
                        if ((x.type.replace("..", "#")).split("#").length != 2) {
                            addError(o, "La expresion de lado derecho de '" + id + "' no es del tipo esperado. ");
                            return;
                        }
                    }
                    if ((de_type.contains("int") && x.type.contains("int"))
                            || (de_type.contains("bln") && x.type.contains("bln"))
                            || (de_type.contains("chr") && x.type.contains("chr"))) {
                    } else {
                        addError(o, "La expresion de lado derecho de '" + id + "' no es del tipo esperado. ");
                    }
                } else {
                    addError(o, "No se encuentra el nombre: '" + id + "' accesible desde este ámbito. ");
                }
            }
        }
    }
    //Solo para ver si un String es numérico
    //Dependiendo del signo

    private static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena.replace(" ", ""));
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    //Vesmos si la declaración es correcta
    private boolean checkDeclr(String id, String type, String val, Object o) {
        if (checkIDExistence(id) == null) {
            if (type.equals(checkExpression(o, 2))) {
                TableRow row = new TableRow(id, type, offsetActual);
                addIDToSubTable(row);
                //Sumar al Offset Actual
                if (type.equals("int")) {
                    offsetActual += 4;
                }
                if (type.equals("bln")) {
                    offsetActual += 1;
                }
                if (type.equals("chr")) {
                    offsetActual += 1;
                }
                return true;
            } else {
                String type_val = checkTypeofValue(model.getChild(o, 2));
                if (type_val.contains("array") && (type_val.replace("..", "#")).split("#").length == 2 && type_val.contains(type)) {
                    TableRow row = new TableRow(id, type, offsetActual);
                    addIDToSubTable(row);
                    //Sumar al Offset Actual
                    if (type.equals("int")) {
                        offsetActual += 4;
                    }
                    if (type.equals("bln")) {
                        offsetActual += 1;
                    }
                    if (type.equals("chr")) {
                        offsetActual += 1;
                    }
                    return true;
                } else if (type_val.contains("array") && (type_val.replace("..", "#")).split("#").length == 3 && type_val.contains(type)) {
                    TableRow row = new TableRow(id, type, offsetActual);
                    addIDToSubTable(row);
                    //Sumar al Offset Actual
                    if (type.equals("int")) {
                        offsetActual += 4;
                    }
                    if (type.equals("bln")) {
                        offsetActual += 1;
                    }
                    if (type.equals("chr")) {
                        offsetActual += 1;
                    }
                    return true;
                } else {
                    addError(o, "La expresión del lado derecho de '" + id + "' no es del tipo esperado. ");
                    return false;
                }
            }
        } else {
            addError(o, "La variable de nombre '" + id + "' ya existe en el ámbito actual. ");
            return false;
        }
    }

    //Vemos si la asignación es correcta
    private boolean checkAssing(String id, String value, Object o) {
        TableRow x = checkIDExistence(id);
        if (x != null) {
            String exp_type = checkExpression(o, 1);
            if (x.type.equals(exp_type)) {
                return true;
            } else {
                //Revisar si son array o matrix
                if ((x.type.contains("array") && exp_type.contains("array"))
                        && (x.type.replace("..", "#")).split("#").length == (exp_type.replace("..", "#")).split("#").length) {
                    if (x.type.contains("int") && exp_type.contains("int")) {
                        return true;
                    } else if (x.type.contains("bln") && exp_type.contains("bln")) {
                        return true;
                    } else if (x.type.contains("chr") && exp_type.contains("chr")) {
                        return true;
                    } else {
                        addError(o, "La matriz del lado derecho de '" + id + "' no es del mismo tipo que " + id + ". ");
                        return false;
                    }
                } else if (exp_type.contains("array") && model.getChildCount(model.getChild(o, 1)) != 0) {
                    //arreglos dentro de la expresion entera
                    Object y = model.getChild(o, 1);
                    if ((exp_type.replace("..", "#")).split("#").length == 2 && model.getChildCount(y) == 1) {
                        Object indice = model.getChild(y, 0);
                        if (checkIntExpression(indice)) {
                            if (exp_type.contains(x.type)) {
                                return true;
                            } else {
                                addError(y, "La matriz del lado derecho de '" + id + "' no es del mismo tipo que " + id + ". ");
                                return false;
                            }
                        } else {
                            addError(y, "La variable de nombre: '" + indice + "' no es de tipo int.");
                            return false;
                        }
                        //Matrices dentro de la expresíon entera
                    } else if ((exp_type.replace("..", "#")).split("#").length == 3 && model.getChildCount(y) == 2) {
                        Object indice = model.getChild(y, 0);
                        if (!checkIntExpression(indice)) {
                            addError(y, "La variable de nombre: '"
                                    + indice + "' no es de tipo int.");
                            return false;
                        }
                        indice = model.getChild(y, 1);
                        if (checkIntExpression(indice)) {
                            if (exp_type.contains(x.type)) {
                                return true;
                            } else {
                                addError(y, "La matriz del lado derecho de '" + id + "' no es del mismo tipo que " + id + ". ");
                                return false;
                            }
                        } else {
                            addError(y, "La variable de nombre: '"
                                    + indice + "' no es de tipo int.");
                            return false;
                        }
                    } else {
                        addError(y, "La matriz '" + y.toString() + "' no tiene el número de índices correctos. ");
                        return false;
                    }
                } else if ((x.type.contains("array") && exp_type.contains("arr") && (x.type.replace("..", "#")).split("#").length == 2)
                        || x.type.contains("array") && exp_type.contains("mtx") && (x.type.replace("..", "#")).split("#").length == 3) {
                    if (x.type.contains("int") && exp_type.contains("int")) {
                        return true;
                    } else if (x.type.contains("bln") && exp_type.contains("bln")) {
                        return true;
                    } else if (x.type.contains("chr") && exp_type.contains("chr")) {
                        return true;
                    } else {
                        addError(o, "La matriz del lado derecho de '" + id + "' no es del mismo tipo que " + id + ". ");
                        return false;
                    }

                } else {
                    addError(o, "La expresión del lado derecho de '" + id + "' no es del tipo esperado. ");
                    return false;
                }
            }
        } else {
            addError(o, "No se encuentra el nombre: '" + id + "' accesible desde este ámbito. ");
            return false;
        }
    }

    //Analiza la parte izquiera de una declaración/asignación y delvuele el tipo de éste
    //En caso de tener algún errorImplicará la muestra del mismo. 
    public String checkExpression(Object o, int i) {
        o = model.getChild(o, i);
        String root = o.toString();
        if (root.equals("false") || root.equals("true")) {
            return "bln";
        } else if (root.equals("nll")) {
            return "nll";
        } else if (isNumeric(root)) {
            return "int";
        } else if (o.toString().contains("'") || o.toString().contains("‘") || o.toString().contains("’")) {
            return "chr";
        } else if (root.equals("+") || root.equals("-") || root.equals("%") || root.equals("/") || root.equals("*")) {
            if (checkIntExpression(o)) {
                return "int";
            } else {
                return "nll";
            }
        } else if (root.equals("AND") || root.equals("OR") || root.equals("<") || root.equals(">")
                || root.equals("<=") || root.equals(">=") || root.equals("=") || root.equals("!=")
                || root.equals("NOT")) {
            if (checkBlnExpression(o)) {
                return "bln";
            } else {
                addError(o, "Error en la expresión del lado derecho de " + model.getChild(o, 0).toString() + ". ");
                return "nll";
            }
        } else if (root.contains(":fun")) {
            String fun = checkFunction(root.replace(":fun", ""));
            if (!fun.equals("")) {
                if (checkFunctionParams(fun.split(" -> ")[0], o)) {
                    return fun.split(" -> ")[1];
                } else {
                    addError(o, "No hay función '" + root.replace(":fun", "") + "' con esos tipos de parámetros. ");
                    return "nll";
                }
            } else {
                addError(o, "La función '" + o.toString().replace(":fun", "") + " no existe");
                return "nll";
            }
        } else {
            TableRow id = checkIDExistence(root);
            if (id == null) {
                addError(o, "La variable de nombre: '"
                        + root + "' no fue encontrada en el ámbito actual ");
                return "nll";
            } else {
                return id.type;
            }
        }
    }

    //Analiza que el arbol de una expresion anidada sea todo de tipos enteros
    private boolean checkIntExpression(Object o) {
        if (model.getChildCount(o) == 0) {
            if (isNumeric(o.toString())) {
                return true;
            } else if (o.toString().contains("'") || o.toString().contains("‘") || o.toString().contains("’")) {
                addError(o, "El caracter '" + o.toString() + "' no puede ir dentro de una expresión aritmética. ");
                return false;
            } else if (o.toString().equals("true") || o.toString().equals("false")) {
                addError(o, "El booleano '" + o.toString() + "' no puede ir dentro de una expresión aritmética. ");
                return false;
            } else {
                TableRow id = checkIDExistence(o.toString());
                if (id != null) {
                    if (id.type.equals("int")) {
                        return true;
                    } else {
                        addError(o, "El " + id.type + " '" + o.toString() + "' no puede ir dentro de una expresión aritmética. ");
                        return false;
                    }
                } else {
                    addError(o, "No hay variable de nombre: '"
                            + o.toString() + "' accesible desde este ámbito actual. ");
                    return false;
                }
            }
        } else {
            if (o.toString().contains(":fun")) {
                String fun = checkFunction(o.toString().replace(":fun", ""));
                if (!fun.equals("")) {
                    if (!fun.split(" -> ")[1].equals("int")) {
                        addError(o, "La función '" + fun + "' no devuelve un int. ");
                        return false;
                    } else {
                        if (checkFunctionParams(fun.split(" -> ")[0], o)) {
                            return true;
                        } else {
                            addError(o, "No hay función '" + fun + "' con esos tipos de parámetros. ");
                            return false;
                        }
                    }
                } else {
                    addError(o, "La función '" + o.toString().replace(":fun", "") + " no existe");
                }
            } else {
                TableRow id = checkIDExistence(o.toString());
                if (id != null) {
                    if (id.type.contains("array") && id.type.contains("int")) {
                        //arreglos dentro de la expresion entera
                        if ((id.type.replace("..", "#")).split("#").length == 2 && model.getChildCount(o) == 1) {
                            Object indice = model.getChild(o, 0);
                            if (checkIntExpression(indice)) {
                                return true;
                            } else {
                                addError(o, "La variable de nombre: '"
                                        + indice + "' no es de tipo int.");
                                return false;
                            }
                            //Matrices dentro de la expresíon entera
                        } else if ((id.type.replace("..", "#")).split("#").length == 3 && model.getChildCount(o) == 2) {
                            Object indice = model.getChild(o, 0);
                            if (checkIntExpression(indice)) {
                            } else {
                                addError(o, "La variable de nombre: '"
                                        + indice + "' no es de tipo int.");
                                return false;
                            }
                            indice = model.getChild(o, 1);
                            if (checkIntExpression(indice)) {
                                return true;
                            } else {
                                addError(o, "La variable de nombre: '"
                                        + indice + "' no es de tipo int.");
                                return false;
                            }
                        } else {
                            addError(o, "La matriz '" + o.toString() + "' no tiene el número de índices correctos. ");
                            return false;
                        }
                    }
                } else {
                    boolean lc = checkIntExpression(model.getChild(o, 0));
                    boolean rc = true;
                    if (model.getChildCount(o) > 1) {
                        rc = checkIntExpression(model.getChild(o, 1));
                    }
                    if (lc && rc) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    //Analiza que el arbol de una expresion anidada sea todo de tipos validos
    private boolean checkBlnExpression(Object o) {
        if (model.getChildCount(o) == 0) {
            if (o.toString().equals("true") || o.toString().equals("false")) {
                return true;
            } else if (o.toString().contains("'") || o.toString().contains("‘") || o.toString().contains("’")) {
                return true;
            } else if (isNumeric(o.toString())) {
                return true;
            } else {
                TableRow id = checkIDExistence(o.toString());
                if (id != null) {
                    return true;
                } else {
                    addError(o, "No hay variable de nombre: '"
                            + o.toString() + "' accesible desde este ámbito actual. ");
                    return false;
                }
            }
        } else {
            if (o.toString().contains(":fun")) {
                String fun = checkFunction(o.toString().replace(":fun", ""));
                if (!fun.equals("")) {
                    if (!fun.split(" -> ")[1].equals("nll")) {
                        addError(o, "La función '" + fun + "' no devuelve un nada. ");
                        return false;
                    } else {
                        if (checkFunctionParams(fun.split(" -> ")[0], o)) {
                            return true;
                        } else {
                            addError(o, "No hay función '" + fun + "' con esos tipos de parámetros. ");
                            return false;
                        }
                    }
                } else {
                    addError(o, "La función '" + o.toString().replace(":fun", "") + " no existe");
                    return false;
                }
            } else if (o.toString().equals("<") || o.toString().equals(">")
                    || o.toString().equals("<=") || o.toString().equals(">=")
                    || o.toString().equals("...")) {
                if (checkTypeofValue(model.getChild(o, 0)).equals("int") && checkTypeofValue(model.getChild(o, 1)).equals("int")) {
                    return true;
                } else {
                    addError(o, "Ambas variables en una comparación(<,>,<=.>=) deben ser de tipo int. ");
                    return false;
                }
            } else if (o.toString().equals("!=") || o.toString().equals("=")) {
                if (checkTypeofValue(model.getChild(o, 0)).equals(checkTypeofValue(model.getChild(o, 1)))) {
                    return true;
                } else {
                    addError(o, "Ambas variables en una comparación(=,!=) deben ser de del mismo tipo. ");
                    return false;
                }
            } else {
                boolean lc = checkBlnExpression(model.getChild(o, 0));
                boolean rc = checkBlnExpression(model.getChild(o, 1));
                if (lc && rc) {
                    return true;
                } else {
                    addError(o, "Error en la expresión booleana. ");
                    return false;
                }
            }
        }
    }

    //Analiza que la comdicion del switch_case sea correcta
    private boolean checkBlnExpressionSWITCH(Object o) {
        if (o.toString().equals("...")) {
            if (checkTypeofValue(model.getChild(o, 0)).equals("int") && checkTypeofValue(model.getChild(o, 1)).equals("int")) {
                return true;
            } else {
                addError(o, "Ambas variables en una comparación(...) deben ser de tipo int. ");
                return false;
            }
        } else if (o.toString().equals("<") || o.toString().equals(">")
                || o.toString().equals("<=") || o.toString().equals(">=")) {
            if (checkTypeofValue(model.getChild(o, 0)).equals("int")) {
                return true;
            } else {
                addError(o, "La variable dentro del SWITCH en una comparación(<,>,<=.>=) deben se de tipo int. ");
                return false;
            }
        } else if (o.toString().equals("!=") || o.toString().equals("=")) {
            if (!checkTypeofValue(model.getChild(o, 0)).equals("nill")) {
                return true;
            } else {
                addError(o, "La variable dentro del SWITCH no es de ningún tipo (nll). ");
                return false;
            }
        }
        return false;
    }

    //Verifica si parametros del llamado son corectos
    public boolean checkFunctionParams(String paramsID, Object o) {
        int cc = model.getChildCount(o);
        String params[] = paramsID.split(" x ");
        if (cc != params.length) {
            return params[0].equals("nll") && cc == 0;
        } else {
            for (int i = 0; i < cc; i++) {
                Object c = model.getChild(o, i);
                String type = checkTypeofValue(c);
                if (!type.equals(params[i])) {
                    if (type.contains("array") && (type.replace("..", "#")).split("#").length == 2 && params[i].contains("arr")
                            && type.contains(params[i].split(" ")[1])) {
                        return true;
                    } else if (type.contains("array") && (type.replace("..", "#")).split("#").length == 3 && params[i].contains("mtx")
                            && type.contains(params[i].split(" ")[1])) {
                        return true;
                    } else {
                        addError(o, "Los parametros enviados son de tipo distinto a los parametros de la función. ");
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private String checkFunction(String id) {
        TypesSubTable x = this;
        while (x.parent != null) {
            x = x.parent;
        }
        if (x.children.containsKey(id)) {
            return x.children.get(id).functionType;
        } else {
            return "";
        }
    }

    private String checkTypeofValue(Object o) {
        String id = o.toString();
        if (id.equals("false") || id.equals("true")) {
            return "bln";
        } else if (id.equals("nll")) {
            return "nll";
        } else if (isNumeric(id)) {
            return "int";
        } else if (id.contains("'") || id.contains("‘") || id.contains("’")) {
            return "chr";
        } else {
            TableRow id1 = checkIDExistence(id);
            if (id1 != null) {
                return id1.type;
            } else {
                addError(o, "No hay variable de nombre: '"
                        + id + "' accesible desde este ámbito actual. ");
                return "nll";
            }
        }
    }

    private int checkArray(String type, Object o) {
        int cc = model.getChildCount(o);
        if (o.toString().contains(":fun")) {
            String fun = checkFunction(o.toString().replace(":fun", ""));
            if (!fun.equals("")) {
                if (fun.split(" -> ")[1].contains("mtx")) {
                    if (!fun.split(" -> ")[1].contains(type)) {
                        addError(o, "La función '" + o.toString().replace(":fun", "") + "' no devuelve el tipo mtx " + type + ".");
                    }
                } else {
                    if (!checkFunctionParams(fun.split(" -> ")[0], o)) {
                        addError(o, "No hay función '" + o.toString().replace(":fun", "") + "' con esos tipos de parámetros. ");
                    }
                }
            } else {
                addError(o, "La función '" + o.toString().replace(":fun", "") + " no existe");
            }
        } else {
            for (int i = 0; i < cc; i++) {
                if (checkExpression(o, i).equals(type)) {
                    return cc;
                } else {
                    addError(o, ""
                            + "El elemento '" + model.getChild(o, i)
                            + "' no es de tipo " + type + ". "
                            + ((InstructionCode) ((DefaultMutableTreeNode) model.getChild(o, i)).getUserObject()).getCodeLine());
                }
            }
        }
        return cc;
    }

    private int[] checkMatrix(String type, Object o) {
        int[] size;
        size = new int[2];
        if (o.toString().contains(":fun")) {
            size[0] = 0;
            size[1] = 0;
            String fun = checkFunction(o.toString().replace(":fun", ""));
            if (!fun.equals("")) {
                if (fun.split(" -> ")[1].contains("mtx")) {
                    if (!fun.split(" -> ")[1].contains(type)) {
                        addError(o, "La función '" + o.toString().replace(":fun", "") + "' no devuelve el tipo mtx " + type + ".");
                    }
                } else {
                    if (!checkFunctionParams(fun.split(" -> ")[0], o)) {
                        addError(o, "No hay función '" + o.toString().replace(":fun", "") + "' con esos tipos de parámetros. ");
                    }
                }
            } else {
                addError(o, "La función '" + o.toString().replace(":fun", "") + " no existe.");
            }
            return size;
        } else {
            size[0] = model.getChildCount(o);
            size[1] = 1;
            for (int i = 0; i < size[0]; i++) {
                if (!model.getChild(o, i).toString().equals("ELEMENTS")) {
                    addError(o, "El elemento en la fila " + i + " no es de tipo arr. ");
                }
                int x = checkArray(type, model.getChild(o, i));
                if (i == 0) {
                    size[1] = x;
                } else if (size[1] != x) {
                    addError(o, "" + "El arr en la fila " + i + " no es del mismo tamaño que arr en la fila " + (i - 1) + ". ");
                }
            }
            return size;
        }
    }

    private boolean checkForParameters(Object o) {
        int cc = model.getChildCount(o);
        Object c = model.getChild(o, 0);
        for (int j = 0; j < model.getChildCount(c); j++) {
            checkBoolFOR(model.getChild(c, j));
        }
        c = model.getChild(o, 1);
        String step = model.getChild(c, 0).toString();
        if (step.contains("+")) {
            step = step.replace("+", "");
        }
        if (step.contains("-")) {
            step = step.replace("-", "");
        }

        if (!isNumeric(step)) {
            TableRow id = checkIDExistence(step);
            if (id != null) {
                return true;
            } else {
                addError(o, "No hay variable de nombre: '"
                        + step + "' accesible desde este ámbito actual. ");
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean checkBoolFOR(Object o) {
        Object childI = model.getChild(o, 0);
        TableRow row = new TableRow(childI.toString(), "int", offsetActual);
        if (addIDToSubTable(row)) {
            offsetActual += 4;
        }
        //verificar si el otro elemento es un ID o un integer
        Object childD = model.getChild(o, 1);
        if (!isNumeric(childD.toString())) {
            TableRow id = checkIDExistence(childD.toString());
            if (id != null) {
                return true;
            } else {
                addError(o, "No hay variable de nombre: '"
                        + childD.toString() + "' accesible desde este ámbito actual. ");
                return false;
            }
        } else {
            return true;
        }
    }

    private String getSize(String x, Object o) {
        if (isNumeric(x)) {
            return x;
        } else {
            TableRow id = checkIDExistence(x);
            if (id != null) {
                if (id.type.equals("int")) {
                    return x;
                } else {
                    addError(o, "La variable de nombre: '"
                            + x + "' debe ser de tipo int. ");
                    return "";
                }
            } else {
                addError(o, "No hay variable de nombre: '"
                        + x + "' accesible desde este ámbito actual. ");
                return "";
            }
        }
    }

    public void addError(Object o, String msm) {
        try {
            errors.add(">!<Error de Tipo. Linea: " + ((InstructionCode) ((DefaultMutableTreeNode) o).getUserObject()).getCodeLine()
                    + "\n   " + msm + "\n");
        } catch (Exception e) {
            errors.add(">!<Error de Tipo. Linea: Desconocida" + "\n    " + msm + "\n");
        }
    }

    //Retorna el Tablerow del ID en un bloque dado
    public TableRow getID(String id, Object o_t_b, TypesSubTable o) {
        for (String i : o.children.keySet()) {
            TypesSubTable child = o.children.get(i);
            System.out.println("entra");
            int numTST = ((InstructionCode) ((DefaultMutableTreeNode) child.treepart).getUserObject()).getCodeLine();
            int numO = ((InstructionCode) ((DefaultMutableTreeNode) o_t_b).getUserObject()).getCodeLine();
            if (numO == numTST) {
                TableRow id_t_ret = child.checkIDExistence(id);
                return id_t_ret;
            } else {
                getID(id, o_t_b, child);
            }
        }
        return null;
    }

    public String toString(String identacion) {
        String t = "-->" + functionName + ":" + functionType + "\n";
        for (TableRow i : ids.values()) {
            t += "  " + identacion + i.toString() + "\n";
        }
        return t;
    }

}
