/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIPS;

import ComprobacionDeTipos.TableRow;
import ComprobacionDeTipos.TypesSubTable;
import ThreeAddressCode.Operacion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Julio Marin
 */
public class Instrucciones {

    public ArrayList<String> temporales = new ArrayList<>(Arrays.asList("$t0", "$t1", "$t2", "$t3",
            "$t4", "$t5", "$t6", "$t7", "$t8", "$t9"));
    public ArrayList<String> argumentos = new ArrayList<>(Arrays.asList(
            "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6",
            "$s7", "$t8", "$t9"));
    private TypesSubTable root = null;
    private HashMap<String, String> DescriptorRegistros = new HashMap();

    public Instrucciones(TypesSubTable root) {
        this.root = root;
        for (String registro : temporales) {
            DescriptorRegistros.put(registro, "");
        }
        for (String argumento : argumentos) {
            DescriptorRegistros.put(argumento, "");
        }
    }

    /*-----------------------------------------------Funciones Importantes-----------------------------------------------*/
    private String GetType(String id) {
        try {
            Integer.parseInt(id);
            return "int";
        } catch (Exception e) {
            if (id.equals("false")) {
                return "bln";
            } else if (id.equals("true")) {
                return "bln";
            } else if (id.contains("'")) {
                return "chr";
            } else {
                return "ID";
            }
        }
    }

    private String getRegistroLibre() {
        for (String R : DescriptorRegistros.keySet()) {
            if (R.contains("t")) {
                if (DescriptorRegistros.get(R).equals("")) {
                    return R;
                }
            }
        }
        return "";
    }

    private void liberarRegistro(String R) {
        DescriptorRegistros.remove(R);
        DescriptorRegistros.put(R, "");
    }

    private String buscarEnRegistros(String id) {
        for (String R : DescriptorRegistros.keySet()) {
            if (DescriptorRegistros.get(R).equals(id)) {
                return R;
            }
        }
        return "";
    }

    private void guardarEnRegistros(String R, String id) {
        DescriptorRegistros.remove(R);
        DescriptorRegistros.put(R, id);
    }

    private String[] getRegistroAritmetico(String D, Object B) {
        String inst = "";
        String registro = "";
        if (GetType(D).equals("int")) {
            String reg = getRegistroLibre();
            inst += "    li " + reg + ", " + D + "\n";
            guardarEnRegistros(reg, D);
            registro = reg;
        } else {
            TableRow id = root.getID(D, B, root);
            //En caso de ser variable
            if (id != null) {
                String r_v = buscarEnRegistros(D);
                //Buscar si el ID no está cargado 
                if (r_v.equals("")) {
                    String r_l = getRegistroLibre();
                    inst += "    lw " + r_l + ", " + id.ubicacion + "\n";
                    guardarEnRegistros(r_l, id.id);
                    registro = r_l;
                } else {
                    registro = r_v;
                }
            } //en caso de ser temoporal ya debería estar en descriptor
            else {
                String reg = buscarEnRegistros(D);
                registro = reg;
            }
        }
        String ret[] = {inst, registro};
        return ret;
    }

    private String[] CargarVariable(String D, Object B) {
        String reg = "";
        String inst = "";
        reg = buscarEnRegistros(D);
        if (reg.equals("")) {
            TableRow id = root.getID(D, B, root);
            if (id != null) {
                reg = getRegistroLibre();
                inst += "    lw " + reg + ", " + id.ubicacion + "\n";
                guardarEnRegistros(reg, D);
            }
        }
        String[] ret = {inst, reg};
        return ret;
    }

    /*-----------------------------------------------Instrucciones del MIPS-----------------------------------------------*/
    public String InstruccionMain() {
        String generarMain = "main: \n";
        return generarMain;
    }

    //Aquí Confundí la derecha con la Izquiera pero funciona bien y mejor no lo toco XD
    public String OperacionAritmetica(Operacion O, String D, String I, String R, Object B) {
        String ret = "";
        String[] registroD = getRegistroAritmetico(D, B);
        ret += registroD[0];
        D = registroD[1];
        String[] registroI = getRegistroAritmetico(I, B);
        ret += registroI[0];
        I = registroI[1];
        String new_reg = getRegistroLibre();
        switch (O) {
            case DIVISION: {
                ret += "    div " + new_reg + ", " + D + ", " + I + "\n";
            }
            break;
            case MULTIPLICACION: {
                ret += "    mul " + new_reg + ", " + D + ", " + I + "\n";
            }
            break;
            case SUMA: {
                ret += "    add " + new_reg + ", " + D + ", " + I + "\n";
            }
            break;
            case RESTA: {
                ret += "    sub " + new_reg + ", " + D + ", " + I + "\n";
            }
            break;
        }

        guardarEnRegistros(new_reg, R);
        liberarRegistro(D);
        liberarRegistro(I);
        return ret;
    }

    public String Asignacion(String I, String R, Object B) {
        String ret = "";
        TableRow id = root.getID(R, B, root);
        if (id != null) {
            if (GetType(I).equals("int")) {
                String reg = getRegistroLibre();
                ret += "    lw " + reg + ", " + id.ubicacion + "\n";
                ret += "    li " + reg + ", " + I + "\n";
                ret += "    sw " + reg + ", " + id.ubicacion + "\n";
            } else {
                String reg = getRegistroLibre();
                String r_i = buscarEnRegistros(I);
                ret += "    lw " + reg + ", " + id.ubicacion + "\n";
                ret += "    move " + reg + ", " + r_i + "\n";
                ret += "    sw " + reg + ", " + id.ubicacion + "\n";
            }
        }
        return ret;
    }

    public String Print(String mensajeGlobal) {
        String generarPrint = "\n    #print\n";
        generarPrint += "    li $v0,4\n";
        generarPrint += "    la $a0," + mensajeGlobal + "\n";
        generarPrint += "    syscall\n\n";
        return generarPrint;
    }

    public String PrintVariable(String ID, Object B) {
        TableRow tr = root.getID(ID, B, root);
        String generarPrint = "\n    #print\n";
        if (tr != null) {
            if (tr.type.equals("int")) {
                generarPrint += "    li $v0,1\n";
            } else {
                generarPrint += "    li $v0,4\n";
            }
            generarPrint += "    la $a0," + tr.ubicacion + "\n";
            generarPrint += "    syscall\n\n";
        }
        return generarPrint;
    }

    public String InicioFuncion(String[] params, Object bloqueActual) {
        String ret = "";
        ret += "   sw $fp, -4($sp) \n";
        ret += "   sw $fp, -4($sp) \n";
        ret += "   sw $ra, -8($sp) \n";
        ret += "\n";
        ret += "   #Guardar los parametros \n";
        for (String param : params) {
            if (GetType(ret).equals("")) {
                TableRow id = root.getID(param, bloqueActual, root);
                ret += "   sw $s0, " + id.ubicacion + " \n";
            } else {
                ret += "   Todavía no sé esta parte \n";
            }
        }
        ret += "\n";
        //Como se mandan en A debe tambien moverse los a a s
        ret += "   #mover los parámetros recibido a las S \n";
        ret += "   move $s0,$a0 \n";
        ret += "\n";
        ret += "   #Mover Apuntadores \n";
        ret += "   mov $fp, $sp \n";
        ret += "   sub $sp,$sp, SUMA_OFFETS \n";
        ret += "\n";
        ret += "   #Código dentro de la función \n";
        return ret;
    }

    public String FinFuncion(String[] Params) {
        String ret = "";
        ret += "   _salida_fun: \n";
        ret += "   mov $sp, $fp \n";
        ret += "   lw $fp, -4($sp) \n";
        ret += "   lw $ra, -8($sp) \n";
        ret += "\n";
        ret += "   #Restaurar los parametros \n";
        ret += "   lw $s0,-12($sp) \n";
        ret += "   jr $ra \n";
        return ret;

    }

    public String InstruccionInputInt(String id, int offset) {
        String ins = "";
        ins += "    li $v0, 5	# read int \n";
        ins += "    syscall		# print it \n";
        //ins += GuardarPalabra("$v0", offset, variable_en_memoria);
        return ins;
    }

    public String InstruccionInputString(String variable_en_memoria, int offset) {
        String ins = "";
        ins += "    la $a0, buffer #load byte space into address";
        ins += "    li $a1, 20 # allot the byte space for string";
        ins += "    move $t0,$a0 #save string to t0";
        ins += "    syscall";
        //ins += GuardarPalabra("$t0", offset, variable_en_memoria);
        return ins;
    }
}

/*
|Codigo de Prueba|
()(main:
	int size.,
	size: 5*(6/9+1/90*7)+6*7.,
	(pp: size:).,
	int a.,
	int b.,
	suma(a,b).,
:)

(int a, int b)suma(fun:nll
	int x:a+b.,
:)
 */
