package Pacman.MainComponents;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

import Pacman.Panels.MapPanel;

public abstract class Ghost extends GameComponent {

  private Random RNJesus = new Random(4);
  protected MapPanel map;
  protected int[] gLoc = new int[4]; // ghost location coords
  protected int direction;
  protected int dx, dy;
  private int[] speed = new int[] { 0, 0 }; // ghost movement speed (can adjust)
  private boolean alive = true;
  private int deathTimer = 500;

  private ImageIcon edible = new ImageIcon("Pacman/Assets/Ghosts/edible_ghost.png"), resizedEdible;
  protected ImageIcon dead = new ImageIcon("Pacman/Assets/Ghosts/dead_ghost.png"), resizedDead;

  // Constructor
  public Ghost(int x, int y, MapPanel map) {
    this.radius = 10;
    this.map = map;
    this.x = x + radius;
    this.y = y + radius;
    this.direction = 0;

    resizedEdible = resizeImage(edible);
    resizedDead = resizeImage(dead);
  }

  // Draws the ghost
  public void draw(Graphics g) {
    if (!alive) {
      g.drawImage(resizedDead.getImage(), x - radius, y - radius, this);
    } else if (map.getConsumptionMode()) {
      g.drawImage(resizedEdible.getImage(), x - radius, y - radius, this);
    } else {
      g.drawImage(getImage(), x - radius, y - radius, this);
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
        dx = 1;
        dy = 0;
        speed[0] = 1;
        speed[1] = 0;
        break;
      case 1: // moves down
        dy = 1;
        dx = 0;
        speed[1] = 1;
        speed[0] = 0;
        break;
      case 2: // moves left
        dx = -1;
        dy = 0;
        speed[0] = -1;
        speed[1] = 0;
        break;
      case 3: // moves up
        dy = -1;
        dx = 0;
        speed[1] = -1;
        speed[0] = 0;
        break;
    }
    // checks if the ghost would be colliding with a wall after moving
    if (checkCollision()) {
      // if you wont collide with a wall, move
      x += dx;
      y += dy;
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
  public boolean checkPlayerCollision(Player p) {
    return (collision(p));
  }

  // checks if the ghost is alive and if not, moves it to the ghost box and counts
  // down respawn timer
  public void ghostDeath() {
    if (alive == false) {
      x = 220;
      y = 210;
      // gLoc[0] = 220;
      // gLoc[1] = 240;
      // gLoc[2] = 210;
      // gLoc[3] = 230;
      deathTimer--;
      // if the timer is up, the ghost respawns
      if (deathTimer == 0) {
        direction = 0;
        alive = true;
        x = 220;
        y = 180;
        // gLoc[0] = 11 * 20;
        // gLoc[1] = 12 * 20;
        // gLoc[2] = 180;
        // gLoc[3] = 200;
        deathTimer = 500;
      }
    }
  }

  public void death(Pacman p) {
    x = 220;
    y = 210;
    if (alive) {
      p.addScore(200);
      p.updateScore();
    }
    this.alive = false;
  }

  abstract Image getImage();
}
