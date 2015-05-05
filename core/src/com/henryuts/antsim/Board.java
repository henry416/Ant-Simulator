package com.henryuts.antsim;


import java.awt.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 * Created by crazytom on 5/5/15.
 *
 * Board is responsible for maintaining the state of the simulation's board
 * It is used to track the statuses of all elements on the board and move / modify these elements
 * Board state is accessed by AntSim to draw elements onto the user's monitor
 */
public class Board {
    //public byte board[][];	// byte array for board, each byte represents a cell (x, y)  DEPRECIATED

    // constants for cell types
    private final byte CELL_CLEAR = 0;	// cell is clear
    private final byte CELL_OBST = -1;   // cell is obstructed
    private final byte CELL_HIVE = 1;    // cell is the hive entrance
    private final byte CELL_FOOD = 2;	// cell is food source

    // constants for board generation
    private int CELL_SIZE = 1;
    private int HIVE_VARIANCE = 10;     // variance of hive position from center
    private int OBST_NO = 2000;        // number of one-celled obstacles to be placed
    private float OBST_PROB = 0.95f;   // percentage chance for obstacle to be placed next to an existing obstacle
    private int FOOD_NO = 5;         // number of food sources on the board
    private int FOOD_QUANTITY = 25;     // mean food quality (no. of uses)
    private int FOOD_VARIANCE = 5;    // variance in food quality
    private int ANT_NO = 10;            // number of ants

    // board size
    public int boardWidth;
    public int boardHeight;

    // hive position
    public Point hivePos = new Point();

    // obstacle map, of all obstacles on the board
    public HashMap<Point, Byte> obstMap = new HashMap<Point, Byte>();   // point can be used as keys since its .equals is overloaded to check value not instance equality
    public Vector<Point> obstVec;     // vector is used for random access of obstacles, uses more memory in exchange for speed

    // food map, relating position to food source (attributes of food)
    public HashMap<Point, FoodSource> foodMap = new HashMap<Point, FoodSource>();

    // TODO: variable arguments Integer... elementNo , such as ObstacleNo, FoodNo, HiveX, HiveY, FoodQuality
    public Board (int width, int height, int cellSize) {
        Random rand = new Random();

        // size up the board
        CELL_SIZE = cellSize;
        boardWidth = width/cellSize;
        boardHeight = height/cellSize;
            //board = new byte[boardWidth][boardHeight];

        // place hive
        hivePos.x = boardWidth/2 + rand.nextInt(HIVE_VARIANCE + 1);
        hivePos.y = boardHeight/2 + rand.nextInt(HIVE_VARIANCE + 1);
        System.out.println("Hive position: " + hivePos.x + ", " + hivePos.y);
        //board[hiveX][hiveY] = CELL_HIVE;

        // generate obstacles
        generateAllObst();

        // generate food sources
        generateAllFood();

        // quick check:
        System.out.println("Does obstacle map contain hive position?  " + obstMap.containsKey(hivePos));
    }

    // scales the points to appropriate sized graphical coordinates used by AntSim camera
    public Point coordTrans(Point p) {
        return new Point(p.x * CELL_SIZE, p.y * CELL_SIZE);
    }

    // generate all obstacles
    private void generateAllObst() {
        Point obstPos;
        float obstRand;
        int uniqueObst = 0;

        Random rand = new Random();     // random factor
        obstVec = new Vector<Point>(OBST_NO);   // create a properly sized vector for all the obstacles

        for (int i = 0; i < OBST_NO; i++) {
            obstPos = new Point();              // new position
            obstRand = rand.nextFloat();        // determine obstacle placement class

            // does not use OBST_PROB
            if (i == 0 || obstRand > OBST_PROB) {
                uniqueObst++;
                // obstacle does not need to be near another obstacle
                do {
                    obstPos.x = rand.nextInt(boardWidth);
                    obstPos.y = rand.nextInt(boardHeight);
                } while (nextToHive(obstPos) || obstMap.containsKey(obstPos));
            }
            else {
                // obstacle must be placed next to another
                do {
                    int obstIndex = rand.nextInt(obstVec.size());
                    Point obstPos1 = obstVec.elementAt(obstIndex);      // get existing obstacle
                    int side = rand.nextInt(4);     // clockwise 0 above, 1 right, 2 below, 3 left

                    // set the position of obstacle
                    obstPos.setLocation(obstPos1);
                    switch (side) {
                        case 0:     obstPos.y++;
                                    break;
                        case 1:     obstPos.x++;
                                    break;
                        case 2:     obstPos.y--;
                                    break;
                        case 3:     obstPos.x--;
                                    break;
                        default:    obstPos.x = -1;
                                    obstPos.y = -1;
                                    break;
                    }

                } while (nextToHive(obstPos) || obstMap.containsKey(obstPos) || !withinBoard(obstPos));
            }

            // place the obstacle onto the board
            obstVec.add(obstPos);
            obstMap.put(obstPos, CELL_OBST);
        }

        System.out.println("Created " + obstVec.size() + " obstacles. " + uniqueObst + " unique obstacles.");
    }

    private boolean nextToHive(Point p) {
        return (p.x <= hivePos.x + 1 && p.x >= hivePos.x - 1 && p.y <= hivePos.y + 1 && p.y >= hivePos.y - 1);
    }

    private boolean withinBoard(Point p) {
        return (p.x >= 0 && p.x < boardWidth) && (p.y >= 0 && p.y < boardHeight);
    }

    // generates all food
    private void generateAllFood() {
        Random rand = new Random();

        // place the food source
        for (int i = 0; i < FOOD_NO; i++) {
            FoodSource fds = new FoodSource();
            fds.quantity = FOOD_QUANTITY - (int)(FOOD_VARIANCE/2) + rand.nextInt(FOOD_VARIANCE + 1);     // assign food quantity

            do {
                fds.foodPos.x = rand.nextInt(boardWidth);
                fds.foodPos.y = rand.nextInt(boardHeight);
            } while (nextToHive(fds.foodPos) || foodMap.containsKey(fds.foodPos) || isSurroundedByObst(fds.foodPos));

            foodMap.put(fds.foodPos, fds);
        }

        System.out.println("Created " + FOOD_NO + " food sources with mean food quantity of " + FOOD_QUANTITY + ".");
    }

    // check if surrounded by obstacles on all sides
    private boolean isSurroundedByObst(Point p){
        if (obstMap.containsKey(p))
            return true;
        if (obstMap.containsKey(new Point(p.x-1, p.y)))
            return true;
        if (obstMap.containsKey(new Point(p.x+1, p.y)))
            return true;
        if (obstMap.containsKey(new Point(p.x, p.y-1)))
            return true;
        if (obstMap.containsKey(new Point(p.x, p.y+1)))
            return true;
        return false;
    }
}
