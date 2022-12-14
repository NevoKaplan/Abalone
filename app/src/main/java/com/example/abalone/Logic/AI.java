package com.example.abalone.Logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

        ArrayList<AIBoard>[] children = board.getNextBoards();

        System.out.println("\n\nSize: " + (children[0].size() + children[1].size() + children[2].size()));

        ArrayList<AIBoard> fullChildren = connectLists(children);
        int sum = 0;
        for (AIBoard a : fullChildren) {
            sum += a.getVal();
        }
        fullChildren.sort(Comparator.comparingDouble(AIBoard::getVal)); // sorting by parameter low -> high

        System.out.println("\n\nSUM: " + sum + "\n");
        List<AIBoard> topBoards = fullChildren.stream().limit(10).collect(Collectors.toList()); // get top 10
        for (AIBoard b : topBoards) {
            System.out.println(b.getVal());
            b.print();
        }
        System.out.println("END");
        return topBoards.get(0);

        //ArrayList<AIBoard> realChildren = connectLists(children);
        //Arrays.sort(realChildren, <>);
        //return children.get(rnd.nextInt(children.size()));
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
                    bestSelected = child.getMadeMove();
                }
            }
        } else {
            double bestVal = Double.MAX_VALUE;
            for (AIBoard child : children) {
                double val = child.getVal();
                if (val <= bestVal) {
                    bestVal = val;
                    bestSelected = child.getMadeMove();
                }
            }
        }
        board.setBestSelected(bestSelected);
        return children.get(rnd.nextInt(children.size()));*/
    }

    // firsts will be the triples, then the doubles and then the singles
    private ArrayList<AIBoard> connectLists(ArrayList<AIBoard>[] nextBoards) {
        ArrayList<AIBoard> fina = new ArrayList<>();
        for (int i = nextBoards.length-1; i >= 0; i--) {
            for (AIBoard board : nextBoards[i]) {
                board.setVal(board.evaluate(AiPlayer, i));
                fina.add(board);
            }
        }
        return fina;
    }

    private double checkBoard(AIBoard board, int depth, double alpha, double beta) {

        double val;
        int winner = board.getWinner();
        if (winner != 0) {
            return Double.MAX_VALUE * winner;
        }
        if (depth == 0) {
            val = board.evaluate(AiPlayer, 0);
            board.setVal(val);
            return val;
        }

        ArrayList<AIBoard> nextBoards = connectLists(board.getNextBoards());

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

    public ArrayList<Stone>[] getMoveOld(Board board) {
        AIBoard bestBoard = (AIBoard)bestMove(new AIBoard(board));
        ArrayList<Stone>[] stones = bestBoard.getMadeMove();
        if (!stones[0].isEmpty())
            return stones;
        return null;
    }

    public AIBoard getMove(Board board) {
        AIBoard bestBoard = (AIBoard)bestMove(new AIBoard(board));

        return bestBoard;
    }

}
