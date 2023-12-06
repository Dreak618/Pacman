package Pacman.MapComponents;
import java.awt.Graphics;
import java.awt.Color;
// Point pellets are the smaller pellets that pacman eats that give points
// this class creates the map component for the point pellets
public class PointPellet extends MapComponent{
    protected int width;
    
    //constructor for the point pellet
    public PointPellet(int x, int y){
        super(x,y);
        this.width = unitWidth/2;
        this.x1 += unitWidth/4;
        this.y1 += unitWidth/4;
        this.x2 = x1 + width;
        this.y2 = y1 + width;
    }

    //get the width of the pellet
    @Override
    public int getWidth(){
        return width;
    }

    //draw the pellet
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.fillOval(x1, y1, width, width);
    }
}
