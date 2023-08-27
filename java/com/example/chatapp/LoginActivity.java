package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText edEmail,edPassword;
    Button btn_Login;
    FirebaseAuth mAut;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       Toolbar toolbar=findViewById(R.id.toolbars);
       setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("Login Accounts");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        edEmail=findViewById(R.id.userE);
        edPassword=findViewById(R.id.userP);
        btn_Login=findViewById(R.id.btnL);

        mAut=FirebaseAuth.getInstance();


        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t_email=edEmail.getText().toString();
                String t_password=edPassword.getText().toString();
                if (TextUtils.isEmpty(t_email)||TextUtils.isEmpty(t_password)){
                    Toast.makeText(LoginActivity.this, "All file are fill", Toast.LENGTH_SHORT).show();
                }else{
                    mAut.signInWithEmailAndPassword(t_email,t_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intent= new Intent(LoginActivity.this,MainActivity2.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}