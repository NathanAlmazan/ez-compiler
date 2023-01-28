package com.language.ez.syntax;

import com.language.ez.Q;
import com.language.ez.lexical.Lexeme;

import java.util.List;
import java.util.Stack;

public class SAnalyzer extends Machines {
    private final List<Lexeme> lexemes;
    private final Stack<Node> nodes;

    public SAnalyzer(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
        this.nodes = new Stack<>();
        this.nodes.push(new Node(Statements.START, null, Q.S0, null));
    }

    public Stack<Node> getNodes() {
        return nodes;
    }

    @Override
    public void analyzeExpressions() throws Exception {
        Q state = Q.S0;

        int index = 0;
        while (index < lexemes.size()) {
            Lexeme current = lexemes.get(index);
            Node node = new Node(Statements.UNKNOWN, current,null);

            state = getExpressionTransition(state, node);

            System.out.println(state);

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
                    Node reduced = nodeReducer(Statements.IDENTIFIER, 1);
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
                case X -> throw new Exception("Syntax error in line " + current.getLine() + " column " + current.getColumn() + ".");
                default -> {
                    node.setState(state);
                    nodes.push(node);
                    index++;
                }
            }
        }
    }

    @Override
    public Node nodeReducer(Statements type, int length) {
        Stack<Node> children = new Stack<>();

        for (int x = 0; x < length; x++) {
            Node popped = nodes.pop();

            if (popped.getChildren() != null) {
                for (Node child : popped.getChildren())
                    children.push(child);
            } else {
                children.push(new Node(Statements.LITERAL, popped.getLexeme(), popped.getState(), null));
            }
        }

        return new Node(type, null, children);
    }
}
