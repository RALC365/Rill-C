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
function = "fun"
ifSwitch = "if"
print = "pp"
while = "wh"
for = "fr"
default = "df"
break = "br"

/* operators */
assignment = ":"
equalTo = "="
noEqualTo = "!="
greaterOrEqual = ">="
lessOrEqual = "<="
lessThan = "<"
greaterThan = ">"
plus = "+"
minus = "-"
multiplication = "*"
division = "/"
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
    {function}          {System.out.println("<function," + yytext() + ">");}
    {ifSwitch}          {System.out.println("<ifSwitch," + yytext() + ">");}
    {print}             {System.out.println("<print," + yytext() + ">");}
    {while}             {System.out.println("<while," + yytext() + ">");}
    {for}               {System.out.println("<for," + yytext() + ">");}
    {default}               {System.out.println("<default," + yytext() + ">");}
    {break}               {System.out.println("<break," + yytext() + ">");}

    /* operators */
    {assignment}        {/*return symbol(sym.EQ);*/
                        System.out.println("<assignment," + yytext() + ">");}
    {equalTo}           {/*return symbol(sym.EQEQ);*/
                        System.out.println("<equalTo," + yytext() + ">");}
    {noEqualTo}         {/*return symbol(sym.EQEQ);*/
                        System.out.println("<noEqualTo," + yytext() + ">");}
    {greaterOrEqual}    {System.out.println("<greaterOrEqual," + yytext() + ">");}
    {lessOrEqual}       {System.out.println("<lessOrEqual," + yytext() + ">");}
    {lessThan}          {System.out.println("<lessThan," + yytext() + ">");}
    {greaterThan}       {System.out.println("<greaterThan," + yytext() + ">");}
    {plus}              {/*return symbol(sym.PLUS);*/
                        System.out.println("<plus," + yytext() + ">");}
    {minus}             {/*return symbol(sym.MINUS);*/
                        System.out.println("<minus," + yytext() + ">");}
    {multiplication}    {/*return symbol(sym.MULT);*/
                        System.out.println("<multiplication," + yytext() + ">");}
    {division}          {/*return symbol(sym.DIV);*/
                        System.out.println("<division," + yytext() + ">");}
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


/* INVESTIGAR MÁS SOBRE LA CLASE SYM ya que es la parte del CUP*/ 