package com.example.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class chatFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ModelClass> modelClasses=new ArrayList<>();

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ArrayList<String> user_list=new ArrayList<>();

    public chatFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_list.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Chats chats=snapshot1.getValue(Chats.class);
                    if (chats.getSender().equals(firebaseUser.getUid())){
                        user_list.add(chats.getReceiver());
                    }
                    if (chats.getReceiver().equals(firebaseUser.getUid())) {
                        user_list.add(chats.getSender());
                    }
                }
                readChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });
        return view;
    }

    private void readChats() {
        reference=FirebaseDatabase.getInstance().getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelClasses.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    ModelClass modelClass=snapshot1.getValue(ModelClass.class);
                    //display one user
                    for (String id:user_list){
                        if (modelClass.getId().equals(id)){
                            if (modelClasses.size() != 0){
                                for (ModelClass modelClass1:modelClasses){
                                    if (!modelClass.getId().equals(modelClass1.getId())){
                                        modelClasses.add(modelClass);
                                    }
                                }
                            }else {
                                modelClasses.add(modelClass);
                            }
                        }
                    }
                }
                AdapterClass adapterClass=new AdapterClass(getContext(),modelClasses);
                recyclerView.setAdapter(adapterClass);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}