package Pacman.MainComponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import Pacman.MapComponents.Path;
import Pacman.MapComponents.PointPellet;
import Pacman.Panels.EndPanel;
import Pacman.Panels.MapPanel;
import Pacman.Panels.StartScreen;
import Pacman.Panels.TopPanel;

public class Pacman extends JPanel implements KeyListener {
    private int score = 0;
    private Player player;
    protected Timer timer;// timer for the game
    private TopPanel top;
    private JPanel canvas;
    private JPanel displayPanel;
    private ArrayList<Path> paths = new ArrayList<>();
    private boolean consumptionMode = false;
    private int consumptionTime = 500; // length of time that consumption mode lasts
    private int consumptionTimer = consumptionTime;
    private MusicManager musicManager;
    private Pacman game = this;

    /**
     * Main method that sets up the frame and starts the game
     */
    public static void main(String[] args) {
        new Pacman();
    }

    /**
     * Constructor for the Pacman class:
     * Sets up the music manager, the timer, the key listener, the canvas, and the
     * top panel
     * 
     * Calls the setLevel method to start the game by setting the display panel to a
     * start screen
     * 
     * Canvas contains a top panel and a display panel
     * - Top panel contains the scoreboard and some buttons
     * - Display panel is either a start screen, map panel, or end panel
     */
    public Pacman() {
        JFrame f = new JFrame("Pacman 2");
        musicManager = new MusicManager();
        timer = new Timer(20, new TimerCallback());
        top = new TopPanel(this);
        canvas = new JPanel();
        canvas.setLayout(new BorderLayout());
        canvas.add(top, BorderLayout.NORTH);
        setLevel(0);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(500, 600));
        f.setResizable(false);
        f.pack();
        f.addKeyListener(this);
        f.setFocusable(true);
        f.add(canvas);
        f.setVisible(true);
    }

    // game timer
    private class TimerCallback implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            MapPanel mapPanel = (MapPanel) displayPanel;
            // Checks if startup music has been played and if not plays it
            if (!musicManager.playedStartup) {
                musicManager.playGameSounds();
            }

            // checks if the player is in consumption mode and if so counts down the timer
            if (consumptionMode) {
                consumptionTimer--;
                if (consumptionTimer == 0) {
                    consumptionMode = false;
                    top.setConsumptionMode(consumptionMode);
                }
            }

            // repaints the map
            displayPanel.repaint();

            // checks if player is colliding with a pellet and if so eats them
            pelletCollision();

            // does player movement();
            playerMovement();

            for (Ghost g : mapPanel.getGhosts()) {
                g.move();
            }
            mapPanel.ghostDeathStuff();
            // check if player collides with a ghost
            mapPanel.checkGhostCollision(game);
        }
    }

    // sets the display type of the display panel depending on the level
    public void setLevel(int level) {
        // removes the old display panel if there is one
        if (displayPanel != null) {
            canvas.remove(displayPanel);
        }
        // starts level based on level number
        switch (level) {
            case 0:
                startLevel0();
                break;
            case 1:
                startLevel1();
                break;
            case 2:
                startLevel2();
                break;
        }
        // adds display panel to canvas
        canvas.add(displayPanel, BorderLayout.CENTER);
        canvas.revalidate();
    }

    // starts level 0 including starting the music and making the display panel a
    // start screen

    private void startLevel0() {
        musicManager.playStartScreenSounds();

        // makes the display panel a new start screen
        displayPanel = new StartScreen();

        // sets top panel state to 0
        top.setPanelState(0);
    }

    // starts level 1 including generating the map, player, and ghosts as well as
    // starting the music
    private void startLevel1() {
        musicManager.playGameSounds();
        // makes the display panel a new map panel
        MapPanel mapPanel = new MapPanel(this);
        displayPanel = mapPanel;

        // sets top panel state to 1
        top.setPanelState(1);

        // gets the paths from the map panel, used in checking if player is on a path
        paths = mapPanel.getPaths();

        // makes a new player and adds it to the map panel
        player = new Player(160, 20, 20, 1);

        mapPanel.addPlayer(player);

        // makes new ghosts and adds them to the map panel
        mapPanel.createGhosts();

        // starts timer since it is now being used
        if (timer.isRunning() == false) {
            timer.start();
        }
    }

    // starts level 2 including making the display panel an end screen
    private void startLevel2() {
        timer.stop();
        musicManager.playDeathSounds();

        // makes the display panel a new end panel
        displayPanel = new EndPanel(this);

        // sets top panel state to 2
        top.setPanelState(2);
    }

    // detects if the player is on a path
    public boolean checkPathCollision(int[] entityPosision, int[] entitySpeed) {// gets coordinates of player and speed
        // sets the x and y coordinates to where they would be after moving
        int x1 = entityPosision[0] + entitySpeed[0];
        int x2 = entityPosision[1] + entitySpeed[0];
        int y1 = entityPosision[2] + entitySpeed[1];
        int y2 = entityPosision[3] + entitySpeed[1];

        // checks if after a player moves they will be on a path
        for (Path p : paths) {
            if (x1 >= p.getX1() && x2 <= p.getX2() && y1 >= p.getY1() && y2 <= p.getY2()) {
                // if player will be on a path after moving returns true
                return true;
            }
        }
        // if player wont be on a path returns false
        return false;
    }

    public boolean checkCollision(GameComponent c1, GameComponent c2) {
        int distance = c1.getRadius() + c2.getRadius();
        int dx = c1.getX() - c2.getX();
        int dy = c1.getY() - c2.getY();
        if (dx * dx + dy * dy < distance * distance) {
            return true;
        } else {
            return false;
        }
    }

    // checks if player is colliding with pellets
    public void pelletCollision() {
        ArrayList<PointPellet> pellets = ((MapPanel) displayPanel).getPellets();
        for (int i = 0; i < pellets.size(); i++) {
            PointPellet p = pellets.get(i);
            if (player.collision(p)) {
                p.consume(this);
                pellets.remove(i);
                top.setScore(score);
            }
        }
        if (pellets.size() == 0) {
            setLevel(1);
        }
    }

    public void toggleConsupmtion() {
        consumptionTimer = consumptionTime;
        consumptionMode = true;
        top.setConsumptionMode(consumptionMode);
    }

    public boolean getConsumptionMode() {
        return consumptionMode;
    }

    public int getScore() {
        return score;
    }

    public void updateScore() {
        top.setScore(score);
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Key handling
    public void keyPressed(KeyEvent e) {
        // Change direction at the next turn
        String direction = "";
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                direction = "up";
                break;
            case KeyEvent.VK_DOWN:
                direction = "down";
                break;
            case KeyEvent.VK_LEFT:
                direction = "left";
                break;
            case KeyEvent.VK_RIGHT:
            default:
                direction = "right";
        }
        player.setBufferDirection(direction);
    }

    // controls player movement
    public void playerMovement() {
        // gets the buffer direction and current direction
        String currentDirection = player.getDirection();
        String direction = player.getBufferDirection();

        // sets the direction to the buffer direction if it is not empty
        // and checks if the player can move in that direction
        // and if it can moves the player

        player.setDirection(direction);
        if (checkPathCollision(player.getCoordinates(), player.getSpeed()) && !direction.equals("")) {
            player.move();
            player.setBufferDirection("");
        } else {
            // if the player cannot move in the buffer direction it sets the direction to
            // the current direction
            // and checks if the player can move in that direction
            // and if it can moves the player
            player.setDirection(currentDirection);
            if (checkPathCollision(player.getCoordinates(), player.getSpeed())) {
                player.move();
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    } // not used

    public void keyReleased(KeyEvent e) {
    }// not used
}
