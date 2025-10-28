package com.example.expense_recording;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expense_recording.Interfaces.SharedPreferences.SPUserManager;
import com.example.expense_recording.Class.SPUserManagerImpl;

import com.example.expense_recording.Enum.Date;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SPUserManager user_m;
    private Date now_stat_type;

    // UI Items
    private TextView tv_date;
    private TextView tv_average;
    private TextView tv_max;
    private TextView tv_min;
    private TextView tv_error_day;
    private TextView tv_error_rec;
    private TextView tv_h_stat;
    private TextView tv_total;
    private EditText et_day ;
    private EditText et_month;
    private EditText et_year;
    private EditText et_record;

    // if date is correct they return 0 else 1
    private int test_date(int day, int month, int year){
        if (month > 12 || month < 1) return 1;
        if (year > 2026 || year < 2000) return 1;
        if (day < 0) return 1;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
            if (day > 31) return 1;
        } else {
            if (day > 30) return 1;
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                if (day > 29) return 1;
            } else {
                if (day > 28) return 1;
            }
        }

        return 0;
    }

    // set date var from store.
    // for non valid data do throw new
    private void set_date(){
        int day   = user_m.getDay();
        int month = user_m.getMonth();
        int year  = user_m.getYear();

        if (test_date(day, month, year) == 1) {
            throw new RuntimeException("error var day/month/year");
        }

        String text = getString(R.string.date_info) + " " + day + " / " + month + " / " + year;
        this.tv_date.setText(text);
    }

    // set to view text statistics
    private void set_stat(){
        String temp = "error";
        Date temp_d =  this.now_stat_type;

        if      (temp_d == Date.WEEK ) temp = getString(R.string.week );
        else if (temp_d == Date.MONTH) temp = getString(R.string.month);

        String average = getString(R.string.statistics_average_info) + " " + temp + " " + String.format(Locale.getDefault(), "%.2f", user_m.get_average(temp_d)) + "$";
        String max     = getString(R.string.statistics_max_info)     + " " + temp + " " + String.format(Locale.getDefault(), "%.2f", user_m.get_max(temp_d))     + "$";
        String min     = getString(R.string.statistics_max_info)     + " " + temp + " " + String.format(Locale.getDefault(), "%.2f", user_m.get_min(temp_d))     + "$";
        String total   = getString(R.string.statistics_total_info)   + " " + temp + " " + String.format(Locale.getDefault(), "%.2f", user_m.total(temp_d))       + "$";

        String h_stat = getString(R.string.statistics_title) + " " + temp;

        this.tv_average.setText(average);
        this.tv_max.    setText(max    );
        this.tv_min.    setText(min    );
        this.tv_total.  setText(total  );

        this.tv_h_stat. setText(h_stat);
    }

    // --buttons Methods--

    // write record
    // if not set day button off
    // if empty or not valid input show message
    private void onClick_record(){
        // not save record
        if (this.user_m.getDay() == -1 || this.user_m.getMonth() == -1 || this.user_m.getYear() == -1) return;

        try {
            float record = Float.parseFloat(this.et_record.getText().toString());
            this.user_m.add_record(record);
            set_stat();
            if (this.tv_error_rec.getVisibility() == TextView.VISIBLE) this.tv_error_rec.setVisibility(TextView.INVISIBLE);
        } catch (Exception e) {
            this.tv_error_rec.setVisibility(TextView.VISIBLE);
        }
    }

    // set date
    // if empty or not valid input show message
    private void onClick_date(){
        try {
            int day   = Integer.parseInt(this.et_day.getText().toString()  );
            int month = Integer.parseInt(this.et_month.getText().toString());
            int year  = Integer.parseInt(this.et_year.getText().toString() );

            this.user_m.change_date(day, month, year);
            set_date();
            set_stat();
            if (this.tv_error_day.getVisibility() == TextView.VISIBLE) this.tv_error_day.setVisibility(TextView.INVISIBLE);
        } catch (Exception e) {
            this.tv_error_day.setVisibility(TextView.VISIBLE);
        }
    }
    // show statistics (week/month)
    private void onClick_set_type_statistics(Date d){
        this.now_stat_type = d;
        set_stat();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // data store
        this.user_m = new SPUserManagerImpl(this);

        // set TextView
        this.tv_date      = findViewById(R.id.textView_data             );
        this.tv_average   = findViewById(R.id.textView_average          );
        this.tv_max       = findViewById(R.id.textView_max              );
        this.tv_min       = findViewById(R.id.textView_min              );
        this.tv_total     = findViewById(R.id.textView_total            );
        this.tv_error_day = findViewById(R.id.textView_error_day        );
        this.tv_h_stat    = findViewById(R.id.textView_header_statistics);
        this.tv_error_rec = findViewById(R.id.textView_error_rec        );

        // set EditText
        this.et_day    = findViewById(R.id.editText_day   );
        this.et_month  = findViewById(R.id.editText_month );
        this.et_year   = findViewById(R.id.editText_year  );
        this.et_record = findViewById(R.id.editText_record);

        // set Buttons
        Button btn_save_record = findViewById(R.id.button_save_record);
        btn_save_record.setOnClickListener(v -> onClick_record());

        Button btn_set_date = findViewById(R.id.button_set_date);
        btn_set_date.setOnClickListener(v -> onClick_date());

        Button btn_week_info  = findViewById(R.id.button_week_info );
        Button btn_month_info = findViewById(R.id.button_month_info);
        btn_week_info.setOnClickListener (v -> onClick_set_type_statistics(Date.WEEK ));
        btn_month_info.setOnClickListener(v -> onClick_set_type_statistics(Date.MONTH));

        // set today day
        this.now_stat_type = Date.WEEK;

        // if day not exist
        try {
            set_date();
        } catch (Exception e){
            String text = getString(R.string.date_info) + " none";
            this.tv_date.setText(text);
        }

        set_stat();
    }

}