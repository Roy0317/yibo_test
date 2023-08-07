package com.example.anuo.immodule.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.utils.GlideUtils;
import com.example.anuo.immodule.view.CircleImageView;

import java.util.List;

public class PersonPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    private Context context;

    private List<String> mData;

    public PersonPhotoAdapter(Context context, List<String> data) {
        this.context = context;
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        return mData.size()+1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final int position = i;
        ViewHolder holder = (ViewHolder) viewHolder;

        if(position < getItemCount()-1){
            GlideUtils.loadHeaderPic(context, mData.get(i), holder.photoImg);
        }else {
            GlideUtils.loadHeaderPic(context, R.mipmap.icon_add, holder.photoImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onClick(position);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_person_photo, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView photoImg;
        public ViewHolder (View view){
            super(view);
            photoImg = view.findViewById(R.id.item_person_photo_img);

        }
    }

    public interface PhotoItemClick{
        void onClick(int position);
    }

    private PhotoItemClick itemClick = null;

    public void setPhotoItemClick(PhotoItemClick roomItemClick){
        this.itemClick = roomItemClick;
    }

}
