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
import Pacman.MapComponents.PowerPellet;
import Pacman.Panels.EndPanel;
import Pacman.Panels.MapPanel;
import Pacman.Panels.StartScreen;
import Pacman.Panels.TopPanel;

public class Pacman extends JPanel implements KeyListener {
    private int score = 0;
    private Player player;
    protected Timer timer = new Timer(20, new TimerCallback());// timer for the game
    private TopPanel top;
    private JPanel canvas;
    private JPanel displayPanel;
    private ArrayList<Path> paths = new ArrayList<>();
    private boolean consumptionMode = false;
    private int consumptionTime = 500; // length of time that consumption mode lasts
    private int consumptionTimer = consumptionTime;
    private MusicManager musicManager;

    public static void main(String[] args) {
        // creates new frame
        JFrame frame = new JFrame("Pacman 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 600));
        frame.setResizable(false);
        frame.pack();

        // creates new instance of the game
        new Pacman(frame);
        frame.setVisible(true);

    }

    public Pacman(JFrame f) {
        musicManager = new MusicManager();
        f.addKeyListener(this);
        f.setFocusable(true);

        // sets up the canvas the game is on
        canvas = new JPanel();
        canvas.setLayout(new BorderLayout());

        // creates and adds the top panel to the canvas which will include the
        // scoreboard
        top = new TopPanel(this);
        canvas.add(top, BorderLayout.NORTH);

        // makes a new display panel of a certain type depending on level and adds it to
        // the canvas
        setLevel(0);

        // adds canvas to frame
        f.add(canvas);

    }

    // game timer
    private class TimerCallback implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            MapPanel panel = (MapPanel) displayPanel;
            // checks if currently viewing the map panel

            if (!musicManager.playedStartup == false) {
                musicManager.playGameSounds();
            }
            // checks if the player is in consumption mode and if so counts down the timer
            if (consumptionMode) {
                consumptionTimer--;
                if (consumptionTimer == 0) {
                    consumptionMode = false;
                    consumptionTimer = consumptionTime;
                    top.setConsumptionMode(consumptionMode);
                }
            }
            // repaints the map
            displayPanel.repaint();

            // checks if player is colliding with a pellet and if so eats them
            checkPelletCollision(player.getPlayerCoordinates());

            // does player movement();
            playerMovement();

            for (Ghost g : ((MapPanel) displayPanel).getGhosts()) {
                g.move();
            }
            ((MapPanel) displayPanel).ghostDeathStuff();
            // check if player collides with a ghost
            if (((MapPanel) displayPanel).checkGhostCollision(player.getPlayerCoordinates())) {
                // if in consumption mode eats ghost otherwise dies
                if (consumptionMode) {
                    score += 200;
                    top.setScore(score);
                    // remove ghost or stuff NYI
                } else {
                    setLevel(2);
                }
            }
        }
    }

    public void level0Callback() {

    }

    public void level1Callback() {

    }

    public void level2Callback() {

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

    // checks if player is colliding with pellets
    public void checkPelletCollision(int[] playerPosition) {
        // and if so removes them and adds to
        // makes an arraylist of al pellets
        ArrayList<PointPellet> pellets = new ArrayList<>();
        pellets = ((MapPanel) displayPanel).getPellets();

        // gets the coordinates of the player
        int x1 = playerPosition[0];
        int x2 = playerPosition[1];
        int y1 = playerPosition[2];
        int y2 = playerPosition[3];
        int pW = x2 - x1;

        // checks if player is colliding with a pellet
        if (pellets.size() > 0) {
            for (PointPellet p : pellets) {
                int w = p.getWidth();
                // checks if the player is colliding with the pellet
                if ((x1 + pW >= p.getX1()
                        && x2 - pW <= p.getX2()
                        && y1 > p.getY1() - w
                        && y2 <= p.getY2() + w)
                        || (x1 >= p.getX1() - w
                                && x2 <= p.getX2() + w
                                && y1 + pW >= p.getY1()
                                && y2 - pW <= p.getY2())) {

                    // if the pellet is a power pellet sets consumption mode to true and increases
                    // score
                    if (p instanceof PowerPellet) {
                        score += 50;
                        consumptionMode = true;
                        top.setConsumptionMode(consumptionMode);
                    } else {// if the pellet is a point pellet increases score
                        score += 10;
                        top.setScore(score);
                    }

                    // removes the pellet from the map (player eats them)
                    ((MapPanel) displayPanel).removePellet(p);
                    break;
                }
            }
        } else {
            // if there are no pellets left goes to next level (adds new pellets to map)
            setLevel(1);
        }
    }

    public boolean getConsumptionMode() {
        return consumptionMode;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Key handling
    public void keyPressed(KeyEvent e) {
        // if(displayPanel instanceof MapPanel){
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
        if (checkPathCollision(player.getPlayerCoordinates(), player.getPlayerSpeed()) && !direction.equals("")) {
            player.move();
            player.setBufferDirection("");
        } else {
            // if the player cannot move in the buffer direction it sets the direction to
            // the current direction
            // and checks if the player can move in that direction
            // and if it can moves the player
            player.setDirection(currentDirection);
            if (checkPathCollision(player.getPlayerCoordinates(), player.getPlayerSpeed())) {
                player.move();
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    } // not used

    public void keyReleased(KeyEvent e) {
    }// not used
}
