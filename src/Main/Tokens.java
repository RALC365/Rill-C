/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author RALC365
 */
public enum Tokens {
    /* code structure */
    Linea,
    closeBlock,
    openParentesis,
    closeParentesis,
    pyc,
    openSqrBracket,
    closeSqrBracket,
    coma,
    colon,
    quotMark,
    apostrophe,
    backslash,
    /* control statements */
    Main,
    variable,
    integer,
    Bool,
    Char,
    array,
    matrix,
    function,
    ifSwitch,
    Print,
    While,
    For,
    Default,
    Return,
    /* operators */
    equalTo,
    noEqualTo,
    orEqual,
    than,
    plus,
    multiplication,
    mod,
    increment,
    decrement,
    To,
    False,
    True,
    /* todavía en veremos */
    conAnd,
    conOr,
    /* identifier */
    ID,
    number,
    ERROR,
}
