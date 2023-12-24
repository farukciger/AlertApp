package com.alert.alertapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;
public class AddAlert extends AppCompatActivity {
    private static final int PICK_AUDIO_REQUEST = 1;
    private Uri selectedAudioUri;
    public static String ringTone;
    public static String time;
    public static String realDateTime;
    public static String text;
    static LocalTime time1;
    public static String subText;
    public static String date;
    public static Spinner secenek;
    public int RINGTONE_PICKER_REQUEST=1;
    public static int leftTime;
    public static String[] secenekler={"Hatırlatma Zamanı Seçin","10 Dakika Önce","20 Dakika Önce","30 Dakika Önce","40 Dakika Önce"};
    private Button dateButton;
    private Button timeButton;
    private Button ringToneButton;
    private Button add;
    public  EditText baslik;
    public  EditText aciklama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);
        secenek=findViewById(R.id.secenek);
        ringToneButton=findViewById(R.id.ringTone);
        dateButton=findViewById(R.id.button2);
        timeButton=findViewById(R.id.timeButton);
        add=findViewById(R.id.addAlert);
        baslik=findViewById(R.id.baslik);
        aciklama=findViewById(R.id.aciklama);
        baslik.setText(text);
        aciklama.setText(subText);
        ArrayAdapter<String > adapter=new ArrayAdapter<>(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,secenekler);
        secenek.setAdapter(adapter);
        secenek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,  int i, long l) {
                switch (i){
                    case 0:
                        leftTime=0;
                        break;
                    case 1:
                       leftTime=1;
                       break;
                    case 2:
                        leftTime=2;
                        break;
                    case 3:
                        leftTime=3;
                        break;
                    case 4:
                        leftTime=4;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ringToneButton.setOnClickListener(view ->openAudioPicker() );
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                 text=baslik.getText().toString();
                 subText=aciklama.getText().toString();
                String t=time;
                String d=date;
                time1=time1.minusMinutes(leftTime*10);
                realDateTime=d+" "+time1;
                Toast.makeText(AddAlert.this, realDateTime, Toast.LENGTH_SHORT).show();
                Uri uri=getContentResolver().insert(MainActivity.CONTENT_URI,AlertProvider.val);
                Intent ınten1=new Intent(AddAlert.this,MainActivity.class);
                startActivity(ınten1);
            }
        });
    }
    final Calendar currentDate = Calendar.getInstance();
    private void openDateDialog(){
        DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                currentDate.set(year,month,day);
                updateSelectedDate();
            }
        }, 2023, 12, 1);
        dialog.show();
    }
    private void updateSelectedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(currentDate.getTime());
        date= formattedDate;
        dateButton.setText(date);
    }
    private void openTimeDiaolog(){
        TimePickerDialog dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minute) {
                time1=LocalTime.of(hours,minute);
                String formattedTime = String.format("%02d:%02d", hours, minute);
                time= formattedTime;
                timeButton.setText(time);
            }
        }, 10, 3, true);
        dialog.show();
    }
    private void openAudioPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_AUDIO_REQUEST);
        ringToneButton.setText(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedAudioUri = data.getData();
            ringTone=selectedAudioUri.toString();
        }
    }
}