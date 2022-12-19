package com.example.abalone.Control;

import java.util.ArrayList;

public class Layouts {

    public static ArrayList<int[][]> allLayouts() {
        ArrayList<int[][]> arrayList = new ArrayList<>();
        arrayList.add(organizeNormal());
        arrayList.add(organizeGermanDaisy());
        arrayList.add(organizeSnake());
        arrayList.add(organizeCs());
        arrayList.add(organizeCrown());
        arrayList.add(organizeDavid());
        arrayList.add(organizeHook());
        arrayList.add(organizeArrows());
        return arrayList;
    }

    // stones layout
    public static int[][] organizeEmpty() {
        int[][] placeAcc =
                {
                        {0,0,0,0,0,4,4,4,4},
                        {0,0,0,0,0,0,4,4,4},
                        {0,0,0,0,0,0,0,4,4},
                        {0,0,0,0,0,0,0,0,4},
                        {0,0,0,0,0,0,0,0,0},
                        {4,0,0,0,0,0,0,0,0},
                        {4,4,0,0,0,0,0,0,0},
                        {4,4,4,0,0,0,0,0,0},
                        {4,4,4,4,0,0,0,0,0}
                };
        return placeAcc;
    }

    public static int[][] organizeNormal() {
        int[][] placeAcc =
                {
                        {1,1,1,1,1,4,4,4,4},
                        {1,1,1,1,1,1,4,4,4},
                        {0,0,1,1,1,0,0,4,4},
                        {0,0,0,0,0,0,0,0,4},
                        {0,0,0,0,0,0,0,0,0},
                        {4,0,0,0,0,0,0,0,0},
                        {4,4,0,0,-1,-1,-1,0,0},
                        {4,4,4,-1,-1,-1,-1,-1,-1},
                        {4,4,4,4,-1,-1,-1,-1,-1}
                };
        return placeAcc;
    }

    // stones layout
    public static int[][] organizeGermanDaisy() {
        // this one is manual
        int[][] placeAcc =
                {
                        {0,0,0,0,0,4,4,4,4},
                        {1,1,0,0,-1,-1,4,4,4},
                        {1,1,1,0,-1,-1,-1,4,4},
                        {0,1,1,0,0,-1,-1,0,4},
                        {0,0,0,0,0,0,0,0,0},
                        {4,0,-1,-1,0,0,1,1,0},
                        {4,4,-1,-1,-1,0,1,1,1},
                        {4,4,4,-1,-1,0,0,1,1},
                        {4,4,4,4,0,0,0,0,0}
                };
        return placeAcc;
    }

    // stones layout
    public static int[][] organizeSnake() {
        // this one is manual
        int[][] placeAcc =
                {
                        {1,1,1,1,1,4,4,4,4},
                        {1,0,0,0,0,0,4,4,4},
                        {1,0,0,0,0,0,0,4,4},
                        {1,0,0,1,1,-1,-1,0,4},
                        {0,1,0,1,0,-1,0,-1,0},
                        {4,0,1,1,-1,-1,0,0,-1},
                        {4,4,0,0,0,0,0,0,-1},
                        {4,4,4,0,0,0,0,0,-1},
                        {4,4,4,4,-1,-1,-1,-1,-1},
                };
        return placeAcc;
    }

    // stones layout
    public static int[][] organizeCrown() {
        // this one is manual
        int[][] placeAcc =
                {
                        {0,0,1,0,0,4,4,4,4},
                        {1,0,1,1,0,1,4,4,4},
                        {0,1,1,-1,1,1,0,4,4},
                        {0,0,1,1,1,1,0,0,4},
                        {0,0,0,0,0,0,0,0,0},
                        {4,0,0,-1,-1,-1,-1,0,0},
                        {4,4,0,-1,-1,1,-1,-1,0},
                        {4,4,4,-1,0,-1,-1,0,-1},
                        {4,4,4,4,0,0,-1,0,0},
                };
        return placeAcc;
    }

    // stones layout
    public static int[][] organizeDavid() {
        // this one is manual
        int[][] placeAcc =
                {
                        {0,0,1,0,0,4,4,4,4},
                        {0,0,1,1,0,0,4,4,4},
                        {-1,-1,1,0,-1,-1,-1,4,4},
                        {0,-1,-1,0,0,-1,-1,0,4},
                        {0,0,-1,0,0,0,1,0,0},
                        {4,0,1,1,0,0,1,1,0},
                        {4,4,1,1,1,0,-1,1,1},
                        {4,4,4,0,0,-1,-1,0,0},
                        {4,4,4,4,0,0,-1,0,0}
                };
        return placeAcc;
    }

    // stones layout
    public static int[][] organizeHook() {
        // this one is manual
        int[][] placeAcc =
                {
                        {0,0,0,0,0,4,4,4,4},
                        {0,0,0,0,0,0,4,4,4},
                        {0,0,1,0,-1,0,0,4,4},
                        {-1,1,1,1,-1,-1,-1,1,4},
                        {-1,-1,-1,1,0,-1,1,1,1},
                        {4,-1,1,1,1,-1,-1,-1,1},
                        {4,4,0,0,1,0,-1,0,0},
                        {4,4,4,0,0,0,0,0,0},
                        {4,4,4,4,0,0,0,0,0}
                };
        return placeAcc;
    }

    // stones layout
    public static int[][] organizeCs() {
        // this one is manual
        int[][] placeAcc =
                {
                        {-1,-1,1,1,1,4,4,4,4},
                        {-1,0,-1,0,0,1,4,4,4},
                        {-1,0,-1,1,1,1,0,4,4},
                        {0,0,-1,0,0,0,0,0,4},
                        {0,0,0,0,0,0,0,0,0},
                        {4,0,0,0,0,0,-1,0,0},
                        {4,4,0,1,1,1,-1,0,-1},
                        {4,4,4,1,0,0,-1,0,-1},
                        {4,4,4,4,1,1,1,-1,-1}
                };
        return placeAcc;
    }

    // stones layout
    public static int[][] organizeArrows() {
        // this one is manual
        int[][] placeAcc =
                {
                        {0,0,-1,0,0,4,4,4,4},
                        {0,0,1,0,0,0,4,4,4},
                        {0,0,1,0,-1,-1,-1,4,4},
                        {0,0,1,0,-1,1,1,1,4},
                        {0,-1,1,1,0,-1,-1,1,0},
                        {4,-1,-1,-1,1,0,-1,0,0},
                        {4,4,1,1,1,0,-1,0,0},
                        {4,4,4,0,0,0,-1,0,0},
                        {4,4,4,4,0,0,1,0,0}
                };
        return placeAcc;
    }
}
