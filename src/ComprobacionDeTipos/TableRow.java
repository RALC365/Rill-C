/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComprobacionDeTipos;

import java.util.Arrays;

/**
 *
 * @author Usuario
 */
class TableRow {

    public final String id;
    public final String type;
    //private Object value;

    public TableRow(String id, String type/*, Object value*/) {
        this.id = id;
        //this.value = value;
        this.type = type;
    }

    /*public Object getValue() {
        return value;
    }*/

    /*public void setValue(Object value) {
        this.value = value;
    } */   

    @Override
    public String toString() {
        /*if ((this.value != null)&&(this.value.getClass().isArray())) {
            if(this.value instanceof String[]) {
                String[] objects = (String[]) this.value;
                return "id: " + id + ", type: (0.."+ (objects.length-1) + ", " + type + "), value: " + Arrays.toString(objects);
            }
            if(this.value instanceof String[][]) {
                String[][] objects = (String[][]) this.value;
                return "id: " + id + ", type: (0.."+ (objects.length-1) + ", (0.." + (objects[0].length-1) + ", " + type + ")), value: " + Arrays.deepToString(objects);
            }
        }*/
        return "id: " + id + ", type: " + type /* + ", value: " + value*/;
    }

}
