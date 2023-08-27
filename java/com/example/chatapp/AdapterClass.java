package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder> {
   private Context context;
   private ArrayList<ModelClass> arrayList1;

    public AdapterClass(Context context, ArrayList<ModelClass> arrayList1) {
        this.context = context;
        this.arrayList1 = arrayList1;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_layout,parent,false);
        ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelClass model=arrayList1.get(position);
        holder.textView.setText(model.getUsername());
        if (model.getImageURL().equals("default")){
            holder.imageView.setImageResource(R.drawable.ic_baseline_person_24);
        }else {
            Glide.with(context).load(model.getImageURL()).into(holder.imageView);
        }
//        if (model.getStatus().equals("online")){
//            holder.img_On.setVisibility(View.VISIBLE);
//            holder.img_Off.setVisibility(View.GONE);
//        }

//        if (isChat){
//            if (model.getStatus().equals("online")){
//                holder.img_On.setVisibility(View.VISIBLE);
//                holder.img_Off.setVisibility(View.GONE);
//
//            }else {
//                holder.img_On.setVisibility(View.GONE);
//                holder.img_Off.setVisibility(View.VISIBLE);
//            }
//        }
//        else {
//            holder.img_On.setVisibility(View.GONE);
//            holder.img_Off.setVisibility(View.VISIBLE);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,MessagesActivity.class);
                intent.putExtra("ID",model.getId());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       public CircleImageView imageView;
       public TextView textView;
       private CircleImageView img_On;
       private CircleImageView img_Off;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.profileS);
            textView=itemView.findViewById(R.id.NamesS);
            img_On=itemView.findViewById(R.id.img_On);
            img_Off=imageView.findViewById(R.id.img_off);
        }
    }
}
