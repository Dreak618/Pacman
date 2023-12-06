package Pacman.Panels;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.FlowLayout;
import Pacman.MainComponents.Pacman;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

//creates a top score that will be displayed on the top of the screen
//will be updated as the game progresses

public class TopPanel extends JPanel implements ActionListener {
    private JLabel scoreBoard = new JLabel("Score: 0");
    private boolean consumptionMode = false;
    private JLabel consumptionModeLabel = new JLabel("       Power Pellet Mode: " + consumptionMode);
    private Pacman game;

    //initializes the top panel
    public TopPanel(Pacman game) {
        this.setLayout(new FlowLayout());
        // setBackground(Color.WHITE);
        this.game = game;
    }
    

    // will change the top panel state depending on the level
    public void setPanelState(int levelNumber) {
        if (levelNumber == 0) {
        add(new JLabel("PACMAN 2"));
        add(new JLabel("By Ben, Nick, Tyler, and Jason"));
        
        //creates button to start the game
        JButton startButton = new JButton("Push to Start");
        startButton.addActionListener(this);
        add(startButton);
        }
        if (levelNumber == 1) {
            removeAll();
            //if level is one has a label with the players score
            setBackground(Color.GREEN);
            add(scoreBoard);
            add(consumptionModeLabel);
        }

        //if level is two the background will be red
        if(levelNumber == 2){
            removeAll();
            setBackground(Color.RED);
        }
    }

    //updates the score
    public void setScore(int score) {
        this.scoreBoard.setText("Score: " + score);
    }
    //updates the consumption mode
    public void setConsumptionMode(boolean consumptionState) {
        this.consumptionModeLabel.setText("       Power Pellet Mode: " + consumptionState);
        if (consumptionState) {
            setBackground(Color.RED);
        } else {
            setBackground(Color.GREEN);
        }
    }
    public void actionPerformed(ActionEvent e) {
        game.setLevel(1);
    }
}
