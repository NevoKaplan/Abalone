package com.example.abalone.Recycler;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abalone.R;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    ArrayList<Drawable> lst;
    Context context;
    View stoneView;
    ViewHolder viewHolder;


    public CustomAdapter(ArrayList<Drawable> arrayList) {
        this.lst = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        stoneView = inflater.inflate(R.layout.card_adapter, parent, false);
        viewHolder = new ViewHolder(stoneView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int wrappedPosition = position % lst.size();
        final Drawable imageView = lst.get(wrappedPosition);
        viewHolder.setImgPicture(imageView);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
