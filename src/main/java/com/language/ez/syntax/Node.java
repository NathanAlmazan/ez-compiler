package com.language.ez.syntax;

import com.language.ez.Q;
import com.language.ez.lexical.Lexeme;

import java.util.Stack;

public class Node {
    private final Statements type;
    private final Lexeme lexeme;
    private Q state;
    private final Stack<Node> children;

    public Node(Statements type, Lexeme lexeme, Q state, Stack<Node> children) {
        this.type = type;
        this.lexeme = lexeme;
        this.state = state;
        this.children = children;
    }

    public Node(Statements type, Lexeme lexeme, Stack<Node> children) {
        this.type = type;
        this.lexeme = lexeme;
        this.state = null;
        this.children = children;
    }

    public void setState(Q state) {
        this.state = state;
    }

    public Statements getType() {
        return type;
    }

    public Lexeme getLexeme() {
        return lexeme;
    }

    public Stack<Node> getChildren() {
        return children;
    }

    public Q getState() {
        return state;
    }
}
