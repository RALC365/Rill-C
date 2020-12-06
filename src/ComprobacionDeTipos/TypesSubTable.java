/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComprobacionDeTipos;

import CUP.InstructionCode;
import java.util.LinkedHashMap;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Usuario
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
    public void getDeclarations(Object a) throws TypeErrorException {
        int cc = model.getChildCount(a);
        //Agregan el ID del FOR a su tabla de IDs
        if (a.toString().equals("FOR")) {
            String for_txt[] = model.getChild(a, 0).toString().split(",");
            TableRow row = new TableRow(for_txt[0], "int", offsetActual);
            addIDToSubTable(row);
        }

        //Recoree los Hijo del nodo Actual 
        for (int i = 0; i < cc; i++) {
            Object o = model.getChild(a, i);

            //FOR
            if (o.toString().equals("FOR")) {
                TypesSubTable st = new TypesSubTable("FOR", "nll", model, o, this, offsetActual);
                this.children.put("FOR "+this.children.size(), st);
                st.getDeclarations(o);
                // System.out.println(st.toString());
            }

            //WHILE
            if (o.toString().equals("WHILE")) {
                if (checkBlnExpression(model.getChild(o, 0))) {
                    TypesSubTable st = new TypesSubTable("WHILE", "nll", model, o, this, offsetActual);
                    this.children.put("WHILE "+this.children.size(), st);
                    st.getDeclarations(o);
                    // System.out.println(st.toString());
                }
            }

            //IF
            if (o.toString().equals("IF")) {
                if (checkBlnExpression(model.getChild(o, 0))) {
                    TypesSubTable st = new TypesSubTable("IF", "nll", model, o, this, offsetActual);
                    this.children.put("IF "+this.children.size(), st);
                    st.getDeclarations(o);
                    // System.out.println(st.toString());
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
                    this.children.put("ELSE_IF "+this.children.size(), st);
                    st.getDeclarations(o);
                    // System.out.println(st.toString());
                }
            }

            //ELSE_IF
            if (o.toString().equals("ELSE")) {
                TypesSubTable st = new TypesSubTable("ELSE", "nll", model, o, this, offsetActual);
                this.children.put("ELSE "+this.children.size(), st);
                st.getDeclarations(o);
                // System.out.println(st.toString());
            }

            //SWITCH
            if (o.toString().equals("SWITCH")) {
                TableRow id = checkIDExistence(model.getChild(o, 0).toString());
                if (id != null) {
                    TypesSubTable st = new TypesSubTable("SWITCH", "nll", model, o, this, offsetActual);
                    this.children.put("SWITCH "+this.children.size(), st);
                    st.getDeclarations(o);
                } else {
                    throw new TypeErrorException("Error de Tipo.\nLa variable de nombre: '"
                            + model.getChild(o, 0).toString() + "' no fue encontrada en el ámbito actual");
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
                    this.children.put("CASE "+this.children.size(), st);
                    st.getDeclarations(o);
                    // System.out.println(st.toString());
                }
            }

            //Declaraciones sin valores
            if (o.toString().equals("DECLR NEST/NO_VAL")) {
                if (checkIDExistence(model.getChild(o, 1).toString()) != null) {
                    throw new TypeErrorException("Error de Declaración. La variable: " + model.getChild(o, 1).toString() + " ya existe en el ámbito.");
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
                            throw new TypeErrorException("Error de Declaración. La variable: " + model.getChild(o, j).toString() + " ya existe en el ámbito. Linea: " + ((InstructionCode)model.getChild(o, j)).getCodeLine());
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
                    throw new TypeErrorException("Error de Declaración. La variable: " + model.getChild(o, 1).toString() + " ya existe en el ámbito. Línea: " + ((InstructionCode)model.getChild(o, 1)).getCodeLine());
                } else {
                    String id = model.getChild(o, 2).toString();
                    int filas = Integer.parseInt(model.getChild(model.getChild(o, 0), 0).toString());
                    int columnas = Integer.parseInt(model.getChild(model.getChild(o, 0), 1).toString());
                    String type = "array(0.." + filas
                            + ",array(0.." + columnas + "," + model.getChild(o, 1).toString() + "))";
                    TableRow row = new TableRow(id, type, offsetActual);
                    addIDToSubTable(row);
                    //Sumar al Offset Actual
                    if (model.getChild(o, 1).toString().equals("int")) {
                        offsetActual += 4 * filas * columnas;
                    }
                    if (model.getChild(o, 1).toString().equals("bln")) {
                        offsetActual += 1 * filas * columnas;
                    }
                    if (model.getChild(o, 1).toString().equals("chr")) {
                        offsetActual += 1 * filas * columnas;
                    }
                }
            }

            //Declaraciones de Matrices
            if (o.toString().equals("DECLR MATRIX")) {
                if (checkIDExistence(model.getChild(o, 2).toString()) != null) {
                    throw new TypeErrorException("Error de Declaración. La variable: " + model.getChild(o, 1).toString() + " ya existe en el ámbito. Línea: " + ((InstructionCode)model.getChild(o, 1)).getCodeLine());
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
                    throw new TypeErrorException("Error de Declaración. La variable: " + model.getChild(o, 1).toString() + " ya existe en el ámbito.. Línea: " + ((InstructionCode)model.getChild(o, 1)).getCodeLine());
                } else {
                    String id = model.getChild(o, 2).toString();
                    int els = Integer.parseInt(model.getChild(model.getChild(o, 0), 0).toString());
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

            //Declaraciones de Arreglos
            if (o.toString().equals("DECLR ARRAY")) {
                if (checkIDExistence(model.getChild(o, 2).toString()) != null) {
                    throw new TypeErrorException("Error de Declaración. La variable: " + model.getChild(o, 1).toString() + " ya existe en el ámbito.. Línea: " + ((InstructionCode)model.getChild(o, 1)).getCodeLine());
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
    private boolean checkDeclr(String id, String type, String val, Object o) throws TypeErrorException {
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
                throw new TypeErrorException("Error de tipo. La expresión del lado izquiero de '" + id + "' no es del tipo esperado.");
            }
        } else {
            throw new TypeErrorException("Error de Tipodadasdasd.\nLa variable de nombre '" + id + "' ya existe en el ámbito actual.");
        }
    }

    //Vemos si la asignación es correcta
    private boolean checkAssing(String id, String value, Object o) throws TypeErrorException {
        TableRow x = checkIDExistence(id);
        if (x != null) {
            if (x.type.equals(checkExpression(o, 1))) {
                return true;
            } else {
                throw new TypeErrorException("Error de tipo. La expresión del lado izquiero de '" + id + "' no es del tipo esperado.");
            }
        } else {
            throw new TypeErrorException("Error de Tipo.\nNo se encuentra el nombre: '" + id + "' accesible desde este ámbito.");
        }
    }

    //Analiza la parte izquiera de una declaración/asignación y delvuele el tipo de éste
    //En caso de tener algún errorImplicará la muestra del mismo. 
    public String checkExpression(Object o, int i) throws TypeErrorException {
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
                throw new TypeErrorException("Error de Tipo.\nDesconocido.");
            }
        } else if (root.equals("AND") || root.equals("OR") || root.equals("<") || root.equals(">")
                || root.equals("<=") || root.equals(">=") || root.equals("=") || root.equals("!=")
                || root.equals("NOT")) {
            if (checkBlnExpression(o)) {
                return "bln";
            } else {
                throw new TypeErrorException("Error de Tipo.\nDesconocido.");
            }
        } else if (root.contains(":fun")) {
            String fun = checkFunction(root.replace(":fun", ""));
            if (checkFunctionParams(fun.split(" -> ")[0], o)) {
                return fun.split(" -> ")[1];
            } else {
                throw new TypeErrorException("Error de Tipo.\nNo hay función '" + root.replace(":fun", "") + "' con esos tipos de parámetros.");
            }
        } else {
            TableRow id = checkIDExistence(root);
            if (id == null) {
                throw new TypeErrorException("Error de Tipo.\nLa variable de nombre: '"
                        + root + "' no fue encontrada en el ámbito actual");
            } else {
                return id.type;
            }
        }
    }

    //Analiza que el arbol de una expresion anidada sea todo de tipos enteros
    private boolean checkIntExpression(Object o) throws TypeErrorException {
        if (model.getChildCount(o) == 0) {
            if (isNumeric(o.toString())) {
                return true;
            } else if (o.toString().contains("'") || o.toString().contains("‘") || o.toString().contains("’")) {
                throw new TypeErrorException("Error de Tipo.\nEl caracter '" + o.toString() + "' no puede ir dentro de una expresión aritmética");
            } else if (o.toString().equals("true") || o.toString().equals("false")) {
                throw new TypeErrorException("Error de Tipo.\nEl booleano '" + o.toString() + "' no puede ir dentro de una expresión aritmética");
            } else {
                TableRow id = checkIDExistence(o.toString());
                if (id != null) {
                    if (id.type.equals("int")) {
                        return true;
                    } else {
                        throw new TypeErrorException("Error de Tipo.\nEl " + id.type + " '" + o.toString() + "' no puede ir dentro de una expresión aritmética");
                    }
                } else {
                    throw new TypeErrorException("Error de Tipo.\nNo hay variable de nombre: '"
                            + o.toString() + "' accesible desde este ámbito actual.");
                }
            }
        } else {
            if (o.toString().contains(":fun")) {
                String fun = checkFunction(o.toString().replace(":fun", ""));
                if (!fun.split(" -> ")[1].equals("int")) {
                    throw new TypeErrorException("Error de Tipo.\nLa función '" + fun + "' no devuelve un int.");
                } else {
                    if (checkFunctionParams(fun.split(" -> ")[0], o)) {
                        return true;
                    } else {
                        throw new TypeErrorException("Error de Tipo.\nNo hay función '" + fun + "' con esos tipos de parámetros.");
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

    //Analiza que el arbol de una expresion anidada sea todo de tipos validos
    private boolean checkBlnExpression(Object o) throws TypeErrorException {
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
                    throw new TypeErrorException("Error de Tipo.\nNo hay variable de nombre: '"
                            + o.toString() + "' accesible desde este ámbito actual.");
                }
            }
        } else {
            if (o.toString().contains(":fun")) {
                String fun = checkFunction(o.toString().replace(":fun", ""));
                if (!fun.split(" -> ")[1].equals("nll")) {
                    throw new TypeErrorException("Error de Tipo.\nLa función '" + fun + "' no devuelve un nada.");
                } else {
                    if (checkFunctionParams(fun.split(" -> ")[0], o)) {
                        return true;
                    } else {
                        throw new TypeErrorException("Error de Tipo.\nNo hay función '" + fun + "' con esos tipos de parámetros.");
                    }
                }
            } else if (o.toString().equals("<") || o.toString().equals(">")
                    || o.toString().equals("<=") || o.toString().equals(">=")
                    || o.toString().equals("...")) {
                if (checkTypeofValue(model.getChild(o, 0).toString()).equals("int") && checkTypeofValue(model.getChild(o, 1).toString()).equals("int")) {
                    return true;
                } else {
                    throw new TypeErrorException("Error de Tipo.\nAmbas variables en una comparación(<,>,<=.>=) deben ser de tipo int.");
                }
            } else if (o.toString().equals("!=") || o.toString().equals("=")) {
                if (checkTypeofValue(model.getChild(o, 0).toString()).equals(checkTypeofValue(model.getChild(o, 1).toString()))) {
                    return true;
                } else {
                    throw new TypeErrorException("Error de Tipo.\nAmbas variables en una comparación(=,!=) deben ser de del mismo tipo.");
                }
            } else {
                boolean lc = checkBlnExpression(model.getChild(o, 0));
                boolean rc = checkBlnExpression(model.getChild(o, 1));
                if (lc && rc) {
                    return true;
                } else {
                    throw new TypeErrorException("Error de Tipo.\nDesconocido.");
                }
            }
        }
        //return false;
    }

    //Analiza que la comdicion del switch_case sea correcta
    private boolean checkBlnExpressionSWITCH(Object o) throws TypeErrorException {
        if (o.toString().equals("...")) {
            if (checkTypeofValue(model.getChild(o, 0).toString()).equals("int") && checkTypeofValue(model.getChild(o, 1).toString()).equals("int")) {
                return true;
            } else {
                throw new TypeErrorException("Error de Tipo.\nAmbas variables en una comparación(...) deben ser de tipo int.");
            }
        } else if (o.toString().equals("<") || o.toString().equals(">")
                || o.toString().equals("<=") || o.toString().equals(">=")) {
            if (checkTypeofValue(model.getChild(o, 0).toString()).equals("int")) {
                return true;
            } else {
                throw new TypeErrorException("Error de Tipo.\nLa variable dentro del SWITCH en una comparación(<,>,<=.>=) deben se de tipo int.");
            }
        } else if (o.toString().equals("!=") || o.toString().equals("=")) {
            if (!checkTypeofValue(model.getChild(o, 0).toString()).equals("nill")) {
                return true;
            } else {
                throw new TypeErrorException("Error de Tipo.\nLa variable dentro del SWITCH no es de ningún tipo (nll).");
            }
        }
        return false;
    }

    //Verifica si parametros del llamado son corectos
    public boolean checkFunctionParams(String paramsID, Object o) throws TypeErrorException {
        int cc = model.getChildCount(o);
        String params = "";
        if (cc == 0) {
            params = "nll";
        } else {
            for (int i = 0; i < cc; i++) {
                String c = model.getChild(o, i).toString();
                TableRow id = checkIDExistence(c);
                if (id != null) {
                    if (i == cc - 1) {
                        params += id.type;
                    } else {
                        params += id.type + " x ";
                    }
                } else {
                    throw new TypeErrorException("Error de Tipo.\nNo hay variable de nombre: '"
                            + c + "' accesible desde este ámbito actual.");
                }
            }
        }
        return paramsID.equals(params);
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

    private String checkTypeofValue(String id) throws TypeErrorException {
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
                throw new TypeErrorException("Error de Tipo.\nNo hay variable de nombre: '"
                        + id + "' accesible desde este ámbito actual.");
            }
        }
    }

    private int checkArray(String type, Object o) throws TypeErrorException {
        int cc = model.getChildCount(o);
        for (int i = 0; i < cc; i++) {
            if (checkExpression(o, i).equals(type)) {
                return cc;
            } else {
                throw new TypeErrorException("Error de Tipo.\n" + "El elemento '" + model.getChild(o, i) + "' no es de tipo " + type);
            }
        }
        return cc;
    }

    private int[] checkMatrix(String type, Object o) throws TypeErrorException {
        int[] size;
        size = new int[2];
        size[0] = model.getChildCount(o);
        size[1] = 1;
        for (int i = 0; i < size[0]; i++) {
            if (!model.getChild(o, i).toString().equals("ELEMENTS")) {
                throw new TypeErrorException("Error de Tipo.\n" + "El elemento en la fila " + i + " no es de tipo arr.");
            }
            int x = checkArray(type, model.getChild(o, i));
            if (i == 0) {
                size[1] = x;
            } else if (size[1] != x) {
                throw new TypeErrorException("Error de Tipo.\n" + "El arr en la fila " + i + " no es del mismo tamaño que arr en la fila " + (i - 1) + ".");
            }
        }
        return size;
    }

    @Override
    public String toString() {
        String t = "function: " + functionName + "\n   ";
        for (TableRow i : ids.values()) {
            t += i.toString() + "\n   ";
        }
        return t;
    }

}
