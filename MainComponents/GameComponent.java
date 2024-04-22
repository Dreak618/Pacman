package Pacman.MainComponents;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class GameComponent extends JComponent {
    protected int x, y, radius;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public boolean collision(GameComponent other) {
        int distance = this.radius + other.getRadius();
        int dx = this.x - other.getX();
        int dy = this.y - other.getY();
        if (dx * dx + dy * dy < distance * distance) {
            return true;
        } else {
            return false;
        }
    }

    // resizes image
    public ImageIcon resizeImage(ImageIcon img) {
        Image image = img.getImage().getScaledInstance(2 * radius, 2 * radius, Image.SCALE_SMOOTH);
        ImageIcon resized = new ImageIcon(image);
        return resized;
    }
}
