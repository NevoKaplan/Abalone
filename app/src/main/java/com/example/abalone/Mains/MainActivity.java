package com.example.abalone.Mains;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.abalone.*;
import com.example.abalone.Control.Control;
import com.example.abalone.Logic.AI;
import com.example.abalone.Logic.Board;
import com.example.abalone.Logic.Stone;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout layout;
    private final int[][] idArray = new int[9][9];
    private Board board;
    private Control control;

    private Drawable bluePiece, redPiece, empty_space;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        control = Control.getInstance(intent.getIntExtra("layoutNum", 1));
        bluePiece = ContextCompat.getDrawable(this, intent.getIntExtra("bluePiece", R.drawable.marble_blue));
        redPiece = ContextCompat.getDrawable(this, intent.getIntExtra("redPiece", R.drawable.marble_red));
        empty_space = ContextCompat.getDrawable(this, R.drawable.empty_space);
        board = control.getBoard();
        onConfigurationChanged(this.getResources().getConfiguration());
        buttons();
    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }*/


    private void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        control.onCheckedChanged(isChecked);
    }


    private void createBoard(int size, Board board) {
        Stone[][] tempHex = board.hex;
        layout = findViewById(R.id.layout);
        ConstraintSet cs = new ConstraintSet();

        for (int iRow = 0; iRow < tempHex.length; iRow++) {
            for (int iCol = 0; iCol < tempHex[iRow].length; iCol++) {
                if (tempHex[iRow][iCol].getMainNum() != 4) {
                    ImageView imageView = new ImageView(this);

                    ConstraintLayout.LayoutParams lp =
                            new ConstraintLayout.LayoutParams(size, size);
                    int id = View.generateViewId();
                    if (iRow == 0) {
                        ImageView background = findViewById(R.id.gridFrame);
                        lp.topMargin = background.getLayoutParams().height / 62;
                    }
                    idArray[iRow][iCol] = id;
                    imageView.setTag(tempHex.length * iRow + iCol);
                    imageView.setId(id);
                    imageView.setOnClickListener(this::gettingStone);
                    if (tempHex[iRow][iCol].getMainNum() == 1) {
                        imageView.setBackground(bluePiece);
                    }
                    else if (tempHex[iRow][iCol].getMainNum() == -1) {
                        imageView.setBackground(redPiece);
                    }
                    else {
                        imageView.setBackground(empty_space);
                    }
                    layout.addView(imageView, lp);
                }
            }
        }
        cs.clone(layout);
        cs.setDimensionRatio(R.id.gridFrame, tempHex.length + ":" + tempHex[0].length);

        int count = 0;
        for (int iRow = 0; iRow < tempHex.length; iRow++) {
            for (int iCol = 0; iCol < tempHex[iRow].length; iCol++) {
                if (tempHex[iRow][iCol].getMainNum() != 4) {
                    count++;
                    int id = idArray[iRow][iCol];
                    cs.setDimensionRatio(id, "1:1");
                    if (iRow == 0) {
                        cs.connect(id, ConstraintSet.TOP, R.id.gridFrame, ConstraintSet.TOP);
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
            cs.createHorizontalChain(R.id.gridFrame, ConstraintSet.LEFT, R.id.gridFrame, ConstraintSet.RIGHT, idArrLine, null, ConstraintSet.CHAIN_PACKED);
        }
        cs.applyTo(layout);
    }

    // this should be in control...
    /*public void gettingStone(View view) {
        outerloop:
        for (int i = 0; i < idArray.length; i++) {
            for (int j = 0; j < idArray.length; j++) {
                if (view.getId() == idArray[i][j]) {
                    if (!(board.selectedSize == 0 && board.hex[i][j].getMainNum() != board.player)) {
                        if (control.setCurrentStone(board.hex[i][j]))
                            updateBoard();
                        else
                            preUpdateBoard();
                    }
                    break outerloop;
                }
            }
        }
    }*/

    public void gettingStone(View view) {
        int row = (int)view.getTag()/board.hex.length;
        int col = (int)view.getTag()%board.hex.length;
        if (!(board.selectedSize == 0 && board.hex[row][col].getMainNum() != board.getPlayer())) {
            if (control.setCurrentStone(board.hex[row][col]))
                updateBoard();
            else
                preUpdateBoard();
        }
    }

    private void updateBoard() {
        for (int i = 0; i < board.hex.length; i++) {
            for (int j = 0; j < board.hex.length; j++) {
                int num = board.hex[i][j].getMainNum();
                if (num != 4) {
                    ImageView imageView = (ImageView) layout.getViewById(idArray[i][j]);
                   // if (imageView != null) {
                        if (num == 1) {
                            imageView.setBackground(bluePiece);
                        } else if (num == -1) {
                            imageView.setBackground(redPiece);
                        } else {
                            imageView.setBackground(empty_space);

                         }
                    //}
                }
            }
        }
    }

    private void preUpdateBoard() {
        updateBoard();

        Drawable currentPiece;

        boolean bool_move2 = board.move2 != null, bool_targets = board.targets != null, bool_selected = board.selected != null;
        if (board.getPlayer() == 1) {
            currentPiece = bluePiece;
        }
        else {
            currentPiece = redPiece;
        }
        if (bool_move2) {
            LayerDrawable layerDrawable = (LayerDrawable) (ContextCompat.getDrawable(this, R.drawable.can_selected_layer));
            assert layerDrawable != null;
            layerDrawable.setDrawableByLayerId(R.id.backStoneCan, currentPiece);
            for (Stone stone : board.move2) {
                View view = layout.getViewById(idArray[stone.row][stone.col]);
                view.setBackground(layerDrawable);
            }
        }
        if (bool_targets) {
            LayerDrawable layerDrawable = (LayerDrawable) (ContextCompat.getDrawable(this, R.drawable.layer_to_move));
            assert layerDrawable != null;
            layerDrawable.setDrawableByLayerId(R.id.backStoneMove, currentPiece);
            for (Stone stone : board.targets) {
                View view = layout.getViewById(idArray[stone.row][stone.col]);
                view.setBackground(layerDrawable);
            }
        }
        if (bool_selected) {
            LayerDrawable layerDrawable = (LayerDrawable) (ContextCompat.getDrawable(this, R.drawable.selected_layer));
            assert layerDrawable != null;
            layerDrawable.setDrawableByLayerId(R.id.backStone, currentPiece);
            for (Stone stone : board.selected) {
                View view = layout.getViewById(idArray[stone.row][stone.col]);
                view.setBackground(layerDrawable);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {

        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;
        int screenHeight = getScreenHeight();
        int screenWidth = getScreenWidth();
        setContentView(R.layout.activity_main);
        ImageView background = findViewById(R.id.gridFrame);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            background.getLayoutParams().height = screenWidth - (int)(screenWidth * 0.02);
            background.getLayoutParams().width = background.getLayoutParams().height;
        }

        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            background.getLayoutParams().height = screenHeight - (int)(screenHeight * 0.1);
            background.getLayoutParams().width = background.getLayoutParams().height;
        }

        background.requestLayout();
        createBoard((int)((background.getLayoutParams().width * 0.97)/9), board);
        buttons();
    }

    private void buttons() {
        findViewById(R.id.bCrown).setOnClickListener(this::onClick);
        findViewById(R.id.bGerman).setOnClickListener(this::onClick);
        findViewById(R.id.bNormal).setOnClickListener(this::onClick);
        findViewById(R.id.bSnakes).setOnClickListener(this::onClick);
        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(this::onCheckedChanged);
        switch1.setChecked(AI.hasInstance());
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    // right now useless but incase needed...
    private void resetBoard() {
        for (int i = 0; i < idArray.length; i++) {
            for (int j = 0; j < idArray[i].length; j++) {
                if (idArray[i][j] != 0) {
                    View namebar = layout.findViewById(idArray[i][j]);
                    ((ViewGroup) namebar.getParent()).removeView(namebar);
                }
            }
        }
    }

    public void onClick(View view) {
        int n = -1;
        switch(view.getId()) {
            case R.id.bNormal:
                n = 1;
                break;
            case R.id.bGerman:
                n = 2;
                break;
            case R.id.bSnakes:
                n = 3;
                break;
            case R.id.bCrown:
                n = 4;
                break;
            default:
                break;
        }
        if (n != -1) {
            board = control.setUpBoard(n);
            updateBoard();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Control.destroy();
        finish();
    }
}