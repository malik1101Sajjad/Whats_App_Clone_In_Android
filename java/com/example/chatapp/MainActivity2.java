package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity2 extends AppCompatActivity {
    CircleImageView profile;
     private TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private String[] title=new String[]{"CHATS","USER","PROFILE"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar =findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");



        profile=findViewById(R.id.profile);
        username=findViewById(R.id.userNames);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelClass modelClass=snapshot.getValue(ModelClass.class);
                username.setText(modelClass.getUsername());
                if (modelClass.getImageURL().equals("default")){
                    profile.setImageResource(R.drawable.ic_baseline_person_24);
                }else {
                    Glide.with(MainActivity2.this).load(modelClass.getImageURL()).into(profile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        TabLayout tb=findViewById(R.id.tab_layout);
        ViewPager2 viewPager2=findViewById(R.id.vp);
        ViewPagerMes adapter= new ViewPagerMes(this);
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tb,viewPager2,((tab, position) -> tab.setText(title[position]))).attach();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.logeOuts){
              FirebaseAuth.getInstance().signOut();
              Intent intent= new Intent(MainActivity2.this,MainActivity.class);
              //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
              finish();
        }
        return true;
    }
    private void Status(String status){
        reference=FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Status("online");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Status("offline");
    }
}