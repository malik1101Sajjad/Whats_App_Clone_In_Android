package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btnLogin,btnRegister;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser !=null){
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser !=null){
//            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
//            startActivity(intent);
//            finish();
//        }


        btnLogin=findViewById(R.id.btn_logins);
        btnRegister=findViewById(R.id.btn_register);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}