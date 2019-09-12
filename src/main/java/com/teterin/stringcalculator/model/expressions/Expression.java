package com.teterin.stringcalculator.model.expressions;

public interface Expression {

    ExpressionType getType();

    Object getValue();
}
