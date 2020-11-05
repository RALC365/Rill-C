/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import AST.SwingDemo;
import CUP.*;
import ComprobacionDeTipos.TypesTable;
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
    NumeroLinea numeroLinea;

    public FrmPrincipal() {
        initComponents();
        this.numeroLinea = new NumeroLinea(this.txtCodigo);
        this.jScrollCodigo.setRowHeaderView(this.numeroLinea);
        this.setLocationRelativeTo(null);
        this.setExtendedState(this.MAXIMIZED_BOTH);

        //this.jScrollCodigo.setColumnHeaderView(numeroLinea);
    }

    private void analizarLexico() throws IOException {
        int cont = 1;

        String expr = (String) txtCodigo.getText();
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
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAnalizarLex = new javax.swing.JTextArea();
        btnAnalizarLex = new javax.swing.JButton();
        btnTree = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAnalizarSin = new javax.swing.JTextArea();
        btnAnalizarSin = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnLimpiarSin1 = new javax.swing.JButton();
        btnLimpiarLex1 = new javax.swing.JButton();
        jScrollCodigo = new javax.swing.JScrollPane();
        txtCodigo = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rill-20");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RILL-20", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtAnalizarLex.setEditable(false);
        txtAnalizarLex.setColumns(20);
        txtAnalizarLex.setRows(5);
        jScrollPane2.setViewportView(txtAnalizarLex);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 110, 270, 240));

        btnAnalizarLex.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnAnalizarLex.setText("Analizar");
        btnAnalizarLex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarLexActionPerformed(evt);
            }
        });
        jPanel1.add(btnAnalizarLex, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 70, -1, -1));

        btnTree.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnTree.setText("Árbol");
        btnTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTreeActionPerformed(evt);
            }
        });
        jPanel1.add(btnTree, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 70, -1, -1));

        txtAnalizarSin.setEditable(false);
        txtAnalizarSin.setColumns(20);
        txtAnalizarSin.setRows(5);
        jScrollPane3.setViewportView(txtAnalizarSin);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(927, 107, 370, 450));

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
        jPanel1.add(btnAnalizarSin, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 70, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 360, 280, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Análisis Síntáctico");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 30, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Análisis Léxico");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 30, -1, -1));

        btnLimpiarSin1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnLimpiarSin1.setText("Limpiar");
        btnLimpiarSin1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarSin1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnLimpiarSin1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 70, -1, -1));

        btnLimpiarLex1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnLimpiarLex1.setText("Limpiar");
        btnLimpiarLex1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarLex1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnLimpiarLex1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 70, -1, -1));

        txtCodigo.setColumns(20);
        txtCodigo.setRows(5);
        jScrollCodigo.setViewportView(txtCodigo);

        jPanel1.add(jScrollCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 590, 510));

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
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.getAccessibleContext().setAccessibleName("Rill-20");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnalizarSinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarSinActionPerformed
        // TODO add your handling code here:
        //Only Lexer
        try {
            analizarLexico();
        } catch (IOException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Cup and Lexer
        //String ST = txtResultado.getText();
        String ST = txtCodigo.getText();
        s = new Sintax(new CUP.LexerCup(new StringReader(ST)));

        try {
            //jtSintactico.setModel(s.createTreeSintax("SintaxTree"));
            s.createTreeSintax("Program");
            s.parse();
            if (s.getERRORES().equalsIgnoreCase("")) {
                /*txtAnalizarSin.setText("Analisis realizado correctamente");
                txtAnalizarSin.setForeground(new Color(25, 111, 61));*/
                txtAnalizarSin.setText("Se completó el análisis sin errores");
                txtAnalizarSin.setForeground(new Color(25, 111, 61));

                System.out.println("Se completo el análisis sintáctico sin errores");

            } else {
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
        try {

            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            File archivo = new File(chooser.getSelectedFile().getAbsolutePath());

            try {
                String ST = new String(Files.readAllBytes(archivo.toPath()));
                //txtResultado.setText(ST);
                txtCodigo.setText(ST);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            System.out.println("No abrio el archivo xD");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        guardarComo();
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void btnTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTreeActionPerformed
        // TODO add your handling code here:
        if ((s != null)) {
            if (s.getcERRORES() == 0) {
                String ST = txtCodigo.getText();
                ASTree = new ASintaxT(new CUP.LexerCup(new StringReader(ST)));

                try {
                    //jtSintactico.setModel(s.createTreeSintax("SintaxTree"));
                    ASTree.createTreeSintax("Program");
                    ASTree.parse();
                } catch (Exception ex) {
                    Symbol sym = ASTree.getS();
                    txtAnalizarSin.setText("Ups! Algo inesperado sucecido. No se logró generar el árbol");
                    txtAnalizarSin.setForeground(Color.red);
                }
                SwingDemo sintaxTree = new SwingDemo(ASTree.getTreeSintaxModel());
                sintaxTree.showTree();
                TypesTable tt = new TypesTable(ASTree.getTreeSintaxModel());
                s = null;
            } else {
                JOptionPane.showMessageDialog(this, "Se encontraron errores en el análsis sintáctico."
                        + "\nAún no se puede generar el árbol", "RILL-20", 0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Aún no ha realizdo en análisis Sintáctico", "RILL-20", 0);
        }
    }//GEN-LAST:event_btnTreeActionPerformed

    private void btnAnalizarLexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarLexActionPerformed
        try {
            analizarLexico();
        } catch (IOException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAnalizarLexActionPerformed

    private void btnLimpiarSin1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarSin1ActionPerformed
        // TODO add your handling code here:
        this.txtAnalizarSin.setText("");
    }//GEN-LAST:event_btnLimpiarSin1ActionPerformed

    private void btnLimpiarLex1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarLex1ActionPerformed
        // TODO add your handling code here:
        this.txtAnalizarLex.setText("");
    }//GEN-LAST:event_btnLimpiarLex1ActionPerformed

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

    public void guardarComo() {

        JFileChooser guardar = new JFileChooser();
        guardar.showSaveDialog(null);
        guardar.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        File archivo = guardar.getSelectedFile();

        //guardarFichero(txtResultado.getText(), archivo);
        guardarFichero(txtCodigo.getText(), archivo);

    }

    public void guardarFichero(String cadena, File archivo) {

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
    Sintax s;
    ASintaxT ASTree;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnalizarLex;
    private javax.swing.JButton btnAnalizarSin;
    private javax.swing.JButton btnLimpiarLex1;
    private javax.swing.JButton btnLimpiarSin1;
    private javax.swing.JButton btnTree;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollCodigo;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea txtAnalizarLex;
    private javax.swing.JTextArea txtAnalizarSin;
    private javax.swing.JTextArea txtCodigo;
    // End of variables declaration//GEN-END:variables
}
