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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotebox.controllers.AuthController;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.interfaces.OnSignupSuccessListener;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SignupActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    CollectionNames collectionNames;
    Users users;
    SharedPreferencesConfig preferencesConfig;
    AuthController authController;

    EditText signupUsernameField, signupEmailField, signupPasswordField;
    Button signupSubmitBtn;
    ProgressBar signupProgressBar, signupUsernameProgressBar;
    CheckBox signupShowPwdCheckBox;
    TextView signupEmailErrMsg, signupUsernameErrMsg, signupPasswordErrMsg;
    ImageView signupUsernameCheckImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        collectionNames = new CollectionNames();
        users = new Users();
        preferencesConfig = new SharedPreferencesConfig(this);
        authController = new AuthController();

        signupUsernameField = findViewById(R.id.signupUsernameField);
        signupEmailField = findViewById(R.id.signupEmailField);
        signupPasswordField = findViewById(R.id.signupPasswordField);
        signupSubmitBtn = findViewById(R.id.signupSubmitBtn);
        signupProgressBar = findViewById(R.id.signupProgressBar);
        signupShowPwdCheckBox = findViewById(R.id.signupShowPwdCheckBox);
        signupEmailErrMsg = findViewById(R.id.signupEmailErrMsg);
        signupUsernameErrMsg = findViewById(R.id.signupUsernameErrMsg);
        signupPasswordErrMsg = findViewById(R.id.signupPasswordErrMsg);
        signupUsernameProgressBar = findViewById(R.id.signupUsernameProgressBar);
        signupUsernameCheckImageView = findViewById(R.id.signupUsernameCheckImageView);




        signupEmailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    signupEmailField.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                }
                else {
                    signupEmailField.setBackgroundResource(R.color.transparent);
                }
            }
        });

        signupUsernameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    signupUsernameField.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                    signupUsernameErrMsg.setVisibility(View.GONE);
                    signupUsernameCheckImageView.setVisibility(View.GONE);
                }

                else {
                    signupUsernameField.setBackgroundResource(R.color.transparent);
                    signupUsernameProgressBar.setVisibility(View.VISIBLE);

                    firestore.collection(collectionNames.getUserCollection())
                            .whereEqualTo(Users.USERNAME, signupUsernameField.getText().toString().trim())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    List<DocumentChange> docChanges = task.getResult().getDocumentChanges();
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        Log.d("SIGNUP_ACTIVITY ", Integer.toString(docChanges.size()));

                                        if (docChanges.size() > 0) {
                                            signupUsernameCheckImageView.setVisibility(View.GONE);
                                            signupUsernameProgressBar.setVisibility(View.GONE);
                                            signupUsernameField.setBackgroundResource(R.drawable.red_rounded_border);
                                            signupUsernameErrMsg.setText("Username already exists");
                                            signupUsernameErrMsg.setVisibility(View.VISIBLE);

                                        }
                                        else {
                                            signupUsernameProgressBar.setVisibility(View.GONE);
                                            signupUsernameCheckImageView.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    else {
                                        Log.d("SIGNUP_ACTIVITY ", Integer.toString(docChanges.size()));
                                    }
                                }
                            });
                }

            }
        });

        signupPasswordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    signupPasswordField.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                }
                else {
                    signupPasswordField.setBackgroundResource(R.color.transparent);
                }
            }
        });

        signupEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!signupEmailField.getText().toString().isEmpty()) {
                    signupEmailField.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                    signupEmailErrMsg.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        signupPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!signupPasswordField.getText().toString().isEmpty()) {
                    signupPasswordField.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                    signupPasswordErrMsg.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        signupUsernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("SIGNUP_ACTIVITY ", "BEFORE usernam text change");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!signupUsernameField.getText().toString().isEmpty()) {
                    signupUsernameField.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                    signupUsernameErrMsg.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("SIGNUP_ACTIVITY ", "after usernam text change");
            }
        });


        signupShowPwdCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    signupPasswordField.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else {
                    signupPasswordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

    }


    public void createNewUser(String username, String email, String password) {

        signupSubmitBtn.setVisibility(View.GONE);
        signupProgressBar.setVisibility(View.VISIBLE);

        signupUsernameField.setFocusable(false);
        signupEmailField.setFocusable(false);
        signupPasswordField.setFocusable(false);

        if (signupUsernameErrMsg.getVisibility() == View.VISIBLE ||
                signupEmailErrMsg.getVisibility() == View.VISIBLE ||
                signupPasswordErrMsg.getVisibility() == View.VISIBLE) {
            Toast.makeText(SignupActivity.this, "Error in SIGNUP", Toast.LENGTH_SHORT).show();

            signupSubmitBtn.setClickable(true);
            signupSubmitBtn.setBackgroundResource(R.drawable.white_bg);
            signupSubmitBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));

            signupSubmitBtn.setVisibility(View.VISIBLE);
            signupProgressBar.setVisibility(View.GONE);
            return;
        }

        authController.signup(this, username, email, password)
                .addOnSignupSuccessListener(new OnSignupSuccessListener() {
                    @Override
                    public void onSignupSuccess(String uid, Users user) {
                        if (user != null) {
                            signupSubmitBtn.setVisibility(View.VISIBLE);
                            signupProgressBar.setVisibility(View.GONE);

                            finish();
                            startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                        }
                        else {
                            Toast.makeText(SignupActivity.this, "Error in SIgnup", Toast.LENGTH_LONG).show();
                            signupSubmitBtn.setVisibility(View.VISIBLE);
                            signupProgressBar.setVisibility(View.GONE);
                            signupEmailField.setBackgroundResource(R.drawable.red_rounded_border);
                            signupEmailErrMsg.setVisibility(View.VISIBLE);
                            signupEmailErrMsg.setText("Email already exists or invalid");

                            signupUsernameField.setFocusable(true);
                            signupEmailField.setFocusable(true);
                            signupPasswordField.setFocusable(true);
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


    public void goToLoginActivity(View view) {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
    }

    public void signupSubmit(View view) {
        String username = signupUsernameField.getText().toString().trim();
        String email = signupEmailField.getText().toString().trim();
        String password = signupPasswordField.getText().toString().trim();

        if (signupEmailErrMsg.getVisibility() == View.VISIBLE ||
            signupPasswordErrMsg.getVisibility() == View.VISIBLE ||
            signupUsernameErrMsg.getVisibility() == View.VISIBLE) {
            return;
        }

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            if (username.isEmpty()) {
                showErrorMsg(signupUsernameField, signupUsernameErrMsg, "Username cannot be empty");
            }
            if (password.isEmpty()) {
                showErrorMsg(signupPasswordField, signupPasswordErrMsg, "Password cannot be empty");
            }
            if (email.isEmpty()) {
                showErrorMsg(signupEmailField, signupEmailErrMsg, "Email cannot be empty");
            }
        } else {
            createNewUser(username, email, password);
        }
    }
}
