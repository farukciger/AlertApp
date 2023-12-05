package com.example.alertapp;

public class Alerts {
    private int imaj;
    private String baslik;
    private String aciklama;
    private String tarih;
    private String saat;
    private long id;

    public Alerts(String baslik, String aciklama, String tarih, String saat) {
        this.imaj=R.drawable.baseline_alarm_24;
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.tarih = tarih;
        this.saat = saat;
    }
    @Override
    public String toString() {
        return
                "baslik='" + baslik + "\n" +
                "aciklama='" + aciklama + "\n"
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

    public void setSaat(String saat) {
        this.saat = saat;
    }
}
