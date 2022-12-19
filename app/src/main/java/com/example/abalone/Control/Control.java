package com.example.abalone.Control;

import com.example.abalone.Logic.AI;
import com.example.abalone.Logic.Board;
import com.example.abalone.Logic.Stone;

public class Control {
    private Stone currentStone;
    private static Control single_Instance = null;
    private int deadBlue, deadRed;

    private AI ai;
    private Board board = null;

    public static Control getInstance() {
        if (single_Instance == null)
            single_Instance = new Control();
        return single_Instance;
    }

    public static Control getInstance(int num) {
        if (single_Instance == null)
            single_Instance = new Control(num);
        return single_Instance;
    }


    public static void destroy() {
        single_Instance = null;
    }

    private Control() {
        currentStone = null;
        if (board == null)
            board = new Board(true, 1);
        deadBlue = board.deadBlue;
        deadRed = board.deadRed;
        ai = AI.getInstance(board.getPlayer() * -1);
    }

    private Control(int num) {
        currentStone = null;
        if (board == null)
            board = new Board(true, num);
        deadBlue = board.deadBlue;
        deadRed = board.deadRed;
        ai = AI.getInstance(board.getPlayer() * -1);
    }

    public Board setUpBoard(int num) {
        board = new Board(true, num);
        return board;
    }

    public Stone getCurrentStone() {
        return currentStone;
    }


    public boolean setCurrentStone(Stone currentStone) {
        this.currentStone = currentStone;
        return onChangeStone();
    }

    private boolean onChangeStone() {
        return board.makeMove(this.currentStone);
    }

    public int getDeadBlue() {
        return this.deadBlue;
    }

    public int getDeadRed() {
        return this.deadRed;
    }

    public Board getBoard() {
        return this.board;
    }

    public void onCheckedChanged(boolean isChecked) {
        if (isChecked) {
            AI.getInstance(board.getPlayer() * -1);
        }
        else {
            AI.removeInstance();
        }
    }
}
