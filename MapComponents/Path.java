package Pacman.MapComponents;

import java.awt.Color;
import java.awt.Graphics;

import Pacman.MainComponents.GameComponent;

/* 
parent class for path map components
path class has 2 child classes: horizontal path and vertical path 
these classes allow for easier creation of the map since you just need to 
provide the type of path which is used to determine orientation
and then given the starting coordinates, and length can make the path
without having to calculate the end coordinates
*/

public class Path extends MapComponent {
    protected int width, height, rectLength;

    // constructor for path
    public Path(int x, int y, int rectLength) {
        super(x, y);
        this.x = x * unitWidth - radius;
        this.y = y * unitWidth - radius;
        this.rectLength = rectLength;
    }

    // get the length of path
    public int getLength() {
        return this.rectLength;
    }

    // draws the path
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x1, y1, width, height);
    }

    @Override
    public boolean collision(GameComponent c) {
        int cx1 = c.getX() - c.getRadius();
        int cx2 = c.getX() + c.getRadius();
        if (cx1 >= x && cx2 <= x + width && c.getY() - c.getRadius() >= y1
                && c.getY() + c.getRadius() <= y1 + height) {
            return true;
        } else {
            return false;
        }
    }

}
