package Pacman.Panels;

import Pacman.MapComponents.*;
import Pacman.MainComponents.*;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Graphics;
import javax.swing.JPanel;


//Map panel is the panel that contains the map and visuals for when the game is in the playing state
public class MapPanel extends JPanel {
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Intersection> intersections = new ArrayList<>();
    private ArrayList<PointPellet> pellets = new ArrayList<>();
    private ArrayList<Ghost> ghosts = new ArrayList<>();
    private Player player;
    private int totalPowerPellets = 0;
    private int totalPellets = 0;
    private Pacman game;

    // creates a new map panel
    public MapPanel(Pacman game) {
        this.game = game;
        // generates all of the paths and intersections in the map
        addPaths();
        addIntersections();
        addPellets();
        createGhosts();
    }

    // paints the map
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        drawGhostBox(g);
        // draws all of the paths, intersections, and pellets
        for (Path path : paths) {
            path.draw(g);
        }
        // draws all of the intersections
        // used for debugging
        // for (Intersection intersection : intersections) {
        //     intersection.draw(g);
        // }
        for (PointPellet pellet : pellets) {
            pellet.draw(g);
        }

        // draws the player
        player.draw(g);

        // draws the ghosts
        for (Ghost c : ghosts) {
            c.draw(g, c);
        }
    }

    // adds player to the map
    public void addPlayer(Player player) {
        this.player = player;
    }

   

    // returns an array list of all paths
    public ArrayList<Path> getPaths() {
        return paths;
    }

    // returns an array list of all intersections
    public ArrayList<Intersection> getIntersections() {
        return intersections;
    }

    // returns an array list of all pellets
    public ArrayList<PointPellet> getPellets() {
        return pellets;
    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

     // creates ghosts
    public void createGhosts() {
        ghosts.add(new Ghost("blue", 20, 20 + getWidth(), 20, 20 + getWidth(),this)); // inky
        ghosts.add(new Ghost("red", 260, 260 + getWidth(), 20, 20 + getWidth(),this)); // blinky
        ghosts.add(new Ghost("pink", 20, 20 + getWidth(), 440, 440 + getWidth(),this)); // pinky
        ghosts.add(new Ghost("orange", 260, 260 + getWidth(), 440, 440 + getWidth(),this)); // clyde
    }
    
    // adds paths to the map
    public void addPaths() {
        paths.add(new HorizontalPath(1, 1, 23));
        paths.add(new HorizontalPath(1, 22, 23));
        paths.add(new HorizontalPath(5, 9, 13));
        paths.add(new HorizontalPath(1, 12, 23));
        paths.add(new VerticalPath(1, 1, 5));
        paths.add(new VerticalPath(23, 1, 5));
        paths.add(new VerticalPath(8, 1, 5));
        paths.add(new VerticalPath(14, 1, 5));
        paths.add(new VerticalPath(11, 3, 7));
        paths.add(new VerticalPath(5, 5, 8));
        paths.add(new VerticalPath(17, 5, 8));
        paths.add(new VerticalPath(20, 5, 8));
        paths.add(new VerticalPath(1, 12, 11));
        paths.add(new VerticalPath(23, 12, 11));
        paths.add(new HorizontalPath(8, 3, 7));
        paths.add(new HorizontalPath(1, 5, 8));
        paths.add(new HorizontalPath(14, 5, 10));
        paths.add(new HorizontalPath(1, 18, 10));
        paths.add(new VerticalPath(5, 18, 5));
        paths.add(new VerticalPath(10, 12, 7));
        paths.add(new HorizontalPath(10, 16, 14));
        paths.add(new VerticalPath(19, 16, 7));
    }

    // adds intersections to the map
    public void addIntersections() {
        intersections.add(new Intersection(1, 1));
        intersections.add(new Intersection(8, 1));
        intersections.add(new Intersection(14, 1));
        intersections.add(new Intersection(23, 1));
        intersections.add(new Intersection(8, 3));
        intersections.add(new Intersection(11, 3));
        intersections.add(new Intersection(14, 3));
        intersections.add(new Intersection(1, 5));
        intersections.add(new Intersection(5, 5));
        intersections.add(new Intersection(8, 5));
        intersections.add(new Intersection(14, 5));
        intersections.add(new Intersection(17, 5));
        intersections.add(new Intersection(20, 5));
        intersections.add(new Intersection(23, 5));
        intersections.add(new Intersection(5, 9));
        intersections.add(new Intersection(11, 9));
        intersections.add(new Intersection(17, 9));
        intersections.add(new Intersection(1, 12));
        intersections.add(new Intersection(5, 12));
        intersections.add(new Intersection(17, 12));
        intersections.add(new Intersection(20, 12));
        intersections.add(new Intersection(23, 12));
        intersections.add(new Intersection(1, 22));
        intersections.add(new Intersection(23, 22));
        intersections.add(new Intersection(10, 18));
        intersections.add(new Intersection(10, 12));
        intersections.add(new Intersection(10, 16));
        intersections.add(new Intersection(19, 16));
        intersections.add(new Intersection(23, 16));
        intersections.add(new Intersection(1, 18));
        intersections.add(new Intersection(5, 18));
        intersections.add(new Intersection(5, 22));
        intersections.add(new Intersection(19, 22));
    }

    // adds pellets to the map
    public void addPellets() {
        for (Path path : paths) {
            // adds pellets shifted by i in the x or y direction depending on type of path
            // i goes from 0 to the length of the path
            for (int i = 1; i < path.getLength() - 1; i++) {
                if (path instanceof HorizontalPath) { // checks if it is a horizontal path
                    createPellet(path.getX1() / path.getWidth() + i, path.getY1() / path.getWidth());
                } else if (path instanceof VerticalPath) { // checks if it is a vertical path
                    createPellet(path.getX1() / path.getWidth(), path.getY1() / path.getWidth() + i);
                }
            }
        }
        // adds pellets at all intersections
        for (Intersection i : intersections) {
            pellets.add(new PointPellet(i.getX1() / i.getWidth(), i.getY1() / i.getWidth()));
        }
    }
    // creates a pellet at the given coordinates
    public void createPellet(int x, int y) {
        // if conditions are met a power pellet is created
        if (totalPellets % 20 == 0 && totalPowerPellets < 6) {
            pellets.add(new PowerPellet(x, y));
            totalPowerPellets++;
        } else { // otherwise a point pellet is created
            pellets.add(new PointPellet(x, y));
        }
        totalPellets++;
    }

    // removes a pellet from the map
    public void removePellet(PointPellet p) {
        pellets.remove(p);
    }
    public boolean checkPathCollision(int[] entityPosision, int[] entitySpeed){
        return game.checkPathCollision(entityPosision, entitySpeed);  
    }
    // checks if something is in an intersection, is used to movement logic
    public boolean checkIntersectionCollision(int[] entityPosision) {
        // gets the coordinates of the entity 
        int x1 = entityPosision[0];
        int y1 = entityPosision[2];
        // checks if the entity is in any of the intersections
        for (Intersection i : intersections) {
            if (x1 == i.getX1() && y1 == i.getY1() ) {
                return true;
            }
        }
        // if the entity is not in any intersections it returns false
        return false;
    }

    // checks if player is colliding with a ghost and returns true if it is
    public boolean checkGhostCollision(int[] entityPosision) {
        boolean ghostCollision = false;
        for (Ghost c : ghosts) {
            // checks if the entity is colliding with any of the ghosts
            ghostCollision = c.checkPlayerCollision(entityPosision, player.getWidth());
            if (ghostCollision) {
                break;
            }
        }
        return ghostCollision;
    }
    public void ghostDeathStuff(){
        for (Ghost g : ghosts) {
            g.ghostDeath();
        }
    }
    public boolean getConsumptionMode(){
        return game.getConsumptionMode();
    }
    public void drawGhostBox(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(200, 180, 60, 80);
    }
}
