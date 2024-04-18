package Pacman.Panels;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

//this is the first screen that the user sees
public class StartScreen extends JPanel {
    private ImageIcon pacRight = new ImageIcon("Pacman/Assets/Player/pacRight.png");

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(pacRight.getImage(), 0, 0, this);
    }
}
