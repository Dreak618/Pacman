package Pacman.Panels;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Pacman.MainComponents.Pacman;
import java.awt.Font;
import java.awt.Color;

public class EndPanel extends JPanel implements ActionListener {
    private Pacman pacman;
    private String spaces = "                                ";
    private JLabel scoreLabel;
    private JPanel scoreBoard = new JPanel();
    private static int topScore = 0;
    private JLabel topScoreLabel = new JLabel(spaces + "Top Score: " + topScore + spaces);

    //this is the panel that will be displayed when the player dies
    public EndPanel(Pacman pacman) {
        this.pacman = pacman;

        // sets the layout of the panel
        setLayout(new BorderLayout());
        setBackground(Color.RED);

        // creates label for when the player dies
        JLabel deathLabel = new JLabel("☹ YOU DIED ☹ ");
        deathLabel.setFont(new Font("Comic Sans MS", Font.ITALIC, 24));
        deathLabel.setHorizontalAlignment(JLabel.CENTER);
        add(deathLabel, BorderLayout.NORTH);

        // creates button to start the game
        JButton startButton = new JButton("Push to Restart");
        startButton.addActionListener(this);
        add(startButton, BorderLayout.SOUTH);

        // updates the score board and adds it to the panel
        updateScoreBoard(pacman.getScore());
        add(scoreBoard, BorderLayout.CENTER);
    }

    public void updateScoreBoard(int score) {
        //creates label with your score
        scoreLabel = new JLabel(spaces + "Your score was: " + pacman.getScore() + spaces);
        scoreLabel.setVerticalAlignment(JLabel.CENTER);

        //updates high score
        if (score > topScore) {
            topScoreLabel.setText(spaces + "High Score is: " + score + spaces);
            topScore = score;
        }

        //adds text to score board
        scoreBoard.setBackground(Color.RED);
        scoreBoard.add(scoreLabel);
        scoreBoard.add(topScoreLabel);
    }

    public void actionPerformed(ActionEvent e) {
        //clears score of game so it can be played again
        pacman.setScore(0);

        // starts the game
        pacman.setLevel(1);
    }
}
