package com.example.alertapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RClickInterface {
    ArrayList<Records> record=new ArrayList<>();
    Records records;
    Context context=this;
    int imaj;
    RecyclerView recyclerView;
    RecylerCustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyleView);
        imaj=R.drawable.baseline_alarm_24;
        records=new Records("Çalar Saat","saat");record.add(records);
        adapter=new RecylerCustomAdapter(context,this,record);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemLongClick(int position) {

    }
}