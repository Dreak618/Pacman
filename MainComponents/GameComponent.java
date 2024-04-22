package Pacman.MainComponents;

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
}
