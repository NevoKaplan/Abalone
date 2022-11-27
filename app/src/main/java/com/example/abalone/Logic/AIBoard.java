package com.example.abalone.Logic;

import java.util.ArrayList;
import java.util.HashMap;

public class AIBoard extends Board {

    final static int MAX_DEPTH = 3;
    int depth;
    private double val;
    private int preDeadRed, preDeadBlue;
    private ArrayList<Stone>[] bestSelected = new ArrayList[2];
    public static int run = 0;

    public boolean sideMoveable;

    private static HashMap<Integer, AIBoard> alreadyBoards = new HashMap<>();
    private static int alreadyBoardsCount;

    private static final double[][] fromMiddle =
            {{5.656854249492381, 5.0, 4.47213595499958, 4.123105625617661, 4.0, 0.0, 0.0, 0.0, 0.0},
            {5.0, 4.242640687119285, 3.605551275463989, 3.1622776601683795, 3.0, 3.1622776601683795, 0.0, 0.0, 0.0},
            {4.47213595499958, 3.605551275463989, 2.8284271247461903, 2.23606797749979, 2.0, 2.23606797749979, 2.8284271247461903, 0.0, 0.0},
            {4.123105625617661, 3.1622776601683795, 2.23606797749979, 1.4142135623730951, 1.0, 1.4142135623730951, 2.23606797749979, 3.1622776601683795, 0.0},
            {4.0, 3.0, 2.0, 1.0, 0.0, 1.0, 2.0, 3.0, 4.0},
            {0.0, 3.1622776601683795, 2.23606797749979, 1.4142135623730951, 1.0, 1.4142135623730951, 2.23606797749979, 3.1622776601683795, 4.123105625617661},
            {0.0, 0.0, 2.8284271247461903, 2.23606797749979, 2.0, 2.23606797749979, 2.8284271247461903, 3.605551275463989, 4.47213595499958},
            {0.0, 0.0, 0.0, 3.1622776601683795, 3.0, 3.1622776601683795, 3.605551275463989, 4.242640687119285, 5.0},
            {0.0, 0.0, 0.0, 0.0, 4.0, 4.123105625617661, 4.47213595499958, 5.0, 5.656854249492381}};

    public AIBoard(Board board) {
        super(false, -1);
        updateBoard(board.hex);
        this.player = AI.getInstance(player * -1).getAiPlayer();
        this.depth = MAX_DEPTH;
        for (int i = 0; i < bestSelected.length; i++)
            bestSelected[i] = new ArrayList<>();
        alreadyBoardsCount = 0;
        alreadyBoards.clear();
        preDeadBlue = deadBlue;
        preDeadRed = deadRed;
    }

    public AIBoard(AIBoard board) {
        super(false, -1);
        run++;
        updateBoard(board.hex);
        this.player = board.player*-1;
        this.depth = board.depth-1;
        this.val = 0;
        for (int i = 0; i < bestSelected.length; i++)
            bestSelected[i] = new ArrayList<>();
        preDeadBlue = deadBlue;
        preDeadRed = deadRed;
    }

    private void updateBoard(Stone[][] stones) {
        for (int i = 0; i < this.hex.length; i++) {
            for (int j = 0; j < this.hex[i].length; j++) {
                this.hex[i][j] = new Stone(stones[i][j]);
            }
        }
    }

    public int getDepth() {
        return this.depth;
    }
    public double getVal() {
        return this.val;
    }
    public void setVal(double val) {
        this.val = val;
    }
    public ArrayList<Stone>[] getBestSelected() {
        return this.bestSelected;
    }
    public void setBestSelected(ArrayList<Stone> selected, ArrayList<Stone> toBe) {
        for (Stone stone : selected) {
            this.bestSelected[0].add(stone);
        }
        for (Stone stone : toBe) {
            this.bestSelected[1].add(stone);
        }
    }

    public void setBestSelected() {
        for (Stone stone : this.selected) {
            this.bestSelected[0].add(stone);
        }
        for (Stone stone : this.toBe) {
            this.bestSelected[1].add(stone);
        }
    }

    public ArrayList<AIBoard> getNextBoards() {
        ArrayList<AIBoard> nextBoards = this.IterateNextBoards();
        //nextBoards.get(0).doMove();
        return nextBoards;
    }


    // need to check for every move
    private ArrayList<AIBoard> IterateNextBoards() {
        ArrayList<AIBoard> nextBoards = new ArrayList<>();
        int count = 0;                               // count for how many troops already checked - to check less
        if (this.player == 1)
            count += this.deadBlue;
        else
            count += this.deadRed;
        for (int i = 0; i < this.hex.length && count <= 14; i++){
            for (int j = 0; j < this.hex[i].length; j++) {
                if (this.hex[i][j].getMainNum() == this.player) {
                    count++;
                    changeSelected(this.hex[i][j]);

                    ArrayList<Stone> targetStones = availableTargets();
                    nextBoards.addAll(iterate(targetStones));

                    this.cleanSelected();
                    changeSelected(this.hex[i][j]);
                    ArrayList<Stone> availableStones = availableStones2();
                    this.cleanSelected();

                    if (availableStones != null) {
                        for (Stone stone : availableStones) {
                            changeSelected(this.hex[i][j]);
                            choosePiece(stone, availableStones);
                            targetStones = availableTargets();
                            nextBoards.addAll(iterate(targetStones));
                            this.cleanSelected();
                        }
                    }
                }
            }
        }

        return nextBoards;
    }

    private ArrayList<AIBoard> iterate(ArrayList<Stone> targetStones) {
        ArrayList<AIBoard> tempList = new ArrayList<>();
        if (targetStones != null) {
            AIBoard aiBoard;
            for (Stone target : targetStones) {
                aiBoard = new AIBoard(this);
                aiBoard.selected = this.selected;
                aiBoard.selectedSize = this.selectedSize;


                aiBoard.sideMoveable = aiBoard.getFullMove(target);

                //aiBoard.selectedSize -= aiBoard.toBe.size();
                //aiBoard.selected.removeAll(aiBoard.toBe);
                aiBoard.setBestSelected();
                aiBoard.doMove();
                if (!alreadyBoards.containsValue(aiBoard)) {
                    tempList.add(aiBoard);
                    alreadyBoards.put(alreadyBoardsCount, aiBoard);
                    alreadyBoardsCount++;
                }

            }
        }
        return tempList;
    }

    private boolean getFullMove(Stone moveTo) {

        boolean reverse = this.shouldReverse(moveTo);
        if (beforeSideMove(moveTo, false))
            return true;

        shouldReverse2(reverse);
        return false;
    }

    public void doMove() {
        System.out.print("\nSelected: ");
        for (Stone stone : this.selected) {
            System.out.print(stone + ", ");
        }
        System.out.print("\nToBe: ");
        for (Stone stone : toBe)
            System.out.print(stone + ", ");
        Stone moveTo = toBe.get(toBe.size()-1);
        moveTo.setMainNum(0);
        boolean reverse = this.shouldReverse(moveTo);

        this.toBe.remove(moveTo);

        Stone last = selected.get(selectedSize - 1);
        int drow = moveTo.row - last.row;
        int dcol = moveTo.col - last.col;


        if (this.sideMoveable) {
            if ((drow >= 1 && dcol <= -1) || (drow <= -1 && dcol >= 1)) { // if more than 1 selected and not same line and too far, need to reverse
                Stone.reverseList(selected);
                last = selected.get(selectedSize - 1);
                drow = (moveTo.row - last.row);
                dcol = (moveTo.col - last.col);
            }
            sideMove(drow, dcol);

        }
        else {
            shouldReverse2(reverse);
            beforeLineMove(drow, dcol);
        }
        //this.moveStones(this.toBe.get(toBe.size()-1));
        this.cleanSelected();
    }

    public int getWinner() {
        if (this.deadBlue >= 6)
            return 1;
        else if (this.deadRed >= 6)
            return -1;
        else
            return 0;
    }

    public double evaluate() {
        double sum = 0;
        for (int i = 0; i < hex.length; i++) {
            for (int j = 0; j < hex[i].length; j++) {
                if (hex[i][j].getMainNum() != 4 && hex[i][j].getMainNum() == player) {
                    sum += fromMiddle[i][j];
                }
            }
        }

        sum = 100 - sum;

        if (deadBlue > preDeadBlue) {
            if (player == 1)
                sum *= 10;
            else
                sum /= 9;
        }
        if (deadRed > preDeadRed) {
            if (player == -1)
                sum *= 10;
            else
                sum /= 9;
        }
        return sum * player;
    }

    @Override
    protected void merge() {
        selected.addAll(toBe);
        selectedSize += toBe.size();
    }

}
