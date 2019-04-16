package com.dsmastrodomenico.contactlistapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginAccess extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LoginAccess";
    private static final int SIGN_IN_GOOGLE_CODE = 1;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;

    private Button btnCreateAccount;
    private Button btnSignIn;
    private SignInButton btnSignInGoogle;

    private EditText edtEmail;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_access);
        btnCreateAccount = (Button)findViewById(R.id.btnCreateAccount);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignInGoogle = (SignInButton)findViewById(R.id.btnSignInGoogle);

        edtEmail =(EditText)findViewById(R.id.edtEmail);
        edtPassword =(EditText)findViewById(R.id.edtPassword);

        initialize();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });
        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signintent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signintent, SIGN_IN_GOOGLE_CODE);
            }
        });
    }

    private void initialize() {
        //Validación de Usuario mediante Firebase Authentification
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.w(TAG, "onAuthStateChanged - signed_in" + firebaseUser.getUid());
                    Log.w(TAG, "onAuthStateChanged - signed_in" + firebaseUser.getEmail());
                } else {
                    Log.w(TAG, "onAuthStateChanged - signed_out");
                }
            }
        };
        //Inicialización de Google Account.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("456917595223-k5qn5ku3nj04j263gijd9vm1iuaa8s3m.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    private void createAccount(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginAccess.this, R.string.create_account_success, Toast.LENGTH_SHORT).show();;
                    edtEmail.setText("");
                    edtPassword.setText("");
                }else{
                    Toast.makeText(LoginAccess.this, R.string.create_account_unsuccess, Toast.LENGTH_SHORT).show();;
                    edtEmail.setText("");
                    edtPassword.setText("");
                }
            }
        });
    }

    private void signIn(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginAccess.this, R.string.authentication_success, Toast.LENGTH_SHORT).show();;
                    Intent access = new Intent(LoginAccess.this, MainMenu.class);
                    startActivity(access);
                    edtEmail.setText("");
                    edtPassword.setText("");
                    finish();

                }else{
                    Toast.makeText(LoginAccess.this, R.string.authentication_unsuccess, Toast.LENGTH_SHORT).show();;
                    edtEmail.setText("");
                    edtPassword.setText("");
                }
            }
        });
    }

//    private void signInGoogleFirebase(GoogleSignInResult googleSignInResult){
//        if (googleSignInResult.isSuccess()){
//            AuthCredential authCredential =
//                    GoogleAuthProvider.getCredential(googleSignInResult.getSignInAccount().getIdToken(), null);
//            firebaseAuth.signInWithCredential(authCredential)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()){
//                        Toast.makeText(LoginAccess.this, R.string.authentication_success, Toast.LENGTH_SHORT).show();;
//                        Intent access = new Intent(LoginAccess.this, MainMenu.class);
//                        startActivity(access);
//                        finish();
//
//                    }else{
//                        Toast.makeText(LoginAccess.this, R.string.authentication_unsuccess, Toast.LENGTH_SHORT).show();;
//                    }
//                }
//            });
//        }else{
//            Toast.makeText(LoginAccess.this, "Google Sign In Unsuccess", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void signInGoogleFirebase(GoogleSignInAccount acct){
        AuthCredential authCredential =
                    GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if (task.isSuccessful()){
                            Toast.makeText(LoginAccess.this, R.string.authentication_success, Toast.LENGTH_SHORT).show();;
                            Intent access = new Intent(LoginAccess.this, MainMenu.class);
                            startActivity(access);
                            finish();

                        }else{
                            Toast.makeText(LoginAccess.this, R.string.authentication_unsuccess, Toast.LENGTH_SHORT).show();;
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        //FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_GOOGLE_CODE){
            //GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //signInGoogleFirebase(googleSignInResult);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                signInGoogleFirebase(account);
            } catch (ApiException e) {
                Toast.makeText(LoginAccess.this, "Google Sign In Unsuccess", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
