package com.example.chatapp;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class profileFragment extends Fragment {
    CircleImageView profileImage;
    TextView userName;
    DatabaseReference reference;
    FirebaseUser firebaseUser;


    StorageReference storageReference;
    private static final int IMAGE_REQUEST=-1;
    private Uri imageURL;
    private StorageTask uploadTask;


    public profileFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view=inflater.inflate(R.layout.fragment_profile, container, false);
        profileImage=view.findViewById(R.id.profile);
        userName=view.findViewById(R.id.userNP);

        storageReference= FirebaseStorage.getInstance().getReference("upload");



        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelClass modelClass=snapshot.getValue(ModelClass.class);
                userName.setText(modelClass.getUsername());
                if (modelClass.getImageURL().equals("default")){
                    profileImage.setImageResource(R.drawable.ic_baseline_person_24);
                }else {
                    Glide.with(getContext()).load(modelClass.getImageURL()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
          return view;
    }

    private void openImage() {
        Intent intent= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        packImage.launch(intent);


    }
    private  String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void loadImage(){
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();
        if (imageURL !=null){
            final StorageReference storageReference1=storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageURL));
            uploadTask=storageReference1.putFile(imageURL);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference1.getDownloadUrl();



                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri download =task.getResult();
                        String mUri =download.toString();
                        reference=FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());
                        HashMap<String ,Object> hashMap=new HashMap<>();
                        hashMap.put("imageURL",mUri);
                        // reference.child(mAuth.getCurrentUser().getUid()).updateChildren(hashMap);
                        reference.updateChildren(hashMap);
                        pd.dismiss();
                    }else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "you are fail"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }
    ActivityResultLauncher<Intent> packImage=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode()==RESULT_OK && result.getResultCode()==IMAGE_REQUEST&& result.getData()!=null){
                imageURL=result.getData().getData();
                if (uploadTask !=null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "upload in progress", Toast.LENGTH_SHORT).show();
                    loadImage();
            }else {
                loadImage();
                }
            }
        }
    });
}