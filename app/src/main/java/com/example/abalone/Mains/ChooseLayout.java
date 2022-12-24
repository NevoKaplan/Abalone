package com.example.abalone.Mains;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.abalone.Control.Layouts;
import com.example.abalone.R;
import com.example.abalone.Recycler.CustomAdapter;
import com.example.abalone.Recycler.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Random;

public class ChooseLayout extends AppCompatActivity {

    private int bluePiece, redPiece;
    private final ArrayList<int[][]> layouts = Layouts.allLayouts();
    private final int boardsAmount = layouts.size();
    private final ImageView[][][] boards = new ImageView[boardsAmount][9][9];
    private final int[][][] idArray = new int[boardsAmount][9][9];
    private final ImageView[] realBoards = new ImageView[boardsAmount];

    private final int positionOffset = -106;

    private CustomAdapter adapter;

    static Random rnd = new Random();

    private final int[] possible = {R.drawable.marble_blue, R.drawable.marble_red,
            R.drawable.marble_gray, R.drawable.marble_white,
            R.drawable.checkers_brown, R.drawable.checkers_blue,
            R.drawable.checkers_white, R.drawable.checkers_gray,
            R.drawable.cyan_space, R.drawable.red_space};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_layout);
        bluePiece = R.drawable.marble_blue;
        redPiece = R.drawable.marble_red;

        ArrayList<Drawable> lst = arrToList();
        adapter = new CustomAdapter(lst);

        onBegin();
        onChange();
        arrowAnimations();
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
                    R.drawable.marble_gray, R.drawable.marble_white,
                    R.drawable.checkers_brown, R.drawable.checkers_blue,
                    R.drawable.checkers_white, R.drawable.checkers_gray,
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

        RecyclerView recyclerViewTop = findViewById(R.id.recyclerViewTop);
        Thread setUpTopRecycler = new Thread(new firstThread(recyclerViewTop, true));

        RecyclerView recyclerViewBottom = findViewById(R.id.recyclerViewBottom);
        Thread setUpBottomRecycler = new Thread(new firstThread(recyclerViewBottom, false));
        //setRecycler(recyclerViewBottom, false);
        //setRecycler(recyclerViewTop, true);
        setUpTopRecycler.start();
        setUpBottomRecycler.start();

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
        findViewById(R.id.topRightArrow).setOnClickListener(this::onArrowClick);
        findViewById(R.id.topLeftArrow).setOnClickListener(this::onArrowClick);
        findViewById(R.id.bottomRightArrow).setOnClickListener(this::onArrowClick);
        findViewById(R.id.bottomLeftArrow).setOnClickListener(this::onArrowClick);
    }

    private class firstThread extends ChooseLayout implements Runnable {

        RecyclerView recycler;
        boolean top;

        public firstThread(RecyclerView recycler, boolean top) {
            this.recycler = recycler;
            this.top = top;
        }

        @Override
        public void run() {
            recycler.setAdapter(adapter);

            SpacesItemDecoration dividerItemDecoration = new SpacesItemDecoration(16);

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    recyclerView.post(() -> selectMiddleItem(recyclerView, top));
                    onChange();
                }
            });

            LinearSnapHelper snapHelper = new LinearSnapHelper() {
                @Override
                public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                    View centerView = findSnapView(layoutManager);
                    if (centerView == null)
                        return RecyclerView.NO_POSITION;

                    int position = layoutManager.getPosition(centerView);
                    int targetPosition = -1;
                    if (layoutManager.canScrollHorizontally()) {
                        if (velocityX < 0) {
                            targetPosition = position - 1;
                        } else {
                            targetPosition = position + 1;
                        }
                    }

                    if (layoutManager.canScrollVertically()) {
                        if (velocityY < 0) {
                            targetPosition = position - 1;
                        } else {
                            targetPosition = position + 1;
                        }
                    }

                    final int firstItem = 0;
                    final int lastItem = layoutManager.getItemCount() - 1;
                    targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                    return targetPosition;
                }
            };
            snapHelper.attachToRecyclerView(recycler);

            if (top)
                layoutManager.scrollToPositionWithOffset(Integer.MAX_VALUE / 2 + 6, positionOffset);
            else
                layoutManager.scrollToPositionWithOffset(Integer.MAX_VALUE / 2 + 7, positionOffset);
            recycler.setLayoutManager(layoutManager);

            recycler.addItemDecoration(dividerItemDecoration);
        }
    }

    public ArrayList<Drawable> arrToList() {

        ArrayList<Drawable> list = new ArrayList<>();
        for (int stoneImg : possible)
            list.add(ContextCompat.getDrawable(this, stoneImg));

        return list;
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
                if (num != 4 && imageViews[i][j] != null) {
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

    private void selectMiddleItem(RecyclerView recycler, boolean top) {
        LinearLayoutManager layoutManager = (LinearLayoutManager)recycler.getLayoutManager();

        int findMiddleLeft = layoutManager.findFirstCompletelyVisibleItemPosition();
        int findMiddleRight = layoutManager.findLastCompletelyVisibleItemPosition();
        if (findMiddleLeft == findMiddleRight) {
            prechange(top, findMiddleRight );
            return;
        }

        int firstVisibleIndex = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleIndex =  layoutManager.findLastVisibleItemPosition();

        for (int i = firstVisibleIndex; i < lastVisibleIndex; i++) {
            RecyclerView.ViewHolder vh = recycler.findViewHolderForLayoutPosition(i);

            int[] location = new int[2];
            vh.itemView.getLocationOnScreen(location);
            int x = location[0];
            int halfWidth = (int)(vh.itemView.getWidth() * .5);
            int rightSide = x + halfWidth;
            int leftSide = x - halfWidth;
            int screenWidth = getScreenWidth();
            boolean isInMiddle = screenWidth * .5 >= leftSide && screenWidth * .5 < rightSide;

            if (isInMiddle) {
                // "i" is your middle index and implement selecting it as you want
                // optionsAdapter.selectItemAtIndex(i);
                prechange(top, i);
                return;
            }
        }
    }

        public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    @SuppressLint("NonConstantResourceId")
    public void onArrowClick(View view) {
        switch (view.getId()) {
            case R.id.topRightArrow:
                moveTheSelection(findViewById(R.id.recyclerViewTop), 1, true);
                break;
            case R.id.topLeftArrow:
                moveTheSelection(findViewById(R.id.recyclerViewTop), -1, true);
                break;
            case R.id.bottomRightArrow:
                moveTheSelection(findViewById(R.id.recyclerViewBottom), 1, false);
                break;
            case R.id.bottomLeftArrow:
                moveTheSelection(findViewById(R.id.recyclerViewBottom), -1, false);
                break;
            default:
                break;
        }
    }

    public void moveTheSelection(RecyclerView recycler, int right, boolean top) {
        LinearLayoutManager layoutManager = (LinearLayoutManager)(recycler.getLayoutManager());
        int pos = layoutManager.findFirstCompletelyVisibleItemPosition(), dx = right;

        if ((top && possible[(pos + dx) % possible.length] == redPiece) || (!top && possible[(pos + dx) % possible.length] == bluePiece))
            dx *= 2;

        int posOffset = 160;

        System.out.println("pos: " + pos + ", posWithIndex: " + (pos + dx));
        prechange(top, pos + dx);
        layoutManager.scrollToPositionWithOffset(pos + dx, posOffset);
    }

    public void prechange(boolean top, int index) {
        if (top) {
            if (possible[index % possible.length] != redPiece)
                bluePiece = possible[index % possible.length];
        }
        else {
            if (possible[index % possible.length] != bluePiece)
                redPiece = possible[index % possible.length];
        }
    }

    // arrow animation
    private void arrowAnimations() {
        Animation right = AnimationUtils.loadAnimation(this, R.anim.move_horizontal_right);
        Animation left = AnimationUtils.loadAnimation(this, R.anim.move_horizontal_left);

        findViewById(R.id.topRightArrow).setAnimation(right);
        findViewById(R.id.bottomRightArrow).setAnimation(right);
        findViewById(R.id.topLeftArrow).setAnimation(left);
        findViewById(R.id.bottomLeftArrow).setAnimation(left);
    }




    private class onChange implements Runnable {

        public onChange() {}

        public void run() {
            int i = 0;
            for (int[][] hex : layouts) {
                update(hex, boards[i]);
                i++;
            }
        }

        /*private void update(int[][] hex, ImageView[][] imageViews) {
            int rows = 9, cols = 9;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int num = hex[i][j];
                    if (num != 4 && imageViews[i][j] != null) {
                        if (num == 1) {
                            imageViews[i][j].setImageResource(bluePiece);
                        }
                        else if (num == -1) {
                            imageViews[i][j].setImageResource(redPiece);
                        }
                    }
                }
            }
        }*/
    }
}