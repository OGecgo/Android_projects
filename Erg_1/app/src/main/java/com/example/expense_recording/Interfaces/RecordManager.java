package com.example.expense_recording.Interfaces;

public interface RecordDao {

    void insert(int day, int record);
    void update(int day, int record);
    int read(int day);
}
