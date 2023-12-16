package com.alert.alertapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;


public class UptadeAlert extends AppCompatActivity {
        public static String utime;
        public static String utext;
        public static String usubText;
        public static String udate;
        public static Spinner usecenek;
        public static int uLeftTime;
        private Button udateButton;
        private Button utimeButton;
        private Button uptade;
        public EditText ubaslik;
        public  EditText uaciklama;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_uptade_alert);
            usecenek=findViewById(R.id.usecenek);
            udateButton=(Button) findViewById(R.id.udateButton);
            utimeButton=findViewById(R.id.utimeButton);
            uptade=findViewById(R.id.uptadeAllert);
            ubaslik=findViewById(R.id.ubaslik);
            uaciklama=findViewById(R.id.uaciklama);
            udateButton.setText(udate);
            utimeButton.setText(utime);
            ubaslik.setText(utext);
            uaciklama.setText(usubText);
            ArrayAdapter<String > adaptr=new ArrayAdapter<>(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,AddAlert.secenekler);
            usecenek.setAdapter(adaptr);
            usecenek.setSelection(uLeftTime);
            usecenek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i){
                        case 0:
                            uLeftTime=0;
                            break;
                        case 1:
                            uLeftTime=1;
                            break;
                        case 2:
                            uLeftTime=2;
                            break;
                        case 3:
                            uLeftTime=3;
                            break;
                        case 4:
                            uLeftTime=4;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
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
                    utext=ubaslik.getText().toString();
                    usubText=uaciklama.getText().toString();
                    String t=utime;
                    String d=udate;
                    getContentResolver().update(MainActivity.CONTENT_URI,null,null,null);
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