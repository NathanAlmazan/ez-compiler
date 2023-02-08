package com.language.ez.syntax;

import com.language.ez.lexical.Tokens;

import java.util.*;

public interface CodeGenerator {
    HashMap<Tokens, String> dataTypeConversion = new HashMap<>(Map.ofEntries(
            new AbstractMap.SimpleEntry<>(Tokens.INT_DATA_TYPE, "int"),
            new AbstractMap.SimpleEntry<>(Tokens.DEC_DATA_TYPE, "float"),
            new AbstractMap.SimpleEntry<>(Tokens.LTR_DATA_TYPE, "str"),
            new AbstractMap.SimpleEntry<>(Tokens.WRD_DATA_TYPE, "str"),
            new AbstractMap.SimpleEntry<>(Tokens.BTN_DATA_TYPE, "bool"),
            new AbstractMap.SimpleEntry<>(Tokens.INCREMENT_OPERATOR, " += 1"),
            new AbstractMap.SimpleEntry<>(Tokens.DECREMENT_OPERATOR, " -= 1")
    ));

    default String convertExpression(Node expression) {
        Stack<String> operations = new Stack<>();
        HashMap<String, String> nullables = new HashMap<>();

        for (Node child : expression.getChildren()) {
            if (child.getLexeme() != null) {
                switch (child.getLexeme().getToken()) {
                    case ADDITION_OPERATOR, SUBTRACTION_OPERATOR, MULTIPLICATION_OPERATOR, DIVISION_OPERATOR,
                            MODULO_OPERATOR, LESS_THAN_OPERATOR, MORE_THAN_OPERATOR, LESS_THAN_EQUAL_OPERATOR, MORE_THAN_EQUAL_OPERATOR,
                            AND_OPERATOR, OR_OPERATOR, ADDITION_ASSIGNMENT, SUBTRACTION_ASSIGNMENT_OPERATOR,
                            MULTIPLICATION_ASSIGNMENT_OPERATOR, DIVISION_ASSIGNMENT_OPERATOR, MODULO_ASSIGNMENT_OPERATOR -> {
                        String[] operands = { operations.pop(), operations.pop() };
                        operations.push(operands[1] + " " + child.getLexeme().getValue() + " " + operands[0]);
                    }
                    case EXPONENT_OPERATOR -> {
                        String[] operands = { operations.pop(), operations.pop() };
                        operations.push(operands[1] + "**" + operands[0]);
                    }
                    case NULL_ASSERTION_DELIMITER -> {
                        String[] operands = { operations.pop(), operations.pop() };
                        nullables.put(operands[1], operands[0]);
                        operations.push(operands[1]);
                    }
                    case NOT_OPERATOR -> operations.push("not " + operations.pop());
                    case SWITCH_KEYWORD_EQUALITY_OPERATOR -> {
                        String[] operands = { operations.pop(), operations.pop() };
                        operations.push(operands[1] + "==" + operands[0]);
                    }
                    case BOOLEAN_FALSE_RESERVE_WORD -> operations.push("False");
                    case BOOLEAN_TRUE_RESERVE_WORD -> operations.push("True");
                    default -> operations.push(child.getLexeme().getValue());
                }
            }
        }

        if (nullables.size() > 0) {
            List<String> nullNames = new ArrayList<>();
            final String[] alternative = { operations.peek() };

            nullables.forEach((key, value) -> {
                nullNames.add(key);
                alternative[0] = alternative[0].replaceAll(key, value);
            });

            String conditions = String.join(" and ", nullNames);

            return operations.pop().replaceAll("==not", " !=") + " if " + conditions + " else " + alternative[0];

        } else return operations.pop().replaceAll("==not", " !=");
    }

    default String convertVariableDeclaration(Node node) {
        Node identifier = null;
        Node expression = null;
        Node dataType = null;
        String id = null;

        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.IDENTIFIER)) identifier = child;
            if (child.getType().equals(Statements.EXPRESSION) || child.getType().equals(Statements.ID) ||
                child.getType().equals(Statements.ARRAY_DECLARATION))
                expression = child;
            if (child.getLexeme() != null && child.getLexeme().getToken().equals(Tokens.NULL_TYPE))
                expression = child;
            if (child.getType().equals(Statements.ACCESS_ARRAY)) {
                if (identifier == null) identifier = child;
                else if (expression == null) expression = child;
            }
            if (child.getLexeme() != null && dataTypeConversion.containsKey(child.getLexeme().getToken()))
                dataType = child;
        }

        if (identifier != null && expression != null) {
            if (identifier.getType().equals(Statements.IDENTIFIER)) {
                for (Node child : identifier.getChildren()) {
                    if (child.getType().equals(Statements.ID))
                        id = getId(child);
                }
            } else if (identifier.getType().equals(Statements.ACCESS_ARRAY))
                id = getAccessArray(identifier);



            if (expression.getType().equals(Statements.EXPRESSION)) return generateVarName(id, convertExpression(expression), dataType);
            else if (expression.getType().equals(Statements.ID)) return  generateVarName(id, getId(expression), dataType);
            else if (expression.getType().equals(Statements.ARRAY_DECLARATION)) return generateVarName(id, getArray(expression), dataType);
            else if (expression.getType().equals(Statements.LITERAL)) return id + " = None" + "\n";
            else if (expression.getType().equals(Statements.ACCESS_ARRAY)) return generateVarName(id, getAccessArray(expression), dataType);
        }
        return null;
    }

    default String convertPrintStatement(Node node) {
        Node statement = null;

        for (Node child : node.getChildren()) {
            if  (child.getType().equals(Statements.EXPRESSION) || child.getType().equals(Statements.ID)
                || child.getType().equals(Statements.ACCESS_ARRAY))
                statement = child;
        }

        if (statement != null && statement.getType().equals(Statements.EXPRESSION))
            return "print(" + convertExpression(statement) + ")\n";
        else if (statement != null && statement.getType().equals(Statements.ID))
            return "print(" + getId(statement) + ")\n";
        else if (statement != null && statement.getType().equals(Statements.ACCESS_ARRAY))
            return "print(" + getAccessArray(statement) + ")\n";

        return null;
    }

    default String convertAssignmentDeclaration(Node node) {
        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.EXPRESSION))
                return convertExpression(child) + "\n";
            if (child.getType().equals(Statements.INCREMENT_DECREMENT_STATEMENT))
                return child.getChildren().pop().getLexeme().getValue() + dataTypeConversion.get(child.getChildren().pop().getLexeme().getToken()) + "\n";
        }
        return null;
    }

    default String convertInputStatement(Node node) {
        Node variable = null;
        Node identifier = null;
        Node expression = null;
        Node dataType = null;
        String id = null;

        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.VARIABLE_DECLARATION)) variable = child;
        }

        if (variable != null) {
            for (Node child : variable.getChildren()) {
                if (child.getType().equals(Statements.IDENTIFIER)) identifier = child;
                if (child.getType().equals(Statements.EXPRESSION)) expression = child;
                if (child.getLexeme() != null && dataTypeConversion.containsKey(child.getLexeme().getToken()))
                    dataType = child;
            }

            if (identifier != null && expression != null) {
                for (Node child : identifier.getChildren()) {
                    if (child.getType().equals(Statements.ID))
                        id = child.getChildren().get(0).getLexeme().getValue();
                }

                return generateInputStatement(id, convertExpression(expression), dataType);
            }
        }
        return null;
    }

    default String convertIfStatement(Node node) {
        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.EXPRESSION))
                return "if " + convertExpression(child) + ": ";
        }
        return null;
    }

    default String convertElseIfStatement(Node node) {
        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.IF_DECLARATION))
                return "el" + convertIfStatement(child);
        }
        return null;
    }

    default String convertForLoopStatement(Node node) {
        Stack<String> values = new Stack<>();
        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.EXPRESSION))
                values.push(convertExpression(child));

            if (child.getType().equals(Statements.ID))
                values.push(getId(child));
        }

        String identifier = values.pop();
        String start = values.pop();
        String end = values.pop();

        if (!values.empty()) {
            String step = values.pop();
            return generateLoopRange(start, end, step, identifier);
        } else return generateLoopRange(start, end, "1", identifier);
    }

    default String convertWhileLoopStatement(Node node) {
        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.EXPRESSION))
                return "while " + convertExpression(child) + ": ";
        }
        return null;
    }

    default String convertDoWhileLoopStatement(Node node, int tabs) {
        String expression = null;

        StringBuilder builder = new StringBuilder();

        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.EXPRESSION))
                expression = reverseExpression(child);
        }

        if (expression != null)
            return builder.append("while True: \n").append("\t".repeat(tabs)).append("\tif ").append(expression).append(": break\n\t").toString();
        return null;
    }

    default String convertForEachLoopStatement(Node node) {
        node.getChildren().pop();
        String identifier = getId(node.getChildren().pop());

        node.getChildren().pop();
        String source = getId(node.getChildren().pop());

        return "for " + identifier + " in " + source + ": ";
    }

    default String convertFunctionDeclaration(Node node) {
        String identifier = null;
        Node parameters = null;

        for (Node child : node.getChildren()) {
            if (child.getLexeme() != null && child.getLexeme().getToken().equals(Tokens.IDENTIFIER))
                identifier = child.getLexeme().getValue();
            if (child.getType().equals(Statements.ID)) identifier = getId(child);
            if (child.getType().equals(Statements.FUNCTION_PARAM_LIST)) parameters = child;
        }

        if (parameters != null) return "def " + identifier + "(" + getFunctionParameters(parameters) + "): ";

        return "def " + identifier + "(): ";
    }

    default String convertFunctionCallStatement(Node node) {
        String identifier = null;
        Node parameters = null;

        for (Node child : node.getChildren()) {
            if (child.getLexeme() != null && child.getLexeme().getToken().equals(Tokens.IDENTIFIER))
                identifier = child.getLexeme().getValue();
            if (child.getType().equals(Statements.FUNCTION_CALL_PARAM)) parameters = child;
        }

        if (parameters != null) return identifier + "(" + getFunctionCallParams(parameters) + ")\n";

        return identifier + "()\n";
    }

    default String convertNestedFunctionCalls(Node node, int tabs) {
        String identifier = null;
        Node functions = null;

        StringBuilder builder = new StringBuilder();

        for (Node child : node.getChildren()) {
            if (child.getLexeme() != null && child.getLexeme().getToken().equals(Tokens.IDENTIFIER))
                identifier = child.getLexeme().getValue();
            if (child.getType().equals(Statements.NESTED_FUNCTION_ELEMENTS))
                functions = child;
        }

        if (identifier != null && functions != null) {
            for (String function : getNestedFunctionElements(functions)) {
                String modifiedFunction = function;

                if (!function.contains("()")) modifiedFunction = modifiedFunction.replaceAll("[(]", "(" + identifier + ", ");
                else modifiedFunction = modifiedFunction.replaceAll("[(]", "(" + identifier);

                builder.append("\n").append("\t".repeat(tabs)).append(identifier).append(" = ").append(modifiedFunction);
            }
        }

        return builder.toString();
    }

    default String convertReturnStatement(Node node) {
        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.ASSIGNMENT_DECLARATION))
                return "return " + convertAssignmentDeclaration(child) + "\n";
            if (child.getType().equals(Statements.ID))
                return "return " + getId(child);
        }
        return null;
    }

    default Model analyzeModelDeclaration(Node node) {
        String name = null;
        List<String> items = null;

        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.FUNCTION_PARAM_LIST))
                items = getModelParameters(child);
            if (child.getType().equals(Statements.ID))
                name = getId(child);
        }

        if (name != null && items != null) return new Model(name, items);
        return null;
    }

    default String convertModelCallStatement(Node node, List<String> items) {
        String name = null;
        List<String> values = null;

        for (Node child : node.getChildren()) {
            if (name == null && child.getType().equals(Statements.ID)) name = getId(child);
            if (child.getType().equals(Statements.FUNCTION_CALL_PARAM))
                values = getModelCallParams(child);
        }

        if (name != null && values != null) {
            List<String> keyValuePairs = new ArrayList<>();

            for (int x = 0; x <items.size(); x++) {
                String key = items.get(x);
                String value = values.get(x);

                keyValuePairs.add("'" + key + "': " + value);
            }

            return name + " = {" + String.join(", ", keyValuePairs) + "}\n";
        }

        return null;
    }

    default String convertSwitchDeclaration(Node node) {
        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.EXPRESSION))
                return "match " + convertExpression(child) + ": \n";
            if (child.getType().equals(Statements.ID))
                return "match " + getId(child) + ": \n";
        }

        return null;
    }

    default String convertSwitchCases(Node node) {
        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.EXPRESSION))
                return "case " + convertExpression(child) + ": ";
            if (child.getType().equals(Statements.ID))
                return "case " + getId(child) + ": ";
        }

        return null;
    }

    // simple statements

    default String getModelName(Node node) {
        return getId(node.getChildren().pop());
    }

    default List<String> getModelCallParams(Node node) {
        List<String> params = new ArrayList<>();

        for (Node child : node.getChildren()) {
            if (child.getLexeme() != null && child.getLexeme().getToken().equals(Tokens.IDENTIFIER))
                params.add(child.getLexeme().getValue());
            if (child.getType().equals(Statements.ID)) params.add(getId(child));
            if (child.getType().equals(Statements.EXPRESSION)) params.add(convertExpression(child));
        }

        Collections.reverse(params);

        return params;
    }

    default List<String> getModelParameters(Node node) {
        List<String> parameters = new ArrayList<>();

        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.FUNCTION_PARAM)) parameters.add(getFunctionParamId(child));
        }

        Collections.reverse(parameters);

        return parameters;
    }

    default List<String> getNestedFunctionElements(Node node) {
        List<String> functions = new ArrayList<>();

        for (Node child : node.getChildren()) {{
            if (child.getType().equals(Statements.FUNCTION_CALL))
                functions.add(convertFunctionCallStatement(child));
        }}

        Collections.reverse(functions);

        return functions;
    }

    default String getFunctionCallParams(Node node) {
        List<String> params = new ArrayList<>();

        for (Node child : node.getChildren()) {
            if (child.getLexeme() != null && child.getLexeme().getToken().equals(Tokens.IDENTIFIER))
                params.add(child.getLexeme().getValue());
            if (child.getType().equals(Statements.ID)) params.add(getId(child));
            if (child.getType().equals(Statements.EXPRESSION)) params.add(convertExpression(child));
        }

        Collections.reverse(params);

        return String.join(", ", params);
    }

    default String getFunctionName(Node node) throws Exception {
        if (!node.getType().equals(Statements.FUNCTION_DECLARATION)) throw new Exception("Implementing task should be a Function.");
        else if (node.getChildren().size() > 3) throw new Exception("Implementing task cannot have a parameter.");

        for (Node child : node.getChildren()) {
            if (child.getLexeme() != null && child.getLexeme().getToken().equals(Tokens.IDENTIFIER))
                return child.getLexeme().getValue();
            if (child.getType().equals(Statements.ID)) return getId(child);
        }

        return null;
    }

    default String getFunctionParameters(Node node) {
        List<String> parameters = new ArrayList<>();

        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.FUNCTION_PARAM)) parameters.add(getFunctionParamId(child));
        }

        Collections.reverse(parameters);

        return String.join(", ", parameters);
    }

    default String getFunctionParamId(Node node) {
        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.ID)) return getId(child);
        }

        return null;
    }

    default String reverseExpression(Node expression) {
        Stack<String> operations = new Stack<>();

        for (Node child : expression.getChildren()) {
            if (child.getLexeme() != null) {
                switch (child.getLexeme().getToken()) {
                    case ADDITION_OPERATOR, SUBTRACTION_OPERATOR, MULTIPLICATION_OPERATOR, DIVISION_OPERATOR,
                            MODULO_OPERATOR, AND_OPERATOR, OR_OPERATOR, ADDITION_ASSIGNMENT, SUBTRACTION_ASSIGNMENT_OPERATOR,
                            MULTIPLICATION_ASSIGNMENT_OPERATOR, DIVISION_ASSIGNMENT_OPERATOR, MODULO_ASSIGNMENT_OPERATOR -> {
                        String[] operands = {operations.pop(), operations.pop()};
                        operations.push(operands[1] + " " + child.getLexeme().getValue() + " " + operands[0]);
                    }
                    case EXPONENT_OPERATOR -> {
                        String[] operands = {operations.pop(), operations.pop()};
                        operations.push(operands[1] + "**" + operands[0]);
                    }
                    case MORE_THAN_OPERATOR -> {
                        String[] operands = {operations.pop(), operations.pop()};
                        operations.push(operands[1] +  "<=" + operands[0]);
                    }
                    case LESS_THAN_OPERATOR -> {
                        String[] operands = {operations.pop(), operations.pop()};
                        operations.push(operands[1] +  ">=" + operands[0]);
                    }
                    case MORE_THAN_EQUAL_OPERATOR -> {
                        String[] operands = {operations.pop(), operations.pop()};
                        operations.push(operands[1] +  "<" + operands[0]);
                    }
                    case LESS_THAN_EQUAL_OPERATOR -> {
                        String[] operands = {operations.pop(), operations.pop()};
                        operations.push(operands[1] +  ">" + operands[0]);
                    }
                    case NOT_OPERATOR -> operations.push("not " + operations.pop());
                    case SWITCH_KEYWORD_EQUALITY_OPERATOR -> {
                        String[] operands = {operations.pop(), operations.pop()};
                        operations.push(operands[1] + "!=" + operands[0]);
                    }
                    default -> operations.push(child.getLexeme().getValue());
                }
            }
        }

        return operations.pop().replaceAll("!=not", " ==");
    }

    default String generateLoopRange(String start, String end, String step, String id) {
        try {
            if (Integer.parseInt(start) > Integer.parseInt(end))
                return "for " + id + " in range(" + start + ", " + end + ", -" + step + "):";
            return "for " + id + " in range(" + start + ", " + end + ", " + step + "):";
        } catch (NumberFormatException e) {
            return "for " + id + " in range(" + start + ", " + end + ", " + step + "):";
        }
    }

    default String getId(Node node) {
        List<String> ids = new ArrayList<>();

        for (Node child : node.getChildren()) {
            if (child.getLexeme() != null && child.getLexeme().getToken().equals(Tokens.IDENTIFIER) ||
                    child.getLexeme().getToken().equals(Tokens.NULLABLE_IDENTIFIER) ||
                    child.getLexeme().getToken().equals(Tokens.CONSTANT_IDENTIFIER))
                ids.add(child.getLexeme().getValue());
        }

        if (ids.size() > 1) return ids.get(1).replaceAll("[^a-zA-Z0-9]", " ") + "['" +  ids.get(0).replaceAll("[^a-zA-Z0-9]", " ") + "']";
        else return ids.get(0).replaceAll("[^a-zA-Z0-9]", "");
    }

    default String getArray(Node node) {
        Node expressionArray = null;
        List<String> elements = new ArrayList<>();

        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.ARRAY_ELEMENTS))
                expressionArray = child;
        }

        if (expressionArray != null) {
            for (Node child : expressionArray.getChildren()) {
                if (child.getType().equals(Statements.EXPRESSION))
                    elements.add(convertExpression(child));
                if (child.getType().equals(Statements.ID))
                    elements.add(getId(child));
            }

            Collections.reverse(elements);

            return "[" + String.join(", ", elements) + "]";
        } else return "[]";
    }

    default String getAccessArray(Node node) {
        String name = null;
        String index = null;

        for (Node child : node.getChildren()) {
            if (child.getType().equals(Statements.ID))
                name = child.getChildren().get(0).getLexeme().getValue();
            if (child.getType().equals(Statements.ARRAY_ELEMENTS)) {
                for (Node c : child.getChildren()) {
                    if (c.getType().equals(Statements.EXPRESSION)) index =  "[" + convertExpression(c) + "]";
                    if (c.getType().equals(Statements.ID)) index = "[" + getId(c) + "]";
                }
            }
            if (child.getType().equals(Statements.ARRAY_DECLARATION))
                index = getArray(child);
        }

        return name + index;
    }

    default String generateVarName(String identifier, String expression, Node dataType) {
        if (dataType ==  null) return identifier + " = " + expression + "\n";

        String type = dataTypeConversion.get(dataType.getLexeme().getToken());

        return identifier + " = " + type + "(" + expression + ")" + "\n";
    }

    default String generateInputStatement(String identifier, String expression, Node dataType) {
        if (dataType ==  null) return identifier + " = input(" + expression + ")" + "\n";

        String type = dataTypeConversion.get(dataType.getLexeme().getToken());

        return identifier + " = " + type + "(input(" + expression + "))" + "\n";
    }

}
