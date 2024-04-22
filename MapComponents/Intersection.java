package Pacman.MapComponents;

import java.awt.Color;
import java.awt.Graphics;

import Pacman.MainComponents.GameComponent;

//creates the intersection map component
//intersections are placed where two paths meet and are used in movement calculations
public class Intersection extends MapComponent {

    public Intersection(int x1, int y1) {
        // sets the coordinates of the intersection to the top left corner
        super(x1, y1);

        // sets the coordinates of the bottom right corner of the intersection
        this.x2 = x1 + unitWidth;
        this.y2 = y1 + unitWidth;
    }

    // drawing is temporary for debugging purposes and will be removed in the future
    public void draw(Graphics g) {
        // since all intersections are squares, the width and height are the same
        g.setColor(Color.RED);
        g.fillRect(x1, y1, unitWidth, unitWidth);
    }

    @Override
    public boolean collision(GameComponent c) {
        // checks if the component is within the bounds of the intersection
        int cx1 = c.getX() - c.getRadius();
        int cx2 = c.getX() + c.getRadius();
        int cy1 = c.getY() - c.getRadius();
        int cy2 = c.getY() + c.getRadius();

        // checks if the component is within the bounds of the intersection (x1, y1, x2,
        // y2 are the bounds of the intersection

        if (cx1 >= x1 && cx2 <= x + unitWidth && cy1 >= y1 && cy2 <= y1 + unitWidth) {
            return true;
        } else {
            return false;
        }
    }
}
