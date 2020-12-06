/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreeAddressCode;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Julio Marin
 */
public class SwingTable {
    
    private ArrayList<Cuadruplos> tablaCuadruplos;
    
    public SwingTable(ArrayList<Cuadruplos> tablaCuadruplos) {
        this.tablaCuadruplos = tablaCuadruplos;
    }

    public void showInDialog() {
        
        String col[] = {"Operador","Parametro Izq","Parametro Der","Resultado"};
        
        DefaultTableModel modelo = new DefaultTableModel(col, 0);
        
        String operacion;
        String parametroA;
        String parametroB;
        String resultado;
        for(Cuadruplos cadaCuadruplo: tablaCuadruplos) {
            operacion = cadaCuadruplo.getOperacion().toString();
            parametroA = cadaCuadruplo.getParametroA();
            parametroB = cadaCuadruplo.getParametroB();
            resultado = cadaCuadruplo.getResultado();
            Object[] data = {operacion, parametroA, parametroB, resultado};
            modelo.addRow(data);
        }
        
        JTable table = new JTable(modelo);
        
        JDialog dialog = new JDialog();
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.setBounds(100, 100, 1300, 700);
        dialog.getContentPane().setLayout(new BorderLayout(0, 0));
        dialog.getContentPane().add(scrollPane, BorderLayout.CENTER);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        //dialog.setModal(true);
        //full screen
        dialog.setLocationRelativeTo(dialog);
        dialog.setPreferredSize(new Dimension(JFrame.MAXIMIZED_HORIZ, JFrame.MAXIMIZED_VERT));
        dialog.setVisible(true);
    }
    
}
