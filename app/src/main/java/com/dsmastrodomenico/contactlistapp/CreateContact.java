package com.dsmastrodomenico.contactlistapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateContact extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String USERS_NODE = "users";
    private static final String TAG = "CreateContact";
    private EditText name, lastName, phone, cellphone;
    private Resources resources;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_contact);

        initialize();

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
            databaseReference.child(firebaseAuth.getUid()).child(contact.getID()).setValue(contact);
            Toast.makeText(this, R.string.done, Toast.LENGTH_LONG).show();
            name.setText("");
            lastName.setText("");
            phone.setText("");
            cellphone.setText("");
        }
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