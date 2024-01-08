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
import android.text.TextUtils;
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
public class UptadeAlert extends AppCompatActivity {
    /*Burdaki Class AddAlert classı ile tamamen aynı
    * sadece farklı olarak değişkenlerin başına uptade edildiğini ifade etmek için
    * u ekledik kullanıcı alarma tıkladğında değerleri değiştirdiği zaman bu değişkenler
    * sayesinde alarmı güncelliyoruz.*/
    private static final int PICK_AUDIO_REQUEST = 1;
    private Uri selectedAudioUri;
        public static String utime;
        public static int hour;
        public static int minute;
        public static String uRingtone;
        public static LocalTime time1;
        public static String utext;
        public static String usubText;
        public static String uRealTime;
        public static String udate;
        public static boolean isUptade;
        public static Spinner usecenek;
        public static int uLeftTime;
        private Button udateButton;
        private Button utimeButton;
        private Button uRingToneButton;
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
            uRingToneButton=findViewById(R.id.uringTone);
            uptade=findViewById(R.id.uptadeAllert);
            ubaslik=findViewById(R.id.ubaslik);
            uaciklama=findViewById(R.id.uaciklama);
            udateButton.setText(udate);
            utimeButton.setText(utime);
            ubaslik.setText(utext);
            uaciklama.setText(usubText);
            uRingToneButton.setText(AddAlert.getFileNameFromUri(this,Uri.parse(uRingtone)));
            ArrayAdapter<String > adaptr=new ArrayAdapter<>(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,AddAlert.secenekler);
            usecenek.setAdapter(adaptr);
            usecenek.setSelection(uLeftTime);
            isUptade=false;
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
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    utext=ubaslik.getText().toString();
                    usubText=uaciklama.getText().toString();
                    String t=utime;
                    String d=udate;
                    time1=LocalTime.of(hour,minute);
                    time1=time1.minusMinutes(uLeftTime*10);
                    String urealTime=time1.toString();
                    uRealTime=udate+" "+urealTime;
                    if(TextUtils.isEmpty(utext))
                        utext=uRealTime;
                    if(!(MainActivity.isFutureAlarm(uRealTime)))
                    //Alarm İleri bir tarihte değilse kurmuyor.
                    {
                        Toast.makeText(UptadeAlert.this, "Lütfen İleri Bir Tarih Seçiniz", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    Toast.makeText(UptadeAlert.this, uRealTime, Toast.LENGTH_SHORT).show();
                    getContentResolver().update(MainActivity.CONTENT_URI,null,null,null);
                    isUptade=true;
                    MainActivity.loadData();
                    Intent intent=new Intent(UptadeAlert.this,MainActivity.class);
                    startActivity(intent);}

                }
            });
            uRingToneButton.setOnClickListener(view -> openAudioPicker());
        }
    private void openAudioPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent,PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedAudioUri = data.getData();
            uRingtone=selectedAudioUri.toString();
            uRingToneButton.setText(AddAlert.getFileNameFromUri(this,selectedAudioUri));
        }
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
        udate= formattedDate;
        udateButton.setText(udate);
    }
        private void openTimeDiaolog(){
            TimePickerDialog dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                    hour=hours;
                    minute=minutes;
                    String formattedTime = String.format("%02d:%02d", hours, minutes);
                    time1=LocalTime.of(hours,minutes);
                    time1=time1.minusMinutes(uLeftTime*10);
                    utime= formattedTime;
                    utimeButton.setText(utime);
                }
            }, 10, 3, true);
            dialog.show();
        }
}