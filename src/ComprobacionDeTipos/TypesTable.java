/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComprobacionDeTipos;

import java.util.ArrayList;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Will
 */
public class TypesTable {

    //private final HashMap<String, TypesSubTable> subtables = new HashMap();
    public ArrayList<String> errors = new ArrayList();
    private TypesSubTable root = new TypesSubTable("Program", "nill", null, null, null, 0);
    public DefaultTreeModel model;

    public TypesTable(DefaultTreeModel model) {
        this.model = model;
        createSubtables(model);
        errors = root.errors;
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
            int offset = 0;
            if (child.toString().equals("MAIN")) {
                //En el main devuelve nll->nll ya que éste no recibe ni devuelve
                name = "main";
                typeOfFuction = "nll -> nll";
                offset = 0;
            } else if (child.toString().equals("VARIABLES_GLOBALES")) {
                root = new TypesSubTable("Program", "nill", model, child, null, 0);
                continue;
            } else {
                offset=12;
                //Obtiene el tipo de la función en base a los parámetros
                typeOfFuction = getParametersType(model, child);
                typeOfFuction += " -> " + child.toString().split(":")[1];
            }
            //Crea una nueva subtabla y la agrega al Hashmap de las subtablas
            //El key de búsqyeda es "nombreFuncion"
            TypesSubTable x = new TypesSubTable(name, typeOfFuction, model, child, root, offset);
            root.children.put(name, x);
        }
        //System.out.println("--------------------------");
        //Verificamos las variables globales
        if (root.treepart != null) {
            root.getDeclarations(root.treepart);
            root.errors.addAll(root.errors);
        }

        //Verificamos las declaraciones de los funciones del programa
        for (String i : root.children.keySet()) {
            TypesSubTable child = root.children.get(i);
            child.getDeclarations(child.treepart);
            root.errors.addAll(child.errors);
        }
    }

    //Devuelve la primerfa parte del tipo de una función (el de los parámetros)
    private String getParametersType(DefaultTreeModel model, Object c) {
        String p = "";
        int ccc = model.getChildCount(c);
        if (ccc == 0) {
            p = "nll";
        } else {
            for (int i = 0; i < ccc; i++) {
                Object child = model.getChild(c, i);
                if ((child.toString()).equals("PARAMETERS")) {
                    int cccc = model.getChildCount(child);
                    if (cccc == 0) {
                        return "nll";
                    }
                    for (int j = 0; j < cccc; j++) {
                        Object ch = model.getChild(child, j);
                        String type = "";
                        if (ch.toString().split(" ").length > 2) {
                            type = (ch.toString().split(" "))[0] + " " + (ch.toString().split(" "))[1];
                        } else {
                            type = (ch.toString().split(" "))[0];
                        }
                        if (j < cccc - 1) {
                            p += type + " x ";
                        } else {
                            p += type;
                        }
                    }
                }
            }
        }
        return p;
    }

    public TypesSubTable getRoot() {
        return root;
    }

}
