package Pacman.MainComponents;

import java.awt.Image;

import javax.swing.ImageIcon;

import Pacman.Panels.MapPanel;

public class GhostRed extends Ghost {
    private ImageIcon red = new ImageIcon("Pacman/Assets/Ghosts/red_ghost.png"), resizedRed;

    public GhostRed(int x, int y, MapPanel map) {
        super(x, y, map);
        resizedRed = resizeImage(red);
    }

    public Image getImage() {
        return resizedRed.getImage();
    }
}
