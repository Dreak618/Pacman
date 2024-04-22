package Pacman.MainComponents;

import java.awt.Image;

import javax.swing.ImageIcon;

import Pacman.Panels.MapPanel;

public class GhostRed extends Ghost {
    private ImageIcon red = new ImageIcon("Pacman/Assets/Ghosts/red_ghost.png"), resizedRed;

    public GhostRed(int x1, int x2, int y1, int y2, MapPanel map) {
        super(x1, x2, y1, y2, map);
        resizedRed = resizeImage(red);
    }

    public Image getImage() {
        return resizedRed.getImage();
    }
}
