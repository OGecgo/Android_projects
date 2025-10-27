package com.example.expense_recording;

import android.content.Context;
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

public class MainActivity extends AppCompatActivity {

    private SPUserManager user_m;

    // UI Items
    private TextView tv_date;
    private TextView tv_average;
    private TextView tv_max;
    private TextView tv_min;
    private TextView tv_total;
    private EditText et_day ;
    private EditText et_month;
    private EditText et_year;
    private EditText et_record;
    private Button btn_save_record;
    private Button btn_set_date;  
    private Button btn_week_info;
    private Button btn_month_info; 



    private void onClick_record(){
        int record = Integer.parseInt(this.et_record.getText().toString());
        this.user_m.add_record(record);
    }
    private void onClick_date(){
        int day   = Integer.parseInt(this.et_day.getText().toString()  );
        int month = Integer.parseInt(this.et_month.getText().toString());
        int year  = Integer.parseInt(this.et_year.getText().toString() );

        this.user_m.change_date(day, month, year);
    }
    private void onClick_set_type_statistics(Date d){
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
        this.tv_date    = (TextView) findViewById(R.id.textView_data   );
        this.tv_average = (TextView) findViewById(R.id.textView_average);
        this.tv_max     = (TextView) findViewById(R.id.textView_max    );
        this.tv_min     = (TextView) findViewById(R.id.textView_min    );
        this.tv_total   = (TextView) findViewById(R.id.textView_total  );

        // set EditText
        this.et_day    = (EditText) findViewById(R.id.editText_day   );
        this.et_month  = (EditText) findViewById(R.id.editText_month );
        this.et_year   = (EditText) findViewById(R.id.editText_year  );
        this.et_record = (EditText) findViewById(R.id.editText_record);

        // set Buttons
        this.btn_save_record = (Button) findViewById(R.id.button_save_record);
        this.btn_save_record.setOnClickListener(v -> onClick_record());

        this.btn_set_date = (Button) findViewById(R.id.button_set_date);
        this.btn_set_date.setOnClickListener(v -> onClick_date());

        this.btn_week_info  = (Button) findViewById(R.id.button_week_info );
        this.btn_month_info = (Button) findViewById(R.id.button_month_info);
        this.btn_week_info.setOnClickListener (v -> onClick_set_type_statistics(Date.WEEK ));
        this.btn_month_info.setOnClickListener(v -> onClick_set_type_statistics(Date.MONTH));


    }

}