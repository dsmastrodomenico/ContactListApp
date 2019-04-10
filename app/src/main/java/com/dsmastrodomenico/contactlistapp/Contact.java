package com.dsmastrodomenico.contactlistapp;

public class Contact {
    private String ID;
    private String Name;
    private String LastName;
    private String Phone;
    private String Cellphone;

    public Contact() { }

    public Contact(String ID, String name, String lastName, String phone, String cellphone) {
        this.ID = ID;
        Name = name;
        LastName = lastName;
        Phone = phone;
        Cellphone = cellphone;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCellphone() {
        return Cellphone;
    }

    public void setCellphone(String cellphone) {
        Cellphone = cellphone;
    }

    //public void saveContact(){
    //    Data.save(this);
    //}
}

