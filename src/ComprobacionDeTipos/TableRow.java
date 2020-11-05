/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComprobacionDeTipos;

/**
 *
 * @author Usuario
 */
class TableRow {

    public final String id;
    public final String type;
    public final Object value;

    public TableRow(String id, String type, Object value) {
        this.id = id;
        this.value = value;
        this.type = type;
    }

    @Override
    public String toString() {
        return "id: " + id + ", type: " + type + ", value: " + value;
    }

}
