%%
%unicode
%class Tokens
%int
%line
%column
%standalone

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
variable = "vb"
integer = "int"
bool = "bln"
char = "chr"
array = "arr"
matrix = "mtr"
function = "fun"
ifSwitch = "if"
print = "pp"
while = "wh"
for = "fr"
default = "els"
//break = "br"
return = "ret"

/* operators */
equalTo = "="
noEqualTo = "!="
orEqual = ">=" || "<="
than = "<" || ">"
plus = "+" || "-"
multiplication = "*" || "/"
mod = "%"
increment = "++"
decrement = "--"


/* todavía en veremos */
conAnd = "and"
conOr = "or"

/* identifier */
ID = [:jletter:] [:jletterdigit:]*
number = 0|[1-9][:digit]*

%%

<YYINITIAL> {

    /* code structure */
    {closeBlock}        {System.out.println("<closeBlock," + yytext() + ">");}
    {openParentesis}    {System.out.println("<openParentesis," + yytext() + ">");}
    {closeParentesis}   {System.out.println("<closeParentesis," + yytext() + ">");}
    {pyc}               {System.out.println("<pyc," + yytext() + ">");}
    {openSqrBracket}    {System.out.println("<openSqrBracket," + yytext() + ">");}
    {closeSqrBracket}   {System.out.println("<closeSqrBracket," + yytext() + ">");}
    {coma}              {System.out.println("<coma," + yytext() + ">");}
    {colon}             {System.out.println("<colon," + yytext() + ">");}
    {quotMark}          {System.out.println("<comilla," + yytext() + ">");}
    {apostrophe}        {System.out.println("<apostrophe," + yytext() + ">");}
    {backslash}         {System.out.println("<backslash," + yytext() + ">");}

    /* control statements */
    {variable}          {System.out.println("<variable," + yytext() + ">");}
    {integer}           {System.out.println("<integer," + yytext() + ">");}
    {bool}              {System.out.println("<bool," + yytext() + ">");}
    {char}              {System.out.println("<char," + yytext() + ">");}
    {array}             {System.out.println("<array," + yytext() + ">");}
    {matrix}            {System.out.println("<matrix," + yytext() + ">");}
    {function}          {System.out.println("<function," + yytext() + ">");}
    {ifSwitch}          {System.out.println("<ifSwitch," + yytext() + ">");}
    {print}             {System.out.println("<print," + yytext() + ">");}
    {while}             {System.out.println("<while," + yytext() + ">");}
    {for}               {System.out.println("<for," + yytext() + ">");}
    {default}           {System.out.println("<default," + yytext() + ">");}
//    {break}             {System.out.println("<break," + yytext() + ">");}
    {return}            {System.out.println("<return," + yytext() + ">");}

    /* operators */
    {equalTo}           {/*return symbol(sym.EQEQ);*/
                        System.out.println("<equalTo," + yytext() + ">");}
    {noEqualTo}         {/*return symbol(sym.EQEQ);*/
                        System.out.println("<noEqualTo," + yytext() + ">");}
    {orEqual}           {System.out.println("<greaterOrEqual," + yytext() + ">");}
    {than}              {System.out.println("<lessThan," + yytext() + ">");}
    {plus}              {/*return symbol(sym.PLUS);*/
                        System.out.println("<plus," + yytext() + ">");}
    {multiplication}    {/*return symbol(sym.MULT);*/
                        System.out.println("<multiplication," + yytext() + ">");}
    {mod}               {/*return symbol(sym.MOD);*/
                        System.out.println("<mod," + yytext() + ">");}
    {increment}         {System.out.println("<increment," + yytext() + ">");}
    {decrement}         {System.out.println("<decrement," + yytext() + ">");}
    /* todavía en veremos */
    {conAnd}            {/*return symbol(sym.AND);*/
                        System.out.println("<conAnd," + yytext() + ">");}
    {conOr}             {/*return symbol(sym.OR);*/
                        System.out.println("<conOr," + yytext() + ">");}
    
    /* identifiers */
    {ID}               {/*return symbol(sym.IDENTIFIER);*/
                        System.out.println("<ID," + yytext() + ">");}
    {number}            {System.out.println("<number," + yytext() + ">");}

    /* comments */
    {comment}           {/* ignore */}
    
    /* whitespace */
    {WhiteSpace}        {/* ignore */}

    /* error */
    [^]                {throw new Error("Illegal character <" + yytext() + ">"
                        + " line: " + yyline + ", column: " + yycolumn); }
}


/* INVESTIGAR MÁS SOBRE LA CLASE SYM ya que es parte del CUP*/ 