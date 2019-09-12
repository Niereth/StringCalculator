package com.teterin.stringcalculator.model;

import com.teterin.stringcalculator.model.expressions.Expression;
import com.teterin.stringcalculator.model.expressions.Operand;
import com.teterin.stringcalculator.model.expressions.Operator;

import java.util.ArrayList;
import java.util.List;

class Tokenizer {

    private String inputExpression;
    private List<Expression> tokens;
    private int position;
    private StringBuilder digit;

    Tokenizer(String expression) {
        inputExpression = expression;
        tokens = new ArrayList<>();
        digit = new StringBuilder();
    }

    List<Expression> getTokens() throws CalculatorException {

        skipSpaces();

        while (position < inputExpression.length()) {
            String operator = getOperatorFromDataBaseIfExists();

            if (operator == null) {
                Character symbol = inputExpression.charAt(position);
                digit.append(symbol);
                skipToken(symbol.toString());
            } else {
                addDigit();
                addOperator(operator);
                skipToken(operator);
                skipSpaces();
            }
        }

        addDigit();

        return tokens;
    }

    private void addDigit() throws CalculatorException {
        if (!digit.toString().isEmpty()) {
            tokens.add(new Operand(digit.toString()));
            digit.setLength(0);
        }
    }

    private void addOperator(String operator) {
        tokens.add(new Operator(operator));
    }

    private void skipToken(String token) {
        position += token.length();
    }

    private void skipSpaces() {
        while (position < inputExpression.length() && inputExpression.charAt(position) == ' ') {
            position++;
        }
    }

    /**
     * Из карты операторов по хэшу символ ">" расположен раньше символа ">=",
     * из-за чего при проверке символа ">=" всегда будет возвращен символ ">", что является ошибкой.
     * Поэтому сначала идет проверка на символ ">=" вне цикла перебора всех операторов.
     */
    private String getOperatorFromDataBaseIfExists() {
        if (inputExpression.startsWith(">=", position)) {
            return ">=";
        }
        for (String operatorSign : Operator.getOperators()) {
            if (inputExpression.startsWith(operatorSign, position)) {
                return operatorSign;
            }
        }
        return null;
    }
}
