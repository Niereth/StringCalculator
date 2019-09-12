package com.teterin.stringcalculator.model.expressions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Operator implements Expression {

    private static final Map<String, Integer> OPERATORS_DB = new HashMap<>();

    static {
        OPERATORS_DB.put("(", 0);
        OPERATORS_DB.put(")", 0);
        OPERATORS_DB.put("?", 1);
        OPERATORS_DB.put(":", 1);
        OPERATORS_DB.put("<", 2);
        OPERATORS_DB.put(">", 2);
        OPERATORS_DB.put("==", 2);
        OPERATORS_DB.put("<=", 2);
        OPERATORS_DB.put(">=", 2);
        OPERATORS_DB.put("!=", 2);
        OPERATORS_DB.put("+", 3);
        OPERATORS_DB.put("-", 3);
        OPERATORS_DB.put("*", 4);
        OPERATORS_DB.put("/", 4);
        OPERATORS_DB.put("±", 5);
    }

    private String value;

    public Operator(String literal) {
        value = literal;
    }

    public int getPriority() {
        return OPERATORS_DB.get(value);
    }

    public static Set<String> getOperators() {
        return OPERATORS_DB.keySet();
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.OPERATION_SIGN;
    }

    @Override
    public String getValue() {
        return value;
    }
}
