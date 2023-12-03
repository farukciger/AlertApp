package com.example.alertapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class AddAlert extends AppCompatActivity {
    private Button dateButton;
    private Button timeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);
        dateButton=findViewById(R.id.button2);
        timeButton=findViewById(R.id.timeButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateDialog();
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimeDiaolog();
            }
        });
    }
    private void openDateDialog(){
        DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateButton.setText(String.valueOf(year)+"."+String.valueOf(month+1)+"."+String.valueOf(day));
            }
        }, 2023, 12, 1);
        dialog.show();
    }
    private void openTimeDiaolog(){
        TimePickerDialog dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minute) {
                timeButton.setText(String.valueOf(hours)+":"+String.valueOf(minute));
            }
        }, 10, 3, true);
        dialog.show();
    }
}