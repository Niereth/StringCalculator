package com.teterin.stringcalculator.model;

import com.teterin.stringcalculator.model.expressions.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

class OperatorExecutor {

    private static final Logger log = LoggerFactory.getLogger(OperatorExecutor.class);
    private Deque<Double> operands;

    OperatorExecutor(Deque<Double> operands) {
        this.operands = operands;
    }

    Double calculateOperation(Operator operator) throws CalculatorException {
        Double result;

        if (operator.getValue().equals("?")) {
            result = calculateTernaryOperation(getParameters(3));
        } else if (operator.getValue().equals("±")) {
            result = calculateUnaryOperation(getParameters(1));
        } else {
            result = calculateBinaryOrComparisionOperation(operator, getParameters(2));
        }

        return result;
    }

    private Double calculateTernaryOperation(List<Double> parameters) throws CalculatorException {
        Double parameter;
        if (parameters.get(2) == 1) {
            parameter = parameters.get(1);
        } else if (parameters.get(2) == 0) {
            parameter = parameters.get(0);
        } else {
            String message = "В тернарном операторе не задано условие";
            log.error(message);
            throw new CalculatorException(message);
        }
        return parameter;
    }

    private Double calculateUnaryOperation(List<Double> parameters) {
        return -parameters.get(0);
    }

    private Double calculateBinaryOrComparisionOperation(Operator operator, List<Double> parameters) {
        double result = 0.0;
        double a = parameters.get(1);
        double b = parameters.get(0);

        switch (operator.getValue()) {
            case "<":
                result = a < b ? 1 : 0;
                break;
            case ">":
                result = a > b ? 1 : 0;
                break;
            case "==":
                result = a == b ? 1 : 0;
                break;
            case "<=":
                result = a <= b ? 1 : 0;
                break;
            case ">=":
                result = a >= b ? 1 : 0;
                break;
            case "!=":
                result = a != b ? 1 : 0;
                break;
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            case "/":
                result = a / b;
                break;
            default:
        }

        return result;
    }

    private List<Double> getParameters(int amount) throws CalculatorException {
        List<Double> parameters = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            if (!operands.isEmpty()) {
                parameters.add(operands.pop());
            } else {
                String message = "Некорректное выражение";
                log.error(message);
                throw new CalculatorException(message);
            }
        }
        return parameters;
    }
}
