/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author WillRomero and RALC365
 */
public class Principal {
    public static void main(String[] args) throws Exception {
        String ruta1 = "./src/LexerS/Lexer.flex";
        String ruta2 = "./src/CUP/LexerCup.flex";
        String[] rutaS = {"-parser", "Sintax", "./src/CUP/Sintax.cup"};
        String[] rutaAST = {"-parser", "ASintaxT", "./src/CUP/ASintaxT.cup"};
        generar(ruta1, ruta2, rutaS);
        generar2(ruta2, ruta2, rutaAST);
    }
    public static void generar(String ruta1, String ruta2, String[] rutaS) throws IOException, Exception{
        File archivo;
        archivo = new File(ruta1);
        JFlex.Main.generate(archivo);
        archivo = new File(ruta2);
        JFlex.Main.generate(archivo);
        java_cup.Main.main(rutaS);
        Path rutaSym = Paths.get("./src/CUP/sym.java");
        if (Files.exists(rutaSym)) {
            Files.delete(rutaSym);
        } 
        Files.move(
                Paths.get("./sym.java"), 
                Paths.get("./src/CUP/sym.java")
        );
        Path rutaSin = Paths.get("./src/CUP/Sintax.java");
        if (Files.exists(rutaSin)) {
            Files.delete(rutaSin);
        }
        Files.move(
                Paths.get("./Sintax.java"), 
                Paths.get("./src/CUP/Sintax.java")
        );
    }
    
    
    public static void generar2(String ruta1, String ruta2, String[] rutaS) throws IOException, Exception{
        File archivo;
        archivo = new File(ruta1);
        JFlex.Main.generate(archivo);
        archivo = new File(ruta2);
        JFlex.Main.generate(archivo);
        java_cup.Main.main(rutaS);
        Path rutaSym = Paths.get("./src/CUP/sym.java");
        if (Files.exists(rutaSym)) {
            Files.delete(rutaSym);
        }
        Files.move(
                Paths.get("./sym.java"), 
                Paths.get("./src/CUP/sym.java")
        );
        Path rutaSin = Paths.get("./src/CUP/ASintaxT.java");
        if (Files.exists(rutaSin)) {
            Files.delete(rutaSin);
        }
        Files.move(
                Paths.get("./ASintaxT.java"), 
                Paths.get("./src/CUP/ASintaxT.java")
        );
    }
}
