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
    private final int offset;

    public TableRow(String id, String type, int offset) {
        this.id = id;
        this.type = type;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "id: " + id + ", type: " + type + ", offset: " + offset;
    }

}
