package Pacman.MainComponents;

import java.awt.Image;

import javax.swing.ImageIcon;

import Pacman.Panels.MapPanel;

public class GhostOrange extends Ghost {
    private ImageIcon orange = new ImageIcon("Pacman/Assets/Ghosts/orange_ghost.png"), resizedOrange;

    public GhostOrange(int x, int y, MapPanel map) {
        super(x, y, map);
        resizedOrange = resizeImage(orange);
    }

    public Image getImage() {
        return resizedOrange.getImage();
    }
}
