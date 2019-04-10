package com.dsmastrodomenico.contactlistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListContact extends AppCompatActivity {
    private static final String USERS_NODE = "users";
    private static final String TAG = "ListContact";
    private DatabaseReference databaseReference;
    private ListView lvContacts;
    private TextView txtNoResults;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> contactNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);
        lvContacts = (ListView)findViewById(R.id.lvcontacts);
        txtNoResults = (TextView)findViewById(R.id.txtNoResults);
        contactNames = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactNames);
        lvContacts.setAdapter(arrayAdapter);
        //contacts = Data.get();
        //contactNames = new ArrayList<String>();
        txtNoResults.setVisibility(View.VISIBLE);
        lvContacts.setVisibility(View.INVISIBLE);
        databaseReference.child("USERS_NODE").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactNames.clear();
                if(dataSnapshot.exists()){
                    lvContacts.setVisibility(View.VISIBLE);
                    txtNoResults.setVisibility(View.INVISIBLE);

                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Contact contact = snapshot.getValue(Contact.class);
                        Log.w(TAG, "Contact Name: " + contact.getName() + " " + contact.getLastName());
                        contactNames.add(contact.getName() + " " + contact.getLastName());
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent contactDetail = new Intent(ListContact.this, ContactDetail.class);
                contactDetail.putExtra("position", position);
                startActivity(contactDetail);
            }
        });
    }

}
