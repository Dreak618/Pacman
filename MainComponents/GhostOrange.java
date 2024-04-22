package Pacman.MainComponents;

import java.awt.Image;

import javax.swing.ImageIcon;

import Pacman.Panels.MapPanel;

public class GhostOrange extends Ghost {
    private ImageIcon orange = new ImageIcon("Pacman/Assets/Ghosts/orange_ghost.png"), resizedOrange;

    public GhostOrange(int x1, int x2, int y1, int y2, MapPanel map) {
        super(x1, x2, y1, y2, map);
        resizedOrange = resizeImage(orange);
    }

    public Image getImage() {
        return resizedOrange.getImage();
    }
}
