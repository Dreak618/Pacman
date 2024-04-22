package Pacman.MainComponents;

import java.awt.Image;

import javax.swing.ImageIcon;

import Pacman.Panels.MapPanel;

public class GhostBlue extends Ghost {
    private ImageIcon blue = new ImageIcon("Pacman/Assets/Ghosts/blue_ghost.png"), resizedBlue;

    public GhostBlue(int x1, int x2, int y1, int y2, MapPanel map) {
        super(x1, x2, y1, y2, map);
        resizedBlue = resizeImage(blue);
    }

    public Image getImage() {
        return resizedBlue.getImage();
    }
}
