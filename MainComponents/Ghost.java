package Pacman.MainComponents;

import java.awt.*;
import java.util.Random;
import Pacman.Panels.MapPanel;
import javax.swing.*;

public class Ghost extends JPanel {

  private Random RNJesus = new Random(4);
  private int width = 20;
  private MapPanel map;
  private int[] gLoc = new int[4]; // ghost location coords
  private int direction;
  private int[] speed = new int[] { 0, 0 }; // ghost movement speed (can adjust)
  private boolean alive = true;
  private int deathTimer = 500;
  private String name;
  private ImageIcon red = new ImageIcon("Pacman/Assets/red_ghost.png"), resizedRed;
  private ImageIcon blue = new ImageIcon("Pacman/Assets/blue_ghost.png"), resizedBlue;
  private ImageIcon pink = new ImageIcon("Pacman/Assets/pink_ghost.png"), resizedPink;
  private ImageIcon orange = new ImageIcon("Pacman/Assets/orange_ghost.png"), resizedOrange;
  private ImageIcon edible = new ImageIcon("Pacman/Assets/edible_ghost.png"), resizedEdible;
  private ImageIcon dead = new ImageIcon("Pacman/Assets/dead_ghost.png"), resizedDead;

  // Constructor
  public Ghost(String name, int x1, int x2, int y1, int y2, MapPanel map) {
    this.map = map;
    this.name = name;
    this.gLoc[0] = x1;
    this.gLoc[1] = x2;
    this.gLoc[2] = y1;
    this.gLoc[3] = y2;
    this.direction = 0;
    resizedBlue = resizeImage(blue);
    resizedRed = resizeImage(red);
    resizedPink = resizeImage(pink);
    resizedOrange = resizeImage(orange);
    resizedEdible = resizeImage(edible);
    resizedDead = resizeImage(dead);
  }

  // Draws the ghost
  public void draw(Graphics g, Ghost c) {
    if (!c.alive) {
      g.drawImage(resizedDead.getImage(), c.gLoc[0], c.gLoc[2], this);
    } else {
      if (map.getConsumptionMode()){
        g.drawImage(resizedEdible.getImage(), c.gLoc[0], c.gLoc[2], this);
      } else {
        if (c.name.equals("red")) {
          g.drawImage(resizedRed.getImage(), c.gLoc[0], c.gLoc[2], this);
        } else if (c.name.equals("blue")) {
          g.drawImage(resizedBlue.getImage(), c.gLoc[0], c.gLoc[2], this);
        } else if (c.name.equals("orange")) {
          g.drawImage(resizedOrange.getImage(), c.gLoc[0], c.gLoc[2], this);
        } else if (c.name.equals("pink")) {
          g.drawImage(resizedPink.getImage(), c.gLoc[0], c.gLoc[2], this);
        } else {
        }
      }
    }
   
  }

  // move method
  public void move() { // Logic for movement
    // check if at intersection and if not moves in direction
    if (!map.checkIntersectionCollision(gLoc)) {
      doMove();
    } else {
      // if at intersection, randomly chooses a direction to turn and move
      direction = RNJesus.nextInt(4);
      doMove();
    }
  }

  public void doMove() {
    // sets speed based on direction
    switch (direction) {
      case 0: // moves right
        speed[0] = 1;
        speed[1] = 0;
        break;
      case 1: // moves down
        speed[1] = 1;
        speed[0] = 0;
        break;
      case 2: // moves left
        speed[0] = -1;
        speed[1] = 0;
        break;
      case 3: // moves up
        speed[1] = -1;
        speed[0] = 0;
        break;
    }
    // checks if the ghost would be colliding with a wall after moving
    if (checkCollision()) {
      // if you wont collide with a wall, move
      gLoc[0] += speed[0];
      gLoc[1] += speed[0];
      gLoc[2] += speed[1];
      gLoc[3] += speed[1];
    } else {
      // if you would collide with a wall, change direction
      direction = RNJesus.nextInt(4);
    }
  }

  // checks if the ghost would be colliding with a wall after moving
  public boolean checkCollision() {
    return map.checkPathCollision(gLoc, speed);
  }

  // checks if the ghost is colliding witht he player
  public boolean checkPlayerCollision(int[] playerPosition, int pWidth) {
    // gets player coodinates
    int pX1 = playerPosition[0];
    int pX2 = playerPosition[1];
    int pY1 = playerPosition[2];
    int pY2 = playerPosition[3];
    // checks if the edges of the ghost are within the player coordinates and
    // returns true if they are
    if (pX1 + pWidth >= gLoc[0] && pX2 - pWidth <= gLoc[1] + width && pY1 >= gLoc[2] && pY2 <= gLoc[2] + width) {
      alive = false;
      return true;
    } else if (pX1 >= gLoc[0] && pX2 <= gLoc[1] + width && pY1 + pWidth >= gLoc[2] && pY2 - pWidth <= gLoc[2] + width) {
      alive = false;
      return true;
    } else {
      return false;
    }
  }

  // checks if the ghost is alive and if not, moves it to the ghost box and counts
  // down respawn timer
  public void ghostDeath() {
    if (alive == false) {
      gLoc[0] = 220;
      gLoc[1] = 240;
      gLoc[2] = 210;
      gLoc[3] = 230;
      deathTimer--;
      // if the timer is up, the ghost respawns
      if (deathTimer == 0) {
        direction = 0;
        alive = true;
        gLoc[0] = 11 * 20;
        gLoc[1] = 12 * 20;
        gLoc[2] = 180;
        gLoc[3] = 200;
        deathTimer = 500;
      }
    }
  }
  public ImageIcon resizeImage(ImageIcon img) {
    Image image = img.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    ImageIcon resized = new ImageIcon(image);
    return resized;
  }
}
