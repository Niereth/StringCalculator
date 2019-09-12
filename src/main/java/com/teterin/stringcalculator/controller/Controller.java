package com.teterin.stringcalculator.controller;

import com.teterin.stringcalculator.model.Calculator;

public class Controller {

    private Calculator model;

    public Controller(Calculator model) {
        this.model = model;
    }

    public void pressButton(String inputText, String symbol) {
        model.onButtonPressed(inputText, symbol);
    }
}
