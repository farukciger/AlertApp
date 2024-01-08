package com.alert.alertapp;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
public class AddAlert extends AppCompatActivity {
    private static final int PICK_AUDIO_REQUEST = 1;
    private Uri selectedAudioUri;
    public static List<Alerts> alerts=new ArrayList<>();
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
        /*Burda Butonları Tanımlıyoruz*/
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
        //Spinner için ArrayAdapter kullandım.
        ArrayAdapter<String > adapter=new ArrayAdapter<>(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,secenekler);
        secenek.setAdapter(adapter);
        secenek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,  int i, long l) {
                switch (i){
                    case 0:
                        leftTime=0;//eğer kullanıcı ilk seceneği seçerse alarm tam zamanında çalcak.
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
        //Tarih saat ve zil sesi seçimleri için butonlar
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
        add.setOnClickListener(new View.OnClickListener()
        //Ekleye basınca yapılcaklar
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String d=date;
                time1=time1.minusMinutes(leftTime*10);
                //üstteki atama gerçek zamanı alıyor(Alarmın Çalacağı Zaman).
                realDateTime=d+" "+time1;
                text=baslik.getText().toString();
                if(TextUtils.isEmpty(text))
                    text=realDateTime;
                    //eğer başlık texti boş bıraklırsa varsayılan olarak alarm zamanı atancak
                subText=aciklama.getText().toString();
                if (!(MainActivity.isFutureAlarm(realDateTime))){
                    Toast.makeText(AddAlert.this, "Lütfen İleri Tarihli Bir Alarm Seçiniz", Toast.LENGTH_SHORT).show();
                }
                else{
                Toast.makeText(AddAlert.this, realDateTime, Toast.LENGTH_SHORT).show();
                //Seçi yapıldıktan sonra seçimi mesaj olarak gösteriyor
                Uri uri=getContentResolver().insert(MainActivity.CONTENT_URI,AlertProvider.val);
                //veri tabanına alarmı ekliyor.
                Intent ınten1=new Intent(AddAlert.this,MainActivity.class);
                startActivity(ınten1);
                //Ana sayfaya dönüyor
            }}
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
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_AUDIO_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedAudioUri = data.getData();
            ringTone=selectedAudioUri.toString();
            ringToneButton.setText(getFileNameFromUri(this,selectedAudioUri));
        }
    }
    public static String getFileNameFromUri(Context context, Uri uri)
    /*Bu metod gönderilen Urinin dosya adını bulmak için.
    Veri tabanında dosya adı uri olarak tutuluyor fakat kullancı seçtiği
    müzik dosyasının adını görmesi gerekiyor bu metod sayesinde uri dosya adına çevriliyor*/
    {
        if (uri == null) {
            return null;
        }

        if (DocumentsContract.isDocumentUri(context, uri)) {
            // SAF kullanarak
            DocumentFile documentFile = DocumentFile.fromSingleUri(context, uri);
            if (documentFile != null) {
                return documentFile.getName();
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // ContentResolver kullanarak
            String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                    return cursor.getString(columnIndex);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // Dosya şeması kullanarak
            return uri.getLastPathSegment();
        }

        return null;
    }
}