package com.example.abalone.Mains;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.abalone.Control.Layouts;
import com.example.abalone.R;

import java.util.ArrayList;
import java.util.Random;

public class ChooseLayout extends AppCompatActivity {

    private int bluePiece, redPiece, empty;
    private ArrayList<int[][]> layouts = Layouts.allLayouts();
    private final int boardsAmount = layouts.size();
    private ImageView[][][] boards = new ImageView[boardsAmount][9][9];
    private int[][][] idArray = new int[boardsAmount][9][9];
    private ImageView[] realBoards = new ImageView[boardsAmount];

    static Random rnd = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_layout);
        bluePiece = R.drawable.marble_blue;
        redPiece = R.drawable.marble_red;
        empty = R.drawable.empty_space;
        onBegin();
        onChange();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void onClick2(View view) {
        int[] possible = {R.drawable.marble_blue, R.drawable.marble_red,
                R.drawable.checkers_brown, R.drawable.checkers_blue,
                R.drawable.checkers_white, R.drawable.marble_gray,
                R.drawable.marble_white, R.drawable.checkers_gray,
                R.drawable.cyan_space, R.drawable.red_space};

        int num = rnd.nextInt(possible.length);
        bluePiece = possible[num];
        int n;
        do {
            n = rnd.nextInt(possible.length);
        } while(n == num);
        redPiece = possible[n];
        onChange();
    }

    public void onClick4(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("bluePiece", bluePiece);
        intent.putExtra("redPiece", redPiece);

        int layoutNum;
        switch (view.getId()) {
            case R.id.board1:
                layoutNum = 1;
                break;
            case R.id.board2:
                layoutNum = 2;
                break;
            case R.id.board3:
                layoutNum = 3;
                break;
            case R.id.board4:
                layoutNum = 4;
                break;
            case R.id.board5:
                layoutNum = 5;
                break;
            case R.id.board6:
                layoutNum = 6;
                break;
            case R.id.board7:
                layoutNum = 7;
                break;
            case R.id.board8:
                layoutNum = 8;
                break;
            default:
                layoutNum = rnd.nextInt(boardsAmount) + 1;
                break;
        }
        intent.putExtra("layoutNum", layoutNum);
        startActivity(intent);
        //finish();
    }


    private void onBegin() {

        realBoards[0] = findViewById(R.id.board1);
        realBoards[1] = findViewById(R.id.board2);
        realBoards[2] = findViewById(R.id.board3);
        realBoards[3] = findViewById(R.id.board4);
        realBoards[4] = findViewById(R.id.board5);
        realBoards[5] = findViewById(R.id.board6);
        realBoards[6] = findViewById(R.id.board7);
        realBoards[7] = findViewById(R.id.board8);

        int size = (int)((realBoards[0].getLayoutParams().width * 0.97)/9);
        int i = 0;
        for (int[][] placeAcc : layouts) {
            createBoard(size, realBoards[i], placeAcc, i);

            // set onClick
            realBoards[i].setOnClickListener(this::onClick4);
            i++;
        }
        findViewById(R.id.shuffle).setOnClickListener(this::onClick4);
    }

    private void onChange() {
        int i = 0;
        for (int[][] hex : layouts) {
            update(hex, boards[i]);
            i++;
        }

    }

    private void update(int[][] hex, ImageView[][] imageViews) {
        int rows = 9, cols = 9;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int num = hex[i][j];
                if (num != 4) {
                    if (num == 1) {
                        imageViews[i][j].setImageResource(bluePiece);
                    }
                    else if (num == -1) {
                        imageViews[i][j].setImageResource(redPiece);
                    }
                }
            }
        }
    }

    private void createBoard(int size, ImageView frame, int[][] tempHex, int index) {

        ConstraintLayout layout = findViewById(R.id.cons);

        layout.requestLayout();
        ConstraintSet cs = new ConstraintSet();

        for (int iRow = 0; iRow < tempHex.length; iRow++) {
            for (int iCol = 0; iCol < tempHex[iRow].length; iCol++) {
                if (tempHex[iRow][iCol] != 4) {
                    ImageView imageView = new ImageView(this);

                        ConstraintLayout.LayoutParams lp =
                            new ConstraintLayout.LayoutParams(size, size);
                    int id = View.generateViewId();
                    if (iRow == 0) {
                        lp.topMargin = frame.getLayoutParams().height / 60;
                    }
                    idArray[index][iRow][iCol] = id;
                    imageView.setId(id);
                    layout.addView(imageView, lp);
                    boards[index][iRow][iCol] = imageView;
                }
                else {
                    boards[index][iRow][iCol] = null;
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
                    int id = idArray[index][iRow][iCol];
                    cs.setDimensionRatio(id, "1:1");
                    if (iRow == 0) {
                        cs.connect(id, ConstraintSet.TOP, frame.getId(), ConstraintSet.TOP);
                    } else {
                        cs.connect(id, ConstraintSet.TOP, idArray[index][iRow - 1][3], ConstraintSet.BOTTOM);
                    }

                }
            }
            int[] idArrLine = new int[count];
            count = 0;
            for (int i = 0; i < idArray[index][iRow].length; i++) {
                if (idArray[index][iRow][i] != 0) {
                    idArrLine[count] = idArray[index][iRow][i];
                    count++;}
            }
            count = 0;
            cs.createHorizontalChain(frame.getId(), ConstraintSet.LEFT, frame.getId(), ConstraintSet.RIGHT, idArrLine, null, ConstraintSet.CHAIN_PACKED);
        }
        frame.requestLayout();
        cs.applyTo(layout);

   }

}