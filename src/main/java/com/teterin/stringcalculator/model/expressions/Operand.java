package com.teterin.stringcalculator.model.expressions;

import com.teterin.stringcalculator.model.CalculatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Operand implements Expression {

    private static final Logger log = LoggerFactory.getLogger(Operand.class);
    private Double value;

    public Operand(String literal) throws CalculatorException {
        setValue(literal);
    }

    private void setValue(String literal) throws CalculatorException {
        try {
            value = Double.parseDouble(literal);
        } catch (NumberFormatException e) {
            String message = String.format("Некорректное число: %s", literal);
            log.error(message);
            throw new CalculatorException(message, e);
        }
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.OPERAND;
    }

    @Override
    public Double getValue() {
        return value;
    }
}
