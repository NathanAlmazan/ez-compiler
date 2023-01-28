package com.language.ez.lexical;

public class Lexeme {
    private final Tokens token;
    private final String value;
    private final Integer line;
    private final Integer column;

    public Lexeme(Tokens token) {
        this.token = token;
        this.value = null;
        this.line = null;
        this.column = null;
    }

    public Lexeme(Tokens token, String value, Integer line, Integer column) {
        this.token = token;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public Tokens getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

    public Integer getLine() {
        return line;
    }

    public Integer getColumn() {
        return column;
    }
}
