package com.dsmastrodomenico.contactlistapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateContact extends AppCompatActivity {
    private static final String USERS_NODE = "users";
    private EditText name, lastName, phone, cellphone;
    private Resources resources;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_contact);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        name = (EditText)findViewById(R.id.name);
        lastName = (EditText)findViewById(R.id.lastName);
        phone = (EditText)findViewById(R.id.phone);
        cellphone = (EditText)findViewById(R.id.cellphone);
        resources = this.getResources();
    }

    public void save(View view){
        if (name.getText().toString().isEmpty() || lastName.getText().toString().isEmpty()
        || phone.getText().toString().isEmpty() || cellphone.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.empty_cell, Toast.LENGTH_LONG).show();
        }else{
            Contact contact = new Contact(databaseReference.push().getKey(),
                    name.getText().toString(), lastName.getText().toString(),
                    phone.getText().toString(), cellphone.getText().toString());
            databaseReference.child("USERS_NODE").child(contact.getID()).setValue(contact);
            Toast.makeText(this, R.string.done, Toast.LENGTH_LONG).show();
            name.setText("");
            lastName.setText("");
            phone.setText("");
            cellphone.setText("");
        }
    }


}