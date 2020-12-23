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
 *
 */
public class Instrucciones {

    public ArrayList<String> temporales = new ArrayList<>(Arrays.asList("$t0", "$t1", "$t2", "$t3",
            "$t4", "$t5", "$t6", "$t7", "$t8", "$t9"));
    public ArrayList<String> argumentos = new ArrayList<>(Arrays.asList(
            "$a0", "$a1", "$a2", "$a3", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7"));
    public ArrayList<String> parametros = new ArrayList<>(Arrays.asList(
            "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7"));
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

    private String getArgumentoLibre() {
        for (String R : DescriptorRegistros.keySet()) {
            if (!R.contains("t")) {
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
            if (I.equals("RET")) {
                String reg = getRegistroLibre();
                ret += "    lw " + reg + ", " + id.ubicacion + "\n";
                ret += "    move " + reg + ", " + "$v0" + "\n";
                ret += "    sw " + reg + ", " + id.ubicacion + "\n\n";
            } else {
                if (GetType(I).equals("int")) {
                    String reg = getRegistroLibre();
                    ret += "    lw " + reg + ", " + id.ubicacion + "\n";
                    ret += "    li " + reg + ", " + I + "\n";
                    ret += "    sw " + reg + ", " + id.ubicacion + "\n\n";
                } else {
                    String reg = getRegistroLibre();
                    String r_i = buscarEnRegistros(I);
                    ret += "    lw " + reg + ", " + id.ubicacion + "\n";
                    ret += "    move " + reg + ", " + r_i + "\n";
                    ret += "    sw " + reg + ", " + id.ubicacion + "\n\n";
                }
            }
        }
        return ret;
    }

    public String Print(String mensajeGlobal) {
        String generarPrint = "    #print\n";
        generarPrint += "    li $v0,4\n";
        generarPrint += "    la $a0," + mensajeGlobal + "\n";
        generarPrint += "    syscall\n\n";
        return generarPrint;
    }

    public String PrintVariable(String ID, Object B) {
        String reg = getRegistroLibre();
        TableRow tr = root.getID(ID, B, root);
        String generarPrint = "    #print\n";
        if (tr != null) {
            generarPrint += "    lw " + reg + "," + tr.ubicacion + "\n";
            if (tr.type.equals("int")) {
                generarPrint += "    li $v0,1\n";
            } else {
                generarPrint += "    li $v0,4\n";
            }
            generarPrint += "    move $a0," + reg + "\n";
            generarPrint += "    syscall\n\n";
            liberarRegistro(reg);
        }
        return generarPrint;
    }

    public String PreinicioFuncion(ArrayList<String> params, Object B, String fun) {
        String ret = "    #Llamado a función\n";
        for (int i = 0; i < params.size(); i++) {
            DescriptorRegistros.remove(argumentos.get(i));
            String id = params.get(i);
            String type = GetType(id);
            if (type.equals("int")) {
                ret += "    li , " + argumentos.get(i) + ", " + id + "\n";
                guardarEnRegistros(argumentos.get(i), id);
            } else {
                TableRow t = root.getID(id, B, root);
                if (t != null) {
                    String reg = getRegistroLibre();
                    ret += "    lw , " + reg + ", " + t.ubicacion + "\n";
                    ret += "    move , " + argumentos.get(i) + ", " + reg + "\n";
                    guardarEnRegistros(argumentos.get(i), id);
                }
            }
            //Faltans los casos donde sea matriz y eso
        }
        ret += "    jal _fun_" + fun + "\n\n";
        return ret;
    }

    public String InicioFuncion(ArrayList<String> params, Object B) {
        int sub = 0;
        String ret = "";
        ret += "   sw $fp, -4($sp) \n";
        ret += "   sw $ra, -8($sp) \n";
        ret += "\n";
        ret += "   #Guardar los parametros \n";
        String storeSW = "";
        for (int i = 0; i < params.size(); i++) {
            System.out.println("Pollo: "+params.get(i));
            //DescriptorRegistros.remove(parametros.get(i));
            String id = params.get(i);
            String type = GetType(id);
            TableRow t = root.getID(id, B, root);
            if (t != null) {
                storeSW += "    sw " + parametros.get(i) + ", " + t.ubicacion + "\n";
                ret += storeSW;
                if (i == params.size() - 1) {
                    sub = t.offset;
                }
            }
        }
        ret += "\n";
        ret += "   #Mover Apuntadores \n";
        ret += "   move $fp, $sp \n";
        ret += "\n";
        ret += "   #mover los parámetros recibido a las S \n";
        for (int i = 0; i < params.size(); i++) {
            ret += "   move " + parametros.get(i) + ", " + argumentos.get(i) + "\n";
        }
        ret += "   sub $sp, $sp, " + sub + "\n";
        ret += "\n";
        ret += storeSW;
        ret += "   #Código dentro de la función \n";
        return ret;
    }

    public String FinFuncion(ArrayList<String> params, Object B, String salida) {
        String ret = salida + ":\n";
        ret += "    move $sp, $fp \n";
        ret += "    lw $fp, -4($sp) \n";
        ret += "    lw $ra, -8($sp) \n";
        ret += "\n";
        ret += "    #Restaurar los parametros \n";
        for (int i = 0; i < params.size(); i++) {
            DescriptorRegistros.remove(parametros.get(i));
            String id = params.get(i);
            String type = GetType(id);
            TableRow t = root.getID(id, B, root);
            if (t != null) {
                ret += "    lw " + parametros.get(i) + ", " + t.ubicacion + "\n";
            }
        }
        ret += "    jr $ra \n";
        return ret;
    }

    public String Ret(String I, Object B) {
        String ret = "    #Retorno\n";
        TableRow t = root.getID(I, B, root);
        if (t != null) {
            ret += "    lw " + "$v0" + ", " + t.ubicacion + "\n";
        }
        return ret;
    }

    public String InputInt(String id, Object B) {
        TableRow tr = root.getID(id.replace(" ", ""), B, root);
        String ins = "    #input\n";
        if (tr != null) {
            if (tr.type.equals("int")) {
                ins += "    li $v0, 5	# read int \n";
                ins += "    syscall     # print it \n";
                ins += "    sw $v0, " + tr.ubicacion + "\n\n";
            } else {
                //estamos leyendo otro tipo
            }
        }
        return ins;
    }

    public String Salto(String dest) {
        String ret = "    b _" + dest + "\n\n";
        return ret;
    }

    public String SaltoCondicional(Operacion O, String D, String I, String R, Object B) {
        String ret = "";
        //Evaluamos si es un registro o un número
        System.out.println("Pepsi: "+D);
        String[] registroD = getRegistroSaltoCondicional(D, B);
        ret += registroD[0];
        D = registroD[1];
        System.out.println("Twist: "+D);
        String[] registroI = getRegistroSaltoCondicional(I, B);
        ret += registroI[0];
        I = registroI[1];
        switch (O) {
            case IFIGUAL: {
                ret += "    beq " + D + ", " + I + ", _" + R + "\n";
            }
            break;
            case IFMAYOR: {
                ret += "    bgt " + D + ", " + I + ", _" + R + "\n";
            }
            break;
            case IFMENOR: {
                ret += "    blt " + D + ", " + I + ", _" + R + "\n";
            }
            break;
            case IFMAYORIGUAL: {
                ret += "    bge " + D + ", " + I + ", _" + R + "\n";
            }
            break;
            case IFMENORIGUAL: {
                ret += "    ble " + D + ", " + I + ", _" + R + "\n";
            }
            break;
            case IFDISTINTO: {
                ret += "    bne " + D + ", " + I + ", _" + R + "\n";
            }
            break;
        }
        return ret;
    }

    private String[] getRegistroSaltoCondicional(String D, Object B) {
        String inst = "";
        String registro = "";
        String typeD = GetType(D);
        switch (typeD) {
            case "int": {
                String reg = getRegistroLibre();
                inst += "    li " + reg + ", " + D + "\n";
                guardarEnRegistros(reg, D);
                registro = reg;
            }
            break;
            case "bln": {
                String reg = getRegistroLibre();
                //false = 0; true = 1. Así comparamos recios
                D = (D.equals("false")) ? (0 + "") : ("" + 1);
                inst += "    li " + reg + ", " + D + "\n";
                guardarEnRegistros(reg, D);
                registro = reg;
            }
            break;
            case "chr": {
                String reg = getRegistroLibre();
                //Comvertimos el char a número para comparar, luego a String
                D = "" + (int) (D.charAt(1));
                inst += "    li " + reg + ", " + D + "\n";
                guardarEnRegistros(reg, D);
                registro = reg;
            }
            break;
            default: {
                TableRow id = new TableRow(D, typeD, 0);
                id = root.getID(D, B, root);
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

        }
        String ret[] = {inst, registro};
        return ret;
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
