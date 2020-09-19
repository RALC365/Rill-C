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
comment = "|" ~"|"

/* code structure */
closeBlock = ":)"
openParentesis = "("
closeParentesis = ")"
pyc = ".,"
openSqrBracket = "["
closeSqrBracket = "]"
coma = ","
colon = ":"
quotMark = "\""
apostrophe = "'"
backslash = "\\"
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]

/* control statements */
Main = "main"
variable = "vb"
integer = "int"
Bool = "bln"
Char = "chr"
array = "arr"
matrix = "mtr"
function = "fun"
ifSwitch = "if"
Print = "pp"
While = "wh"
For = "fr"
Default = "els"
//break = "br"
Return = "ret"

/* operators */
equalTo = "="
noEqualTo = "!="
orEqual = ">=" | "<="
than = "<" | ">"
plus = "+" | "-"
multiplication = "*" | "/"
mod = "%"
increment = "++"
decrement = "--"
To = "->" | "to"
False = "False"
True = "True"


/* todavía en veremos */
conAnd = "and"
conOr = "or"

/* identifier */
ID = [:jletter:] [:jletterdigit:]*
number = 0|[1-9][:digit]*

//Codigo
%{
    public Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    };

    public Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    };

%}
%%

<YYINITIAL> {

    /* code structure */
    {closeBlock}        {return new Symbol(sym.closeBlock, yycolumn, yyline, yytext());}
    {openParentesis}    {return new Symbol(sym.openParentesis, yycolumn, yyline, yytext());}
    {closeParentesis}   {return new Symbol(sym.closeParentesis, yycolumn, yyline, yytext());}
    {pyc}               {return new Symbol(sym.pyc, yycolumn, yyline, yytext());}
    {openSqrBracket}    {return new Symbol(sym.openSqrBracket, yycolumn, yyline, yytext());}
    {closeSqrBracket}   {return new Symbol(sym.closeSqrBracket, yycolumn, yyline, yytext());}
    {coma}              {return new Symbol(sym.coma, yycolumn, yyline, yytext());}
    {colon}             {return new Symbol(sym.colon, yycolumn, yyline, yytext());}
    {quotMark}          {return new Symbol(sym.quotMark, yycolumn, yyline, yytext());}     
    {apostrophe}        {return new Symbol(sym.apostrophe, yycolumn, yyline, yytext());}         
    {backslash}         {return new Symbol(sym.backslash, yycolumn, yyline, yytext());}         

    /* control statements */
    {Main}              {return new Symbol(sym.Main, yycolumn, yyline, yytext());}     
    {variable}          {return new Symbol(sym.variable, yycolumn, yyline, yytext());}     
    {integer}           {return new Symbol(sym.integer, yycolumn, yyline, yytext());}     
    {Bool}              {return new Symbol(sym.Bool, yycolumn, yyline, yytext());} 
    {Char}              {return new Symbol(sym.Char, yycolumn, yyline, yytext());} 
    {array}             {return new Symbol(sym.array, yycolumn, yyline, yytext());}     
    {matrix}            {return new Symbol(sym.matrix, yycolumn, yyline, yytext());}     
    {function}          {return new Symbol(sym.function, yycolumn, yyline, yytext());}     
    {ifSwitch}          {return new Symbol(sym.ifSwitch, yycolumn, yyline, yytext());}     
    {Print}             {return new Symbol(sym.Print, yycolumn, yyline, yytext());}     
    {While}             {return new Symbol(sym.While, yycolumn, yyline, yytext());}     
    {For}               {return new Symbol(sym.For, yycolumn, yyline, yytext());} 
    {Default}           {return new Symbol(sym.Default, yycolumn, yyline, yytext());}     
//    {break}             {System.out.println("<break," + yytext() + ">");}         
    {Return}            {return new Symbol(sym.Return, yycolumn, yyline, yytext());}     

    /* operators */
    {equalTo}           {return new Symbol(sym.equalTo, yycolumn, yyline, yytext());}     
    {noEqualTo}         {return new Symbol(sym.noEqualTo, yycolumn, yyline, yytext());}         
    {orEqual}           {return new Symbol(sym.orEqual, yycolumn, yyline, yytext());}     
    {than}              {return new Symbol(sym.than, yycolumn, yyline, yytext());} 
    {plus}              {return new Symbol(sym.plus, yycolumn, yyline, yytext());} 
    {multiplication}    {return new Symbol(sym.multiplication, yychar, yyline, yytext());}           
    {mod}               {return new Symbol(sym.mod, yycolumn, yyline, yytext());} 
    {increment}         {return new Symbol(sym.increment, yycolumn, yyline, yytext());}         
    {decrement}         {return new Symbol(sym.decrement, yycolumn, yyline, yytext());}         
    /* todavía en veremos */            
    {conAnd}            {return new Symbol(sym.conAnd, yycolumn, yyline, yytext());}     
    {conOr}             {return new Symbol(sym.conOr, yycolumn, yyline, yytext());}    
    {To}                {return new Symbol(sym.To, yycolumn, yyline, yytext());}
    {False}             {return new Symbol(sym.False, yycolumn, yyline, yytext());}
    {True}              {return new Symbol(sym.True, yycolumn, yyline, yytext());}
    
    /* identifiers */
    {ID}               {return new Symbol(sym.ID, yycolumn, yyline, yytext());}  
    {number}           {return new Symbol(sym.number, yycolumn, yyline, yytext());}      

    /* comments */
    {comment}           {/* ignore */}          
    
    /* whitespace */
    {WhiteSpace}        {/* ignore */}          

    /* error */
    [^]                {return new Symbol(sym.ERROR, yycolumn, yyline, yytext());}        
}


/* INVESTIGAR MÁS SOBRE LA CLASE SYM ya que es parte del CUP*/ 