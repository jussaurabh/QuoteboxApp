package com.example.quotebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotebox.controllers.InsertData;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class SignupActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    CollectionNames collectionNames;
    InsertData insertData;
    Users users;

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
        insertData = new InsertData();
        users = new Users();

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
                            .whereEqualTo(users.USERNAME, signupUsernameField.getText().toString().trim())
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


    public void createNewUser(final String username, final String email, final String password) {

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

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("SIGNUP : ", "createUserWithEmailAndPassword SUCCESS USERID : " + task.getResult().getUser().getUid());



                            // setting the default value at user register/signup
                            Users users = new Users(
                                    username,
                                    email,
                                    password,
                                    null,
                                    null,
                                    null,
                                    0,
                                    0
                            );

                            firestore.collection(collectionNames.getUserCollection())
                                    .document(task.getResult().getUser().getUid())
                                    .set(users.getUsersCreds())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            signupSubmitBtn.setVisibility(View.VISIBLE);
                                            signupProgressBar.setVisibility(View.GONE);
                                            resetSignupPage();
                                        }
                                    });
                        }
                        else {
                            Log.d("SIGNUP: ", "ERROR in createUserWithEmailAndPassword", task.getException());
                            Toast.makeText(SignupActivity.this, "Error in SIgnup", Toast.LENGTH_LONG).show();
                            signupSubmitBtn.setVisibility(View.VISIBLE);
                            signupProgressBar.setVisibility(View.GONE);
                            signupEmailField.setBackgroundResource(R.drawable.red_rounded_border);
                            signupEmailErrMsg.setVisibility(View.VISIBLE);
                            signupEmailErrMsg.setText("Email already exists");
                            return;
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

    public void resetSignupPage() {
        signupUsernameField.setText("");
        signupEmailField.setText("");
        signupPasswordField.setText("");

        signupUsernameCheckImageView.setVisibility(View.GONE);
        signupShowPwdCheckBox.setChecked(false);

        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
