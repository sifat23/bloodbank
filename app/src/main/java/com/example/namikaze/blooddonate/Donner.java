package com.example.namikaze.blooddonate;

public class Donner {

    String id;
    public String imageURL;
    public String name;
    public String address;
    public String number;
    public String sex;
    public String birthDate;
    public String bloodGroup;

    public Donner(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Donner (String id, String imageURL, String name, String address, String number, String sex, String birthDate, String bloodGroup){
        this.id = id;
        this.imageURL = imageURL;
        this.name = name;
        this.address = address;
        this.number = number;
        this.sex = sex;
        this.birthDate = birthDate;
        this.bloodGroup = bloodGroup;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
