package Pacman.MapComponents;

import java.awt.Graphics;
import java.awt.Color;

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
    public Path(int x1, int y1, int rectLength) {
        super(x1, y1);
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

}
