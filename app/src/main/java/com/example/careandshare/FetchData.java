package com.example.careandshare;

public class FetchData {

    String id;
    String name;
    Long age;


    public FetchData(String id,Long age,String name){
    this.id=id;
    this.age=age;
    this.name=name;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}
