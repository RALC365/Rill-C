/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComprobacionDeTipos;

import java.util.HashMap;

/**
 *
 * @author Usuario
 */
public class TypesSubTable {

    public final Object treepart;
    private final String functionName;
    private final String functionType;
    private final HashMap<String, TableRow> ids = new HashMap();
    private TypesSubTable parent;
    private final HashMap<String, TypesSubTable> children = new HashMap();

    public TypesSubTable(String functionName, String functionType, Object treepart) {
        this.functionName = functionName;
        this.functionType = functionType;
        this.treepart = treepart;
    }

    public boolean addIDToSubTable(TableRow t) {
        if (!ids.containsKey(t.id)) {
            ids.put(t.id, t);
            return true;
        }
        return false;
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
