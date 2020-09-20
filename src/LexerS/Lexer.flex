package LexerS;
//Clase numeracion donde incluiremos los numeros de todos los tokens
import static Main.Tokens.*;
import Main.Tokens;
%%
%class Lexer
%type Tokens
%unicode
%line
%column
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
CHAR_ELEMENT = "'" [:jletter:] "'"
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
    public String lexeme;
%}
%%

<YYINITIAL> {
    /* code structure */
    
    {CLOSE_BLOCK}           {lexeme=yytext(); return CLOSE_BLOCK;}
    {OPEN_PARENTESIS}       {lexeme=yytext(); return OPEN_PARENTESIS;}
    {CLOSE_PARENTESIS}      {lexeme=yytext(); return CLOSE_PARENTESIS;}
    {PYC}                   {lexeme=yytext(); return PYC;}
    {BETWEEN}               {lexeme=yytext(); return BETWEEN;}
    {OPEN_SQR_BRACKET}      {lexeme=yytext(); return OPEN_SQR_BRACKET;}
    {CLOSE_SQR_BRACKET}     {lexeme=yytext(); return CLOSE_SQR_BRACKET;}
    {COMA}                  {lexeme=yytext(); return COMA;}
    {COLON}                 {lexeme=yytext(); return COLON;}
    {BACK_SLASH}            {lexeme=yytext(); return BACK_SLASH;}
    {NEW_LINE}              {lexeme=yytext(); return NEW_LINE;}
    {NULL}                  {lexeme=yytext(); return NULL;}
    {TRUE}                  {lexeme=yytext(); return TRUE;}
    {FALSE}                 {lexeme=yytext(); return FALSE;}
    {STRING}                {lexeme=yytext(); return STRING;}
    {CHAR_ELEMENT}          {lexeme=yytext(); return CHAR_ELEMENT;}

    /* control statements */
    {FUNCTION}              {lexeme=yytext(); return FUNCTION;}
    {IF_SWITCH}             {lexeme=yytext(); return IF_SWITCH;}
    {PRINT}                 {lexeme=yytext(); return PRINT;}
    {WHILE}                 {lexeme=yytext(); return WHILE;}
    {FOR}                   {lexeme=yytext(); return FOR;}
    {ELSE}                  {lexeme=yytext(); return ELSE;}
    {RETURN}                {lexeme=yytext(); return RETURN;}
    {CIN}                   {lexeme=yytext(); return CIN;}
    {TO}                    {lexeme=yytext(); return TO;}
    {MAIN}                  {lexeme=yytext(); return MAIN;}

    /* operators */
    {OP_REL}                {lexeme=yytext(); return OP_REL;}
    {OP_SUM}                {lexeme=yytext(); return OP_SUM;}
    {OP_MULTI_DIV}          {lexeme=yytext(); return OP_MULTI_DIV;}
    {MOD}                   {lexeme=yytext(); return MOD;}
    {INCREMENT}             {lexeme=yytext(); return INCREMENT;}
    {CON_AND}               {lexeme=yytext(); return CON_AND;}
    {CON_OR}                {lexeme=yytext(); return CON_OR;}
    
    /* data types */
    {INTENGER}              {lexeme=yytext(); return INTENGER;}
    {BOOLEAN}               {lexeme=yytext(); return BOOLEAN;}
    {CHAR}                  {lexeme=yytext(); return CHAR;}
    {ARRAY}                 {lexeme=yytext(); return ARRAY;}
    {MATRIX}                {lexeme=yytext(); return MATRIX;}
    {VARIABLE}              {lexeme=yytext(); return VARIABLE;}

    /* identifiers */
    {ID}                    {lexeme=yytext(); return ID;}
    {NUMBER}                {lexeme=yytext(); return NUMBER;}

    /* comments */
    {COMMENT}               {/* ignore */}
    
    /* whitespace */
    {WHITE_SPACE}           {/* ignore */}

    /* error */
    [^]                {throw new Error("Illegal character <" + yytext() + ">"
                        + " line: " + yyline + ", column: " + yycolumn); }
}


/* INVESTIGAR MÁS SOBRE LA CLASE SYM ya que es parte del CUP*/ 