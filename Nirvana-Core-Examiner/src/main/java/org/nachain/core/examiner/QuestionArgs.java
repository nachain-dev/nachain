package org.nachain.core.examiner;


public class QuestionArgs {


    private String text;


    private double number;


    private int runTotal;

    public QuestionArgs(String text, int runTotal) {
        this.text = text;
        this.runTotal = runTotal;
    }

    public QuestionArgs(double number, int runTotal) {
        this.number = number;
        this.runTotal = runTotal;
    }


    public String getText() {
        return text;
    }

    public QuestionArgs setText(String text) {
        this.text = text;
        return this;
    }

    public double getNumber() {
        return number;
    }

    public QuestionArgs setNumber(double number) {
        this.number = number;
        return this;
    }

    public int getRunTotal() {
        return runTotal;
    }

    public QuestionArgs setRunTotal(int runTotal) {
        this.runTotal = runTotal;
        return this;
    }

    @Override
    public String toString() {
        return "QuestionArgs{" +
                "text='" + text + '\'' +
                ", number=" + number +
                ", runTotal=" + runTotal +
                '}';
    }
}
