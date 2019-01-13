package com.example.davkimfray.shuta4;

public class Results {
    private String exaName, subName;
    private double subMarks;
    private int exaId;

    public Results(int exaId, String exaName, String subName, double subMarks){
        this.exaId = exaId;
        this.exaName = exaName;
        this.subName = subName;
        this.subMarks = subMarks;
    }

    public int getExaId() {
        return exaId;
    }

    public String getExaName() {
        return exaName;
    }

    public String getSubName() {
        return subName;
    }

    public double getSubMarks() {
        return subMarks;
    }
}
