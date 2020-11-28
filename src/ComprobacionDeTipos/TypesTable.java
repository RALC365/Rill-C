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
 * @author Usuario
 */
public class TypesTable {

    //private final HashMap<String, TypesSubTable> subtables = new HashMap();
    private final ArrayList<String> errors = new ArrayList();
    public TypesSubTable root = new TypesSubTable("Program", "nill", null, null, null);

    public TypesTable(DefaultTreeModel model) throws TypeErrorException {
        createSubtables(model);
        //analizeTypes(model);
    }

    //Crea las subTablaas de Tipos en Base a las funciones dentro del programa
    //Un tabla por Función, de esta manera se facilita la definición del ámbito
    private void createSubtables(DefaultTreeModel model) throws TypeErrorException {
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
                typeOfFuction += " -> " + child.toString().split(":")[1];
            }
            //Crea una nueva subtabla y la agrega al Hashmap de las subtablas
            //El key de búsqyeda es "nombreFuncion"
            //System.out.println(name + " : " + typeOfFuction);
            TypesSubTable x = new TypesSubTable(name, typeOfFuction, model, child, root);
            root.children.put(name, x);
            //x.getDeclarations(model, child, x);
            //subtables.put(name, x);
        }
        //System.out.println("--------------------------");
        for (String i : root.children.keySet()) {
            TypesSubTable child = root.children.get(i);
            child.getDeclarations(child.treepart);
            //System.out.println(child.toString());
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
                        if (j < cccc - 1) {
                            p += (ch.toString().split(" "))[0] + " x ";
                        } else {
                            p += (ch.toString().split(" "))[0];
                        }

                    }
                }
            }
        }
        return p;
    }
}
