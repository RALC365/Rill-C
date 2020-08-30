%%
%unicode
%class Tokens
%int
%line
%column
%standalone

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]

ID = [:jletter:] [:jletterdigit:]*

/* operators */
assignment = ":"
equalTo = "="
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


%%

/* key words */
<YYINITIAL> "br" { return symbol(sym.BREAK); }

<YYINITIAL> {
    /* identifiers */
    {ID}               {/*return symbol(sym.IDENTIFIER);*/
                        System.out.print(yytext());}

    /* operators */
    {assignment}       {/*return symbol(sym.EQ);*/
                        System.out.print(yytext());}
    {equalTo}          {/*return symbol(sym.EQEQ);*/
                        System.out.print(yytext());}
    {greaterOrEqual}   {System.out.print(yytext());}
    {lessOrEqual}      {System.out.print(yytext());}
    {lessThan}         {System.out.print(yytext());}
    {greaterThan}      {System.out.print(yytext());}
    {plus}             {/*return symbol(sym.PLUS);*/
                        System.out.print(yytext());}
    {minus}            {/*return symbol(sym.MINUS);*/
                        System.out.print(yytext());}
    {multiplication}   {/*return symbol(sym.MULT);*/
                        System.out.print(yytext());}
    {division}         {/*return symbol(sym.DIV);*/
                        System.out.print(yytext());}
    {mod}              {/*return symbol(sym.MOD);*/
                        System.out.print(yytext());}
    {increment}        {System.out.print(yytext());}
    {decrement}        {System.out.print(yytext());}
    /* todavía en veremos */
    {conAnd}           {/*return symbol(sym.AND);*/
                        System.out.print(yytext());}
    {conOr}            {/*return symbol(sym.OR);*/
                        System.out.print(yytext());}
    
    /* comments */
    {Comment}          {/* ignore */}
    
    /* whitespace */
    {WhiteSpace}       {/* ignore */}

    /* error */
    [^]                {throw new Error("Illegal character <"+ yytext()+">" 
                        + " line: " + yyline + ", column: " + yycolumn); }
}
