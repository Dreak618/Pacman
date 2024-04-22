package Pacman.MainComponents;

import java.awt.Image;

import javax.swing.ImageIcon;

import Pacman.Panels.MapPanel;

public class GhostPink extends Ghost {
    private ImageIcon pink = new ImageIcon("Pacman/Assets/Ghosts/pink_ghost.png"), resizedPink;

    public GhostPink(int x, int y, MapPanel map) {
        super(x, y, map);
        resizedPink = resizeImage(pink);
    }

    public Image getImage() {
        return resizedPink.getImage();
    }
}
