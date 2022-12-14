package com.example.abalone;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.abalone.Mains.ChooseLayout;

public class fakeBoard extends ChooseLayout {
    private int[][] idArray = new int[9][9];
    public ImageView[][] imageViews = new ImageView[9][9];

    public fakeBoard(int size, ImageView frame, int[][] placeAcc, ConstraintLayout layout2, Context context) {

        createBoard(size, frame, placeAcc, layout2, context);
    }

    private void createBoard(int size, ImageView frame, int[][] tempHex, ConstraintLayout layout2, Context context) {
        ConstraintLayout layout = layout2;
        ConstraintSet cs = new ConstraintSet();

        for (int iRow = 0; iRow < tempHex.length; iRow++) {
            for (int iCol = 0; iCol < tempHex[iRow].length; iCol++) {
                if (tempHex[iRow][iCol] != 4) {
                    ImageView imageView = new ImageView(context);

                    ConstraintLayout.LayoutParams lp =
                            new ConstraintLayout.LayoutParams(size, size);
                    int id = View.generateViewId();
                    if (iRow == 0) {
                        ImageView background = frame;
                        lp.topMargin = background.getLayoutParams().height / 62;
                    }
                    imageView.setTag(tempHex.length * iRow + iCol);
                    imageView.setId(id);
                    layout.addView(imageView, lp);
                    imageViews[iRow][iCol] = imageView;
                }
                else {
                    imageViews[iRow][iCol] = null;
                }
            }
        }
        cs.clone(layout);
        cs.setDimensionRatio(frame.getId(), tempHex.length + ":" + tempHex[0].length);

        int count = 0;
        for (int iRow = 0; iRow < tempHex.length; iRow++) {
            for (int iCol = 0; iCol < tempHex[iRow].length; iCol++) {
                if (tempHex[iRow][iCol] != 4) {
                    count++;
                    int id = idArray[iRow][iCol];
                    cs.setDimensionRatio(id, "1:1");
                    if (iRow == 0) {
                        cs.connect(id, ConstraintSet.TOP, frame.getId(), ConstraintSet.TOP);
                    } else {
                        cs.connect(id, ConstraintSet.TOP, idArray[iRow - 1][3], ConstraintSet.BOTTOM);
                    }

                }
            }
            int[] idArrLine = new int[count];
            count = 0;
            for (int i = 0; i < idArray[iRow].length; i++) {
                if (idArray[iRow][i] != 0) {
                    idArrLine[count] = idArray[iRow][i];
                    count++;}
            }
            count = 0;
            cs.createHorizontalChain(frame.getId(), ConstraintSet.LEFT, frame.getId(), ConstraintSet.RIGHT, idArrLine, null, ConstraintSet.CHAIN_PACKED);
        }
        cs.applyTo(layout);
    }

}
