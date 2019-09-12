package com.teterin.stringcalculator.model;

public interface Observer {

    void infixExpressionUpdated(String infixExpression);

    void errorThrown(String error);
}
