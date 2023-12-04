package Pacman.MapComponents;
import javax.swing.JPanel;

//general class for all map components
//allows for easily moving all map components if needed and resizing them all
public class MapComponent extends JPanel {
    protected int unitWidth = 20;//width of each unit in the map
    protected int x1, y1, x2, y2;

    //constructor for map components
    public MapComponent(int x1, int y1) {
        // x1 and y1 are the coordinates of the top left corner of the component
        this.x1 = x1 * unitWidth;
        this.y1 = y1 * unitWidth;
    }
   
    //methods to get the coordinates and width of map components
    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }
    public int getWidth(){
        return unitWidth;
    }
}
