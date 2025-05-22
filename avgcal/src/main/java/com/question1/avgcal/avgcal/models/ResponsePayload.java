package com.question1.avgcal.avgcal.models;

import java.util.*;

public class ResponsePayload {
    private List<Integer> windowPrevState;
    private List<Integer> windowCurrState;
    private List<Integer> numbers;
    private double avg;

    public List<Integer> getWindowPrevState() {
        return windowPrevState;
    }
    public List<Integer> getWindowCurrState() {
        return windowCurrState;
    }
    public List<Integer> getNumbers() {
        return numbers;
    }
    public double getAvg() {
        return avg;
    }
    public ResponsePayload(List<Integer> windowPrevState, List<Integer> windowCurrState, List<Integer> numbers, double avg) {
        this.windowPrevState = windowPrevState;
        this.windowCurrState = windowCurrState;
        this.numbers = numbers;
        this.avg = avg;
    }
}

