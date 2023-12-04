package Pacman.MainComponents;
import java.awt.*;
import javax.swing.*;

//Player class
public class Player extends JPanel {
    private ImageIcon pacRight = new ImageIcon("Pacman/Assets/pacRight.png"), resizedRight;
    private ImageIcon pacLeft = new ImageIcon("Pacman/Assets/pacLeft.png"), resizedLeft;
    private ImageIcon pacUp = new ImageIcon("Pacman/Assets/pacUp.png"), resizedUp;
    private ImageIcon pacDown = new ImageIcon("Pacman/Assets/pacDown.png"), resizedDown;  
    private int x1, x2, y1, y2, speed, deltaX, deltaY, tWidth, tempDeltaX, tempDeltaY;
    private String direction = "";
    private String bufferDirection = "";

    public Player(int x, int y, int width, int speed) { // Defines starting parameters of character
        x1 = x;
        y1 = y;
        update();
        tWidth = width;
        this.speed = speed;
        deltaX = 0;
        deltaY = 0;
        resizedDown = resizeImage(pacDown);
        resizedUp = resizeImage(pacUp);
        resizedLeft = resizeImage(pacLeft);
        resizedRight = resizeImage(pacRight);
        move();
    }

    //gets the width of the player
    public int getWidth() {
        return tWidth;
    }

    //sets the direction
    public void setDirection(String direction) {
        this.direction = direction;
        updateSpeed(direction);
    }

    //gets the direction
    public String getDirection() {
        return direction;
    }

// sets the buffer direction
    public void setBufferDirection(String direction) {
        this.bufferDirection = direction;
    }

    //gets the buffer direction
    public String getBufferDirection() {
        return bufferDirection;
    }

    //updates speed based on direction
    public void updateSpeed(String direction) {
        switch (direction) {
            case "up":
                // System.out.println("up");
                deltaY = -speed;
                deltaX = 0;
                break;
            case "down":
                deltaY = speed;
                deltaX = 0;
                break;
            case "left":
                // System.out.println("left");
                deltaX = -speed;
                deltaY = 0;
                break;
            case "right":
                deltaX = speed;
                deltaY = 0;
                break;
            default:
                deltaX = 0;
                deltaY = 0;
        }
    }

    //moves player
    public void move() {
        tempDeltaX = deltaX;
        tempDeltaY = deltaY;
        x1 += tempDeltaX;
        y1 += tempDeltaY;
        update();
    }

    //updates player x2 and y2 coordinates
    public void update() {
        x2 = x1 + tWidth;
        y2 = y1 + tWidth;
    }

    //gives player coordinates
    public int[] getPlayerCoordinates() {
        return new int[] { x1, x2, y1, y2 };
    }

    //gives player speed
    public int[] getPlayerSpeed() {
        return new int[] { deltaX, deltaY };
    }

    //resizes image
    public ImageIcon resizeImage(ImageIcon img) {
        Image image = img.getImage().getScaledInstance(tWidth, tWidth, Image.SCALE_SMOOTH);
        ImageIcon resized = new ImageIcon(image);
        return resized;
    }
    


    //draws player
    public void draw(Graphics g) {
        if(resizedRight == null) {
            System.out.println("null");
        }
        else{
         switch (direction) {
            case "up":
                g.drawImage(resizedUp.getImage(), x1, y1, this);
                break;
            case "down":
                g.drawImage(resizedDown.getImage(), x1, y1, this);
                break;
            case "left":
                g.drawImage(resizedLeft.getImage(), x1, y1, this);
                break;
            case "right":
                g.drawImage(resizedRight.getImage(), x1, y1, this);
                break;
            default:
                g.drawImage(resizedRight.getImage(), x1, y1, this);
        }
    }
       
    }
}