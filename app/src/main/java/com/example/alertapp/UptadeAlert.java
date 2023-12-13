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

public class UptadeAlert extends AppCompatActivity {
        public static String utime;
        public static String utext;
        public static String usubText;
        public static String udate;
        private Button udateButton;
        private Button utimeButton;
        private Button uptade;
        public EditText ubaslik;
        public  EditText uaciklama;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_uptade_alert);
            udateButton=(Button) findViewById(R.id.udateButton);
            utimeButton=findViewById(R.id.utimeButton);
            uptade=findViewById(R.id.uptadeAllert);
            ubaslik=findViewById(R.id.ubaslik);
            uaciklama=findViewById(R.id.uaciklama);
            udateButton.setText(udate);
            utimeButton.setText(utime);
            ubaslik.setText(utext);
            uaciklama.setText(usubText);
            udateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDateDialog();
                }
            });
            utimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openTimeDiaolog();
                }
            });
            uptade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String key=MainActivity.uId;
                    utext=ubaslik.getText().toString();
                    usubText=uaciklama.getText().toString();
                    String t=utime;
                    String d=udate;
                    DataSource.updateValueForKey(key);
                    MainActivity.loadData();
                    Intent intent=new Intent(UptadeAlert.this,MainActivity.class);
                    startActivity(intent);

                }
            });
        }
        private void openDateDialog(){
            DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    udate= year +"."+ (month + 1) +"."+ day;
                    udateButton.setText(udate);
                }
            }, 2023, 12, 1);
            dialog.show();
        }
        private void openTimeDiaolog(){
            TimePickerDialog dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hours, int minute) {
                    utime= hours +":"+ minute;
                    utimeButton.setText(utime);
                }
            }, 10, 3, true);
            dialog.show();
        }
}