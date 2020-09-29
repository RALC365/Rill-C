/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import AST.SwingDemo;
import CUP.*;
import LexerS.Lexer;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author WillRomero and RALC365
 */
public class FrmPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form FrmPrincipal
     */
    public FrmPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setExtendedState(this.MAXIMIZED_BOTH); 
    }
    
    private void analizarLexico() throws IOException{
        int cont = 1;
        
        String expr = (String) txtResultado.getText();
        Lexer lexer = new Lexer(new StringReader(expr));
        String resultado = "LINEA " + cont + "\t\tSIMBOLO\n";
        while (true) {
            Tokens token = lexer.yylex();
            if (token == null) {
                txtAnalizarLex.setText(resultado);
                return;
            }
            switch (token) {
                case LINEA:
                    cont++;
                    resultado += "\nLINEA " + cont + "\n";
                    break;
                case CLOSE_BLOCK:
                    resultado += "  <CLOSE_BLOCK>\t\t" + lexer.lexeme + "\n";
                    break;
                case OPEN_PARENTESIS:
                    resultado += "  <OPEN_PARENTESIS>\t\t" + lexer.lexeme + "\n";
                    break;
                case CLOSE_PARENTESIS:
                    resultado += "  <CLOSE_PARENTESIS>\t\t" + lexer.lexeme + "\n";
                    break;
                case PYC:
                    resultado += "  <PYC>\t\t" + lexer.lexeme + "\n";
                    break;
                case BETWEEN:
                    resultado += "  <BETWEEN>\t\t" + lexer.lexeme + "\n";
                    break;
                case OPEN_SQR_BRACKET:
                    resultado += "  <OPEN_SQR_BRACKET>\t\t" + lexer.lexeme + "\n";
                    break;
                case CLOSE_SQR_BRACKET:
                    resultado += "  <CLOSE_SQR_BRACKET>\t\t" + lexer.lexeme + "\n";
                    break;
                case COMA:
                    resultado += "  <COMA>\t\t" + lexer.lexeme + "\n";
                    break;
                case COLON:
                    resultado += "  <COLON>\t\t" + lexer.lexeme + "\n";
                    break;
                case BACK_SLASH:
                    resultado += "  <BACK_SLASH>\t\t" + lexer.lexeme + "\n";
                    break;
                case NEW_LINE:
                    resultado += "  <NEW_LINE>\t\t" + lexer.lexeme + "\n";
                    break;
                case NULL:
                    resultado += "  <NULL>\t\t" + lexer.lexeme + "\n";
                    break;
                case TRUE:
                    resultado += "  <TRUE>\t\t" + lexer.lexeme + "\n";
                    break;
                case FALSE:
                    resultado += "  <FALSE>\t\t" + lexer.lexeme + "\n";
                    break;
                case STRING:
                    resultado += "  <STRING>\t\t" + lexer.lexeme + "\n";
                    break;
                case CHAR_ELEMENT:
                    resultado += "  <CHAR_ELEMENT>\t\t" + lexer.lexeme + "\n";
                    break;
                    /* control statements */
                case FUNCTION:
                    resultado += "  <FUNCTION>\t\t" + lexer.lexeme + "\n";
                    break;
                case IF_SWITCH:
                    resultado += "  <IF_SWITCH>\t\t" + lexer.lexeme + "\n";
                    break;
                case WHILE:
                    resultado += "  <WHILE>\t\t" + lexer.lexeme + "\n";
                    break;
                case FOR:
                    resultado += "  <FOR>\t\t" + lexer.lexeme + "\n";
                    break;
                case ELSE:
                    resultado += "  <ELSE>\t\t" + lexer.lexeme + "\n";
                    break;
                case RETURN:
                    resultado += "  <RETURN>\t\t" + lexer.lexeme + "\n";
                    break;
                case CIN:
                    resultado += "  <CIN>\t\t" + lexer.lexeme + "\n";
                    break;
                    
                case TO:
                    resultado += "  <TO>\t\t" + lexer.lexeme + "\n";
                    break;
                case MAIN:
                    resultado += "  <MAIN>\t\t" + lexer.lexeme + "\n";
                    break;
                    /* operators */
                case OP_REL:
                    resultado += "  <OP_REL>\t\t" + lexer.lexeme + "\n";
                    break;
                case OP_SUM:
                    resultado += "  <OP_SUM>\t\t" + lexer.lexeme + "\n";
                    break;
                case OP_MULTI_DIV:
                    resultado += "  <OP_MULTI_DIV>\t\t" + lexer.lexeme + "\n";
                    break;
                 
                case MOD:
                    resultado += "  <MOD>\t\t" + lexer.lexeme + "\n";
                    break;
                case INCREMENT:
                    resultado += "  <INCREMENT>\t\t" + lexer.lexeme + "\n";
                    break;
                case CON_AND:
                    resultado += "  <CON_AND>\t\t" + lexer.lexeme + "\n";
                    break;
                case CON_OR:
                    resultado += "  <CON_OR>\t\t" + lexer.lexeme + "\n";
                    break;
                    /* data types */
                case INTENGER:
                    resultado += "  <INTENGER>\t\t" + lexer.lexeme + "\n";
                    break;
                    
                case BOOLEAN:
                    resultado += "  <BOOLEAN>\t\t" + lexer.lexeme + "\n";
                    break;
                case CHAR:
                    resultado += "  <CHAR>\t\t" + lexer.lexeme + "\n";
                    break;
                case ARRAY:
                    resultado += "  <ARRAY>\t\t" + lexer.lexeme + "\n";
                    break;
                case MATRIX:
                    resultado += "  <MATRIX>\t\t" + lexer.lexeme + "\n";
                    break;
                case VARIABLE:
                    resultado += "  <VARIABLE>\t\t" + lexer.lexeme + "\n";
                    break;
                    /* identifiers */
                case ID:
                    resultado += "  <ID>\t\t" + lexer.lexeme + "\n";
                    break;
                case NUMBER:
                    resultado += "  <NUMBER>\t\t" + lexer.lexeme + "\n";
                    break;        
                case ERROR:
                    resultado += "  <Simbolo no definido>\t\t" + lexer.lexeme + "\n";
                    break;
                
                default:
                    resultado += "  < " + lexer.lexeme + " >\n";
                    break;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtResultado = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAnalizarLex = new javax.swing.JTextArea();
        btnAnalizarLex = new javax.swing.JButton();
        btnLimpiarLex = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAnalizarSin = new javax.swing.JTextArea();
        btnLimpiarSin = new javax.swing.JButton();
        btnAnalizarSin = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rill-20");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Analizador Lexico", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtResultado.setBackground(new java.awt.Color(0, 0, 0));
        txtResultado.setColumns(20);
        txtResultado.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtResultado.setForeground(new java.awt.Color(255, 255, 255));
        txtResultado.setRows(5);
        jScrollPane1.setViewportView(txtResultado);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 644, 520));

        txtAnalizarLex.setEditable(false);
        txtAnalizarLex.setColumns(20);
        txtAnalizarLex.setRows(5);
        jScrollPane2.setViewportView(txtAnalizarLex);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(684, 106, 270, 240));

        btnAnalizarLex.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnAnalizarLex.setText("Analizar");
        btnAnalizarLex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarLexActionPerformed(evt);
            }
        });
        jPanel1.add(btnAnalizarLex, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 70, -1, -1));

        btnLimpiarLex.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnLimpiarLex.setText("Limpiar");
        btnLimpiarLex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarLexActionPerformed(evt);
            }
        });
        jPanel1.add(btnLimpiarLex, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 70, -1, -1));

        txtAnalizarSin.setEditable(false);
        txtAnalizarSin.setColumns(20);
        txtAnalizarSin.setRows(5);
        jScrollPane3.setViewportView(txtAnalizarSin);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 107, 317, 450));

        btnLimpiarSin.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnLimpiarSin.setText("Limpiar");
        btnLimpiarSin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarSinActionPerformed(evt);
            }
        });
        jPanel1.add(btnLimpiarSin, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 70, -1, -1));

        btnAnalizarSin.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnAnalizarSin.setText("Analizar");
        btnAnalizarSin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAnalizarSinMouseClicked(evt);
            }
        });
        btnAnalizarSin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarSinActionPerformed(evt);
            }
        });
        jPanel1.add(btnAnalizarSin, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 70, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 360, 280, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Análisis Síntáctico");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 30, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Análisis Léxico");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 30, -1, -1));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jMenu1.setText("Archivo");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Abrir Archivo");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Guardar Archivo");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel1.getAccessibleContext().setAccessibleName("Rill-20");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarSinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarSinActionPerformed
        // TODO add your handling code here:
        txtAnalizarSin.setText(null);
    }//GEN-LAST:event_btnLimpiarSinActionPerformed

    private void btnAnalizarSinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarSinActionPerformed
        // TODO add your handling code here:
        //Only Lexer
        try {
            analizarLexico();
        } catch (IOException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Cup and Lexer
        String ST = txtResultado.getText();
        Sintax s = new Sintax(new CUP.LexerCup(new StringReader(ST)));
        
        try {
            //jtSintactico.setModel(s.createTreeSintax("SintaxTree"));
            s.createTreeSintax("Program");
            s.parse();
            /*for (int i = 0; i < s.reduce_table().length; i++) {
                for (int j = 0; j < s.reduce_table()[i].length; j++) {
                    System.out.print(" "+ s.reduce_table()[i][j]+ " ,");
                }
                System.out.println("");
            }*/
            if(s.getERRORES().equalsIgnoreCase("")){
                /*txtAnalizarSin.setText("Analisis realizado correctamente");
                txtAnalizarSin.setForeground(new Color(25, 111, 61));*/
                    txtAnalizarSin.setText("Se completó el análisis sin errores");
                    txtAnalizarSin.setForeground(new Color(25, 111, 61));
                    SwingDemo sintaxTree = new SwingDemo(s.getTreeSintaxModel());
                    sintaxTree.showTree();
                System.out.println("Se completo el análisis sintáctico sin errores");
                
            }else{
                txtAnalizarSin.setText("Cantidad de Errores: " + s.getcERRORES() + "\n" + s.getERRORES());
                txtAnalizarSin.setForeground(Color.red);
                System.out.println("Se completo el análisis sintáctico con errores");
            }
            
        } catch (Exception ex) {
            System.out.println("Entro al exeption que pinta en rojo");
            Symbol sym = s.getS();
            txtAnalizarSin.setText("Error de sintaxis. Linea: " + (sym.right + 1) + " Columna: " + (sym.left + 1) + ", Texto: \"" + sym.value + "\"");
            txtAnalizarSin.setForeground(Color.red);
        }
        //Setearear errores
        s.setERRORES("");
    }//GEN-LAST:event_btnAnalizarSinActionPerformed

    private void btnAnalizarSinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnalizarSinMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAnalizarSinMouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File archivo = new File(chooser.getSelectedFile().getAbsolutePath());
        
        try {
            String ST = new String(Files.readAllBytes(archivo.toPath()));
            txtResultado.setText(ST);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        guardarComo();
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void btnLimpiarLexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarLexActionPerformed
        // TODO add your handling code here:
        txtAnalizarLex.setText(null);
    }//GEN-LAST:event_btnLimpiarLexActionPerformed

    private void btnAnalizarLexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarLexActionPerformed
        try {
            analizarLexico();
        } catch (IOException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAnalizarLexActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPrincipal().setVisible(true);
            }
        });
    }
    
    public void guardarComo(){

    JFileChooser guardar = new JFileChooser();
    guardar.showSaveDialog(null);
    guardar.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

    File archivo = guardar.getSelectedFile();

    guardarFichero(txtResultado.getText(), archivo);

}
    
    public void guardarFichero(String cadena, File archivo){

    FileWriter escribir;
    try {

        escribir = new FileWriter(archivo, true);
        escribir.write(cadena);
        escribir.close();

    } catch (FileNotFoundException ex) {
        JOptionPane.showMessageDialog(null, "Error al guardar, ponga nombre al archivo");
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(null, "Error al guardar, en la salida");
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnalizarLex;
    private javax.swing.JButton btnAnalizarSin;
    private javax.swing.JButton btnLimpiarLex;
    private javax.swing.JButton btnLimpiarSin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea txtAnalizarLex;
    private javax.swing.JTextArea txtAnalizarSin;
    private javax.swing.JTextArea txtResultado;
    // End of variables declaration//GEN-END:variables
}
