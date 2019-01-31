package com.example.namikaze.blooddonate;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DonnerList extends RecyclerView.Adapter<DonnerList.ImageViewHolder> {
    private Context mContext;
    private List<Donner> donnerList;

    public DonnerList(Activity context, List<Donner> donners) {
        mContext = context;
        donnerList = donners;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_layout, parent, false);

        RecyclerView.ViewHolder viewHolder = new ImageViewHolder(v);
        return (ImageViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Donner donner = donnerList.get(position);

        holder.tvName.setText(donner.getName());
        holder.tvAddress.setText(donner.getAddress());
        holder.tvPhoneNumber.setText(donner.getNumber());
        holder.tvSex.setText(donner.getSex());
        holder.tvBloodGroup.setText(donner.getBloodGroup());

        Picasso.get().load(donner.getImageURL()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.ivUser);

        final String b =  holder.tvPhoneNumber.getText().toString();
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:" + b;
                i.setData(Uri.parse(p));
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return donnerList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvAddress;
        TextView tvPhoneNumber;
        TextView tvSex;
        TextView tvBloodGroup;
        ImageView ivUser;

        Button button;

        public ImageViewHolder(View itemView) {
            super(itemView);

            ivUser = (ImageView) itemView.findViewById(R.id.list_image);
            tvName = (TextView) itemView.findViewById(R.id.list_name);
            tvAddress = (TextView) itemView.findViewById(R.id.list_address);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.list_number);
            tvSex = (TextView) itemView.findViewById(R.id.list_sex);
            tvBloodGroup = (TextView) itemView.findViewById(R.id.list_blood_group);

            button = (Button)itemView.findViewById(R.id.list_call);
        }
    }
}
