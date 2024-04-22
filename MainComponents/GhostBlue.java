package Pacman.MainComponents;

import java.awt.Image;

import javax.swing.ImageIcon;

import Pacman.Panels.MapPanel;

public class GhostBlue extends Ghost {
    private ImageIcon blue = new ImageIcon("Pacman/Assets/Ghosts/blue_ghost.png"), resizedBlue;

    public GhostBlue(int x, int y, MapPanel map) {
        super(x, y, map);
        resizedBlue = resizeImage(blue);
    }

    public Image getImage() {
        return resizedBlue.getImage();
    }
}
