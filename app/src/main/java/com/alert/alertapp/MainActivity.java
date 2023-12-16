package com.alert.alertapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity implements RCClickInterface {
    private static CustomAdapter customAdapter;
    private RecyclerView recyclerView;
    public static  AlertProvider source;
    public static String uId;
    public final static Uri CONTENT_URI=AlertProvider.CONTENT_URI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlertProvider.myDb=new DataBase(this);
        source=new AlertProvider(this);
        source.openDb();
        Button addButton =(Button) findViewById(R.id.addButton);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customAdapter = new CustomAdapter(this, null,this);
        recyclerView.setAdapter(customAdapter);
        loadData();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AddAlert.subText=null;
                    AddAlert.text=null;
                Intent intent=new Intent(MainActivity.this,AddAlert.class);
                startActivity(intent);
            }
        });
}//on create




    public static void loadData() {
        Cursor cursor = source.getData();
        customAdapter.swapCursor(cursor);
    }

    @Override
    public void onItemClick(String content) {
        String[] columns = {"baslik,aciklama,tarih,saat,sure"};
        uId=content;
        getContentResolver().query(CONTENT_URI,columns,null,null,null);
        Intent intent=new Intent(MainActivity.this,UptadeAlert.class);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(String content) throws InterruptedException {
        getContentResolver().delete(CONTENT_URI,"id="+content,null);
        loadData();

    }
}