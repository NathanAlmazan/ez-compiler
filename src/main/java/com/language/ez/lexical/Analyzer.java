package com.language.ez.lexical;

import java.util.*;

public class Analyzer {
    private final static HashMap<Q, List<Q>> transitionTable = new HashMap<>();
    private final static HashMap<Character, Integer> tableColum = new HashMap<>();
    private final static HashMap<String, Tokens> alphabet = new HashMap<>();
    private final Stack<Lexeme> lexemes;
    private final List<String> statements;

    static {
        //                   Q                   n     +     -      *     /    %      ^     =     <      >    spc   ltr   _     "     '       ?       .
        transitionTable.put(Q.S0,  Arrays.asList(Q.S1, Q.S3, Q.S2, Q.S4, Q.S4, Q.S4, Q.S4, Q.S4, Q.S14, Q.S4, Q.S18,Q.S6, Q.X,  Q.S8, Q.S10, Q.S13,Q.S13)); // machine entry point
        transitionTable.put(Q.S1,  Arrays.asList(Q.S1, Q.R1, Q.R1, Q.R1, Q.R1, Q.R1, Q.R1, Q.R1, Q.R1,  Q.R1, Q.R1, Q.X,  Q.X,  Q.X,  Q.X,   Q.R1, Q.S22)); // number rules
        transitionTable.put(Q.S2,  Arrays.asList(Q.S1, Q.X,  Q.S5, Q.X,  Q.X,  Q.X,  Q.X,  Q.S5, Q.X,   Q.X,  Q.R2, Q.R2, Q.X,  Q.X,  Q.X,   Q.R2, Q.X  )); // subtraction rules
        transitionTable.put(Q.S3,  Arrays.asList(Q.R2, Q.S5, Q.R2, Q.X,  Q.X,  Q.X,  Q.X,  Q.S5, Q.X,   Q.X,  Q.R2, Q.R2, Q.X,  Q.R2, Q.R2,  Q.R2, Q.X  )); // addition rules
        transitionTable.put(Q.S4,  Arrays.asList(Q.R2, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S5, Q.X,   Q.X,  Q.R2, Q.R2, Q.X,  Q.X,  Q.X,   Q.R2, Q.X  )); // other operation symbol rules
        transitionTable.put(Q.S5,  Arrays.asList(Q.R2, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,   Q.X,  Q.R2, Q.R2, Q.X,  Q.X,  Q.X,   Q.R2, Q.R2 )); // relational, increment and decrement operation symbol rules
        transitionTable.put(Q.S6,  Arrays.asList(Q.S7, Q.R3, Q.R3, Q.R3, Q.R3, Q.R3, Q.R3, Q.R3, Q.R3,  Q.R3, Q.R3, Q.S6, Q.S6, Q.X,  Q.X,   Q.R3, Q.R3 )); // identifier and keyword rules
        transitionTable.put(Q.S7,  Arrays.asList(Q.S7, Q.R3, Q.R3, Q.R3, Q.R3, Q.R3, Q.R3, Q.R3, Q.R3,  Q.R3, Q.R3, Q.X,  Q.X,  Q.X,  Q.X,   Q.R3, Q.R3 )); // identifier rules
        transitionTable.put(Q.S8,  Arrays.asList(Q.S8, Q.S8, Q.S8, Q.S8, Q.S8, Q.S8, Q.S8, Q.S8, Q.S8,  Q.S8, Q.S8, Q.S8, Q.S8, Q.S9, Q.S8,  Q.S8, Q.S8 )); // string literal rules
        transitionTable.put(Q.S9,  Arrays.asList(Q.X,  Q.R4, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,   Q.X,  Q.R4, Q.X,  Q.X,  Q.X,  Q.X,   Q.R4, Q.R4 )); // string literal rules
        transitionTable.put(Q.S10, Arrays.asList(Q.S11,Q.S11,Q.S11,Q.S11,Q.S11,Q.S11,Q.S11,Q.S11,Q.S11, Q.S11,Q.S11,Q.S11,Q.S11,Q.S11,Q.S12, Q.S11,Q.S11)); // character literal rules
        transitionTable.put(Q.S11, Arrays.asList(Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,   Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S12, Q.X,  Q.X  )); // character literal rules
        transitionTable.put(Q.S12, Arrays.asList(Q.X,  Q.R5, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,   Q.X,  Q.R5, Q.X,  Q.X,  Q.X,  Q.X,   Q.R5, Q.R5 )); // character literal rules
        transitionTable.put(Q.S13, Arrays.asList(Q.R6, Q.R6, Q.R6, Q.R6, Q.R6, Q.R6, Q.R6, Q.R6, Q.R6,  Q.R6, Q.R6, Q.R6, Q.R6, Q.R6, Q.R6,  Q.R6, Q.R6 )); // other symbols rules
        transitionTable.put(Q.S14, Arrays.asList(Q.R2, Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.S5, Q.S15, Q.X,  Q.R2, Q.R2, Q.X,  Q.X,  Q.X,   Q.R2, Q.X  )); // comment rules
        transitionTable.put(Q.S15, Arrays.asList(Q.S15,Q.S15,Q.S15,Q.S15,Q.S15,Q.S15,Q.S15,Q.S15,Q.S15, Q.S16,Q.S15,Q.S15,Q.S15,Q.S15,Q.S15, Q.S15,Q.S15)); // comment rules
        transitionTable.put(Q.S16, Arrays.asList(Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,  Q.X,   Q.S17,Q.X,  Q.X,  Q.X,  Q.X,  Q.X,   Q.X,  Q.X  )); // comment rules
        transitionTable.put(Q.S17, Arrays.asList(Q.R7, Q.R7, Q.R7, Q.R7, Q.R7, Q.R7, Q.R7, Q.R7, Q.R7,  Q.R7, Q.R7, Q.R7, Q.R7, Q.R7, Q.R7,  Q.R7, Q.R7 )); // comment rules
        transitionTable.put(Q.S18, Arrays.asList(Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8,  Q.R8, Q.S19,Q.R8, Q.R8, Q.R8, Q.R8,  Q.R8, Q.R8 )); // space to tab rules
        transitionTable.put(Q.S19, Arrays.asList(Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8,  Q.R8, Q.S20,Q.R8, Q.R8, Q.R8, Q.R8,  Q.R8, Q.R8 )); // space to tab rules
        transitionTable.put(Q.S20, Arrays.asList(Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8, Q.R8,  Q.R8, Q.S21,Q.R8, Q.R8, Q.R8, Q.R8,  Q.R8, Q.R8 )); // space to tab rules
        transitionTable.put(Q.S21, Arrays.asList(Q.R9, Q.R9, Q.R9, Q.R9, Q.R9, Q.R9, Q.R9, Q.R9, Q.R9,  Q.R9, Q.R9, Q.R9, Q.R9, Q.R9, Q.R9,  Q.R9, Q.R9 )); // space to tab rules
        transitionTable.put(Q.S22, Arrays.asList(Q.S22,Q.R10,Q.R10,Q.R10,Q.R10,Q.R10,Q.R10,Q.R10,Q.R10, Q.R10,Q.R10,Q.X,  Q.X,  Q.X,  Q.X,   Q.R10,Q.R10)); // decimal rules

        // get the column in the transition table
        tableColum.put('+', 1);
        tableColum.put('-', 2);
        tableColum.put('*', 3);
        tableColum.put('/', 4);
        tableColum.put('%', 5);
        tableColum.put('^', 6);
        tableColum.put('=', 7);
        tableColum.put('<', 8);
        tableColum.put('>', 9);
        tableColum.put(' ', 10);
        tableColum.put('_', 12);
        tableColum.put('"', 13);
        tableColum.put('\'', 14);
        tableColum.put('.', 16);

        // alphabet of operations
        alphabet.put("+", Tokens.ADDITION_OPERATOR);
        alphabet.put("-", Tokens.SUBTRACTION_OPERATOR);
        alphabet.put("*", Tokens.MULTIPLICATION_OPERATOR);
        alphabet.put("/", Tokens.DIVISION_OPERATOR);
        alphabet.put("^", Tokens.EXPONENT_OPERATOR);
        alphabet.put("%", Tokens.MODULO_OPERATOR);
        alphabet.put("++", Tokens.INCREMENT_OPERATOR);
        alphabet.put("--", Tokens.DECREMENT_OPERATOR);
        alphabet.put("+=", Tokens.ADDITION_ASSIGNMENT);
        alphabet.put("-=", Tokens.SUBTRACTION_ASSIGNMENT);
        alphabet.put("*=", Tokens.MULTIPLICATION_ASSIGNMENT);
        alphabet.put("/=", Tokens.DIVISION_ASSIGNMENT);
        alphabet.put("%=", Tokens.MODULO_ASSIGNMENT);
        alphabet.put("==", Tokens.EQUALITY);
        alphabet.put("<", Tokens.LESS_THAN);
        alphabet.put(">", Tokens.MORE_THAN);
        alphabet.put("<=", Tokens.LESS_THAN_EQUAL);
        alphabet.put(">=", Tokens.MORE_THAN_EQUAL);
        alphabet.put("!", Tokens.NOT_OPERATOR);

        // alphabets of special symbols (subject for revision)
        alphabet.put(".", Tokens.STATEMENT_END);
        alphabet.put(":", Tokens.COLON_SEPARATOR);
        alphabet.put(",", Tokens.COMMA_SEPARATOR);
        alphabet.put("@", Tokens.CONCAT_SYMBOL);
        alphabet.put("{", Tokens.OPEN_DICT);
        alphabet.put("}", Tokens.CLOSE_DICT);
        alphabet.put("(", Tokens.OPEN_PARENTHESIS);
        alphabet.put(")", Tokens.CLOSE_PARENTHESIS);
        alphabet.put("[", Tokens.OPEN_LIST);
        alphabet.put("]", Tokens.CLOSE_LIST);
        alphabet.put("?", Tokens.NULL_TYPE);
        
        // alphabet of keywords and reserve words (subject for revision)
        alphabet.put("int", Tokens.DATA_TYPE);
        alphabet.put("dec", Tokens.DATA_TYPE);
        alphabet.put("ltr", Tokens.DATA_TYPE);
        alphabet.put("wrd", Tokens.DATA_TYPE);
        alphabet.put("btn", Tokens.DATA_TYPE);
        alphabet.put("to", Tokens.ASSIGN_KEYWORD);
        alphabet.put("from", Tokens.SCOPE_KEYWORD);
        alphabet.put("as", Tokens.ASSIGN_TYPE_KEYWORD);
        alphabet.put("if", Tokens.IF_KEYWORD);
        alphabet.put("else", Tokens.ELIF_KEYWORD);
        alphabet.put("in", Tokens.SCOPE_KEYWORD);
        alphabet.put("is", Tokens.SWITCH_KEYWORD);
        alphabet.put("print", Tokens.PRINT_KEYWORD);
        alphabet.put("count", Tokens.LOOP_KEYWORD);
        alphabet.put("foreach", Tokens.LOOP_KEYWORD);
        alphabet.put("while", Tokens.LOOP_KEYWORD);
        alphabet.put("do", Tokens.LOOP_KEYWORD);
        alphabet.put("and", Tokens.AND_OPERATOR);
        alphabet.put("or", Tokens.OR_OPERATOR);
        alphabet.put("put", Tokens.SCANNER_DECLARATION);
        alphabet.put("out", Tokens.RETURN_STATEMENT);
        alphabet.put("task", Tokens.FUNCTION_DECLARATION);
        alphabet.put("on", Tokens.BOOLEAN_TRUE);
        alphabet.put("off", Tokens.BOOLEAN_FALSE);
    }

    public Analyzer(List<String> statements) {
        this.statements = statements;
        this.lexemes = new Stack<>();
    }

    public Stack<Lexeme> getLexemes() {
        return this.lexemes;
    }

    /*
        Machine that detects words, operators, numbers,
        string literals, character literals, and special symbols
        in spite of absence in spaces. It can also detect
        invalid series of characters.
    */
    public void analyzeStatements() throws Exception {
        Q currentState = Q.S0; // starting state
        int cursor = 0; // number of collected values

        // scan each code lines
        for (int x = 0; x <= statements.size(); x++) {
            String statement = x < statements.size() ? statements.get(x) : "\n"; // need extra line or last token will be missed (bug solution)
            // scan each character in a line
            int index = 0;
            while (index < statement.length()) {
                char ch = statement.charAt(index);
                int column = getTableColumn(ch); // get column in the transition table

                Q nextState = transitionTable.get(currentState).get(column); // get next state

                switch (nextState) {
                    case R1 -> {
                        reduceValues(Tokens.INTEGER, cursor); // reduce integers
                        cursor = 0; // reset cursor
                        currentState = Q.S0; // reset state
                    }
                    case R2 -> {
                        reduceValues(Tokens.OPERATOR, cursor); // reduce operators
                        cursor = 0; // reset cursor
                        currentState = Q.S0; // reset state
                    }
                    case R3 -> {
                        reduceValues(Tokens.WORD, cursor); // reduce words
                        cursor = 0; // reset cursor
                        currentState = Q.S0; // reset state
                    }
                    case R4 -> {
                        reduceValues(Tokens.STRING_LITERAL, cursor); // reduce string literals
                        cursor = 0; // reset cursor
                        currentState = Q.S0; // reset state
                    }
                    case R5 -> {
                        reduceValues(Tokens.CHARACTER_LITERAL, cursor); // reduce character literals
                        cursor = 0; // reset cursor
                        currentState = Q.S0; // reset state
                    }
                    case R6 -> {
                        reduceValues(Tokens.SYMBOLS, cursor); // reduce special symbols
                        cursor = 0; // reset cursor
                        currentState = Q.S0; // reset state
                    }
                    case R7 -> {
                        reduceValues(Tokens.COMMENTS, cursor); // ignore comments
                        cursor = 0; // reset cursor
                        currentState = Q.S0; // reset state
                    }
                    case R8 -> { // ignore unnecessary spaces
                        cursor = 0; // reset cursor
                        currentState = Q.S0; // reset state
                    }
                    case R9 -> {
                        reduceValues(Tokens.TAB, 4); // reduce 4 spaces to a tab space
                        cursor = 0; // reset cursor
                        currentState = Q.S0; // reset state
                    }
                    case R10 -> {
                        reduceValues(Tokens.DECIMAL, cursor); // reduce decimal values
                        cursor = 0; // reset cursor
                        currentState = Q.S0; // reset state
                    }
                    case X -> throw new Exception("Unexpected token '" + ch + "' in line " + (x + 1) + " column " + index + ".");
                    default -> {
                        if (index == 0) this.lexemes.push(new Lexeme(Tokens.NEW_LINE, null)); // add new line marker
                        this.lexemes.push(new Lexeme(Tokens.UNKNOWN, Character.toString(ch))); // add character to stack
                        cursor++; // store value length if not space
                        currentState = nextState; // go to next state
                        index++; // go to next character
                    }
                }
            }
        }


    }

    /*
        Machine that specifies the detected lexemes. For example,
        if the first machine detects an operator, what kind of operator
        is it? If the first machine detects a word, is it an identifier,
        a reserve word, or a keyword?

        This machine also simplifies the indents using indent
        and dedent tokens. Indent specify the start of a
        code block while dedent specify the end of a code
        block.
    */
    public void optimizeLexemes() throws Exception {
        Stack<Lexeme> newLexemes = new Stack<>();

        int indent = 0; // count of nested indent
        int index = 0;
        while (index < this.lexemes.size()) {
            Lexeme current = this.lexemes.get(index);

            switch (current.getToken()) {
                case SYMBOLS, OPERATOR -> {
                    // check what kind of symbol or operator
                    Tokens token = alphabet.get(current.getValue());
                    // throw exception if unidentified
                    if (token == null) throw new Exception("Invalid symbol or punctuation '" + current.getValue() + "'.");
                    // push to new stack if identified
                    newLexemes.push(new Lexeme(token, current.getValue()));

                    // check for indention
                    if ((token == Tokens.COLON_SEPARATOR || token == Tokens.COMMA_SEPARATOR) && (index + 1) < this.lexemes.size()) {
                        Lexeme next = this.lexemes.get(index + 1);

                        if (next.getToken() == Tokens.NEW_LINE) {
                            newLexemes.push(new Lexeme(Tokens.INDENT, null)); // push indent
                            indent++; // record indention to ignore tabs later
                        }
                    }
                    index++; // go to next lexeme
                }
                case WORD -> {
                    // check what kind of symbol or operator
                    Tokens token = alphabet.get(current.getValue());
                    // push as identifier if unidentified but push to new stack if identified
                    newLexemes.push(new Lexeme(Objects.requireNonNullElse(token, Tokens.IDENTIFIER), current.getValue()));
                    index++; // go to next lexeme
                }
                case NEW_LINE -> {
                    newLexemes.push(current);

                    // check for dedent
                    int skipped = 1;
                    if (indent > 0 && (index + indent) < this.lexemes.size()) {
                        int temp = indent; // temporary store indent
                        for (int i = 1; i <= temp; i++) {
                            if (this.lexemes.get(index + i).getToken() == Tokens.TAB) skipped++; // skip tabs if indented
                            else {
                                newLexemes.push(new Lexeme(Tokens.DEDENT, null)); // push dedent if indent ends
                                indent--; // delete record of indention
                            }
                        }
                    }

                    index += skipped; // go to next lexeme
                }
                default -> {
                    // push to new stack if not space
                    if (current.getToken() != Tokens.UNKNOWN) newLexemes.push(current);
                    index++; // go to next lexeme
                }
            }
        }

        // update lexemes but ignore new line tokens because it is used only to detect indents
        this.lexemes.clear();
        for (Lexeme lexeme : newLexemes) {
            if (lexeme.getToken() != Tokens.NEW_LINE) this.lexemes.push(lexeme);
        }
    }

    private int getTableColumn(char ch) {
        if (Character.isDigit(ch)) return 0;
        if (Character.isAlphabetic(ch)) return 11;

        Integer column = tableColum.get(ch);

        if (column != null) return column;
        else return 15;
    }

    private void reduceValues(Tokens tokenType, int valueLength) {
        StringBuilder value = new StringBuilder();

        for (int x = 0; x < valueLength; x++) {
            Lexeme lexeme = this.lexemes.pop(); // pop values that will be reduced
            value.insert(0, lexeme.getValue()); // concat collected values
        }

        Lexeme newLexeme = new Lexeme(tokenType, value.toString()); // save concat value in new lexeme
        this.lexemes.push(newLexeme);
    }
}
