package com.teterin.stringcalculator;

import com.teterin.stringcalculator.controller.Controller;
import com.teterin.stringcalculator.model.Calculator;
import com.teterin.stringcalculator.view.GUI;

import javax.swing.*;

public class Launcher {

    public static void main(String[] args) {
        Calculator model = new Calculator();
        Controller controller = new Controller(model);
        SwingUtilities.invokeLater(() -> {
            GUI view = new GUI(controller);
            model.addObserver(view);
        });
    }
}
