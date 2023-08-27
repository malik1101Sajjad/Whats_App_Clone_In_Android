package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    FirebaseUser firebaseUser;

    Context context;
    ArrayList<Chats> arrayList;
    private String imageURL;
    public MessageAdapter(Context context, ArrayList<Chats> arrayList, String image) {
        this.context = context;
        this.arrayList = arrayList;
        this.imageURL = image;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_itams_right, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_items_left, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chats chats=arrayList.get(position);
        holder.show_M.setText(chats.getMessage());
        if (imageURL.equals("default")){
            holder.imageView.setImageResource(R.drawable.ic_baseline_person_24);
        }else {
            Glide.with(context).load(imageURL).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView show_M;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.profile_images_s);
            show_M=itemView.findViewById(R.id.Message_s);
        }
    }
    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (arrayList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
