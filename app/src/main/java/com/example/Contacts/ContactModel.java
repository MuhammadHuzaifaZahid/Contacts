package com.example.Contacts;

class ContactModel {

    int Contact_ID;
    String Name;
    String Phone_No;
    String Address;
    String Image;

    public ContactModel(String name, String phone_No, String address, String image) {
        Name = name;
        Phone_No = phone_No;
        Address = address;
        Image = image;
    }

    public ContactModel() {
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone_No() {
        return Phone_No;
    }

    public void setPhone_No(String phone_No) {
        Phone_No = phone_No;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getContact_ID() {
        return Contact_ID;
    }

    public void setContact_ID(int contact_ID) {
        Contact_ID = contact_ID;
    }
}
