package com.language.ez.syntax;

import com.language.ez.Q;
import com.language.ez.lexical.Lexeme;
import com.language.ez.lexical.Tokens;

import java.util.*;

public abstract class Machines {
    protected static HashMap<Tokens, List<P>> operatorPrecedence = new HashMap<>();
    protected static HashMap<Tokens, Integer> precedenceColumn = new HashMap<>();
    protected static HashMap<Q, List<Q>> expressionTransition = new HashMap<>();
    protected static HashMap<Q, List<Q>> primaryTransition = new HashMap<>();
    protected static HashMap<Q, List<Q>> secondaryTransition = new HashMap<>();
    protected static HashMap<Q, List<Q>> tertiaryTransition = new HashMap<>();
    protected static List<Statements> secondaryStatements = new ArrayList<>();

    static {
                                                                       //             n    id   +    -    *    /    ^    %    <    >    <=  >=    ==   &&   ||    !   (    )    +=   -=   *=   /=   %=   |    $
        operatorPrecedence.put(Tokens.INTEGER,                         Arrays.asList(P.X, P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.X, P.G));
        operatorPrecedence.put(Tokens.BOOLEAN_TRUE_RESERVE_WORD,       Arrays.asList(P.X, P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.X, P.G));
        operatorPrecedence.put(Tokens.BOOLEAN_FALSE_RESERVE_WORD,      Arrays.asList(P.X, P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.X, P.G));
        operatorPrecedence.put(Tokens.DECIMAL,                         Arrays.asList(P.X, P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.X, P.G));
        operatorPrecedence.put(Tokens.STRING_LITERAL,                  Arrays.asList(P.X, P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.X, P.G));
        operatorPrecedence.put(Tokens.CHARACTER_LITERAL,               Arrays.asList(P.X, P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.X, P.G));
        operatorPrecedence.put(Tokens.IDENTIFIER,                      Arrays.asList(P.X, P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G));
        operatorPrecedence.put(Tokens.CONSTANT_IDENTIFIER,             Arrays.asList(P.X, P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G));
        operatorPrecedence.put(Tokens.NULLABLE_IDENTIFIER,             Arrays.asList(P.X, P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G));
        operatorPrecedence.put(Tokens.ADDITION_OPERATOR,               Arrays.asList(P.L, P.L, P.G, P.G, P.L, P.L, P.L, P.L, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.SUBTRACTION_OPERATOR,            Arrays.asList(P.L, P.L, P.G, P.G, P.L, P.L, P.L, P.L, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.MULTIPLICATION_OPERATOR,         Arrays.asList(P.L, P.L, P.G, P.G, P.G, P.G, P.L, P.L, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.DIVISION_OPERATOR,               Arrays.asList(P.L, P.L, P.G, P.G, P.G, P.G, P.L, P.L, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.EXPONENT_OPERATOR,               Arrays.asList(P.L, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.MODULO_OPERATOR,                 Arrays.asList(P.L, P.L, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.LESS_THAN_OPERATOR,              Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.MORE_THAN_OPERATOR,              Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.LESS_THAN_EQUAL_OPERATOR,        Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.MORE_THAN_EQUAL_OPERATOR,        Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.SWITCH_KEYWORD_EQUALITY_OPERATOR,Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.L, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.AND_OPERATOR,                    Arrays.asList(P.X, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.G, P.G, P.L, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.OR_OPERATOR,                     Arrays.asList(P.X, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.G, P.L, P.L, P.L, P.L, P.L, P.L, P.G, P.L, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.NOT_OPERATOR,                    Arrays.asList(P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.L, P.L, P.L, P.L, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.OPEN_PARENTHESIS_DELIMITER,      Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.E, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.CLOSE_PARENTHESIS_DELIMITER,     Arrays.asList(P.L, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.ADDITION_ASSIGNMENT,             Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.SUBTRACTION_ASSIGNMENT_OPERATOR, Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.MULTIPLICATION_ASSIGNMENT_OPERATOR,Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.DIVISION_ASSIGNMENT_OPERATOR,    Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.MODULO_ASSIGNMENT_OPERATOR,      Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.NULL_ASSERTION_DELIMITER,        Arrays.asList(P.L, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.G));
        operatorPrecedence.put(Tokens.UNKNOWN,                         Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.A));

        // columns
        precedenceColumn.put(Tokens.INTEGER, 0);
        precedenceColumn.put(Tokens.BOOLEAN_FALSE_RESERVE_WORD, 0);
        precedenceColumn.put(Tokens.BOOLEAN_TRUE_RESERVE_WORD, 0);
        precedenceColumn.put(Tokens.DECIMAL, 0);
        precedenceColumn.put(Tokens.STRING_LITERAL, 0);
        precedenceColumn.put(Tokens.CHARACTER_LITERAL, 0);
        precedenceColumn.put(Tokens.IDENTIFIER, 1);
        precedenceColumn.put(Tokens.CONSTANT_IDENTIFIER, 1);
        precedenceColumn.put(Tokens.NULLABLE_IDENTIFIER, 1);
        precedenceColumn.put(Tokens.ADDITION_OPERATOR, 2);
        precedenceColumn.put(Tokens.SUBTRACTION_OPERATOR, 3);
        precedenceColumn.put(Tokens.MULTIPLICATION_OPERATOR, 4);
        precedenceColumn.put(Tokens.DIVISION_OPERATOR, 5);
        precedenceColumn.put(Tokens.EXPONENT_OPERATOR, 6);
        precedenceColumn.put(Tokens.MODULO_OPERATOR, 7);
        precedenceColumn.put(Tokens.LESS_THAN_OPERATOR, 8);
        precedenceColumn.put(Tokens.MORE_THAN_OPERATOR, 9);
        precedenceColumn.put(Tokens.LESS_THAN_EQUAL_OPERATOR, 10);
        precedenceColumn.put(Tokens.MORE_THAN_EQUAL_OPERATOR, 11);
        precedenceColumn.put(Tokens.SWITCH_KEYWORD_EQUALITY_OPERATOR, 12);
        precedenceColumn.put(Tokens.AND_OPERATOR, 13);
        precedenceColumn.put(Tokens.OR_OPERATOR, 14);
        precedenceColumn.put(Tokens.NOT_OPERATOR, 15);
        precedenceColumn.put(Tokens.OPEN_PARENTHESIS_DELIMITER, 16);
        precedenceColumn.put(Tokens.CLOSE_PARENTHESIS_DELIMITER, 17);
        precedenceColumn.put(Tokens.ADDITION_ASSIGNMENT, 18);
        precedenceColumn.put(Tokens.SUBTRACTION_ASSIGNMENT_OPERATOR, 19);
        precedenceColumn.put(Tokens.MULTIPLICATION_ASSIGNMENT_OPERATOR, 20);
        precedenceColumn.put(Tokens.DIVISION_ASSIGNMENT_OPERATOR, 21);
        precedenceColumn.put(Tokens.MODULO_ASSIGNMENT_OPERATOR, 22);
        precedenceColumn.put(Tokens.NULL_ASSERTION_DELIMITER, 23);
        precedenceColumn.put(Tokens.UNKNOWN, 24);

        // expression transition table                 n      id    o    (     )     $     !      E     N     I     oc  aod
        expressionTransition.put(Q.S0,  Arrays.asList(Q.S1, Q.S10, Q.X, Q.S4, Q.S0, Q.S0, Q.S11, Q.S13,Q.S1, Q.S0, Q.X, Q.S0));
        expressionTransition.put(Q.S1,  Arrays.asList(Q.X,  Q.X,   Q.S2,Q.X,  Q.R1, Q.R1, Q.X,   Q.X,  Q.X,  Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S2,  Arrays.asList(Q.S1, Q.S1,  Q.X, Q.S4, Q.X, Q.X,  Q.S11, Q.S3, Q.S1,  Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S3,  Arrays.asList(Q.X,  Q.X,   Q.R2,Q.X,  Q.R2,Q.R2, Q.X,   Q.X,  Q.X,   Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S4,  Arrays.asList(Q.S5, Q.S5,  Q.X, Q.S4, Q.X, Q.X,  Q.X,   Q.S8, Q.X,   Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S5,  Arrays.asList(Q.X,  Q.X,   Q.S6,Q.X,  Q.R1,Q.X,  Q.X,   Q.X,  Q.X,   Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S6,  Arrays.asList(Q.S5, Q.S5,  Q.X, Q.S4, Q.X, Q.X,  Q.S11, Q.S7, Q.S5,  Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S7,  Arrays.asList(Q.X,  Q.X,   Q.R2,Q.X,  Q.R2,Q.X,  Q.X,   Q.X,  Q.X,   Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S8,  Arrays.asList(Q.X,  Q.X,   Q.S6,Q.X,  Q.S9,Q.X,  Q.X,   Q.X,  Q.X,   Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S9,  Arrays.asList(Q.X,  Q.X,   Q.R3,Q.X,  Q.R3,Q.R3, Q.X,   Q.X,  Q.X,   Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S10, Arrays.asList(Q.X,  Q.X,   Q.S2,Q.S0, Q.S0,Q.R4, Q.X,   Q.X,  Q.X,   Q.X,  Q.S14,Q.S15));
        expressionTransition.put(Q.S11, Arrays.asList(Q.S12,Q.S12, Q.X, Q.X,  Q.X, Q.X,  Q.X,   Q.X,  Q.X,   Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S12, Arrays.asList(Q.X,  Q.X,   Q.R5,Q.X,  Q.X, Q.R5, Q.X,   Q.X,  Q.X,   Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S13, Arrays.asList(Q.S1, Q.S10, Q.S2,Q.X,  Q.S0,Q.S0, Q.X,   Q.X,  Q.X,   Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S14, Arrays.asList(Q.X,  Q.X,   Q.X, Q.X,  Q.X, Q.R6, Q.X,   Q.X,  Q.X,   Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S15, Arrays.asList(Q.X,  Q.S16, Q.X, Q.X,  Q.X, Q.X,  Q.X,   Q.X,  Q.X,   Q.X,  Q.X, Q.X));
        expressionTransition.put(Q.S16, Arrays.asList(Q.X,  Q.X,   Q.R7,Q.S0, Q.X, Q.R7,  Q.X,   Q.X,  Q.X,   Q.X,  Q.X, Q.X));

        // transition for primary statements        E     to   id    ,    as  typ    .      if   els  ind    pr     :    $     D      V     F    EF    P    [    ]    AE     A     AR  ct   frm   by    c
        primaryTransition.put(Q.S0,  Arrays.asList(Q.S1, Q.S0,Q.S45,Q.S0, Q.X, Q.S0, Q.S0,Q.S12,Q.S15,Q.S0, Q.S17,Q.S0, Q.S0, Q.X,  Q.S0, Q.S0, Q.S0,Q.S0,Q.S21,Q.S0, Q.X,  Q.S1, Q.S1,Q.S40,Q.X, Q.X, Q.S0));
        primaryTransition.put(Q.S1,  Arrays.asList(Q.X,  Q.S2,Q.X,  Q.S0, Q.X, Q.X, Q.S11,Q.X,  Q.X,  Q.X,  Q.X,  Q.S0, Q.S0, Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S2,  Arrays.asList(Q.X,  Q.X, Q.S3, Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S5, Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.S5,Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S3,  Arrays.asList(Q.X,  Q.X, Q.X,  Q.S4, Q.R1,Q.X, Q.R1, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.S27,Q.X,  Q.S30,Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S4,  Arrays.asList(Q.X,  Q.X, Q.S3, Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S10,Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S5,  Arrays.asList(Q.X,  Q.X, Q.X,  Q.X,  Q.S7,Q.X, Q.S6, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S6,  Arrays.asList(Q.R2, Q.X, Q.R2, Q.X,  Q.X, Q.X, Q.X,  Q.R2, Q.R2, Q.R2, Q.R2, Q.S0, Q.R2, Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.R2, Q.X,  Q.X,  Q.X,  Q.X, Q.R2,Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S7,  Arrays.asList(Q.X,  Q.X, Q.X,  Q.X,  Q.X, Q.S8,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S8,  Arrays.asList(Q.X,  Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.S9, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S9,  Arrays.asList(Q.R3, Q.X, Q.R3, Q.X,  Q.X, Q.X, Q.X,  Q.R3, Q.R3, Q.R3, Q.R3, Q.S0, Q.R3, Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.R3, Q.X,  Q.X,  Q.X,  Q.X, Q.R3,Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S10, Arrays.asList(Q.X,  Q.X, Q.X,  Q.X,  Q.R4,Q.X, Q.R4, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S11, Arrays.asList(Q.R5, Q.X, Q.R5, Q.X,  Q.X, Q.X, Q.X,  Q.R5, Q.R5, Q.R5, Q.R5, Q.X,  Q.R5, Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.R5, Q.X,  Q.X,  Q.X,  Q.X, Q.R5, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S12, Arrays.asList(Q.S13,Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S13, Arrays.asList(Q.X,  Q.X, Q.X,  Q.S14,Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S14, Arrays.asList(Q.R6, Q.X, Q.R6, Q.X,  Q.X, Q.X, Q.X,  Q.R6, Q.X,  Q.R6, Q.R6, Q.X,  Q.R6, Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.R6, Q.X,  Q.X,  Q.X,  Q.X, Q.R6,Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S15, Arrays.asList(Q.X,  Q.X, Q.X,  Q.S41,Q.X, Q.X, Q.X,  Q.S12,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S16,Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S16, Arrays.asList(Q.R7, Q.X, Q.R7, Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.R7, Q.R7, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.R7, Q.X,  Q.X,  Q.X,  Q.X, Q.R7,Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S17, Arrays.asList(Q.X,  Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S18,Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S18, Arrays.asList(Q.S19,Q.X, Q.S19,Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S19,Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.S19,Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S19, Arrays.asList(Q.X,  Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.S20,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.S21,Q.X,  Q.X,  Q.S30,Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S20, Arrays.asList(Q.R8, Q.X, Q.R8, Q.X,  Q.X, Q.X, Q.X,  Q.R8, Q.R8, Q.R8, Q.R8, Q.X,  Q.R8, Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.R8, Q.X,  Q.X,  Q.X,  Q.X, Q.R8,Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S21, Arrays.asList(Q.S22,Q.X, Q.S22,Q.X,  Q.X, Q.S42,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.S44,Q.S23,Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S22, Arrays.asList(Q.X,  Q.X, Q.X,  Q.S24,Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S0, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.R9, Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S23, Arrays.asList(Q.X,  Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.S26,Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S24, Arrays.asList(Q.S22,Q.X, Q.S22,Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.S25,Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S25, Arrays.asList(Q.X,  Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.R10,Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S26, Arrays.asList(Q.X,  Q.R11,Q.X,  Q.X,  Q.X, Q.X, Q.R11,Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.R11,Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S27, Arrays.asList(Q.S28,Q.X,  Q.S28,Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S28, Arrays.asList(Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.S29,Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S29, Arrays.asList(Q.X,  Q.R12,Q.X,  Q.X, Q.R12, Q.X, Q.R12,Q.X,Q.X,  Q.X,  Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S30, Arrays.asList(Q.X,  Q.R13,Q.X,  Q.X, Q.R13,Q.X, Q.R13,Q.X,Q.X,  Q.X,  Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S31, Arrays.asList(Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.S32,Q.X, Q.X));
        primaryTransition.put(Q.S32, Arrays.asList(Q.S33,Q.X,  Q.S33,Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S33, Arrays.asList(Q.X,  Q.S34,Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S34, Arrays.asList(Q.S35,Q.X,  Q.S35,Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S35, Arrays.asList(Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.S36,Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.S37,Q.X));
        primaryTransition.put(Q.S36, Arrays.asList(Q.R14,Q.X,  Q.R14,Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.R14,Q.R14,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.R14,Q.X,  Q.X,  Q.X,  Q.X, Q.R14,Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S37, Arrays.asList(Q.S38,Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S38, Arrays.asList(Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.S39,Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S39, Arrays.asList(Q.R15,Q.X,  Q.R15,Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.R15,Q.R15,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.R15,Q.X,  Q.X,  Q.X,  Q.X, Q.R15,Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S40, Arrays.asList(Q.X,  Q.X,  Q.S31,Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S41, Arrays.asList(Q.R17,Q.X,  Q.R17,Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.R17,Q.R17,Q.X,  Q.R17,Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.R17,Q.X,  Q.X,  Q.X,  Q.X, Q.R17,Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S42, Arrays.asList(Q.X,  Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.S43,Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S43, Arrays.asList(Q.X,  Q.X, Q.X,  Q.R18,Q.X, Q.X, Q.R18,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R18,Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S44, Arrays.asList(Q.X,  Q.R19,Q.X, Q.X,  Q.X, Q.X, Q.R19,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));
        primaryTransition.put(Q.S45, Arrays.asList(Q.X,  Q.S2,Q.X, Q.S0,  Q.X, Q.X, Q.S0, Q.X,  Q.X,  Q.X,  Q.X,  Q.S0, Q.S0, Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.S27,Q.X,  Q.S30, Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X));


        // transition for functions, and loops        E     id     fc     w     do  put     in    :     V    task   mod   {     }    dt    (    )    ,   ind   pr    out   .   dd   P     $    P    PL
        secondaryTransition.put(Q.S0,  Arrays.asList(Q.S0, Q.S29, Q.S1, Q.S6, Q.S9,Q.S11, Q.X,  Q.S0, Q.S0, Q.S13,Q.S13,Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.S0,Q.S0,Q.S0, Q.S26,Q.S0,Q.S0,Q.X, Q.S0, Q.X, Q.X));
        secondaryTransition.put(Q.S1,  Arrays.asList(Q.X,  Q.S2,  Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X));
        secondaryTransition.put(Q.S2,  Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.S3, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X));
        secondaryTransition.put(Q.S3,  Arrays.asList(Q.X,  Q.S4,  Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X));
        secondaryTransition.put(Q.S4,  Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.S5, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X));
        secondaryTransition.put(Q.S5,  Arrays.asList(Q.R1, Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.R1, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.R1,Q.R1, Q.X,  Q.X, Q.X, Q.X, Q.R1, Q.X, Q.X));
        secondaryTransition.put(Q.S6,  Arrays.asList(Q.S7, Q.S7,  Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X));
        secondaryTransition.put(Q.S7,  Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.S8, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X));
        secondaryTransition.put(Q.S8,  Arrays.asList(Q.R2, Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.R2, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.R2,Q.R2, Q.X,  Q.X, Q.X, Q.X, Q.R2, Q.X, Q.X));
        secondaryTransition.put(Q.S9,  Arrays.asList(Q.X,  Q.X,   Q.X,  Q.S29,Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X));
        secondaryTransition.put(Q.S10, Arrays.asList(Q.R3, Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.R3, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.R3,Q.R3, Q.X,  Q.X, Q.X, Q.X, Q.R3, Q.X, Q.X));
        secondaryTransition.put(Q.S11, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.S12,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X));
        secondaryTransition.put(Q.S12, Arrays.asList(Q.R4, Q.R4,  Q.R4, Q.X,  Q.X, Q.R4,  Q.X,  Q.X,  Q.R4, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.R4,Q.R4, Q.R4, Q.X, Q.R4,Q.X, Q.R4, Q.X, Q.X));

        //                                             E     id    fc     w    do  put     in    :     V    task   mod   {     }    dt    (       )    ,     ind   pr    out   .   dd   P     $    P    PL
        secondaryTransition.put(Q.S13, Arrays.asList(Q.X,  Q.S14, Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X,   Q.X, Q.X));
        secondaryTransition.put(Q.S14, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.S43,Q.X,  Q.X,  Q.X,  Q.S15,Q.X,  Q.X,  Q.S15,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X,   Q.X, Q.X));
        secondaryTransition.put(Q.S15, Arrays.asList(Q.X,  Q.S16, Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S31,Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.S19,Q.S22,Q.X, Q.X));
        secondaryTransition.put(Q.S16, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.S17,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S17, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S18,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S18, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R5, Q.X,  Q.X,  Q.R5, Q.R5, Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S19, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R6, Q.X,  Q.X,  Q.R6, Q.S20,Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X,  Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S20, Arrays.asList(Q.X,  Q.S16, Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.S19,Q.S21, Q.X, Q.X));
        secondaryTransition.put(Q.S21, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R7, Q.X,  Q.X,  Q.R7, Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S22, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S25,Q.X,  Q.X,  Q.S23,Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S23, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.S24,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S24, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.R8, Q.R8, Q.R8, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R8, Q.R8, Q.R8, Q.X, Q.X, Q.X, Q.R8, Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S25, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.R9, Q.R9, Q.R9, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R9, Q.R9, Q.R9, Q.X, Q.X, Q.X, Q.R9, Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S26, Arrays.asList(Q.S27,Q.S27, Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S27, Arrays.asList(Q.R11,Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.R11,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R11,Q.X,  Q.S28,Q.R11,Q.X,Q.X, Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S28, Arrays.asList(Q.R10,Q.X,   Q.R10,Q.R10,Q.R10,Q.R10,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,Q.R10,Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S29, Arrays.asList(Q.S30,Q.S30, Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.S0, Q.X,  Q.X,  Q.X,  Q.S33,Q.X,  Q.X,  Q.S33,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.S0, Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S30, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.S10,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S31, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.S32,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S32, Arrays.asList(Q.X,  Q.R12, Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.R12,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R12,Q.R12,Q.R12,Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S33, Arrays.asList(Q.S34,Q.S34, Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S44,Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.S36, Q.X));
        secondaryTransition.put(Q.S34, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R13,Q.X,  Q.X,  Q.R13,Q.S35,Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S35, Arrays.asList(Q.S34,Q.S34, Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.S39, Q.X));
        secondaryTransition.put(Q.S36, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S38,Q.X,  Q.X,  Q.S38,Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X, Q.X, Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S37, Arrays.asList(Q.R14,Q.R14, Q.R14,Q.R14,Q.R14,Q.R14,Q.X,  Q.X,  Q.R14,Q.R14,Q.R14,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R14,Q.X,  Q.R14,Q.R14,Q.X, Q.R14,Q.X,Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S38, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S37,Q.X, Q.X,Q.S37,Q.X, Q.X, Q.X, Q.S40));
        secondaryTransition.put(Q.S39, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R15,Q.X,  Q.X,  Q.R15,Q.X,  Q.X,  Q.X,  Q.X,  Q.S37,Q.X, Q.X,Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S40, Arrays.asList(Q.X,  Q.S41, Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X, Q.X,Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S41, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S42,Q.X, Q.X,Q.X,  Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S42, Arrays.asList(Q.R16,Q.R16, Q.R16,Q.R16,Q.R16,Q.R16,Q.X,  Q.X,  Q.R16,Q.R16,Q.R16,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R16,Q.X,  Q.R16,Q.X,  Q.X,  Q.R16,Q.X,Q.R16, Q.X, Q.X,Q.X, Q.X));
        secondaryTransition.put(Q.S43, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R17,Q.X,  Q.X,  Q.X,  Q.X, Q.X,Q.X,Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S44, Arrays.asList(Q.X,  Q.X,   Q.X,  Q.X,  Q.X, Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S45,Q.X, Q.X,Q.S45,Q.X, Q.X, Q.X, Q.X));
        secondaryTransition.put(Q.S45, Arrays.asList(Q.R18,Q.R18, Q.R18,Q.R18,Q.R18,Q.R18,Q.X,  Q.X,  Q.R18,Q.R18,Q.R18,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R18,Q.X,  Q.R18,Q.R18,Q.X, Q.R18,Q.X,Q.X,  Q.X, Q.X, Q.X, Q.X));

        secondaryStatements.add(Statements.FOREACH_LOOP_DECLARATION);
        secondaryStatements.add(Statements.WHILE_LOOP_DECLARATION);
        secondaryStatements.add(Statements.DO_WHILE_LOOP_DECLARATION);
        secondaryStatements.add(Statements.INPUT_STATEMENT);

        // function calls                           id   ->     FC      $   FS   NF    s      .     w    ar     n     :     ,     exp   ind   dd
        tertiaryTransition.put(Q.S0, Arrays.asList(Q.S1, Q.X,  Q.S0, Q.S0, Q.X,  Q.S0, Q.S6, Q.X,  Q.S8, Q.S11,Q.S14,Q.S0, Q.X,  Q.X,  Q.S16,Q.S0));
        tertiaryTransition.put(Q.S1, Arrays.asList(Q.X,  Q.S2, Q.X,  Q.S0, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X));
        tertiaryTransition.put(Q.S2, Arrays.asList(Q.X,  Q.X,  Q.S3, Q.S0, Q.S5, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X));
        tertiaryTransition.put(Q.S3, Arrays.asList(Q.X,  Q.X,  Q.S3, Q.R1, Q.S4, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X));
        tertiaryTransition.put(Q.S4, Arrays.asList(Q.X,  Q.X,  Q.X,  Q.R2, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X));
        tertiaryTransition.put(Q.S5, Arrays.asList(Q.X,  Q.X,  Q.X,  Q.R3, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X));
        tertiaryTransition.put(Q.S6, Arrays.asList(Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S7, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X));
        tertiaryTransition.put(Q.S7, Arrays.asList(Q.R4, Q.X,  Q.R4, Q.R4, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R4, Q.X,  Q.R4));
        tertiaryTransition.put(Q.S8, Arrays.asList(Q.S9, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S9, Q.X,  Q.X));
        tertiaryTransition.put(Q.S9, Arrays.asList(Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S10,Q.X,  Q.X,  Q.X,  Q.X));
        tertiaryTransition.put(Q.S10,Arrays.asList(Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R5, Q.X));
        tertiaryTransition.put(Q.S11,Arrays.asList(Q.S12,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S12,Q.X,  Q.X));
        tertiaryTransition.put(Q.S12,Arrays.asList(Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S13,Q.X,  Q.X,  Q.X));
        tertiaryTransition.put(Q.S13,Arrays.asList(Q.R6, Q.X,  Q.X,  Q.R6, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R6, Q.X));
        tertiaryTransition.put(Q.S14,Arrays.asList(Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S15,Q.X,  Q.X,  Q.X));
        tertiaryTransition.put(Q.S15,Arrays.asList(Q.X,  Q.X,  Q.X,  Q.R7, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.R7, Q.X));
        tertiaryTransition.put(Q.S16,Arrays.asList(Q.S1, Q.X,  Q.S0, Q.S0, Q.X,  Q.S0, Q.S6, Q.X,  Q.S8, Q.S11,Q.S14,Q.S0, Q.X,  Q.X,  Q.X,  Q.X));
    }

    // -----------------------------------------------------------------------------------------------------
    // Machines for Mathematical, Logical, and Relational Expressions

    public int getPrecedenceColumn(Tokens token) {
        Integer column = precedenceColumn.get(token);

        if (column != null) return column;
        return 23;
    }

    public P getOperatorPrecedence(Tokens stackToken, Tokens currentToken) {
        return operatorPrecedence.get(stackToken).get(getPrecedenceColumn(currentToken));
    }

    public Node assertOperatorPrecedence(Node node) throws Exception {
        if (node.getType().equals(Statements.EXPRESSION) && node.getChildren() != null) {
            List<Node> inputTape = new ArrayList<>();

            while (!node.getChildren().empty()) inputTape.add(node.getChildren().pop());
            inputTape.add(new Node(Statements.UNKNOWN, new Lexeme(Tokens.UNKNOWN), null));

            Stack<Node> buffer = new Stack<>();
            Stack<Node> children = new Stack<>();

            buffer.push(new Node(Statements.UNKNOWN, new Lexeme(Tokens.UNKNOWN), null));

            int index = 0;
            while (index < inputTape.size()) {
                Node current = inputTape.get(index);

                P precedence = getOperatorPrecedence(buffer.peek().getLexeme().getToken(), current.getLexeme().getToken());

                System.out.println(buffer.peek().getLexeme().getToken() + " | " +  current.getLexeme().getToken());

                switch (precedence) {
                    case L -> {
                        buffer.push(current);
                        index++;
                    }
                    case G -> {
                        Node popped = buffer.pop();
                        children.push(popped);
                    }
                    case E -> {
                        buffer.pop();
                        index++;
                    }
                    case A -> {
                        return new Node(Statements.EXPRESSION, node.getLexeme(), node.getState(), children);
                    }
                    case X -> throw new Exception("Syntax error in line " + current.getLexeme().getLine() + " column " + current.getLexeme().getColumn() + ".");
                    default -> index++;
                }
            }
        }

        return node;
    }

    public int getExpressionColumn(Node node) {
        if (node.getLexeme() != null) {
            Tokens token = node.getLexeme().getToken();

            switch (token) {
                case INTEGER, DECIMAL, CHARACTER_LITERAL, STRING_LITERAL, BOOLEAN_FALSE_RESERVE_WORD, BOOLEAN_TRUE_RESERVE_WORD -> {
                    return 0;
                }
                case IDENTIFIER, CONSTANT_IDENTIFIER, NULLABLE_IDENTIFIER -> {
                    return 1;
                }
                case ADDITION_OPERATOR, SUBTRACTION_OPERATOR, MULTIPLICATION_OPERATOR, DIVISION_OPERATOR, EXPONENT_OPERATOR,
                        MODULO_OPERATOR, LESS_THAN_OPERATOR, MORE_THAN_OPERATOR, LESS_THAN_EQUAL_OPERATOR, MORE_THAN_EQUAL_OPERATOR,
                        SWITCH_KEYWORD_EQUALITY_OPERATOR, AND_OPERATOR, OR_OPERATOR, ADDITION_ASSIGNMENT, SUBTRACTION_ASSIGNMENT_OPERATOR,
                        MULTIPLICATION_ASSIGNMENT_OPERATOR, DIVISION_ASSIGNMENT_OPERATOR, MODULO_ASSIGNMENT_OPERATOR, NULL_ASSERTION_DELIMITER -> {
                    return 2;
                }
                case OPEN_PARENTHESIS_DELIMITER -> {
                    return 3;
                }
                case CLOSE_PARENTHESIS_DELIMITER -> {
                    return 4;
                }
                case NOT_OPERATOR -> {
                    return 6;
                }
                case INCREMENT_OPERATOR, DECREMENT_OPERATOR -> {
                    return 10;
                }
                case ACCESS_OBJECT_DELIMITER -> {
                    return 11;
                }
                default -> {
                    return 5;
                }
            }
        }
        else if (node.getType().equals(Statements.EXPRESSION)) return 7;
        else if (node.getType().equals(Statements.NOT_EXPRESSION)) return 8;
        else if (node.getType().equals(Statements.ID)) return 9;

        return 5;
    }

    public Q getExpressionTransition(Q state, Node node) {
        return expressionTransition.get(state).get(getExpressionColumn(node));
    }

    abstract public void analyzeExpressions() throws Exception;

    // --------------------------------------------------------------------------------------------------------------
    // Machines for Primary Statements such as Variable Declarations, If-Declaration, and Loop Declaration

    public int getPrimaryColumn(Node node) {
        if (node.getLexeme() != null) {
            switch (node.getLexeme().getToken()) {
                case NULL_TYPE, BOOLEAN_FALSE_RESERVE_WORD, BOOLEAN_TRUE_RESERVE_WORD -> {
                    return 0;
                }
                case ASSIGN_OR_SCOPE_KEYWORD -> {
                    return 1;
                }
                case IDENTIFIER, NULLABLE_IDENTIFIER, CONSTANT_IDENTIFIER -> {
                    return 2;
                }
                case COMMA_DELIMITER -> {
                    return 3;
                }
                case ASSIGN_TYPE_KEYWORD -> {
                    return 4;
                }
                case DEC_DATA_TYPE, BTN_DATA_TYPE, INT_DATA_TYPE, LTR_DATA_TYPE, WRD_DATA_TYPE, DYNAMIC_TYPE_RESERVE_WORD -> {
                    return 5;
                }
                case STATEMENT_END_DELIMITER -> {
                    return 6;
                }
                case IF_KEYWORD -> {
                    return 7;
                }
                case ELIF_KEYWORD -> {
                    return 8;
                }
                case INDENT_DELIMITER -> {
                    return 9;
                }
                case PRINT_KEYWORD -> {
                    return 10;
                }
                case COLON_DELIMITER -> {
                    return 11;
                }
                case OPEN_LIST_DELIMITER -> {
                    return 18;
                }
                case CLOSE_LIST_DELIMITER -> {
                    return 19;
                }
                case COUNT_LOOP_KEYWORD -> {
                    return 23;
                }
                case FROM_SCOPE_KEYWORD -> {
                    return 24;
                }
                case LOOP_STEP_KEYWORD -> {
                    return 25;
                }
                default -> {
                    return 12;
                }
            }
        } else {
            switch (node.getType()) {
                case EXPRESSION, INCREMENT_DECREMENT_STATEMENT -> {
                    return 0;
                }
                case ID -> {
                    return 2;
                }
                case IDENTIFIER -> {
                    return 13;
                }
                case VARIABLE_DECLARATION, ASSIGNMENT_DECLARATION -> {
                    return 14;
                }
                case IF_DECLARATION -> {
                    return 15;
                }
                case ELIF_DECLARATION -> {
                    return 16;
                }
                case PRINT_STATEMENT -> {
                    return 17;
                }
                case ARRAY_ELEMENTS -> {
                    return 20;
                }
                case ARRAY_DECLARATION -> {
                    return 21;
                }
                case ACCESS_ARRAY -> {
                    return 22;
                }
                case FOR_LOOP_DECLARATION -> {
                    return 26;
                }
                default -> {
                    return 12;
                }
            }
        }
    }

    public Q getPrimaryTransition(Q state, Node node) {
        return primaryTransition.get(state).get(getPrimaryColumn(node));
    }

    abstract public void analyzePrimaryStatements() throws Exception;

    // Secondary Machine
    public int getSecondaryColumn(Node node) {
        if (node.getLexeme() != null) {
            switch (node.getLexeme().getToken()) {
                case NULL_TYPE -> {
                    return 0;
                }
                case IDENTIFIER -> {
                    return 1;
                }
                case FOREACH_LOOP_KEYWORD -> {
                    return 2;
                }
                case WHILE_LOOP_KEYWORD -> {
                    return 3;
                }
                case DO_WHILE_LOOP_KEYWORD -> {
                    return 4;
                }
                case SCANNER_DECLARATION_KEYWORD -> {
                    return 5;
                }
                case SCOPE_KEYWORD -> {
                    return 6;
                }
                case COLON_DELIMITER -> {
                    return 7;
                }
                case FUNCTION_DECLARATION_KEYWORD -> {
                    return 9;
                }
                case MODEL_DECLARATION_KEYWORD -> {
                    return 10;
                }
                case OPEN_DICT_DELIMITER -> {
                    return 11;
                }
                case CLOSE_DICT_DELIMITER -> {
                    return 12;
                }
                case DEC_DATA_TYPE, BTN_DATA_TYPE, INT_DATA_TYPE, LTR_DATA_TYPE, WRD_DATA_TYPE  -> {
                    return 13;
                }
                case OPEN_PARENTHESIS_DELIMITER -> {
                    return 14;
                }
                case CLOSE_PARENTHESIS_DELIMITER -> {
                    return 15;
                }
                case COMMA_DELIMITER -> {
                    return 16;
                }
                case INDENT_DELIMITER -> {
                    return 17;
                }
                case PRINT_KEYWORD -> {
                    return 18;
                }
                case RETURN_STATEMENT_KEYWORD -> {
                    return 19;
                }
                case STATEMENT_END_DELIMITER -> {
                    return 20;
                }
                case DEDENT_DELIMITER -> {
                    return 21;
                }
                case ASSIGN_OR_SCOPE_KEYWORD -> {
                    return 27;
                }
                default -> {
                    return 23;
                }
            }
        } else {
            switch (node.getType()) {
                case EXPRESSION, ASSIGNMENT_DECLARATION -> {
                    return 0;
                }
                case ARRAY_DATA_TYPE -> {
                    return 13;
                }
                case ID -> {
                    return 1;
                }
                case VARIABLE_DECLARATION -> {
                    return 8;
                }
                case PRINT_STATEMENT -> {
                    return 18;
                }
                case FUNCTION_CALL_PARAMS -> {
                    return 22;
                }
                case FUNCTION_PARAM -> {
                    return 24;
                }
                case FUNCTION_PARAM_LIST -> {
                    return 25;
                }
                case FUNCTION_CALL_PARAM -> {
                    return 26;
                }
                default -> {
                    return 23;
                }
            }
        }
    }

    public Q getSecondaryTransition(Q state, Node node) {

        if (secondaryStatements.contains(node.getType())) return Q.S0;

        return secondaryTransition.get(state).get(getSecondaryColumn(node));
    }

    // third machine
    public int getTertiaryColumn(Node node) {
        if (node.getLexeme() != null) {
            switch (node.getLexeme().getToken()) {
                case IDENTIFIER -> {
                    return 0;
                }
                case ACCESS_OBJECT_DELIMITER -> {
                    return 1;
                }
                case BREAK_LOOP_KEYWORD -> {
                    return 6;
                }
                case STATEMENT_END_DELIMITER -> {
                    return 7;
                }
                case SWITCH_STATEMENT_KEYWORD -> {
                    return 8;
                }
                case SWITCH_CASE_KEYWORD -> {
                    return 9;
                }
                case SWITCH_DEFAULT_KEYWORD -> {
                    return 10;
                }
                case COLON_DELIMITER -> {
                    return 11;
                }
                case COMMA_DELIMITER -> {
                    return 12;
                }
                case INDENT_DELIMITER -> {
                    return 14;
                }
                case DEDENT_DELIMITER -> {
                    return 15;
                }
                default -> {
                    return 3;
                }
            }
        } else {
            switch (node.getType()) {
                case ID -> {
                    return 0;
                }
                case FUNCTION_CALL -> {
                    return 2;
                }
                case NESTED_FUNCTION_ELEMENTS -> {
                    return 4;
                }
                case NESTED_FUNCTIONS -> {
                    return 5;
                }
                case EXPRESSION -> {
                    return 13;
                }
                default -> {
                    return 3;
                }
            }
        }
    }

    public Q getTertiaryTransition(Q state, Node node) {
        return tertiaryTransition.get(state).get(getTertiaryColumn(node));
    }

    abstract public void analyzeSecondaryStatements() throws Exception;

    abstract public Node nodeReducer(Statements type, int length);
}
