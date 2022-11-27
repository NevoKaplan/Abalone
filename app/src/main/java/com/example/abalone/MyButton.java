package com.example.abalone;

import android.content.Context;

import androidx.annotation.NonNull;

public class MyButton extends androidx.appcompat.widget.AppCompatImageView {

    public MyButton(@NonNull Context context) {super(context);}

    public MyButton(Context context, int image) {
        super(context);
        this.setImageResource(image);
    }

}
