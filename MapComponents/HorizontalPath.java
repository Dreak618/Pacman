package Pacman.MapComponents;
//creates a vertical path
public class HorizontalPath extends Path {
    public HorizontalPath(int x, int y, int rectLength) {
        super(x, y, rectLength);
        this.width = rectLength * unitWidth;//sets the width to be the length of the rectangle
        this.height = unitWidth;//sets the height of the path to the unit width
        
        //sets the end coordinates of the path
        this.x2= x1 + (rectLength * unitWidth);
        this.y2 = y1 + unitWidth;
    }
}
