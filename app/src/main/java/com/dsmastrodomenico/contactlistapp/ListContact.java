package com.dsmastrodomenico.contactlistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListContact extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Contact> contacts;
    private ArrayList<String> contactNames;
    private TextView txtNoResults;
    //private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        listView = (ListView) findViewById(R.id.lvcontacts);
        txtNoResults = (TextView)findViewById(R.id.txtNoResults);
        contacts = Data.get();
        contactNames = new ArrayList<String>();

        txtNoResults.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);

        if (contacts.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            txtNoResults.setVisibility(View.INVISIBLE);

            for (int i = 0; i < contacts.size(); i++) {
                contactNames.add(contacts.get(i).getName() + " " + contacts.get(i).getLastName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactNames);
        listView.setAdapter(adapter);

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
