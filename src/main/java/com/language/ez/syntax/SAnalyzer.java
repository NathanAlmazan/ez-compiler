package com.language.ez.syntax;

import com.language.ez.Q;
import com.language.ez.lexical.Lexeme;
import com.language.ez.lexical.Tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class SAnalyzer extends Machines implements CodeGenerator {
    private final List<Lexeme> lexemes;
    private final StringBuilder codeBuilder;
    private final StringBuilder astBuilder;
    private final HashMap<String, List<String>> modelList;
    private Stack<Node> nodes;

    public SAnalyzer(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
        this.nodes = new Stack<>();
        this.codeBuilder = new StringBuilder();
        this.nodes.push(new Node(Statements.START, null, Q.S0, null));
        this.modelList = new HashMap<>();
        this.astBuilder = new StringBuilder();
    }

    public String getCode() {
        return codeBuilder.toString();
    }

    public void printChildren(Node child, int tab) {
        if (child == null) return;

        astBuilder.append("\t".repeat(tab));
        if (child.getLexeme() != null) {
            if (child.getLexeme().getToken().equals(Tokens.INDENT_DELIMITER)) astBuilder.append("\nSTART BLOCK \n");
            else if (child.getLexeme().getToken().equals(Tokens.DEDENT_DELIMITER)) astBuilder.append("\nEND BLOCK \n");
            else astBuilder.append(child.getLexeme().getToken());
        }
        else {
            astBuilder.append(child.getType());
            tab++;
        }
        astBuilder.append("\n");

        if (child.getChildren() != null) {
            for (Node children : child.getChildren())
                printChildren(children, tab);
        } else printChildren(null, 0);
    }

    public String generateAbstractSyntaxTree() {
        for (Node statement : nodes)
            printChildren(statement, 0);

        return astBuilder.toString();
    }

    public void generateCode() throws Exception {
       String mainFunction = null;

       for (int x = 0; x < nodes.size(); x++) {
           Node current = nodes.get(x);

           if (current.getLexeme() != null && current.getLexeme().getToken().equals(Tokens.IMPLEMENTING_TASK_KEYWORD))
               mainFunction = getFunctionName(nodes.get(x + 1));
       }

       if (mainFunction == null) throw new Exception("Implementing Function is missing.");

       int tabs = 0;
       int switches = 0;
       int cases = 0;
       int defaults = 0;
       for (Node statement : nodes) {

           if (tabs > 0) codeBuilder.append("\t".repeat(tabs));

            switch (statement.getType()) {
                case VARIABLE_DECLARATION -> codeBuilder.append(convertVariableDeclaration(statement));
                case PRINT_STATEMENT -> codeBuilder.append(convertPrintStatement(statement));
                case ASSIGNMENT_DECLARATION -> codeBuilder.append(convertAssignmentDeclaration(statement));
                case INPUT_STATEMENT -> codeBuilder.append(convertInputStatement(statement));
                case IF_DECLARATION -> codeBuilder.append(convertIfStatement(statement));
                case ELIF_DECLARATION -> codeBuilder.append(convertElseIfStatement(statement));
                case ELSE_DECLARATION -> codeBuilder.append("else: ");
                case FOR_LOOP_DECLARATION -> codeBuilder.append(convertForLoopStatement(statement));
                case WHILE_LOOP_DECLARATION -> codeBuilder.append(convertWhileLoopStatement(statement));
                case DO_WHILE_LOOP_DECLARATION -> codeBuilder.append(convertDoWhileLoopStatement(statement, tabs));
                case FOREACH_LOOP_DECLARATION -> codeBuilder.append(convertForEachLoopStatement(statement));
                case FUNCTION_DECLARATION -> codeBuilder.append(convertFunctionDeclaration(statement));
                case FUNCTION_CALL -> codeBuilder.append(convertFunctionCallStatement(statement));
                case NESTED_FUNCTIONS -> codeBuilder.append(convertNestedFunctionCalls(statement, tabs));
                case RETURN_STATEMENT -> codeBuilder.append(convertReturnStatement(statement));
                case BREAK_LOOP_STATEMENT -> codeBuilder.append("break\n");
                case SWITCH_DECLARATION -> {
                    codeBuilder.append(convertSwitchDeclaration(statement));
                    switches++;
                }
                case SWITCH_CASE_DECLARATION -> {
                    codeBuilder.append(convertSwitchCases(statement));
                    cases++;
                }
                case SWITCH_DEFAULT_DECLARATION -> {
                    codeBuilder.append("case _:  ");
                    defaults++;
                }
                case MODEL_DECLARATION -> {
                    Model model = analyzeModelDeclaration(statement);
                    modelList.put(model.getName(), model.getItems());
                }
                case MODEL_CALL -> {
                    List<String> items = modelList.get(getModelName(statement));

                    if (items == null) throw new Exception("Model does not exist.");

                    codeBuilder.append(convertModelCallStatement(statement, items));
                }
                default -> {
                    if (statement.getLexeme() != null && statement.getLexeme().getToken().equals(Tokens.INDENT_DELIMITER)) {
                        codeBuilder.append("\n");
                        tabs++;
                    }
                    else if (statement.getLexeme() != null && statement.getLexeme().getToken().equals(Tokens.DEDENT_DELIMITER)) {
                        codeBuilder.append("\n");
                        tabs--;
                    }
                }
            }
       }

       if (switches > cases) throw new Exception("Conditions are required in using 'when' clause.");
       if (defaults > switches) throw new Exception("One default condition only for every 'when' clause.");

       codeBuilder.append("\n")
               .append("try: \n\t")
               .append(mainFunction).append("()\n")
               .append("except NameError as e: \n\t")
               .append("print('Semantic Error: ' + str(e))\n")
               .append("except TypeError as e: \n\t")
               .append("print('Semantic Error: ' + str(e))\n")
                .append("except Exception as e: \n\t")
                .append("print('Runtime Error...')");
    }

    @Override
    public void analyzeExpressions() throws Exception {
        Q state = Q.S0;

        int index = 0;
        while (index < lexemes.size()) {
            if (lexemes.get(index).getToken().equals(Tokens.COMMENTS)) index++;

            Lexeme current = lexemes.get(index);

            System.out.println("\nExpression Token: " + current.getValue());

            Node node = new Node(Statements.UNKNOWN, current,null);

            state = getExpressionTransition(state, node);

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
                case R7 -> {
                    Node reduced = nodeReducer(Statements.ID, 3);
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

            System.out.println("Expression State: " + state);
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

            if (current.getLexeme() == null) System.out.println("\nPrimary Token: " + current.getType());
            else System.out.println("\nPrimary Token: " + current.getLexeme().getValue());

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

            System.out.println("Primary State: " + state);
        }
    }

    @Override
    public void analyzeSecondaryStatements() throws Exception {
        // store old stack
        List<Node> temp = new ArrayList<>(nodes);
        nodes.clear();

        Q state = Q.S0;

        int index = 0;
        while (index < temp.size()) {
            Node current = temp.get(index);

            if (current.getLexeme() == null) System.out.println("\nSecondary Token: " + current.getType());
            else System.out.println("\nSecondary Token: " + current.getLexeme().getValue());

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
                case R17 -> {
                    Node reduced = nodeReducer(Statements.FUNCTION_DECLARATION, 3);
                    state = getSecondaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R18 -> {
                    Node reduced = nodeReducer(Statements.FUNCTION_CALL, 4);
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

           System.out.println("Secondary State: " + state);
        }

        analyzeSpecialFunctionCalls();
    }

    public void analyzeSpecialFunctionCalls() throws Exception {
        List<Node> temp = new ArrayList<>(nodes);
        nodes.clear();

        Q state = Q.S0;

        int index = 0;
        while (index < temp.size()) {
            Node current = temp.get(index);

            if (current.getLexeme() == null) System.out.println("\nTertiary Token: " + current.getType());
            else System.out.println("\nTertiary Token: " + current.getLexeme().getToken());

            state = getTertiaryTransition(state, current);

            switch (state) {
                case R1 -> {
                    Node reduced = nodeReducer(Statements.NESTED_FUNCTION_ELEMENTS, 1);
                    state = getTertiaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R2 -> {
                    Node reduced = nodeReducer(Statements.NESTED_FUNCTION_ELEMENTS, 2);
                    state = getTertiaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R3 -> {
                    Node reduced = nodeReducer(Statements.NESTED_FUNCTIONS, 3);
                    state = getTertiaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R4 -> {
                    Node reduced = nodeReducer(Statements.BREAK_LOOP_STATEMENT, 2);
                    state = getTertiaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R5 -> {
                    Node reduced = nodeReducer(Statements.SWITCH_DECLARATION, 3);
                    state = getTertiaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R6 -> {
                    Node reduced = nodeReducer(Statements.SWITCH_CASE_DECLARATION, 3);
                    state = getTertiaryTransition(nodes.peek().getState(), reduced);
                    reduced.setState(state);
                    nodes.push(reduced);
                }
                case R7 -> {
                    Node reduced = nodeReducer(Statements.SWITCH_DEFAULT_DECLARATION, 2);
                    state = getTertiaryTransition(nodes.peek().getState(), reduced);
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

            System.out.println("Tertiary State: " + state);
        }
    }

    @Override
    public Node nodeReducer(Statements type, int length) {
        Stack<Node> children = new Stack<>();

        for (int x = 0; x < length; x++) {
            Node popped = nodes.pop();

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
