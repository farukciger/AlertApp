package com.alert.alertapp;

public interface RCClickInterface {
    void onItemClick(String content);
    void onItemLongClick(String content) throws InterruptedException;
    //Bu interface tıklama olayları için yazıldı
}
