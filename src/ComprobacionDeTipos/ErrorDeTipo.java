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
public class ErrorDeTipo extends Exception {

    String id;

    public ErrorDeTipo(String x) {
        id = x;
    }

    public String toString() {
        return "Error de Tipos [" + id + "]";
    }
}
