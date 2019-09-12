package com.teterin.stringcalculator.model;

public interface Observed {

    void addObserver(Observer observer);

    void removeObserver(Observer observer);
}
