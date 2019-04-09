package com.dsmastrodomenico.contactlistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactDetail extends AppCompatActivity {
    private Intent intent;
    private ArrayList<Contact> contacts;
    private TextView name, phone, cellphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);

        intent = getIntent();
        contacts = Data.get();

        int position = intent.getIntExtra("position", 0);
        name = (TextView)findViewById(R.id.txtcontactname);
        phone = (TextView)findViewById(R.id.txtphone);
        cellphone = (TextView)findViewById(R.id.txtcellphone);

        loadData(contacts.get(position));
    }

    private void loadData(Contact contact) {
        name.setText(contact.getName());
        phone.setText(contact.getPhone());
        cellphone.setText(contact.getCellphone());
    }
}

