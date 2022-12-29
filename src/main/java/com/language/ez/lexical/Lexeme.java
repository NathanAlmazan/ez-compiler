package com.language.ez.lexical;

public class Lexeme {
    private final Tokens token;
    private final String value;

    public Lexeme(Tokens token, String value) {
        this.token = token;
        this.value = value;
    }

    public Tokens getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }
}
