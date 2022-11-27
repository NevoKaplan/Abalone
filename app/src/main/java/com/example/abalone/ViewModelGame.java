package com.example.abalone;

import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.example.abalone.Logic.Stone;

public class ViewModelGame {
    public MutableLiveData<Stone[][]> mBoard;

    private final Model model;

    public ViewModelGame() {
        this.model = new Model();
        this.mBoard = model.getBoard();
    }

    public void onTileClick(View view) {
        MyButton square = ((MyButton)view);
        //model.doAction(square.getStone());
        print();
    }

    public void print() {
        Stone[][] hex = model.board.hex;
        StringBuilder space = new StringBuilder("          ");
        for (int i = 0; i < hex.length; i++) {
            for (int j = 0; j < hex[i].length; j++) {
                if (hex[i][j].getMainNum() != 4) {
                    if (hex[i][j].getSelected())
                        System.out.print(space + " " + 3 + "  ");
                    else if (hex[i][j].getMainNum() == -1)
                        System.out.print(space + " " + 2 + "  ");
                    else
                        System.out.print(space + " " +hex[i][j].getMainNum() + "  ");
                    space = new StringBuilder();
                }
                else if (hex[i][j].getMainNum() == 4)
                    space.append("  ");
            }
            if (i >= hex.length/2)
                space.append("  ");
            System.out.println();
        }
    }

    public void boardChange(int num) {
        this.model.setBoard(num);
    }
}
