package Pacman.MainComponents;

import javax.swing.JFrame;
import javax.swing.JPanel;
import Pacman.MapComponents.*;
import Pacman.Panels.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Pacman extends JPanel implements KeyListener {
    private int score = 0;
    private Player player;
    // timer for the game
    protected Timer timer = new Timer(20, new TimerCallback());;
    private TopPanel top;
    private JPanel canvas;
    private JPanel displayPanel;
    private ArrayList<Path> paths = new ArrayList<>();
    private Clip startSoundClip;
    private Clip deathSoundClip;
    private Clip chompSoundClip;
    private Clip startScreenMusic;
    private Clip gameScreenMusic;
    private Clip deathScreenMusic;
    private boolean isChompPlaying = false;
    private boolean consumptionMode = false;
    // length of time that consumption mode lasts
    private int consumptionTime = 500;
    private int consumptionTimer = consumptionTime;

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
        f.addKeyListener(this);
        f.setFocusable(true);

        // sets up the canvas the game is on
        canvas = new JPanel();
        canvas.setLayout(new BorderLayout());

        // sets up sounds
        loadSounds();
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                stopSounds();
            }
        });

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

            // checks if currently viewing the map panel
            if (displayPanel instanceof MapPanel) {

                // checks if the player is in consumption mode and if so counts down the timer
                if (consumptionMode) {
                    consumptionTimer--;
                    if (consumptionTimer == 0) {
                        consumptionMode = false;
                        consumptionTimer = consumptionTime;
                        top.setConsumptionMode(consumptionMode);
                    }
                }
                // plays game music if it is not already playing
                if (!startSoundClip.isRunning() && !gameScreenMusic.isRunning()) {
                    playGameMusic();
                }
                // repaints the map
                displayPanel.repaint();

                // checks if player is colliding with a pellet and if so eats them
                checkPelletCollision(player.getPlayerCoordinates());

                // does player movement();
                playerMovement();

                for(Ghost g : ((MapPanel) displayPanel).getGhosts()){
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
                        playDeathSound();
                        setLevel(2);
                    }
                }
            }
            // checks if currently viewing the end panel and if so stards the death music
            // and stops timer
            if (displayPanel instanceof EndPanel) {
                if (!deathSoundClip.isActive() && !deathScreenMusic.isActive()) {
                    playDeathMusic();
                    timer.stop();
                }
            }
        }
    }


    // sets the display type of the display panel depending on the level
    public void setLevel(int levelNumber) {
        // removes the old display panel if there is one
        if (displayPanel != null) {
            canvas.remove(displayPanel);
        }
        // starts level based on level number
        switch (levelNumber) {
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
        // starts starter music
        playStartMusic();

        // makes the display panel a new start screen
        displayPanel = new StartScreen();

        // sets top panel state to 0
        top.setPanelState(0);
    }

    // starts level 1 including generating the map, player, and ghosts as well as
    // starting the music
    private void startLevel1() {
        // makes the display panel a new map panel
        MapPanel mapPanel = new MapPanel(this);
        displayPanel = mapPanel;

        // starts and stops music
        stopStartMusic();
        stopDeathMusic();
        playStartSound();
        startChompSound();

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

    public boolean getConsumptionMode(){
        return consumptionMode;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private void loadSounds() {
        try {
            AudioInputStream startSound = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/pacman_beginning.wav"));
            startSoundClip = AudioSystem.getClip();
            startSoundClip.open(startSound);

            AudioInputStream deathSound = AudioSystem.getAudioInputStream(new File("Pacman/Assets/pacman_death.wav"));
            deathSoundClip = AudioSystem.getClip();
            deathSoundClip.open(deathSound);

            AudioInputStream chompSound = AudioSystem.getAudioInputStream(new File("Pacman/Assets/pacman_chomp.wav"));
            chompSoundClip = AudioSystem.getClip();
            chompSoundClip.open(chompSound);

            AudioInputStream startMusic = AudioSystem.getAudioInputStream(
                    new File("Pacman/Assets/02 Retro Platforming.wav"));
            startScreenMusic = AudioSystem.getClip();
            startScreenMusic.open(startMusic);

            AudioInputStream gameMusic = AudioSystem.getAudioInputStream(
                    new File("Pacman/Assets/03 A Bit Of Hope 1.wav"));
            gameScreenMusic = AudioSystem.getClip();
            gameScreenMusic.open(gameMusic);

            AudioInputStream gameOver = AudioSystem.getAudioInputStream(
                    new File("Pacman/Assets/Funny Bit.wav"));
            deathScreenMusic = AudioSystem.getClip();
            deathScreenMusic.open(gameOver);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playStartSound() {
        if (startSoundClip != null) {
            startSoundClip.setFramePosition(0);
            startSoundClip.start();
        }
    }

    private void playStartMusic() {
        if (startScreenMusic != null) {
            startScreenMusic.setFramePosition(0);
            startScreenMusic.loop(Clip.LOOP_CONTINUOUSLY);
            startScreenMusic.start();
        }
    }

    private void playGameMusic() {
        if (gameScreenMusic != null) {
            gameScreenMusic.setFramePosition(0);
            gameScreenMusic.loop(Clip.LOOP_CONTINUOUSLY);
            gameScreenMusic.start();
        }
    }

    private void playDeathMusic() {
        if (deathScreenMusic != null) {
            deathScreenMusic.setFramePosition(0);
            deathScreenMusic.loop(Clip.LOOP_CONTINUOUSLY);
            deathScreenMusic.start();
        }
    }

    private void playDeathSound() {
        if (deathSoundClip != null) {
            deathSoundClip.setFramePosition(0);
            deathSoundClip.start();
            // Stop the chomp sound and game music when Pacman dies
            stopChompSound();
            stopGameMusic();
        }
    }

    private void startChompSound() {
        if (chompSoundClip != null && !isChompPlaying) {
            chompSoundClip.loop(Clip.LOOP_CONTINUOUSLY);
            isChompPlaying = true;
        }
    }

    private void stopChompSound() {
        if (chompSoundClip != null) {
            chompSoundClip.stop();
            isChompPlaying = false;
        }
    }

    private void stopGameMusic() {
        if (gameScreenMusic != null) {
            gameScreenMusic.stop();
        }
    }

    private void stopStartMusic() {
        if (startScreenMusic != null) {
            startScreenMusic.stop();
        }
    }

    private void stopDeathMusic() {
        if (deathScreenMusic != null) {
            deathScreenMusic.stop();
        }
    }

    private void stopSounds() {
        if (startSoundClip != null) {
            startSoundClip.close();
        }
        if (startScreenMusic != null) {
            startScreenMusic.close();
        }
        if (deathSoundClip != null) {
            deathSoundClip.close();
        }
        if (gameScreenMusic != null) {
            startScreenMusic.close();
        }
        // Stop the chomp sound when stopping all sounds
        stopChompSound();
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
        //gets the buffer direction and current direction
        String currentDirection = player.getDirection();
        String direction = player.getBufferDirection();

        //sets the direction to the buffer direction if it is not empty
        //and checks if the player can move in that direction
        //and if it can moves the player

        player.setDirection(direction);
        if (checkPathCollision(player.getPlayerCoordinates(), player.getPlayerSpeed()) && !direction.equals("")) {
            player.move();
            player.setBufferDirection("");
        } else {
            //if the player cannot move in the buffer direction it sets the direction to the current direction
            //and checks if the player can move in that direction
            //and if it can moves the player
            player.setDirection(currentDirection);
            if (checkPathCollision(player.getPlayerCoordinates(), player.getPlayerSpeed())) {
                player.move();
            }
        }
    }

    public void keyTyped(KeyEvent e) {
        // Not needed
    }

    public void keyReleased(KeyEvent e) {
        // Not needed
    }
}
