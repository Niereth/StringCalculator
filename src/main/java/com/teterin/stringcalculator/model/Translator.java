package com.teterin.stringcalculator.model;

import com.teterin.stringcalculator.model.expressions.Expression;
import com.teterin.stringcalculator.model.expressions.ExpressionType;
import com.teterin.stringcalculator.model.expressions.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class Translator {

    private static final Logger log = LoggerFactory.getLogger(Translator.class);
    private String inputExpression;
    private List<Expression> outputExpression;
    private Deque<Expression> operators;
    private List<Expression> tokens;

    Translator(String expression) {
        inputExpression = expression;
        outputExpression = new ArrayList<>();
        operators = new ArrayDeque<>();
    }

    List<Expression> translateToPostfixNotation() throws CalculatorException {
        Tokenizer tokenizer = new Tokenizer(inputExpression);
        tokens = tokenizer.getTokens();

        for (Expression token : tokens) {
            processToken(token);
        }

        while (!operators.isEmpty()) {
            Expression topOperator = operators.pop();
            if (topOperator.getValue().equals("(")) {
                bracketsMismatch();
            } else {
                outputExpression.add(topOperator);
            }
        }

        return outputExpression;
    }

    private void bracketsMismatch() throws CalculatorException {
        String message = "В выражении не согласованы скобки";
        log.error(message);
        throw new CalculatorException(message);
    }

    private void processToken(Expression token) throws CalculatorException {
        ExpressionType tokenType = token.getType();
        String value = token.getValue().toString();

        if (tokenType == ExpressionType.OPERAND) {
            outputExpression.add(token);
        } else if (value.equals("(")) {
            operators.push(token);
        } else if (value.equals(")")) {
            processCloseBracket();
        } else if (value.equals("?") || value.equals(":")) {
            processTernaryOperator(token);
        } else if (tokenType == ExpressionType.OPERATION_SIGN) {
            processOperationSign(token);
        }
    }

    private void processCloseBracket() throws CalculatorException {
        if (operators.isEmpty()) {
            bracketsMismatch();
        }

        Expression topOperator = operators.pop();
        while (!topOperator.getValue().equals("(")) {
            if (operators.isEmpty()) {
                bracketsMismatch();
            }
            outputExpression.add(topOperator);
            topOperator = operators.pop();
        }
    }

    private void processTernaryOperator(Expression token) throws CalculatorException {
        validateTernaryOperator(token);

        Expression topOperator;
        while (!operators.isEmpty()) {
            if (operators.peek().getValue().equals("?")) {
                break;
            }
            topOperator = operators.pop();
            outputExpression.add(topOperator);
        }

        if (token.getValue().equals("?")) {
            operators.push(token);
        }
    }

    private void validateTernaryOperator(Expression token) throws CalculatorException {
        int index = tokens.indexOf(token);

        if (index == 0) {
            ternaryOperatorMismatch();
        } else {
            Expression previousToken = tokens.get(index - 1);
            if (previousToken.getValue() != ")"
                    && previousToken.getType() != ExpressionType.OPERAND) {
                ternaryOperatorMismatch();
            }
        }
    }

    private void ternaryOperatorMismatch() throws CalculatorException {
        String message = "Ошибка записи тернарного оператора";
        log.error(message);
        throw new CalculatorException(message);
    }

    private void processOperationSign(Expression token) {
        Operator operatorToken = (Operator) token;

        if (operatorToken.getValue().equals("-")) {
            int index = tokens.indexOf(operatorToken);

            if (index == 0) {
                operatorToken.setValue("±");
            } else {
                Set<String> possibleOperatorsBeforeUnaryMinus = new HashSet<>(
                        List.of("(", "?", ":", "<", ">", "==", "<=", ">=", "!="));
                Expression previousToken = tokens.get(index - 1);
                if (possibleOperatorsBeforeUnaryMinus.contains(previousToken.getValue().toString())) {
                    operatorToken.setValue("±");
                }
            }
        }

        if (operators.isEmpty()) {
            operators.push(operatorToken);
        } else {
            Operator topOperator = (Operator) operators.peek();
            while (topOperator.getPriority() >= operatorToken.getPriority()) {
                outputExpression.add(operators.pop());
                if (operators.isEmpty()) {
                    break;
                }
                topOperator = (Operator) operators.peek();
            }
            operators.push(operatorToken);
        }
    }
}
