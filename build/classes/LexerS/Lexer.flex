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
    public String lexeme;
%}
%%

<YYINITIAL> {

    /* code structure */
    {closeBlock}        {lexeme=yytext(); return closeBlock;}
    {openParentesis}    {lexeme=yytext(); return openParentesis;}
    {closeParentesis}   {lexeme=yytext(); return closeParentesis;}
    {pyc}               {lexeme=yytext(); return pyc;}
    {openSqrBracket}    {lexeme=yytext(); return openSqrBracket;}
    {closeSqrBracket}   {lexeme=yytext(); return closeSqrBracket;}
    {coma}              {lexeme=yytext(); return coma;}
    {colon}             {lexeme=yytext(); return colon;}
    {quotMark}          {lexeme=yytext(); return quotMark;}
    {apostrophe}        {lexeme=yytext(); return apostrophe;}
    {backslash}         {lexeme=yytext(); return backslash;}

    /* control statements */
    {Main}              {lexeme=yytext(); return Main;}
    {variable}          {lexeme=yytext(); return variable;}
    {integer}           {lexeme=yytext(); return integer;}
    {Bool}              {lexeme=yytext(); return Bool;}
    {Char}              {lexeme=yytext(); return Char;}
    {array}             {lexeme=yytext(); return array;}
    {matrix}            {lexeme=yytext(); return matrix;}
    {function}          {lexeme=yytext(); return function;}
    {ifSwitch}          {lexeme=yytext(); return ifSwitch;}
    {Print}             {lexeme=yytext(); return Print;}
    {While}             {lexeme=yytext(); return While;}
    {For}               {lexeme=yytext(); return For;}
    {Default}           {lexeme=yytext(); return Default;}
//    {break}             {System.out.println("<break," + yytext() + ">");}
    {Return}            {lexeme=yytext(); return Return;}

    /* operators */
    {equalTo}           {lexeme=yytext(); return equalTo;}
    {noEqualTo}         {lexeme=yytext(); return noEqualTo;}
    {orEqual}           {lexeme=yytext(); return orEqual;}
    {than}              {lexeme=yytext(); return than;}
    {plus}              {lexeme=yytext(); return plus;}
    {multiplication}    {lexeme=yytext(); return multiplication;}
    {mod}               {lexeme=yytext(); return mod;}
    {increment}         {lexeme=yytext(); return increment;}
    {decrement}         {lexeme=yytext(); return decrement;}
    /* todavía en veremos */
    {conAnd}            {lexeme=yytext(); return conAnd;}
    {conOr}             {lexeme=yytext(); return conOr;}
    {To}                {lexeme=yytext(); return To;}
    {False}             {lexeme=yytext(); return False;}
    {True}              {lexeme=yytext(); return True;}
 
    /* identifiers */
    {ID}               {lexeme=yytext(); return ID;}
    {number}           {lexeme=yytext(); return number;}

    /* comments */
    {comment}           {/* ignore */}
    
    /* whitespace */
    {WhiteSpace}        {/* ignore */}

    /* error */
    [^]                {throw new Error("Illegal character <" + yytext() + ">"
                        + " line: " + yyline + ", column: " + yycolumn); }
}


/* INVESTIGAR MÁS SOBRE LA CLASE SYM ya que es parte del CUP*/ 