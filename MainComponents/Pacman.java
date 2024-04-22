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

import Pacman.MapComponents.Intersection;
import Pacman.MapComponents.Path;
import Pacman.MapComponents.PointPellet;
import Pacman.Panels.EndPanel;
import Pacman.Panels.MapPanel;
import Pacman.Panels.StartScreen;
import Pacman.Panels.TopPanel;

// TODO Make pellets game components but not map components so I can use one collision thing for all map components and all game components
public class Pacman extends JPanel implements KeyListener {
    private int score = 0;
    private Player player;
    protected Timer timer;// timer for the game
    private TopPanel top;
    private JPanel canvas;
    private JPanel displayPanel;
    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<Intersection> intersections = new ArrayList<>();
    private boolean consumptionMode = false;
    private int consumptionTime = 500; // length of time that consumption mode lasts
    private int consumptionTimer = consumptionTime;
    private MusicManager musicManager;
    private Pacman game = this;
    private double bufferDirection = 0.0;
    private double direction = 0.0;

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
            displayPanel.repaint();

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

            MapPanel mapPanel = (MapPanel) displayPanel;

            playerMovement();

            for (Ghost g : mapPanel.getGhosts()) {
                g.move(game);
            }
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

    /**
     * starts level 0 including starting the music and making the display panel a
     * start screen
     */
    private void startLevel0() {
        musicManager.playStartScreenSounds();// plays the start screen music
        displayPanel = new StartScreen();// makes the display panel a new start screen
        top.setPanelState(0); // sets top panel state to 0
    }

    /**
     * starts level 1 including generating the map, player, and ghosts as well as
     * starting the music
     */
    private void startLevel1() {
        musicManager.playGameSounds();

        // makes the display panel a new map panel
        MapPanel mapPanel = new MapPanel(this);
        displayPanel = mapPanel;

        top.setPanelState(1); // sets top panel state to 1

        // gets the paths from the map panel, used in checking if player is on a path
        paths = mapPanel.getPaths();
        intersections = mapPanel.getIntersections();

        // makes a new player and adds it to the map panel
        player = new Player(160, 20, 20, 1);
        mapPanel.addPlayer(player);

        // starts timer since it is now being used
        if (timer.isRunning() == false) {
            timer.start();
        }
    }

    // starts level 2 including making the display panel an end screen
    private void startLevel2() {
        timer.stop();
        musicManager.playDeathSounds(); // plays the death music
        displayPanel = new EndPanel(this); // makes the display panel a new end panel
        top.setPanelState(2); // sets top panel state to 2
    }

    // detects if the player is on a path
    public boolean pathCollision(GameComponent c) {
        // checks if after a player moves they will be on a path
        for (Path p : paths) {
            if (p.collision(c)) {
                return true;
            }
        }
        // if player wont be on a path returns false
        return false;
    }

    public boolean intersectionCollision(GameComponent c) {
        for (Intersection i : intersections) {
            if (i.collision(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if the player is colliding with any pellets and if so consumes them
     */
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

    // controls player movement
    public void playerMovement() {
        pelletCollision();
        double currentDirection = direction;
        if (intersectionCollision(player)) {
            player.updateSpeed(bufferDirection);
            player.move();
            if (pathCheck(player) == false) {
                player.undoMove();
                player.updateSpeed(currentDirection);
            }
        } else {
            if (bufferDirection)
                player.move();
        }

    }

    public boolean pathCheck(GameComponent c) {
        GameComponent temp = new GameComponent();
        int dx = player.getDx();
        int dy = player.getDy();

        if (dx == 0)
            dx = 1;
        if (dy == 0)
            dy = 1;

        temp.setX(c.getX() + 2 * c.getRadius() * player.getDx());
        temp.setY(c.getY() + 2 * c.getRadius() * player.getDy());
        return pathCollision(temp);
    }

    // Key handling
    public void keyPressed(KeyEvent e) {
        // Change direction at the next turn
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                bufferDirection = 0.5;
                break;
            case KeyEvent.VK_DOWN:
                bufferDirection = 2;
                break;
            case KeyEvent.VK_LEFT:
                bufferDirection = 0.25;
                break;
            case KeyEvent.VK_RIGHT:
            default:
                bufferDirection = 4;
        }

    }

    public void keyTyped(KeyEvent e) {
    } // not used

    public void keyReleased(KeyEvent e) {
    }// not used

    public Player getPlayer() {
        return player;
    }
}
