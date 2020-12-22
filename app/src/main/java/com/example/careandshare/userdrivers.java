package com.example.careandshare;

public class userdrivers {
    public String destination;
    public String fare;
    public String origin;
    public String seeatsavailability;
    public String uid;


    public userdrivers(String destination,String fare,String origin,String seatsavailablility,String uid) {

        this.destination=destination;
        this.fare=fare;
        this.origin=origin;
        this.seeatsavailability=seatsavailablility;
        this.uid=uid;


    }


    public userdrivers(){


    }


    public String getDestination() {
        return destination;
    }

    public String getFare() {
        return fare;
    }

    public String getOrigin() {
        return origin;
    }

    public String getSeeatsavailability() {
        return seeatsavailability;
    }

    public String getUid() {
        return uid;
    }
}

