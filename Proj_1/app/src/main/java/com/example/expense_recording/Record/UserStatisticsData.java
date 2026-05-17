package com.example.expense_recording.Record;

// record
public class UserStatisticsData {
    private float average, max, min, total;
    public UserStatisticsData(float average, float max, float min, float total){
        this.average = average;
        this.max = max;
        this.min = min;
        this.total = total;
    }

    public void setAverage(float average) { this.average = average; }
    public void setMax(float max){ this.max = max; }
    public void setMin(float min){ this.min = min; }
    public void setTotal(float total){ this.total = total; }

    public float getAverage() { return this.average; }
    public float getMax() { return this.max; }
    public float getMin() { return this.min;}
    public float getTotal() { return this.total; }
}
