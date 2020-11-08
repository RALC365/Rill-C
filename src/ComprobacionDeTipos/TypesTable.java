/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComprobacionDeTipos;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Usuario
 */
public class TypesTable {

    private final HashMap<String, TypesSubTable> subtables = new HashMap();
    private final ArrayList<String> errors = new ArrayList();

    public TypesTable(DefaultTreeModel model) {
        createSubtables(model);
        //analizeTypes(model);
    }

    //Crea las subTablaas de Tipos en Base a las funciones dentro del programa
    //Un tabla por Función, de esta manera se facilita la definición del ámbito
    private void createSubtables(DefaultTreeModel model) {
        int cc = model.getChildCount(model.getRoot());
        for (int i = 0; i < cc; i++) {
            Object child = model.getChild(model.getRoot(), i);
            //Nombre de la función
            String name = child.toString().split(":")[0];
            String typeOfFuction = "";
            if (child.toString().equals("MAIN")) {
                //En el main devuelve nll->nll ya que éste no recibe ni devuelve
                name = "main";
                typeOfFuction = "nll -> nll";
            } else {
                //Obtiene el tipo de la función en base a los parámetros
                typeOfFuction = getParametersType(model, child);
                typeOfFuction += "-> " + child.toString().split(":")[1];
            }
            //Crea una nueva subtabla y la agrega al Hashmap de las subtablas
            //El key de búsqyeda es "nombreFuncion"
            TypesSubTable x = new TypesSubTable(name, typeOfFuction, model, child, null);
            //x.getDeclarations(model, child, x);
            subtables.put(name, x);
        }
    }

    //Devuelve la primerfa parte del tipo de una función (el de los parámetros)
    private String getParametersType(DefaultTreeModel model, Object c) {
        String p = "";
        int ccc = model.getChildCount(c);
        for (int i = 0; i < ccc; i++) {
            Object child = model.getChild(c, i);
            if ((child.toString()).equals("PARAMETERS")) {
                int cccc = model.getChildCount(child);
                if (cccc == 0) {
                    return "nll";
                }
                for (int j = 0; j < cccc; j++) {
                    Object ch = model.getChild(child, j);
                    if (j < cccc - 1) {
                        p += (ch.toString().split(" "))[0] + " x ";
                    } else {
                        p += (ch.toString().split(" "))[0] + " ";
                    }

                }
            }
        }
        return p;
    }

    //Agregación de tipos a cada una de las subtablas
    /*private void analizeTypes(DefaultTreeModel model) {
        for (String i : subtables.keySet()) {
            getDeclarations(model, subtables.get(i).treepart, subtables.get(i));
            System.out.println(subtables.get(i).toString());
        }
    }

    //Agregar Declaraciones a las subtabla y creacion de más subtablas
    //Para la creación del ámbito
    private void getDeclarations(DefaultTreeModel model, Object o, TypesSubTable t) {
        int m[][] = {{1, 2}, {1}};
        int cc = model.getChildCount(o);
        //Al ser estas declaraciones sin valores, no necesita ver si el valor 
        //es del mismo tipo de la variable
        if (o.toString().equals("DECLR NEST/NO_VAL")) {
            TableRow row = new TableRow(model.getChild(o, 1).toString(), model.getChild(o, 0).toString(), "nll");
            addtoSubTable(row, t);
            int ccc = model.getChildCount(o);
            if (ccc > 2) {
                for (int j = 2; j < ccc; j++) {
                    row = new TableRow(model.getChild(o, j).toString(), model.getChild(o, 0).toString(), "nll");
                    addtoSubTable(row, t);
                }
            }
            return;
        }

        //Declaraciones con Valor (Y análisis de Tipo)
        if (o.toString().equals("DECLR")) {
            String id = model.getChild(o, 1).toString();
            String type = model.getChild(o, 0).toString();
            String val = model.getChild(o, 2).toString();
            TableRow row = null;
            //Ver si es bool y su valor es false o true
            if (type.equals("bln") && (val.equals("false") || val.equals("true"))) {
                row = new TableRow(id, type, val);
                addtoSubTable(row, t);
            } else if (type.equals("chr")) {
                row = new TableRow(id, type, val.charAt(1));
                addtoSubTable(row, t);
            } else if (type.equals("int")) {
                if (model.getChild(o, 2).toString().equals("+") || model.getChild(o, 2).toString().equals("-")) {
                    val += model.getChild(model.getChild(o, 2), 0);
                }
                if (isNumeric(val)) {
                    row = new TableRow(id, type, Integer.parseInt(val));
                    addtoSubTable(row, t);
                } else {
                    //Error de Tipo
                    System.out.println("Error de Tipo. El tipo de la variable: " + id + " y el valor: " + val + "son incompatibles.");
                }
            } else {
                //Error de Tipo
                System.out.println("Error de Tipo. El tipo de la variable: " + id + " y el valor: " + val + "son incompatibles.");
            }
            return;
        }

        for (int i = 0; i < cc; i++) {
            Object child = model.getChild(o, i);
            System.out.println(">>>" + child.toString());
            if (!model.isLeaf(child)) {
                getDeclarations(model, child, t);
            }
        }
    }

    private void addtoSubTable(TableRow row, TypesSubTable t) {
        if (!t.addIDToSubTable(row)) {
            //throw new ErrorDeTipo("La variable " + model.getChild(o, j).toString() + " ya fue declarada antes. Linea: " + "linea");
            System.out.println("La variable " + row.id + " ya fue declarada antes.");
            errors.add("La variable " + row.id + " ya fue declarada antes. Linea: " + "linea");
        }
    }

    //Solo para ver si un String es numérico y al traer el +- lo convierte a menos o más
    //Dependiendo del signo
    private static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }*/
}
