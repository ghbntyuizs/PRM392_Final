package com.example.project_prm392.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.project_prm392.R;
import com.example.project_prm392.entities.Banks;


import java.util.ArrayList;


public class BankAdapter extends RecyclerView.Adapter<BankAdapter.viewholder> {
    ArrayList<Banks> banks;
    Context context;

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_bank, parent, false);
        return new viewholder(view);
    }

    public BankAdapter(Context context, ArrayList<Banks> banks) {
        this.context = context;
        this.banks = banks;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.tv_name.setText(banks.get(position).getName());
        Glide.with(context)
                .load(banks.get(position).getLogo())
                .override(50, 50) // Set the width and height to match the ImageView dimensions
                .transform(new CenterCrop(), new RoundedCorners(5))
                .into(holder.img_logo);

    }

    @Override
    public int getItemCount() {
        return banks.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_id, tv_bin;
        ImageView img_logo;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.bank_name);
            img_logo = itemView.findViewById(R.id.bank_logo);

        }
    }
}