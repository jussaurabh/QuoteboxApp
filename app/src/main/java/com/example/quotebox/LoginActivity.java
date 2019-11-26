package com.example.quotebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private final static int GOOGLE_SIGN = 2;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    ProgressBar loginProgressBar,loginWithGoogleProgressBar;
    GoogleSignInClient mGoogleSignInClient;
    CollectionNames collectionNames;
    SharedPreferencesConfig preferencesConfig;


    TextView loginEmailErrMsg, loginPasswordErrMsg, loginErrorMsg;
    EditText loginEmailField, loginPasswordField;
    Button loginSubmitBtn, loginWithGoogleBtn, loginGoToSignupBtn;
    CheckBox loginShowPwdCheckBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firestore = FirebaseFirestore.getInstance();
        collectionNames = new CollectionNames();
        preferencesConfig = new SharedPreferencesConfig(this);

        loginEmailField = findViewById(R.id.loginEmailField);
        loginPasswordField = findViewById(R.id.loginPasswordField);
        loginSubmitBtn = findViewById(R.id.loginSubmitBtn);
        loginWithGoogleBtn = findViewById(R.id.loginWithGoogleBtn);
        loginGoToSignupBtn = findViewById(R.id.loginGoToSignupBtn);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        loginWithGoogleProgressBar = findViewById(R.id.loginWithGoogleProgressBar);
        loginShowPwdCheckBox = findViewById(R.id.loginShowPwdCheckBox);
        loginEmailErrMsg = findViewById(R.id.loginEmailErrMsg);
        loginPasswordErrMsg = findViewById(R.id.loginPasswordErrMsg);
        loginErrorMsg = findViewById(R.id.loginErrorMsg);

        loginGoToSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        loginEmailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) { loginEmailField.setBackgroundResource(R.drawable.light_rounded_solid_bg); }
                else { loginEmailField.setBackgroundResource(R.color.transparent);}
            }
        });


        loginEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!loginEmailField.getText().toString().trim().isEmpty()) {
                    loginEmailErrMsg.setVisibility(View.GONE);
                    loginEmailField.setBackgroundResource(R.drawable.whitesmoke_bg);
                    loginErrorMsg.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        loginPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!loginPasswordField.getText().toString().trim().isEmpty()) {
                    loginPasswordErrMsg.setVisibility(View.GONE);
                    loginPasswordField.setBackgroundResource(R.drawable.whitesmoke_bg);
                    loginErrorMsg.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loginPasswordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) { loginPasswordField.setBackgroundResource(R.drawable.light_rounded_solid_bg); }
                else { loginPasswordField.setBackgroundResource(R.color.transparent);}
            }
        });



        loginShowPwdCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) { loginPasswordField.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); }
                else { loginPasswordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); }
            }
        });

    }

    public void loginWithEmailAndPassword(final String email, String password) {
        loginProgressBar.setVisibility(View.VISIBLE);
        loginSubmitBtn.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("LOGIN: ", "loginWithEmailAndPassword SUCCESS");

                    finish();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                    loginProgressBar.setVisibility(View.GONE);
                    loginSubmitBtn.setVisibility(View.VISIBLE);
                }
                else {
                    Log.d("LOGIN: ", "ERROR in loginWithEmailAndPassword", task.getException());
                    Toast.makeText(LoginActivity.this, "Error in login", Toast.LENGTH_LONG).show();
                    loginErrorMsg.setVisibility(View.VISIBLE);
                    loginProgressBar.setVisibility(View.GONE);
                    loginSubmitBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    // set this function on "onClick" attribute in loginWithGoogleBtn
    public void loginWithGoogle(View v) {
        loginWithGoogleBtn.setVisibility(View.GONE);
        loginWithGoogleProgressBar.setVisibility(View.VISIBLE);
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }


    // set this function on "onClick" attribute for loginSubmitBtn
    public void loginValidation(View v) {
        String email = loginEmailField.getText().toString().trim();
        String password = loginPasswordField.getText().toString().trim();

        if (email.isEmpty() && password.isEmpty()) {
            showErrorMsg(loginEmailField, loginEmailErrMsg, "Email cannot be empty");
            showErrorMsg(loginPasswordField, loginPasswordErrMsg, "Password cannot be empty");
        }
        else if (email.isEmpty()) {
            showErrorMsg(loginEmailField, loginEmailErrMsg, "Email cannot be empty");
        }
        else if (password.isEmpty()) {
            showErrorMsg(loginPasswordField, loginPasswordErrMsg, "Password cannot be empty");
        }
        else {
            loginWithEmailAndPassword(email, password);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }



    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            final String gLoginUserId = task.getResult().getUser().getUid();

                            firestore.collection(CollectionNames.USERS)
                                    .whereEqualTo(Users.EMAIL, account.getEmail())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.getResult() != null && task.getResult().size() > 0) {
                                                finish();
                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                                                loginWithGoogleBtn.setVisibility(View.VISIBLE);
                                                loginWithGoogleProgressBar.setVisibility(View.GONE);
                                            }
                                            else {
                                                HashMap<String, List<String>> userPostColl = new HashMap<>();
                                                userPostColl.put(Users.DEFAULT_POST_COLLECTION, new ArrayList<String>());
                                                Users users = new Users(
                                                        account.getEmail().split("@")[0],
                                                        account.getEmail(),
                                                        "",
                                                        account.getPhotoUrl().toString(),
                                                        null,
                                                        null,
                                                        new ArrayList<String>(),
                                                        0,
                                                        0,
                                                        0,
                                                        new ArrayList<String>(),
                                                        new ArrayList<String>(),
                                                        userPostColl,
                                                        false
                                                );

                                                firestore.collection(CollectionNames.USERS)
                                                        .document(gLoginUserId)
                                                        .set(users)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(LoginActivity.this, "LOGIN SUCCESS ", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                                loginWithGoogleBtn.setVisibility(View.VISIBLE);
                                                                loginWithGoogleProgressBar.setVisibility(View.GONE);
                                                            }
                                                        });
                                            }
                                        }
                                    });

                        }
                        else {
                            Log.d("LOGIN_TAG", "LOGIN ERROR" + task.getException());
                            Toast.makeText(LoginActivity.this, "Signin ERRRORRRR", Toast.LENGTH_SHORT).show();
                            loginWithGoogleBtn.setVisibility(View.VISIBLE);
                            loginWithGoogleProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


    // show error message if any fields are empty
    private void showErrorMsg(EditText e, TextView t, String msg) {
        e.setBackgroundResource(R.drawable.red_rounded_border);
        t.setText(msg);
        t.setVisibility(View.VISIBLE);
    }
}
