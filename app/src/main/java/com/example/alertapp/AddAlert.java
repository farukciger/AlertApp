package com.example.alertapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class AddAlert extends AppCompatActivity {
    public String time;
    public String date;
    private Button dateButton;
    private Button timeButton;
    private Button add;
    public EditText baslik;
    public EditText aciklama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);
        dateButton=findViewById(R.id.button2);
        timeButton=findViewById(R.id.timeButton);
        add=findViewById(R.id.addAlert);
        baslik=findViewById(R.id.baslik);
        aciklama=findViewById(R.id.aciklama);
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=baslik.getText().toString();
                String subText=aciklama.getText().toString();
                String t=time;
                String d=date;
                Alerts alerts=new Alerts(text,subText,d,t);
                MainActivity.source.openDb();
                MainActivity.source.addAlert(alerts);
                Intent ınten1=new Intent(AddAlert.this,MainActivity.class);
                startActivity(ınten1);
            }
        });
    }
    private void openDateDialog(){
        DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date= year +"."+ (month + 1) +"."+ day;
                dateButton.setText(date);
            }
        }, 2023, 12, 1);
        dialog.show();
    }
    private void openTimeDiaolog(){
        TimePickerDialog dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minute) {
                time= hours +":"+ minute;
                timeButton.setText(time);
            }
        }, 10, 3, true);
        dialog.show();
    }

}