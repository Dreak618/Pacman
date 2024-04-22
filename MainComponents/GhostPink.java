package Pacman.MainComponents;

import java.awt.Image;

import javax.swing.ImageIcon;

import Pacman.Panels.MapPanel;

public class GhostPink extends Ghost {
    private ImageIcon pink = new ImageIcon("Pacman/Assets/Ghosts/pink_ghost.png"), resizedPink;

    public GhostPink(int x1, int x2, int y1, int y2, MapPanel map) {
        super(x1, x2, y1, y2, map);
        resizedPink = resizeImage(pink);
    }

    public Image getImage() {
        return resizedPink.getImage();
    }
}
