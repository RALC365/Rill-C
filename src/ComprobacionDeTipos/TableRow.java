/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComprobacionDeTipos;

/**
 *
 * @author Will
 */
public class TableRow {

    public final String id;
    public final String type;
    public final int offset;
    public String ubicacion;


    public TableRow(String id, String type, int offset) {
        this.id = id;
        this.type = type;
        this.offset = offset;
        if(offset!=0)
            this.ubicacion = "-" + offset + "($sp)";
        else
            this.ubicacion =  offset + "($sp)";
    }

    @Override
    public String toString() {
        return "id: " + id + ", type: " + type + ", offset: " + offset+", ubicación: "+ubicacion;
    }

}
