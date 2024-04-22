package Pacman.MapComponents;

import java.awt.Color;
import java.awt.Graphics;

import Pacman.MainComponents.Pacman;

/**
 * Power pellets are the larger pellets that allow pacman to eat ghosts
 * they have different dimensions than regular pellets
 * this class is a subclass of PointPellet that changes the dimensions of the
 * pellet
 */
public class PowerPellet extends PointPellet {
    // constructor for the power pellet
    public PowerPellet(int x, int y) {
        super(x, y);
        this.width = unitWidth / 4 * 3;
        this.x1 -= width / 4;
        this.y1 -= width / 4;
        this.x2 = x1 + width;
        this.y2 = y1 + width;
        this.radius = width / 2;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x1, y1, width, width);
    }

    @Override
    public int consume(Pacman p) {
        p.toggleConsupmtion();
        return 50;
    }
}
