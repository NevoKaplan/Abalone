package com.example.abalone.Recycler;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.abalone.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;

    public ViewHolder(View itemView) {
        super(itemView);
        this.img = itemView.findViewById(R.id.imgPhoto);
    }

    public void setImgPicture(Drawable drawable) {
        this.img.setImageDrawable(drawable);
    }
}
