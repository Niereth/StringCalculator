package com.teterin.stringcalculator.model;

import com.teterin.stringcalculator.model.expressions.Expression;
import com.teterin.stringcalculator.model.expressions.ExpressionType;
import com.teterin.stringcalculator.model.expressions.Operand;
import com.teterin.stringcalculator.model.expressions.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Calculator implements Observed {

    private static final Logger log = LoggerFactory.getLogger(Calculator.class);
    private List<Observer> observers;
    private StringBuilder infixExpression;
    private Deque<Double> operands;
    private String result;

    public Calculator() {
        observers = new ArrayList<>();
        infixExpression = new StringBuilder();
        operands = new ArrayDeque<>();
    }

    public void onButtonPressed(String inputText, String symbol) {
        infixExpression.setLength(0);
        infixExpression.append(inputText);

        if (symbol.equals("%")) {
            notifyErrorThrown("Операция '%' еще не реализована.");
            return;
        }

        try {
            handleButtonCommand(symbol);
        } catch (CalculatorException e) {
            notifyErrorThrown(e.getMessage());
        }
    }

    private void handleButtonCommand(String symbol) throws CalculatorException {
        switch (symbol) {
            case "C":
                infixExpression.setLength(0);
                break;
            case "<–":
                infixExpression.setLength(Math.max(infixExpression.length() - 1, 0));
                break;
            case "=":
                startCalculationProcess();
                removeFractionalZeroFromResultIfNecessary();
                infixExpression.setLength(0);
                infixExpression.append(result);
                break;
            default:
                infixExpression.append(symbol);
        }
        notifyInfixExpressionUpdated();
    }

    private void startCalculationProcess() throws CalculatorException {
        Translator translator = new Translator(infixExpression.toString());
        List<Expression> postfixExpression = translator.translateToPostfixNotation();

        checkDivisionByZero(postfixExpression);

        for (Expression token : postfixExpression) {
            processPostfixToken(token);
        }

        if (operands.size() != 1) {
            String message = "Некорректное выражение";
            log.error(message);
            throw new CalculatorException(message);
        }

        result = operands.pop().toString();
    }

    private void checkDivisionByZero(List<Expression> postfixExpression) throws CalculatorException {
        for (int i = 1; i < postfixExpression.size() - 1; i++) {
            if (postfixExpression.get(i + 1).getValue().equals("/")
                    && postfixExpression.get(i).getValue().toString().equals("0.0")) {
                String message = "Разделить на ноль нельзя";
                log.error(message);
                throw new CalculatorException(message);
            }
        }
    }

    private void processPostfixToken(Expression token) throws CalculatorException {
        ExpressionType tokenType = token.getType();
        if (tokenType == ExpressionType.OPERAND) {
            Operand operand = (Operand) token;
            operands.push(operand.getValue());
        } else {
            Operator operator = (Operator) token;
            OperatorExecutor executor = new OperatorExecutor(operands);
            operands.push(executor.calculateOperation(operator));
        }
    }

    private void removeFractionalZeroFromResultIfNecessary() {
        String[] numerals = result.split("\\.");
        if (numerals[1].equals("0")) {
            result = numerals[0];
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyInfixExpressionUpdated() {
        for (Observer observer : observers) {
            observer.infixExpressionUpdated(infixExpression.toString());
        }
    }

    private void notifyErrorThrown(String error) {
        for (Observer observer : observers) {
            observer.errorThrown(error);
        }
    }
}
