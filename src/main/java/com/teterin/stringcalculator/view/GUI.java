package com.teterin.stringcalculator.view;

import com.teterin.stringcalculator.controller.Controller;
import com.teterin.stringcalculator.model.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GUI implements Observer {

    private static final List<String> SYMBOLS = new ArrayList<>(List.of(
            "C", "<–", "%", "*", "(", ")",
            "7", "8", "9", "/", ">", "<",
            "4", "5", "6", "+", ">=", "<=",
            "1", "2", "3", "-", "==", "!=",
            "0", "00", ".", "=", "?", ":"
    ));
    private static final String FONT_NAME = "TimesRoman";
    private Controller controller;
    private JFrame frame;
    private JTextField input;
    private JLabel systemMessages;

    public GUI(Controller controller) {
        this.controller = controller;
        frame = new JFrame();
        initDisplaysPanel();
        initButtonsPanel();
        initFrame();
    }

    private void initDisplaysPanel() {
        Font inputFont = new Font(FONT_NAME, Font.PLAIN, 20);
        input = new JTextField();
        input.setFont(inputFont);
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (input.getText().length() >= 24) {
                    e.consume();
                }
            }
        });
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    controller.pressButton(input.getText(), "=");
                }
            }
        });
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    systemMessages.setText("");
                }
            }
        });

        Font errorFont = new Font(FONT_NAME, Font.PLAIN, 12);
        systemMessages = new JLabel();
        systemMessages.setFont(errorFont);
        systemMessages.setForeground(Color.RED);

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setPreferredSize(new Dimension(0, 74));
        displayPanel.setBorder(new EmptyBorder(24, 4, 0, 4));
        displayPanel.add(input, BorderLayout.NORTH);
        displayPanel.add(systemMessages, BorderLayout.SOUTH);

        frame.getContentPane().add(BorderLayout.NORTH, displayPanel);
    }

    private void initButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
        buttonsPanel.setLayout(new GridLayout(5, 6, 3, 3));

        for (String symbol : SYMBOLS) {
            JButton button = new JButton(symbol);
            Font font = new Font(FONT_NAME, Font.BOLD, 17);
            button.setFont(font);
            button.setPreferredSize(new Dimension(55, 40));
            button.addActionListener(e -> controller.pressButton(input.getText(), symbol));
            buttonsPanel.add(button);
        }

        frame.getContentPane().add(BorderLayout.SOUTH, buttonsPanel);
    }

    private void initFrame() {
        frame.setTitle("Калькулятор");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void infixExpressionUpdated(String infixExpression) {
        input.setText(infixExpression);
        systemMessages.setText("");
    }

    @Override
    public void errorThrown(String error) {
        systemMessages.setText(error);
    }
}
