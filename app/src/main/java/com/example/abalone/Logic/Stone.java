package com.example.abalone.Logic;

import java.util.ArrayList;
import java.util.Collections;

public class Stone {

    private int mainNum;
    public int row, col;
    public boolean isSelected;
    private int ogNum;

    public Stone(int mainNum, int row, int col) {
        this.mainNum = mainNum;
        this.row = row;
        this.col = col;
        this.isSelected = false;
        this.ogNum = mainNum;
    }

    public Stone(Stone stone) {
        this.mainNum = stone.mainNum;
        this.row = stone.row;
        this.col = stone.col;
        this.isSelected = false;
        this.ogNum = this.mainNum;
    }

    public int getMainNum() {return this.mainNum;}

    public void setMainNum(int num) {this.mainNum = num;}

    public int getOgNum() {return this.ogNum;}

    public void setOgNum(int num) {this.ogNum = num;}

    public String toString() {
        return this.mainNum + " [" + this.row + "]" + "[" + this.col + "]";
    }

    public boolean getSelected() {return this.isSelected;}

    public void setSelected(boolean bool) {this.isSelected = bool;}

    public boolean isLarger(Stone stone) { // returns true if parameter is larger than instance
        if (this.row < stone.row)
            return false;
        if (this.row == stone.row) {
            if (this.col < stone.col)
                return false;
            return true;
        }
        return true;
    }

    public boolean equals(Stone stone) {
        return stone.col == this.col && stone.row == this.row;
    }

    public static void sort(ArrayList<Stone> stones) { // NOT WORKING!!
    int size = stones.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                Stone current = stones.get(j);
                Stone next = stones.get(j+1);
                if (current.equals(next)) {
                    stones.remove(j+1);
                    size--;
                }
                else if (current.isLarger(next)) {
                    Stone stone = stones.get(j);
                    stones.set(j, stones.get(j + 1));
                    stones.set(j+1, stone);
                }
            }
        }
    }

    public static void reverseList(ArrayList<Stone> stones) {
        Collections.reverse(stones);
    }

    public int[] getPosition() {
        return new int[]{this.row, this.col};
    }

}