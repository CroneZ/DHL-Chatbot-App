package com.example.imdc;

public class DeliveryJob {
    private double lat, logi;
    private String address,eta;

    DeliveryJob(double lat,double logi,String address){
        this.lat = lat;
        this.logi = logi;
        this.address = address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public void setEta(String eta) {
//        this.eta = eta;
//    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLogi(double logi) {
        this.logi = logi;
    }

    public double getLat() {
        return lat;
    }

    public double getLogi() {
        return logi;
    }

    public String getAddress() {
        return address;
    }

//    public String getEta() {
//        return eta;
//    }
}
