package com.example.careandshare;

public class locationhelper {

    private double Longitude;
    private double Latitude;


    public locationhelper(double longitude,double latitude){
        Longitude=longitude;
        Latitude=latitude;


    }


    public double getLatitude11() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }


    public double getLongitude12() {
        return Longitude;
    }


    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
