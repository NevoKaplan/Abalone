package com.example.abalone.Logic;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    private int AiPlayer;
    private static AI instance = null;
    private static Random rnd = new Random();



    public void setAiPlayer(int aiPlayer) {
        this.AiPlayer = aiPlayer;
    }

    public int getAiPlayer() {
        return this.AiPlayer;
    }

    private AI (int player) {
        AiPlayer = player;
    }

    public static AI getInstance(int player) {
        if (instance == null) {
            instance = new AI(player);
        }
        return instance;
    }

    public static boolean hasInstance() {
        return instance != null;
    }

    public static void removeInstance() {
        instance = null;
    }

    public Board bestMove(AIBoard board) {
        Board bestSelected = null;

        ArrayList<AIBoard> children = board.getNextBoards();
        AIBoard board1;

  /*      do {
            board1 = children.get(rnd.nextInt(children.size()));
        } while(!board1.sideMoveable || board1.selectedSize != 2 || board1.toBe.get(0).row != 4);
*/
        System.out.println("Size: " + children.size());
        for (AIBoard a : children) {
            a.print();
        }
        System.out.println("END");
        return children.get(rnd.nextInt(children.size()));
        /*System.out.println("Before: " + children.size());
        int i = 1;
        for (AIBoard child : children) {
            System.out.println("i: "+i);
            child.setVal(checkBoard(child, child.getDepth(), Double.MIN_VALUE, Double.MAX_VALUE));
            i++;
        }
        if (board.player == AiPlayer) {
            double bestVal = Double.MIN_VALUE;
            for (AIBoard child : children) {
                double val = child.getVal();
                if (val >= bestVal) {
                    bestVal = val;
                    bestSelected = child.getBestSelected();
                }
            }
        } else {
            double bestVal = Double.MAX_VALUE;
            for (AIBoard child : children) {
                double val = child.getVal();
                if (val <= bestVal) {
                    bestVal = val;
                    bestSelected = child.getBestSelected();
                }
            }
        }
        board.setBestSelected(bestSelected);
        return children.get(rnd.nextInt(children.size()));*/
    }

    private double checkBoard(AIBoard board, int depth, double alpha, double beta) {

        double val;
        int winner = board.getWinner();
        if (winner != 0) {
            return Double.MAX_VALUE * winner;
        }
        if (depth == 0) {
            val = board.evaluate();
            board.setVal(val);
            return val;
        }

        ArrayList<AIBoard> nextBoards = board.getNextBoards();

        // checking whose turn it is
        if (board.player == AiPlayer) {
            for (AIBoard child : nextBoards) {
                alpha = Math.max(alpha, checkBoard(child, depth - 1, alpha, beta));
                if (beta < alpha)
                    break;
            }
            return alpha;
        } else
        {
            for (AIBoard child : nextBoards) {
                beta = Math.min(beta, checkBoard(child, depth - 1, alpha, beta));
                if (beta < alpha)
                    break;
            }
            return beta;
        }
    }

    public ArrayList<Stone>[] getMove(Board board) {
        AIBoard bestBoard = (AIBoard)bestMove(new AIBoard(board));
        System.out.println("HERE1");
        ArrayList<Stone>[] stones = bestBoard.getBestSelected();
        if (!stones[0].isEmpty())
            return stones;
        System.out.println("ASJDOIA");
        return null;
    }

}
