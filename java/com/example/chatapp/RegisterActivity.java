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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText edName,edEmail,edPassword;
    Button btn_Register;

    FirebaseAuth mAuth;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Toolbar toolbar =findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register New Accounts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        edName=findViewById(R.id.userN);
        edEmail=findViewById(R.id.userE);
        edPassword=findViewById(R.id.userP);
        btn_Register=findViewById(R.id.btnR);
        mAuth=FirebaseAuth.getInstance();
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String T_name=edName.getText().toString();
                String T_email=edEmail.getText().toString();
                String T_password=edPassword.getText().toString();
                if (TextUtils.isEmpty(T_name)||TextUtils.isEmpty(T_email)||TextUtils.isEmpty(T_password)){
                    Toast.makeText(RegisterActivity.this, "please enter Data", Toast.LENGTH_SHORT).show();
                }else if (T_password.length()<6){
                    Toast.makeText(RegisterActivity.this, "password large then 5 character", Toast.LENGTH_SHORT).show();
                }else {
                    register(T_name,T_email,T_password);
                }
            }
        });

    }
    private void register(String name,String email,String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser= mAuth.getCurrentUser();
                            String userid= firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("user")
                                    .child(userid);
                            HashMap<String ,String> hashMap= new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",name);
                            hashMap.put("imageURL","default");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent= new Intent(RegisterActivity.this,MainActivity2.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}