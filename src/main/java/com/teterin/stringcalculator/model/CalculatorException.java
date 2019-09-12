package com.teterin.stringcalculator.model;

public class CalculatorException extends Exception {

    CalculatorException(String message) {
        super(message);
    }

    public CalculatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
