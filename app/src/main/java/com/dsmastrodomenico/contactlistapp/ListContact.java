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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListContact extends AppCompatActivity {
    private static final String USERS_NODE = "users";
    private static final String TAG = "ListContact";
    private ListView listView;
    private TextView txtNoResults;
    private ArrayAdapter arrayAdapter;
    private List<String> contactNames;
    private DatabaseReference databaseReference;
    //private ArrayList<Contact> contacts;
    //private ArrayList<String> contactNames;
    //private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, contactNames);
        listView.setAdapter(arrayAdapter);
        listView = (ListView) findViewById(R.id.lvcontacts);
        txtNoResults = (TextView)findViewById(R.id.txtNoResults);
        //contacts = Data.get();
        //contactNames = new ArrayList<String>();

        txtNoResults.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        databaseReference.child(USERS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contactNames.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Contact contact = snapshot.getValue(Contact.class);
                        Log.w(TAG, "Contact Name: " + contact.getName() + " " + contact.getLastName());
                        contactNames.add(contact.getName());
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //if (contacts.size() > 0) {
        //    listView.setVisibility(View.VISIBLE);
        //    txtNoResults.setVisibility(View.INVISIBLE);

        //    for (int i = 0; i < contacts.size(); i++) {
        //        contactNames.add(contacts.get(i).getName() + " " + contacts.get(i).getLastName());
        //    }
        //}

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactNames);
        //listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent contactDetail = new Intent(ListContact.this, ContactDetail.class);
                contactDetail.putExtra("position", position);
                startActivity(contactDetail);
            }
        });
    }
}
