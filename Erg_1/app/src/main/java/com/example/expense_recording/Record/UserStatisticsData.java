package com.example.expense_recording.Record;

// record
public class UserData {
    private int average, max, min, total;
    public UserData(int average, int max, int min, int total){
        this.average = average;
        this.max = max;
        this.min = min;
        this.total = total;
    }

    public void setAverage(int average) { this.average = average; }
    public void setMax(int max){ this.max = max; }
    public void setMin(int min){ this.min = min; }
    public void setTotal(int total){ this.total = total; }

    public int getAverage() { return this.average; }
    public int getMax() { return this.max; }
    public int getMin() { return this.min;}
    public int getTotal() { return this.total; }
}
