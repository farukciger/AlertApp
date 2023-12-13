package com.example.alertapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements RCClickInterface {
    private static CustomAdapter customAdapter;
    private RecyclerView recyclerView;
    public static  DataSource source;
    public static String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_main);
        source=new DataSource(this);
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
        uId=content;
        DataSource.query(content);
        Intent intent=new Intent(MainActivity.this,UptadeAlert.class);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(String content) throws InterruptedException {
        DataSource.removeAlert(content);
        loadData();

    }
}