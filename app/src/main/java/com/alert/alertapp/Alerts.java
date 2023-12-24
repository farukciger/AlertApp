package com.alert.alertapp;



public class Alerts {
    private int imaj;
    public String ringTone;
    private String baslik;
    private String aciklama;
    private String tarih;
    private String saat;
    private int leftTime;
    private long id;

    public Alerts(String baslik, String aciklama, String tarih, String saat,int lefTime,String ringTone) {
        this.imaj= R.drawable.baseline_alarm_24;
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.tarih = tarih;
        this.saat = saat;
        this.leftTime=lefTime;
        this.ringTone=ringTone;
    }

    public int getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(int leftTime) {
        this.leftTime = leftTime;
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

    public int getImaj() {
        return imaj;
    }

    public void setImaj(int imaj) {
        this.imaj = imaj;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getSaat() {
        return saat;
    }

    public String getRingTone() {
        return ringTone;
    }

    public void setRingTone(String ringTone) {
        this.ringTone = ringTone;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }
}
