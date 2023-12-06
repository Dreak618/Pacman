package Pacman.MapComponents;
import java.awt.Graphics;
import java.awt.Color;
//creates the intersection map component
//intersections are placed where two paths meet and are used in movement calculations
public class Intersection extends MapComponent {

    public Intersection(int x1, int y1) {
        //sets the coordinates of the intersection to the top left corner
        super(x1, y1);
        //sets the coordinates of the bottom right corner of the intersection
        this.x2 = x1 + unitWidth;
        this.y2 = y1 + unitWidth;
    }
    //drawing is temporary for debugging purposes and will be removed in the future
    public void draw(Graphics g) {
        //since all intersections are squares, the width and height are the same
        g.setColor(Color.RED);
        g.fillRect(x1, y1, unitWidth, unitWidth);
    }
}
