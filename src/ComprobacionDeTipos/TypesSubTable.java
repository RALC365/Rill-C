/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComprobacionDeTipos;

import java.util.HashMap;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Usuario
 */
public class TypesSubTable {

    private final Object treepart;
    private final DefaultTreeModel model;
    private final String functionName;
    private final String functionType;
    private final HashMap<String, TableRow> ids = new HashMap();
    private TypesSubTable parent;
    private final HashMap<String, TypesSubTable> children = new HashMap();

    public TypesSubTable(String functionName, String functionType, DefaultTreeModel model, Object treepart) {
        this.functionName = functionName;
        this.functionType = functionType;
        this.treepart = treepart;
        this.model = model;
        getDeclarations(model, treepart, this);
        System.out.println(this.toString());
    }

    public boolean addIDToSubTable(TableRow t) {
        if (!ids.containsKey(t.id)) {
            ids.put(t.id, t);
            return true;
        }
        return false;
    }

    //Agregar Declaraciones a las subtabla y creacion de más subtablas
    //Para la creación del ámbito
    private void getDeclarations(DefaultTreeModel model, Object a, TypesSubTable t) {
        int cc = model.getChildCount(a);
        for (int i = 0; i < cc; i++) {
            Object o = model.getChild(a, i);
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
            }

            if (o.toString().equals("FOR")) {
                TypesSubTable for_t = new TypesSubTable("FOR", "nll", model, o);
                for_t.parent = this;
                this.children.put("FOR", for_t);
            }
            
            if (o.toString().equals("WHILE")) {
                TypesSubTable for_t = new TypesSubTable("WHILE", "nll", model, o);
                for_t.parent = this;
                this.children.put("FOR", for_t);
            }
        }
    }

    private void addtoSubTable(TableRow row, TypesSubTable t) {
        if (!t.addIDToSubTable(row)) {
            //throw new ErrorDeTipo("La variable " + model.getChild(o, j).toString() + " ya fue declarada antes. Linea: " + "linea");
            System.out.println("La variable " + row.id + " ya fue declarada antes.");
            //errors.add("La variable " + row.id + " ya fue declarada antes. Linea: " + "linea");
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
    }

    public void getParentIds() {
        if (parent != null) {
            for (String i : parent.ids.keySet()) {
                ids.put(i, parent.ids.get(i));
            }
        }
    }

    @Override
    public String toString() {
        String t = "function: " + functionName + "\n";
        for (String i : ids.keySet()) {
            t += ids.get(i).toString() + "\n";
        }
        return t;
    }

}
