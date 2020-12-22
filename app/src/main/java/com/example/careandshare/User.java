package com.example.careandshare;


class User {


    public String address;
    public String availability;
    public String city;
    public String price;
    public String roomdescription;
    public String roomtype;
    public String uid;

public String key;
    public User(String address,String availability,String city,String price,String roomdescription,String roomtype,String uid) {

      this.address=address;
        this.availability=availability;
        this.city=city;
        this.uid=uid;
        this.price=price;
        this.roomdescription=roomdescription;
        this.roomtype=roomtype;

    }
    public User(){


    }


    public String getAvailability() {
        return availability;
    }

    public String getCity() {
        return city;
    }

    public String getPrice() {
        return price;
    }

    public String getRoomdescription() {
        return roomdescription;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public String getUid() {
        return uid;
    }

    public String getaddress(){
        return address;
    }







}
