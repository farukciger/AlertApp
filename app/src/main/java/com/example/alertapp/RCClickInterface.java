package com.example.alertapp;

public interface RCClickInterface {
    void onItemClick(String content);
    void onItemLongClick(String content) throws InterruptedException;
}
