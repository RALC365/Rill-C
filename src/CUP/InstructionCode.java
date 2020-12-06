/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CUP;

/**
 *
 * @author RALC365
 */
public class InstructionCode {
    private String name;
    private int codeLine;

    public InstructionCode(String name, int codeLine) {
        this.name = name;
        this.codeLine = codeLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCodeLine() {
        return codeLine;
    }

    public void setCodeLine(int codeLine) {
        this.codeLine = codeLine;
    }

    @Override
    public String toString() {
        return name;
    }
    
    
}
