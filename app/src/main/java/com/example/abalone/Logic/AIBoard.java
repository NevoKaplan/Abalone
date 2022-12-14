package com.example.abalone.Logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AIBoard extends Board {

    final static int MAX_DEPTH = 3;
    int depth;
    private double val;
    private int preDeadRed, preDeadBlue;
    private ArrayList<Stone>[] madeMove = new ArrayList[2];
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
        for (int i = 0; i < madeMove.length; i++)
            madeMove[i] = new ArrayList<>();
        alreadyBoardsCount = 0;
        alreadyBoards.clear();
        preDeadBlue = deadBlue;
        preDeadRed = deadRed;

        sideMoveable = false;
    }

    public AIBoard(AIBoard board) {
        super(false, -1);
        run++;
        updateBoard(board.hex);
        this.player = board.player*-1;
        this.depth = board.depth-1;
        this.val = 0;
        for (int i = 0; i < madeMove.length; i++)
            madeMove[i] = new ArrayList<>();
        preDeadBlue = deadBlue;
        preDeadRed = deadRed;

        sideMoveable = false;
    }

    private void updateBoard(Stone[][] stones) {
        for (int i = 0; i < this.hex.length; i++) {
            for (int j = 0; j < this.hex[i].length; j++) {
                this.hex[i][j].setMainNum(stones[i][j].getMainNum());
                this.hex[i][j].setOgNum(stones[i][j].getOgNum());
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
    public ArrayList<Stone>[] getMadeMove() {
        return this.madeMove;
    }
    public void setBestSelected(ArrayList<Stone> selected, ArrayList<Stone> toBe) {
        for (Stone stone : selected) {
            this.madeMove[0].add(stone);
        }
        for (Stone stone : toBe) {
            this.madeMove[1].add(stone);
        }
    }

    public void setBestSelected() {
        for (Stone stone : this.selected) {
            this.madeMove[0].add(stone);
        }
        for (Stone stone : this.toBe) {
            this.madeMove[1].add(stone);
        }
    }

    public ArrayList<AIBoard>[] getNextBoards() {
        ArrayList<AIBoard>[] nextBoards = this.IterateNextBoards2();
        return nextBoards;
    }


    private ArrayList<AIBoard>[] IterateNextBoards2() {
        ArrayList<AIBoard>[] nextBoards = new ArrayList[3];
        for (int i = 0; i < nextBoards.length; i++)
            nextBoards[i] = new ArrayList<>();
        int count = 0;                               // count for how many troops already checked - to check less
        if (this.player == 1)
            count += this.deadBlue;
        else
            count += this.deadRed;

        for (int i = 0; i < this.hex.length && count <= 14; i++) {
            for (int j = 0; j < this.hex[i].length; j++) {
                if (this.hex[i][j].getMainNum() == this.player) {
                    count++;
                    nextBoards[0].addAll(findMoveSingle(hex[i][j]));
                    ArrayList<Stone> availableDouble = availableStonesForSingle(hex[i][j]);
                    ArrayList<Stone> tempSelected;

                    for (Stone availlable : availableDouble) {
                        tempSelected = new ArrayList<>();
                        tempSelected.add(hex[i][j]);
                        tempSelected.add(availlable);
                        nextBoards[1].addAll(findMoveMultiple(tempSelected)); // double - problem here
                        ArrayList<Stone> availableTriple = availableStonesForDouble(tempSelected);

                        for (Stone available2 : availableTriple) {
                            tempSelected = new ArrayList<>();
                            tempSelected.add(hex[i][j]);
                            tempSelected.add(availlable);
                            tempSelected.add(available2);

                            Stone.sort(tempSelected);
                            nextBoards[2].addAll(findMoveMultiple(tempSelected)); // triple
                        }
                    }
                }
            }
        }
        return nextBoards;
    }

    private ArrayList<AIBoard> findMoveSingle(Stone start) {
        ArrayList<AIBoard> boards = new ArrayList<>();
        ArrayList<Stone> targets = availableTargetsForSingle(start);
        for (Stone target : targets) {
            AIBoard aiBoard = new AIBoard(this);
            aiBoard.changeSelected(aiBoard.hex[start.row][start.col]);
            for (Stone s : aiBoard.selected)
                aiBoard.madeMove[0].add(new Stone(s));
            aiBoard.doMoveSingle(aiBoard.hex[target.row][target.col]);
            boards.add(aiBoard);
            aiBoard.cleanSelected();
        }
        return boards;
    }

    private ArrayList<AIBoard> findMoveMultiple(ArrayList<Stone> starts) {

        ArrayList<AIBoard> boards = new ArrayList<>();
        ArrayList<Stone> targets = availableTargetsMultiple(starts);

        for (Stone target : targets) {
            AIBoard aiBoard = new AIBoard(this);
            for (Stone start : starts)
                aiBoard.changeSelected(aiBoard.hex[start.row][start.col]);

            for (Stone s : aiBoard.selected)
                aiBoard.madeMove[0].add(new Stone(s));

            aiBoard.doMoveMultiple(aiBoard.hex[target.row][target.col]);
            boards.add(aiBoard);
            aiBoard.cleanSelected();
        }
        return boards;
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
                    ArrayList<Stone> tempSelected = new ArrayList<>();
                    tempSelected.add(this.hex[i][j]);
                    nextBoards.addAll(iterate(targetStones, tempSelected));

                    this.cleanSelected();
                    changeSelected(this.hex[i][j]);
                    ArrayList<Stone> availableStones = availableStones2();
                    this.cleanSelected();


                    if (availableStones != null) {
                        for (Stone stone : availableStones) {
                            changeSelected(this.hex[i][j]);
                            choosePiece(stone, this.hex[i][j]);
                            tempSelected.addAll(this.selected);
                            targetStones = availableTargets();
                            nextBoards.addAll(iterate(targetStones, tempSelected));
                            this.cleanSelected();
                        }
                    }
                }
            }
        }

        return nextBoards;
    }

    private ArrayList<AIBoard> iterate(ArrayList<Stone> targetStones, ArrayList<Stone> tempSelected) {
        this.cleanSelected();

        ArrayList<AIBoard> tempList = new ArrayList<>();
        if (targetStones != null) {
            AIBoard aiBoard;
            for (Stone target : targetStones) {
                aiBoard = new AIBoard(this);
                for (Stone sel : tempSelected)
                    aiBoard.changeSelected(sel);
                //aiBoard.selected = this.selected;
                //aiBoard.selectedSize = this.selectedSize;
                aiBoard.sideMoveable = aiBoard.getFullMove(target);

                //aiBoard.selectedSize -= aiBoard.toBe.size();
                //aiBoard.selected.removeAll(aiBoard.toBe);
                aiBoard.setBestSelected();
                aiBoard.doMoveOld();
                if (!alreadyBoards.containsValue(aiBoard)) {
                    tempList.add(aiBoard);
                    alreadyBoards.put(alreadyBoardsCount, aiBoard);
                    alreadyBoardsCount++;
                }
                this.cleanSelected();
            }
        }
        return tempList;
    }

    private void doMoveSingle(Stone moveTo) {
        moveTo.setMainNum(0);

        this.toBe.add(moveTo);
        for (Stone s : this.toBe)
            this.madeMove[1].add(new Stone(s));

        Stone last = selected.get(0);
        int drow = last.row - moveTo.row;
        int dcol = last.col - moveTo.col;

        beforeLineMove(drow, dcol);
    }

    private void doMoveMultiple(Stone moveTo) {
        boolean reverse = this.shouldReverse(moveTo);

        this.madeMove[1].add(new Stone(moveTo));
        if (beforeSideMove(moveTo, true)) {
            this.sideMoveable = true;
            return;
        }

        shouldReverse2(reverse);

        Stone last = selected.get(selectedSize - 1);
        int drow = moveTo.row - last.row;
        int dcol = moveTo.col - last.col;

        this.madeMove[1] = new ArrayList<>();
        for (Stone s : this.toBe)
            this.madeMove[1].add(new Stone(s));

        beforeLineMove(drow, dcol);

    }

    private boolean getFullMove(Stone moveTo) {

        boolean reverse = this.shouldReverse(moveTo);
        if (beforeSideMove(moveTo, false))
            return true;

        shouldReverse2(reverse);
        return false;
    }

    public void doMoveOld() {
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

    public double evaluate(int player, int amount) {
        double sum = 0;
        for (int i = 0; i < hex.length; i++) {
            for (int j = 0; j < hex[i].length; j++) {
                if (hex[i][j].getMainNum() != 4 && hex[i][j].getMainNum() == player) {
                    sum += fromMiddle[i][j];
                }
            }
        }

        sum = 100 - sum;

        switch (amount) {
            case 2:
                sum += 10;
                break;
            case 1:
                sum += 5;
                break;
            default:
                break;
        }

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


    // checks if player
    protected void choosePiece(Stone available, Stone chosen) {

        // there is only one stone in the array...
        for (int[] var: dirArr) { // all directions
            if (var[0] == available.row - chosen.row && var[1] == available.col - chosen.col) {
                changeSelected(available);
                return;
            }
            else if (2 * var[0] == available.row - chosen.row && 2 * var[1] == available.col - chosen.col) {
                changeSelected(available);
                changeSelected(hex[chosen.row + var[0]][chosen.col + var[1]]);
                return;
            } // checks for "legality" of the stone and adds them to the list if legal
        }
    }


    protected ArrayList<Stone> availableTargetsForSingle(Stone temp) {
        // if code here, can only be 1 selected
        ArrayList<Stone> targets = new ArrayList<>();
        for (int[] var : dirArr) { // all directions list
            try {
                if (hex[temp.row + var[0]][temp.col + var[1]].getMainNum() == 0) {
                    targets.add(hex[temp.row + var[0]][temp.col + var[1]]);
                }
            } catch (IndexOutOfBoundsException ignored) {

            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return targets;
    }

    private ArrayList<Stone> availableTargetsMultiple(ArrayList<Stone> chosen) {
        Stone first = chosen.get(0);
        Stone second = chosen.get(1);
        int drow = first.row - second.row;
        int dcol = first.col - second.col;
        ArrayList<Stone> stones = new ArrayList<>();
        int size = chosen.size();
        // gets the difference between the 2 stones

        targetLine(drow, dcol, first, 0, stones, size); // from first and on
        targetLine(-drow, -dcol, chosen.get(size-1), 0, stones, size); // from last and on

        Iterator<Stone> it;
        boolean flag, added; // flag checks if stones can be moved to the side


        for (int[] var : dirArr) { // list of all directions
            it = chosen.iterator();
            flag = true;
            added = false;
            ArrayList<Stone> maybe = new ArrayList<>();
            if (!((var[0] == drow && var[1] == dcol) || (var[0] == -drow && var[1] == -dcol))) { // don't check the already checked
                if (((first.col < first.col + var[1]) && (first.row + var[0] <= first.row)) || (drow == 0 && first.col <= first.col + var[1])) { // change only if the current one is more to the left than the next one
                    Stone.reverseList(chosen);
                }
                while (it.hasNext() && flag) {
                    Stone temp = it.next();
                    try {
                        if (hex[temp.row + var[0]][temp.col + var[1]].getMainNum() != 0) { // if future doesn't equal to 0, can't be moved
                            flag = false;
                        } else {
                            if (!added) {
                                maybe.add(hex[temp.row + var[0]][temp.col + var[1]]);
                                added = true;
                            }
                        }
                    }
                    catch (IndexOutOfBoundsException e) {
                        flag = false;
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            else {
                flag = false;
            }
            if (flag) { // if all stones can be moved then add the maybe list
                stones.addAll(maybe);
            }
        }
        Stone.sort(stones);
        return stones;
    }

    protected ArrayList<Stone> availableStonesForSingle(Stone temp) {
        // checks for available when 1 is selected
        ArrayList<Stone> arrayList = new ArrayList<>();
        for (int[] var : dirArr) {
            try {
                if (hex[temp.row + var[0]][temp.col + var[1]].getMainNum() == player) {
                    arrayList.add(hex[temp.row + var[0]][temp.col + var[1]]);
                }
            }
            catch (IndexOutOfBoundsException ignored) {

            }
            catch (Exception e) {System.out.println(e.getMessage());}
        }
        return arrayList;
    }

    private ArrayList<Stone> availableStonesForDouble(ArrayList<Stone> temp) {
        ArrayList<Stone> stones = new ArrayList<>();
        Stone.sort(temp);
        Stone first = temp.get(0);
        Stone second = temp.get(1);
        int drow = first.row - second.row;
        int dcol = first.col - second.col;
        // checks the edges
        if (first.row + drow < hex.length && first.row + drow >= 0 && first.col + dcol < hex.length && first.col + dcol >= 0) {
            if (hex[first.row + drow][first.col + dcol].getMainNum() == player) {
                stones.add(hex[first.row + drow][first.col + dcol]);
            }
        }
        if (second.row - drow < hex.length && second.row - drow >= 0 && second.col - dcol < hex.length && second.col - dcol >= 0) {
            if (hex[second.row - drow][second.col - dcol].getMainNum() == player) {
                stones.add(hex[second.row - drow][second.col - dcol]);
            }
        }
        return stones;
    }

}
