package com.alert.alertapp;
public class Alerts {
    public String ringTone;
    private String baslik;
    private String aciklama;
    private String tarih;
    private String saat;
    private int leftTime;
    private long id;
    private String realTime;

    public Alerts(String baslik, String aciklama, String tarih, String saat,int lefTime,String ringTone,String realTime) {
        /*Bu Classımız Alarmın özelliklerini içeriyor*/
        int imaj = R.drawable.baseline_alarm_24;
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.tarih = tarih;
        this.saat = saat;
        this.leftTime=lefTime;
        this.ringTone=ringTone;
        this.realTime=realTime;
    }

    public int getLeftTime() {
        return leftTime;
    }

    @Override
    public String toString() {
        return
                "baslik='" + baslik + "\n" +
                "aciklama='" + aciklama + "\n"+
                "id="+id
                ;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {

        this.id = id;
    }
    public String getBaslik() {
        return baslik;
    }
    public String getAciklama() {
        return aciklama;
    }
    public String getTarih() {
        return tarih;
    }

    public String getSaat() {
        return saat;
    }

    public String getRingTone() {
        return ringTone;
    }
    public String getRealTime() {
        return realTime;
    }

}
