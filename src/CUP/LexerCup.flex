package CUP;
//Clase numeracion donde incluiremos los numeros de todos los tokens
import java_cup.runtime.Symbol;
%%
%class LexerCup
%type java_cup.runtime.Symbol
%cup
//para retornar toda la cadena
%full
%int
%unicode
%line
%column
%char
%public

/* comment */
COMMENT = "|"~"|"

/* code structure */
CLOSE_BLOCK = ":)"
OPEN_PARENTESIS = "("
CLOSE_PARENTESIS = ")"
PYC = ".,"
BETWEEN = "..."
OPEN_SQR_BRACKET = "["
CLOSE_SQR_BRACKET = "]"
COMA = ","
COLON = ":"
BACK_SLASH = "\\"
NEW_LINE = "ln"
NULL = "nll"
TRUE = "true"
FALSE = "false"
STRING = "\""~"\""
CHAR_ELEMENT = "'" [:jletter:] "'" | "‘" [:jletter:] "’"
LINE_TERMINATOR = \r|\n|\r\n
INPUT_CHARACTER = [^\r\n]
WHITE_SPACE = {LINE_TERMINATOR} | [ \t\f]

/* control statements */
FUNCTION = "fun"
IF_SWITCH = "if"
PRINT = "pp"
WHILE = "wh"
FOR = "fr"
ELSE = "els"
RETURN = "ret"
CIN = "in"
TO = "to" | "->"
MAIN = "main"

/* operators */
OP_REL = "="|">="|"<="|"<"|">"
OP_SUM = "+" | "-"
OP_MULTI_DIV = "*" | "/"
MOD = "%"
INCREMENT = "++"|"--"
CON_AND = "and"
CON_OR = "or"

/* data types */
INTENGER = "int"
BOOLEAN = "bln"
CHAR = "chr"
ARRAY = "arr"
MATRIX = "mtx"
VARIABLE = "vb"

/* identifier */
ID = [:jletter:] [:jletterdigit:]*
NUMBER = 0 | [1-9][0-9]*

//Codigo
%{
    public Symbol symbol(int type, Object value){
        return new Symbol(type, yycolumn, yyline, value);
    };

    public Symbol symbol(int type){
        return new Symbol(type, yycolumn, yyline);
    };

%}
%%

<YYINITIAL> {

    /* code structure */
    
    {CLOSE_BLOCK}           {System.out.println("<CLOSE_BLOCK," + yytext() + ">");
                            return new Symbol(sym.CLOSE_BLOCK, yycolumn, yyline, yytext());}
    {OPEN_PARENTESIS}       {System.out.println("<OPEN_PARENTESIS," + yytext() + ">");
                            return new Symbol(sym.OPEN_PARENTESIS, yycolumn, yyline, yytext());}
    {CLOSE_PARENTESIS}      {System.out.println("<CLOSE_PARENTESIS," + yytext() + ">");
                            return new Symbol(sym.CLOSE_PARENTESIS, yycolumn, yyline, yytext());}
    {PYC}                   {System.out.println("<PYC," + yytext() + ">");
                            return new Symbol(sym.PYC, yycolumn, yyline, yytext());}
    {BETWEEN}               {System.out.println("<BETWEEN," + yytext() + ">");
                            return new Symbol(sym.BETWEEN, yycolumn, yyline, yytext());}
    {OPEN_SQR_BRACKET}      {System.out.println("<OPEN_SQR_BRACKET," + yytext() + ">");
                            return new Symbol(sym.OPEN_SQR_BRACKET, yycolumn, yyline, yytext());}
    {CLOSE_SQR_BRACKET}     {System.out.println("<CLOSE_SQR_BRACKET," + yytext() + ">");
                            return new Symbol(sym.CLOSE_SQR_BRACKET, yycolumn, yyline, yytext());}
    {COMA}                  {System.out.println("<COMA," + yytext() + ">");
                            return new Symbol(sym.COMA, yycolumn, yyline, yytext());}
    {COLON}                 {System.out.println("<COLON," + yytext() + ">");
                            return new Symbol(sym.COLON, yycolumn, yyline, yytext());}
    {BACK_SLASH}            {System.out.println("<BACK_SLASH," + yytext() + ">");
                            return new Symbol(sym.BACK_SLASH, yycolumn, yyline, yytext());}
    {NEW_LINE}              {System.out.println("<NEW_LINE," + yytext() + ">");
                            return new Symbol(sym.NEW_LINE, yycolumn, yyline, yytext());}
    {NULL}                  {System.out.println("<NULL," + yytext() + ">");
                            return new Symbol(sym.NULL, yycolumn, yyline, yytext());}
    {TRUE}                  {System.out.println("<TRUE," + yytext() + ">");
                            return new Symbol(sym.TRUE, yycolumn, yyline, yytext());}
    {FALSE}                 {System.out.println("<FALSE," + yytext() + ">");
                            return new Symbol(sym.FALSE, yycolumn, yyline, yytext());}
    {STRING}                {System.out.println("<STRING," + yytext() + ">");
                            return new Symbol(sym.STRING, yycolumn, yyline, yytext());}
    {CHAR_ELEMENT}          {System.out.println("<CHAR_ELEMENT," + yytext() + ">");
                            return new Symbol(sym.CHAR_ELEMENT, yycolumn, yyline, yytext());}

    /* control statements */
    {FUNCTION}              {System.out.println("<FUNCTION," + yytext() + ">");
                            return new Symbol(sym.FUNCTION, yycolumn, yyline, yytext());}
    {IF_SWITCH}             {System.out.println("<IF_SWITCH," + yytext() + ">");
                            return new Symbol(sym.IF_SWITCH, yycolumn, yyline, yytext());}
    {PRINT}                 {System.out.println("<PRINT," + yytext() + ">");
                            return new Symbol(sym.PRINT, yycolumn, yyline, yytext());}
    {WHILE}                 {System.out.println("<WHILE," + yytext() + ">");
                            return new Symbol(sym.WHILE, yycolumn, yyline, yytext());}
    {FOR}                   {System.out.println("<FOR," + yytext() + ">");
                            return new Symbol(sym.FOR, yycolumn, yyline, yytext());}
    {ELSE}                  {System.out.println("<ELSE," + yytext() + ">");
                            return new Symbol(sym.ELSE, yycolumn, yyline, yytext());}
    {RETURN}                {System.out.println("<RETURN," + yytext() + ">");
                            return new Symbol(sym.RETURN, yycolumn, yyline, yytext());}
    {CIN}                   {System.out.println("<CIN," + yytext() + ">");
                            return new Symbol(sym.CIN, yycolumn, yyline, yytext());}
    {TO}                    {System.out.println("<TO," + yytext() + ">");
                            return new Symbol(sym.TO, yycolumn, yyline, yytext());}
    {MAIN}                    {System.out.println("<MAIN," + yytext() + ">");
                            return new Symbol(sym.MAIN, yycolumn, yyline, yytext());}

    /* operators */
    {OP_REL}                {System.out.println("<OP_REL," + yytext() + ">");
                            return new Symbol(sym.OP_REL, yycolumn, yyline, yytext());}
    {OP_SUM}                {System.out.println("<OP_SUM," + yytext() + ">");
                            return new Symbol(sym.OP_SUM, yycolumn, yyline, yytext());}
    {OP_MULTI_DIV}          {System.out.println("<OP_MULTI_DIV," + yytext() + ">");
                            return new Symbol(sym.OP_MULTI_DIV, yycolumn, yyline, yytext());}
    {MOD}                   {System.out.println("<MOD," + yytext() + ">");
                            return new Symbol(sym.MOD, yycolumn, yyline, yytext());}
    {INCREMENT}             {System.out.println("<INCREMENT," + yytext() + ">");
                            return new Symbol(sym.INCREMENT, yycolumn, yyline, yytext());}
    {CON_AND}               {System.out.println("<CON_AND," + yytext() + ">");
                            return new Symbol(sym.CON_AND, yycolumn, yyline, yytext());}
    {CON_OR}                {System.out.println("<CON_OR," + yytext() + ">");
                            return new Symbol(sym.CON_OR, yycolumn, yyline, yytext());}
    
    /* data types */
    {INTENGER}              {System.out.println("<INTENGER," + yytext() + ">");
                            return new Symbol(sym.INTENGER, yycolumn, yyline, yytext());}
    {BOOLEAN}               {System.out.println("<BOOLEAN," + yytext() + ">");
                            return new Symbol(sym.BOOLEAN, yycolumn, yyline, yytext());}
    {CHAR}                  {System.out.println("<CHAR," + yytext() + ">");
                            return new Symbol(sym.CHAR, yycolumn, yyline, yytext());}
    {ARRAY}                 {System.out.println("<ARRAY," + yytext() + ">");
                            return new Symbol(sym.ARRAY, yycolumn, yyline, yytext());}
    {MATRIX}                {System.out.println("<MATRIX," + yytext() + ">");
                            return new Symbol(sym.MATRIX, yycolumn, yyline, yytext());}
    {VARIABLE}              {System.out.println("<VARIABLE," + yytext() + ">");
                            return new Symbol(sym.VARIABLE, yycolumn, yyline, yytext());}

    /* identifiers */
    {ID}                    {System.out.println("<ID," + yytext() + ">");
                            return new Symbol(sym.ID, yycolumn, yyline, yytext());}
    {NUMBER}                {System.out.println("<NUMBER," + yytext() + ">");
                            return new Symbol(sym.NUMBER, yycolumn, yyline, new Integer(Integer.parseInt(yytext())));}

    /* comments */
    {COMMENT}               {/* ignore */}
    
    /* whitespace */
    {WHITE_SPACE}           {/* ignore */}

    /* error */
    [^]                {System.out.println("<ERROR," + yytext() + ">");
                        return new Symbol(sym.ERROR, yycolumn, yyline, yytext());}        
}
