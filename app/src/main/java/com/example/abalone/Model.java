package com.example.abalone;

import androidx.lifecycle.MutableLiveData;

import com.example.abalone.Logic.Board;
import com.example.abalone.Logic.Stone;

public class Model {
    protected Board board;
    private final MutableLiveData<Stone[][]> mBoard;

    public Model () {
        this.board = new Board(false, -1);
        this.mBoard = new MutableLiveData<>();
        this.mBoard.setValue(board.hex);
    }


    public MutableLiveData<Stone[][]> getBoard() {
        return this.mBoard;
    }

    public void doAction(Stone stone) {
        this.board.makeMove(stone);
        this.mBoard.setValue(this.board.hex);
    }

    public void setBoard(int num) {
        this.board = new Board(true, num);
        this.mBoard.setValue(this.board.hex);
    }

}
