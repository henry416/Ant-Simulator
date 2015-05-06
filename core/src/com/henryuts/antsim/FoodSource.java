package com.henryuts.antsim;

import java.awt.*;

/**
 * Created by crazytom on 5/5/15.
 *
 * FoodSource tracks the position and remaining quantity of food left in an individual food source placed on the board
 */
public class FoodSource {
    public Point foodPos = new Point();
    public int quantity;

    public FoodSource() {
        foodPos = new Point();
        foodPos.x = 0;
        foodPos.y = 0;
        quantity = 0;
    }

    public void take(){
        this.quantity--;
    }
}
