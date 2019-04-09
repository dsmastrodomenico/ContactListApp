package com.dsmastrodomenico.contactlistapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateContact extends AppCompatActivity {
    private EditText name, lastName, phone, cellphone;
    private Resources resources;
    private ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_contact);

        name = (EditText)findViewById(R.id.name);
        lastName = (EditText)findViewById(R.id.lastName);
        phone = (EditText)findViewById(R.id.phone);
        cellphone = (EditText)findViewById(R.id.cellphone);

        resources = this.getResources();
        contacts = Data.get();
    }

    public void save(View view){
        String id, nameV, lastNameV, phoneV, cellphoneV;
        id = (contacts.size() + 1) + "";
        nameV = name.getText().toString();
        lastNameV = lastName.getText().toString();
        phoneV = phone.getText().toString();
        cellphoneV = cellphone.getText().toString();

        Contact c = new Contact(id, nameV, lastNameV, phoneV, cellphoneV);
        c.saveContact();
        Toast.makeText(this, R.string.done, Toast.LENGTH_LONG).show();
    }
}