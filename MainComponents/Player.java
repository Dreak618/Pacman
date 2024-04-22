package Pacman.MainComponents;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

//Player class
public class Player extends GameComponent {
    private ImageIcon pacRight = new ImageIcon("Pacman/Assets/Player/pacRight.png"), resizedRight;
    private ImageIcon pacLeft = new ImageIcon("Pacman/Assets/Player/pacLeft.png"), resizedLeft;
    private ImageIcon pacUp = new ImageIcon("Pacman/Assets/Player/pacUp.png"), resizedUp;
    private ImageIcon pacDown = new ImageIcon("Pacman/Assets/Player/pacDown.png"), resizedDown;
    private int x1, x2, y1, y2, speed, deltaX, deltaY, tWidth;
    private int dx, dy;
    private String direction = "";
    private String bufferDirection = "";

    public Player(int x, int y, int width, int speed) { // Defines starting parameters of character
        this.radius = width / 2;
        this.x = x + radius;
        this.y = y + radius;

        x1 = x;
        y1 = y;
        update();
        tWidth = width;
        this.speed = speed;
        dx = 0;
        dy = 0;
        deltaX = 0;
        deltaY = 0;
        resizedDown = resizeImage(pacDown);
        resizedUp = resizeImage(pacUp);
        resizedLeft = resizeImage(pacLeft);
        resizedRight = resizeImage(pacRight);
        move();
    }

    // gets the width of the player
    public int getWidth() {
        return tWidth;
    }

    // sets the direction
    public void setDirection(String direction) {
        this.direction = direction;
        updateSpeed(direction);
    }

    // gets the direction
    public String getDirection() {
        return direction;
    }

    // sets the buffer direction
    public void setBufferDirection(String direction) {
        this.bufferDirection = direction;
    }

    // gets the buffer direction
    public String getBufferDirection() {
        return bufferDirection;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    // updates speed based on direction
    public void updateSpeed(double direction) {
        switch (direction) {
            case 0.5:
                // System.out.println("up");
                deltaY = -speed;
                deltaX = 0;
                break;
            case 2.0:
                deltaY = speed;
                deltaX = 0;
                break;
            case 0.25:
                // System.out.println("left");
                deltaX = -speed;
                deltaY = 0;
                break;
            case 4.0:
                deltaX = speed;
                deltaY = 0;
                break;
            default:
                deltaX = 0;
                deltaY = 0;
        }
    }

    // moves player
    public void move() {
        // tempDeltaX = deltaX;
        // tempDeltaY = deltaY;
        dx = deltaX;
        dy = deltaY;
        x += dx;
        y += dy;
        // x1 += tempDeltaX;
        // y1 += tempDeltaY;
        // update();
        // move2();
    }

    public void move2() {
        x = x1 + radius;
        y = y1 + radius;
    }

    // updates player x2 and y2 coordinates
    public void update() {
        x2 = x1 + tWidth;
        y2 = y1 + tWidth;
    }

    public void undoMove() {
        x -= dx;
        y -= dy;
    }

    // gives player coordinates
    public int[] getCoordinates() {
        return new int[] { x1, x2, y1, y2 };
    }

    // gives player speed
    public int[] getSpeed() {
        return new int[] { dx, dy };
    }

    // draws player
    public void draw(Graphics g) {
        Image image;
        switch (direction) {
            case "up":
                image = resizedUp.getImage();
                break;
            case "down":
                image = resizedDown.getImage();
                break;
            case "left":
                image = resizedLeft.getImage();
                break;
            case "right":
                image = resizedRight.getImage();
                break;
            default:
                image = resizedRight.getImage();
        }
        g.drawImage(image, x - radius, y - radius, this);
    }
}
