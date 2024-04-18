package Pacman.MapComponents;

/**
 * Creates a vertical path
 */
public class VerticalPath extends Path {
    public VerticalPath(int x, int y, int rectLength) {
        super(x, y, rectLength);
        this.width = unitWidth; // sets the width of the path to the unit width
        this.height = rectLength * unitWidth; // sets the height to be the length of the rectangle

        // sets the end coordinates of the path
        this.x2 = x1 + unitWidth;
        this.y2 = y1 + (rectLength * unitWidth);
    }
}
