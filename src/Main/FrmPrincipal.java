/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import AST.SwingDemo;
import CUP.*;
import ComprobacionDeTipos.TypeErrorException;
import ComprobacionDeTipos.TypesSubTable;
import ComprobacionDeTipos.TypesTable;
import LexerS.Lexer;
import ThreeAddressCode.SwingTable;
import ThreeAddressCode.ThreeAddressTable;
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
import javax.swing.JDialog;
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
    TypesTable tt;
    ThreeAddressTable tablaCuadruplos;

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
        bandera = true;
        String expr = (String) txtCodigo.getText();
        Lexer lexer = new Lexer(new StringReader(expr));
        String resultado = "LINEA " + cont + "\t\tSIMBOLO\n";
        while (true) {
            Tokens token = lexer.yylex();
            if (token == null) {
                console_txt.setText(resultado);
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
                    bandera = false;
                    break;

                default:
                    resultado += "  < " + lexer.lexeme + " >\n";
                    break;
            }
        }
    }

    private void analizarSintactico() throws Exception {
        if (bandera) {

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
//            s = new Sintax(new CUP.LexerCup(new StringReader(ST)));
//            try {
//                //jtSintactico.setModel(s.createTreeSintax("SintaxTree"));
//                s.createTreeSintax("Program");
//                s.parse();
//                if (s.getERRORES().equalsIgnoreCase("")) {
//                    /*txtAnalizarSin.setText("Analisis realizado correctamente");
//                txtAnalizarSin.setForeground(new Color(25, 111, 61));*/
//                    console_txt.setForeground(Color.green);
//                    console_txt.setText("Se completó el análisis Sintáctico sin errores");
//                } else {
//                    console_txt.setForeground(Color.red);
//                    console_txt.setText("Cantidad de Errores: " + s.getcERRORES() + "\n" + s.getERRORES());
//                    bandera = false;
//                }
//            } catch (Exception ex) {
//                Symbol sym = s.getS();
//                console_txt.setForeground(Color.red);
//                console_txt.setText("Error de sintaxis. Linea: " + (sym.right + 1) + " Columna: " + (sym.left + 1) + ", Texto: \"" + sym.value + "\"");
//                bandera = false;
//            }
//            //Setearear errores
//            s.setERRORES("");

            ASTree = new ASintaxT(new CUP.LexerCup(new StringReader(ST)));

            try {
                //jtSintactico.setModel(s.createTreeSintax("SintaxTree"));
                ASTree.createTreeSintax("Program");
                ASTree.parse();
            } catch (Exception ex) {
                Symbol sym = ASTree.getS();
                console_txt.setText("Ups! Algo inesperado sucecido. No se logró generar el árbol");
                console_txt.setForeground(Color.red);
            }
            s = null;
        }
    }

    private void showTree() {
        SwingDemo sintaxTree = new SwingDemo(ASTree.getTreeSintaxModel());
        sintaxTree.showTree();
    }

    private void ComprobacionTipos() throws TypeErrorException {
        if (bandera) {
            tt = new TypesTable(ASTree.getTreeSintaxModel());
            if (tt.errors.isEmpty()) {
                console_txt.setForeground(Color.green);
                console_txt.setText("Analisis Léxico, Sintáctico y de Tipo Completos y Sin errores.");
            } else {
                bandera = false;
                String errors = "";
                for (String i : tt.errors) {
                    errors += i + "\n";
                }
                console_txt.setForeground(Color.red);
                console_txt.setText(errors);
            }
        }
    }

    private void DesplegarCuadruplos() {
        try {
            tablaCuadruplos = new ThreeAddressTable(ASTree.getTreeSintaxModel());
            SwingTable cuadruplosGraficos = new SwingTable(tablaCuadruplos.getTablaCuadruplos());
            cuadruplosGraficos.showInDialog();
            //console_txt.setForeground(Color.green);
        } catch (TypeErrorException e) {
            //console_txt.setText(e.getMessage());
            //console_txt.setForeground(Color.red);
        }
    }

    private void VerTablasDeTipo(TypesSubTable t) {
        console_txt.setText(console_txt.getText() + "   " + t + "\n");
        if (!t.children.isEmpty()) {
            t.children.keySet().forEach((i) -> {
                VerTablasDeTipo(t.children.get(i));
            });
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

        jdConsole = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        console_txt = new javax.swing.JTextPane();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenuSee = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollCodigo = new javax.swing.JScrollPane();
        txtCodigo = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();

        jdConsole.setTitle("Rill-20 Console ");
        jdConsole.setBackground(new java.awt.Color(0, 0, 0));
        jdConsole.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jdConsole.setMinimumSize(new java.awt.Dimension(600, 600));
        jdConsole.setSize(new java.awt.Dimension(600, 600));
        jdConsole.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        console_txt.setBackground(new java.awt.Color(0, 0, 0));
        console_txt.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        console_txt.setForeground(new java.awt.Color(255, 255, 255));
        console_txt.setText("Welcome To Rill - 20....");
        console_txt.setToolTipText("");
        jScrollPane1.setViewportView(console_txt);

        jdConsole.getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 560, 520));

        jMenuSee.setText("Ver");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Lexical Analysis ");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenuSee.add(jMenuItem3);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setText("Análisis Sintáctico");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenuSee.add(jMenuItem11);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("AST");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenuSee.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Comprobación de Tipos");
        jMenuSee.add(jMenuItem6);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem9.setText("Ver Tablas de Tipos");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenuSee.add(jMenuItem9);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setLabel("Desplegar Cuadruplos");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenuSee.add(jMenuItem7);

        jMenuBar2.add(jMenuSee);

        jdConsole.setJMenuBar(jMenuBar2);

        jMenuItem4.setText("jMenuItem4");

        jMenuItem8.setText("jMenuItem8");

        jMenu4.setText("jMenu4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rill-20");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RILL-20", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 170, 280, -1));

        txtCodigo.setColumns(20);
        txtCodigo.setRows(5);
        jScrollCodigo.setViewportView(txtCodigo);

        jPanel1.add(jScrollCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 970, 510));

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
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu5.setText("Ejecutar");
        jMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu5ActionPerformed(evt);
            }
        });

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        jMenuItem10.setText("Ejecutar Archivo");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem10);

        jMenuBar1.add(jMenu5);

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

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        bandera = true;
        try {
            analizarLexico();
        } catch (IOException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        bandera = true;
        try {
            analizarLexico();
        } catch (IOException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            analizarSintactico();
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        showTree();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu5ActionPerformed

    }//GEN-LAST:event_jMenu5ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        bandera = true;
        try {
            analizarSintactico();
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        bandera = true;
        jdConsole.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jdConsole.setLocationRelativeTo(this);
        jdConsole.setVisible(true);
        console_txt.setText("");
        try {
            analizarSintactico();
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ComprobacionTipos();
        } catch (TypeErrorException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        bandera = true;
        try {
            analizarSintactico();
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ComprobacionTipos();
        } catch (TypeErrorException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        console_txt.setText("");
        VerTablasDeTipo(tt.root);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        guardarComo();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        DesplegarCuadruplos();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

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
    boolean bandera = true;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane console_txt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenu jMenuSee;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollCodigo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JDialog jdConsole;
    private javax.swing.JTextArea txtCodigo;
    // End of variables declaration//GEN-END:variables
}
