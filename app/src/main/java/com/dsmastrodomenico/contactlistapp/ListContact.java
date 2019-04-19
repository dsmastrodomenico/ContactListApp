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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListContact extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String USERS_NODE = "users";
    private static final String TAG = "ListContact";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

    private ListView lvContacts;
    private TextView txtNoResults;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> contactNames;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);
        lvContacts = (ListView)findViewById(R.id.lvcontacts);
        txtNoResults = (TextView)findViewById(R.id.txtNoResults);
        contactNames = new ArrayList<>();

        initialize();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactNames);
        lvContacts.setAdapter(arrayAdapter);
        //contacts = Data.get();
        //contactNames = new ArrayList<String>();
        txtNoResults.setVisibility(View.VISIBLE);
        lvContacts.setVisibility(View.INVISIBLE);
        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
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
    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.w(TAG, "onAuthStateChanged - signed_in " + firebaseUser.getUid());
                    Log.w(TAG, "onAuthStateChanged - signed_in " + firebaseUser.getEmail());
                } else {
                    Log.w(TAG, "onAuthStateChanged - signed_out");
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient =  new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
