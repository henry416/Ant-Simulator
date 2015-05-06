package com.henryuts.antsim;

import java.awt.*;

/**
 * Created by crazytom on 5/6/15.
 */
public class Ant {
    public Point antPos = new Point();

    public static final float PHER_PROB = 0.5f;     // base probability of ant following phermone trail
    public static final float PHER_SCALE = 0.1f;    // scaling probability of phermone intensity

    public Ant() {

    }

    public void moveRight() {
        antPos.x++;
    }

    public void moveLeft() {
        antPos.x--;
    }

    public void moveUp() {
        antPos.y++;
    }

    public void moveDown() {
        antPos.y--;
    }
}
