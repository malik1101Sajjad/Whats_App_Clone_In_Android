package com.example.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userFragment extends Fragment {

    RecyclerView recyclerView;
    AdapterClass adapterClass;
    ArrayList<ModelClass> arrayList=new ArrayList<>();

    public userFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_user, container, false);
      recyclerView=view.findViewById(R.id.recycler_view);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      recyclerView.setHasFixedSize(true);
      readUser();
      return view;
    }
    private void readUser() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("user");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    ModelClass modelClass=snapshot1.getValue(ModelClass.class);
                    assert modelClass !=null;
                    assert firebaseUser !=null;
                    if (!modelClass.getId().equals(firebaseUser.getUid())){
                        arrayList.add(modelClass);
                    }
                }
                adapterClass=new AdapterClass(getContext(),arrayList);
                recyclerView.setAdapter(adapterClass);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}