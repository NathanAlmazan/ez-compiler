package com.language.ez.syntax;

import com.language.ez.Q;
import com.language.ez.lexical.Lexeme;
import com.language.ez.lexical.Tokens;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SAnalyzer extends Machines {
    private final List<Lexeme> lexemes;
    private Stack<Node> nodes;

    public SAnalyzer(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
        this.nodes = new Stack<>();
        this.nodes.push(new Node(Statements.START, null, Q.S0, null));
    }

    public void printChildren(Node child, int tab) {
        if (child == null) return;

        for (int x = 0; x < tab; x++) System.out.print("    ");
        if (child.getLexeme() != null) System.out.print(child.getLexeme().getToken() + " | " + child.getLexeme().getValue());
        else {
            System.out.print(child.getType());
            tab++;
        }
        System.out.println();

        if (child.getChildren() != null) {
            for (Node children : child.getChildren())
                printChildren(children, tab);
        } else printChildren(null, 0);
    }

    public void printNodes() {
       for (Node statement : nodes)
           printChildren(statement, 0);
    }

    @Override
    public void analyzeExpressions() throws Exception {
        Q state = Q.S0;

        int index = 0;
        while (index < lexemes.size()) {
            if (lexemes.get(index).getToken().equals(Tokens.COMMENTS)) index++;

            Lexeme current = lexemes.get(index);

            Node node = new Node(Statements.UNKNOWN, current,null);

            state = getExpressionTransition(state, node);

            System.out.println(state + " | " + current.getValue());

            switch (state) {
                case R1 -> {
                    Node reduced = nodeReducer(Statements.EXPRESSION, 1);
                    state = getExpressionTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R2, R3 -> {
                    Node reduced = nodeReducer(Statements.EXPRESSION, 3);
                    state = getExpressionTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R4 -> {
                    Node reduced = nodeReducer(Statements.ID, 1);
                    state = getExpressionTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R5 -> {
                    Node reduced = nodeReducer(Statements.NOT_EXPRESSION, 2);
                    state = getExpressionTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R6 -> {
                    Node reduced = nodeReducer(Statements.INCREMENT_DECREMENT_STATEMENT, 2);
                    state = getExpressionTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case X -> throw new Exception("Syntax error in line " + current.getLine() + " column " + current.getColumn() + ".");
                default -> {
                    node.setState(state);
                    nodes.push(node);
                    index++;
                }
            }
        }

        Stack<Node> temp = new Stack<>();
        for (Node node : nodes) {
            temp.push(assertOperatorPrecedence(node));
        }

        nodes = temp;
    }

    @Override
    public void analyzePrimaryStatements() throws Exception {
        // store old stack
        List<Node> temp = new ArrayList<>(nodes);
        nodes.clear();

        temp.add(new Node(Statements.END, null, null)); // put a puffer element to avoid null exception

        Q state = Q.S0;

        int index = 0;
        while (index < temp.size()) {
            Node current = temp.get(index);

            state = getPrimaryTransition(state, current);

            switch (state) {
                case R1 -> {
                    Node reduced = nodeReducer(Statements.IDENTIFIER, 1);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R2 -> {
                    Node reduced = nodeReducer(Statements.VARIABLE_DECLARATION, 4);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R3 -> {
                    Node reduced = nodeReducer(Statements.VARIABLE_DECLARATION, 6);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R4 -> {
                    Node reduced = nodeReducer(Statements.IDENTIFIER, 3);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R5 -> {
                    Node reduced = nodeReducer(Statements.ASSIGNMENT_DECLARATION, 2);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R6 -> {
                    Node reduced = nodeReducer(Statements.IF_DECLARATION, 3);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R7 -> {
                    Node reduced = nodeReducer(Statements.ELIF_DECLARATION, 2);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R8 -> {
                    Node reduced = nodeReducer(Statements.PRINT_STATEMENT, 4);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R9 -> {
                    Node reduced = nodeReducer(Statements.ARRAY_ELEMENTS, 1);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R10, R12 -> {
                    Node reduced = nodeReducer(Statements.ARRAY_ELEMENTS, 3);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R11 -> {
                    Node reduced = nodeReducer(Statements.ARRAY_DECLARATION, 3);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R13 -> {
                    Node reduced = nodeReducer(Statements.ACCESS_ARRAY, 2);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R14 -> {
                    Node reduced = nodeReducer(Statements.FOR_LOOP_DECLARATION, 7);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R15 -> {
                    Node reduced = nodeReducer(Statements.FOR_LOOP_DECLARATION, 9);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R16 -> {
                    Node reduced = nodeReducer(Statements.FUNCTION_CALL_PARAMS, 3);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R17 -> {
                    Node reduced = nodeReducer(Statements.ELSE_DECLARATION, 2);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R18 -> {
                    Node reduced = nodeReducer(Statements.ARRAY_DATA_TYPE, 3);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R19 -> {
                    Node reduced = nodeReducer(Statements.ARRAY_DECLARATION, 2);
                    state = getPrimaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case X -> throw new Exception(exceptionBuilder(current));
                default -> {
                    current.setState(state);
                    nodes.push(current);
                    index++;
                }
            }
        }
    }

    @Override
    public void analyzeSecondaryStatements() throws Exception {
        // store old stack
        List<Node> temp = new ArrayList<>(nodes);
        nodes.clear();

        System.out.println(temp.size());

        Q state = Q.S0;

        int index = 0;
        while (index < temp.size()) {
            Node current = temp.get(index);

            state = getSecondaryTransition(state, current);

            switch (state) {
                case R1 -> {
                    Node reduced = nodeReducer(Statements.FOREACH_LOOP_DECLARATION, 5);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R2 -> {
                    Node reduced = nodeReducer(Statements.WHILE_LOOP_DECLARATION, 3);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R3 -> {
                    Node reduced = nodeReducer(Statements.DO_WHILE_LOOP_DECLARATION, 4);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R4 -> {
                    Node reduced = nodeReducer(Statements.INPUT_STATEMENT, 2);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R5 -> {
                    Node reduced = nodeReducer(Statements.FUNCTION_PARAM, 3);
                    System.out.println(nodes.peek().getState() + " " +  reduced.getType());
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R6 -> {
                    Node reduced = nodeReducer(Statements.FUNCTION_PARAM_LIST, 1);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R7 -> {
                    Node reduced = nodeReducer(Statements.FUNCTION_PARAM_LIST, 3);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R8 -> {
                    Node reduced = nodeReducer(Statements.FUNCTION_DECLARATION, 6);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R9 -> {
                    Node reduced = nodeReducer(Statements.MODEL_DECLARATION, 5);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R10 -> {
                    Node reduced = nodeReducer(Statements.RETURN_STATEMENT, 3);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R11 -> {
                    Node reduced = nodeReducer(Statements.RETURN_STATEMENT, 2);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R12 -> {
                    Node reduced = nodeReducer(Statements.FUNCTION_DECLARATION, 5);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R13 -> {
                    Node reduced = nodeReducer(Statements.FUNCTION_CALL_PARAM, 1);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R14 -> {
                    Node reduced = nodeReducer(Statements.FUNCTION_CALL, 5);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R15 -> {
                    Node reduced = nodeReducer(Statements.FUNCTION_CALL_PARAM, 3);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R16 -> {
                    Node reduced = nodeReducer(Statements.MODEL_CALL, 7);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case X -> throw new Exception(exceptionBuilder(current));
                default -> {
                    current.setState(state);
                    nodes.push(current);
                    index++;
                }
            }

            System.out.println(state);
        }
    }

    @Override
    public Node nodeReducer(Statements type, int length) {
        Stack<Node> children = new Stack<>();

        for (int x = 0; x < length; x++) {
            Node popped = nodes.pop();

            if (popped.getLexeme() != null) System.out.println(popped.getLexeme().getToken());
            else System.out.println(popped.getType());

            if (popped.getChildren() != null) {
                if (popped.getType().equals(type) || popped.getType().equals(Statements.NOT_EXPRESSION) && type.equals(Statements.EXPRESSION)) {
                    for (Node child : popped.getChildren())
                        children.push(child);
                } else children.push(popped);
            }
            else {
                children.push(new Node(Statements.LITERAL, popped.getLexeme(), popped.getState(), null));
            }
        }

        return new Node(type, null, children);
    }

    public String exceptionBuilder(Node node) {
        if (node.getLexeme() != null)
            return "Syntax error in line " + node.getLexeme().getLine() + " column " + node.getLexeme().getColumn() + ".";

        Node child = node.getChildren().peek();
        return "Syntax error in line " + child.getLexeme().getLine() + " column " + child.getLexeme().getColumn() + ".";
    }
}
