package com.language.ez.syntax;

import com.language.ez.Q;
import com.language.ez.lexical.Tokens;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Machines {
    protected static HashMap<Tokens, List<P>> operatorPrecedence = new HashMap<>();
    protected static HashMap<Tokens, Integer> precedenceColumn = new HashMap<>();
    protected static HashMap<Q, List<Q>> expressionTransition = new HashMap<>();

    static {
                                                                       //             n    +    -    *    /    ^    %    <    >    <=  >=    ==   &&   ||    !   (    )    +=   -=   *=   /=   %=   |    $
        operatorPrecedence.put(Tokens.INTEGER,                         Arrays.asList(P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G));
        operatorPrecedence.put(Tokens.DECIMAL,                         Arrays.asList(P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G));
        operatorPrecedence.put(Tokens.STRING_LITERAL,                  Arrays.asList(P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G));
        operatorPrecedence.put(Tokens.CHARACTER_LITERAL,               Arrays.asList(P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G));
        operatorPrecedence.put(Tokens.IDENTIFIER,                      Arrays.asList(P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G));
        operatorPrecedence.put(Tokens.CONSTANT_IDENTIFIER,             Arrays.asList(P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G));
        operatorPrecedence.put(Tokens.NULLABLE_IDENTIFIER,             Arrays.asList(P.X, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G));
        operatorPrecedence.put(Tokens.ADDITION_OPERATOR,               Arrays.asList(P.L, P.G, P.G, P.L, P.L, P.L, P.L, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.SUBTRACTION_OPERATOR,            Arrays.asList(P.L, P.G, P.G, P.L, P.L, P.L, P.L, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.MULTIPLICATION_OPERATOR,         Arrays.asList(P.L, P.G, P.G, P.G, P.G, P.L, P.L, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.DIVISION_OPERATOR,               Arrays.asList(P.L, P.G, P.G, P.G, P.G, P.L, P.L, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.EXPONENT_OPERATOR,               Arrays.asList(P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.MODULO_OPERATOR,                 Arrays.asList(P.L, P.G, P.G, P.G, P.G, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.X, P.X, P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.LESS_THAN_OPERATOR,              Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.MORE_THAN_OPERATOR,              Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.LESS_THAN_EQUAL_OPERATOR,        Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.MORE_THAN_EQUAL_OPERATOR,        Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.SWITCH_KEYWORD_EQUALITY_OPERATOR,Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.AND_OPERATOR,                    Arrays.asList(P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.L, P.L, P.L, P.L, P.G, P.G, P.L, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.OR_OPERATOR,                     Arrays.asList(P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.L, P.L, P.L, P.L, P.L, P.G, P.L, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.NOT_OPERATOR,                    Arrays.asList(P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.L, P.L, P.L, P.L, P.G, P.G, P.G, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.OPEN_PARENTHESIS_DELIMITER,      Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.E, P.G, P.G, P.G, P.G, P.G, P.L, P.G));
        operatorPrecedence.put(Tokens.CLOSE_PARENTHESIS_DELIMITER,     Arrays.asList(P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.ADDITION_ASSIGNMENT,             Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.SUBTRACTION_ASSIGNMENT_OPERATOR, Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.MULTIPLICATION_ASSIGNMENT_OPERATOR,Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.DIVISION_ASSIGNMENT_OPERATOR,    Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.MODULO_ASSIGNMENT_OPERATOR,      Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.X, P.L, P.G, P.X, P.X, P.X, P.X, P.X, P.L, P.G));
        operatorPrecedence.put(Tokens.NULL_ASSERTION_DELIMITER,        Arrays.asList(P.L, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.G, P.X, P.G));
        operatorPrecedence.put(Tokens.UNKNOWN,                         Arrays.asList(P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.L, P.X));

        // columns
        precedenceColumn.put(Tokens.INTEGER, 0);
        precedenceColumn.put(Tokens.DECIMAL, 0);
        precedenceColumn.put(Tokens.STRING_LITERAL, 0);
        precedenceColumn.put(Tokens.CHARACTER_LITERAL, 0);
        precedenceColumn.put(Tokens.IDENTIFIER, 0);
        precedenceColumn.put(Tokens.CONSTANT_IDENTIFIER, 0);
        precedenceColumn.put(Tokens.NULLABLE_IDENTIFIER, 0);
        precedenceColumn.put(Tokens.ADDITION_OPERATOR, 1);
        precedenceColumn.put(Tokens.SUBTRACTION_OPERATOR, 2);
        precedenceColumn.put(Tokens.MULTIPLICATION_OPERATOR, 3);
        precedenceColumn.put(Tokens.DIVISION_OPERATOR, 4);
        precedenceColumn.put(Tokens.EXPONENT_OPERATOR, 5);
        precedenceColumn.put(Tokens.MODULO_OPERATOR, 6);
        precedenceColumn.put(Tokens.LESS_THAN_OPERATOR, 7);
        precedenceColumn.put(Tokens.MORE_THAN_OPERATOR, 8);
        precedenceColumn.put(Tokens.LESS_THAN_EQUAL_OPERATOR, 9);
        precedenceColumn.put(Tokens.MORE_THAN_EQUAL_OPERATOR, 10);
        precedenceColumn.put(Tokens.SWITCH_KEYWORD_EQUALITY_OPERATOR, 11);
        precedenceColumn.put(Tokens.AND_OPERATOR, 12);
        precedenceColumn.put(Tokens.OR_OPERATOR, 13);
        precedenceColumn.put(Tokens.NOT_OPERATOR, 14);
        precedenceColumn.put(Tokens.OPEN_PARENTHESIS_DELIMITER, 15);
        precedenceColumn.put(Tokens.CLOSE_PARENTHESIS_DELIMITER, 16);
        precedenceColumn.put(Tokens.ADDITION_ASSIGNMENT, 17);
        precedenceColumn.put(Tokens.SUBTRACTION_ASSIGNMENT_OPERATOR, 18);
        precedenceColumn.put(Tokens.MULTIPLICATION_ASSIGNMENT_OPERATOR, 19);
        precedenceColumn.put(Tokens.DIVISION_ASSIGNMENT_OPERATOR, 20);
        precedenceColumn.put(Tokens.MODULO_ASSIGNMENT_OPERATOR, 21);
        precedenceColumn.put(Tokens.NULL_ASSERTION_DELIMITER, 22);
        precedenceColumn.put(Tokens.UNKNOWN, 23);

        // expression transition table                 n      id    o    (     )     $     !      E     N     I
        expressionTransition.put(Q.S0,  Arrays.asList(Q.S1, Q.S10, Q.X, Q.S4, Q.X, Q.S0, Q.S11, Q.S13,Q.S1, Q.S0));
        expressionTransition.put(Q.S1,  Arrays.asList(Q.X,  Q.X,   Q.S2,Q.X,  Q.X, Q.R1, Q.X,   Q.X,  Q.X,  Q.X ));
        expressionTransition.put(Q.S2,  Arrays.asList(Q.S1, Q.S1,  Q.X, Q.S4, Q.X, Q.X,  Q.S11, Q.S3, Q.S1, Q.X ));
        expressionTransition.put(Q.S3,  Arrays.asList(Q.X,  Q.X,   Q.R2,Q.X,  Q.X, Q.R2, Q.X,   Q.X,  Q.X,  Q.X ));
        expressionTransition.put(Q.S4,  Arrays.asList(Q.S5, Q.S5,  Q.X, Q.S4, Q.X, Q.X,  Q.X,   Q.S8, Q.X,  Q.X ));
        expressionTransition.put(Q.S5,  Arrays.asList(Q.X,  Q.X,   Q.S6,Q.X,  Q.R1,Q.X,  Q.X,   Q.X,  Q.X,  Q.X ));
        expressionTransition.put(Q.S6,  Arrays.asList(Q.S5, Q.S5,  Q.X, Q.S4, Q.X, Q.X,  Q.S11, Q.S7, Q.S5, Q.X ));
        expressionTransition.put(Q.S7,  Arrays.asList(Q.X,  Q.X,   Q.X, Q.X,  Q.R2,Q.X,  Q.X,   Q.X,  Q.X,  Q.X ));
        expressionTransition.put(Q.S8,  Arrays.asList(Q.X,  Q.X,   Q.S6,Q.X,  Q.S9,Q.X,  Q.X,   Q.X,  Q.X,  Q.X ));
        expressionTransition.put(Q.S9,  Arrays.asList(Q.X,  Q.X,   Q.R3,Q.X,  Q.R3,Q.R3, Q.X,   Q.X,  Q.X,  Q.X ));
        expressionTransition.put(Q.S10, Arrays.asList(Q.X,  Q.X,   Q.S2,Q.X,  Q.X, Q.R4, Q.X,   Q.X,  Q.X,  Q.X ));
        expressionTransition.put(Q.S11, Arrays.asList(Q.X,  Q.S12, Q.X, Q.X,  Q.X, Q.X,  Q.X,   Q.X,  Q.X,  Q.X ));
        expressionTransition.put(Q.S12, Arrays.asList(Q.X,  Q.X,   Q.R5,Q.X,  Q.X, Q.R5, Q.X,   Q.X,  Q.X,  Q.X ));
        expressionTransition.put(Q.S13, Arrays.asList(Q.S1, Q.S10, Q.S2,Q.X,  Q.X, Q.S0, Q.X,   Q.X,  Q.X,  Q.X ));
    }

    public int getPrecedenceColumn(Tokens token) {
        Integer column = precedenceColumn.get(token);

        if (column != null) return column;
        return 23;
    }

    public P getOperatorPrecedence(Tokens stackToken, Tokens currentToken) {
        return operatorPrecedence.get(stackToken).get(getPrecedenceColumn(currentToken));
    }

    public int getExpressionColumn(Node node) {
        if (node.getLexeme() != null) {
            Tokens token = node.getLexeme().getToken();

            switch (token) {
                case INTEGER, DECIMAL, CHARACTER_LITERAL, STRING_LITERAL -> {
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
                default -> {
                    return 5;
                }
            }
        }
        else if (node.getType().equals(Statements.EXPRESSION)) return 7;
        else if (node.getType().equals(Statements.NOT_EXPRESSION)) return 8;
        else if (node.getType().equals(Statements.IDENTIFIER)) return 9;

        return 5;
    }

    public Q getExpressionTransition(Q state, Node node) {
        System.out.println(state);
        if (node.getLexeme() != null) System.out.println(node.getLexeme().getToken());
        else System.out.println(node.getType());
        return expressionTransition.get(state).get(getExpressionColumn(node));
    }

    abstract public void analyzeExpressions() throws Exception;

    abstract public Node nodeReducer(Statements type, int length);
}
