package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessagesActivity extends AppCompatActivity {
    MessageAdapter messageAdapter;
    ArrayList<Chats> arrayList=new ArrayList<>();
    RecyclerView recyclerView;
    CircleImageView profile;
    TextView name;
    FirebaseUser user;
    DatabaseReference reference;
    Intent intent;
    ImageButton send;
    EditText message;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
          recyclerView=findViewById(R.id.recycler);
          recyclerView.setHasFixedSize(true);
          recyclerView.setLayoutManager(new LinearLayoutManager(this));
          Toolbar toolbar=findViewById(R.id.tool);
          setSupportActionBar(toolbar);
          getSupportActionBar().setTitle("");
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent= new Intent(MessagesActivity.this,MainActivity2.class);
                 startActivity(intent);
                 finish();
             }
         });
         send=findViewById(R.id.btn_send);
         message=findViewById(R.id.sendM);

         profile=findViewById(R.id.profile);
         name=findViewById(R.id.userNames);
         intent=getIntent();
         String userid=intent.getStringExtra("ID") ;
         user= FirebaseAuth.getInstance().getCurrentUser();
         reference= FirebaseDatabase.getInstance().getReference("user").child(userid);

         send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String Messages=message.getText().toString();
                 if (!Messages.equals("")){
                     SendMessage(user.getUid(),userid,Messages);
                 }else {
                     Toast.makeText(MessagesActivity.this, "Message Empty", Toast.LENGTH_SHORT).show();
                 }
                 message.setText("");
             }
         });

         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 ModelClass modelClass=snapshot.getValue(ModelClass.class);
                 name.setText(modelClass.getUsername());
                 if (modelClass.getImageURL().equals("default")){
                     profile.setImageResource(R.drawable.ic_baseline_person_24);
                 }else {
                     Glide.with(MessagesActivity.this).load(modelClass.getImageURL()).into(profile);
                 }
                  readMessages(user.getUid(),userid,modelClass.getImageURL());
             }
             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }
    private  void SendMessage(String sender,String receiver,String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        reference.child("chats").push().setValue(hashMap);
    }
    private void readMessages(String id,String userid,String imageURL){
        reference=FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Chats chats=snapshot1.getValue(Chats.class);
                    if (chats.getReceiver().equals(id)  && chats.getSender().equals(userid) ||
                            chats.getReceiver().equals(userid) && chats.getSender().equals(id)){
                        arrayList.add(chats);
                    }
                    messageAdapter=new MessageAdapter(MessagesActivity.this,arrayList,imageURL);
                    recyclerView.setAdapter(messageAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}