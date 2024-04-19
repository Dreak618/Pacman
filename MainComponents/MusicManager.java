package Pacman.MainComponents;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicManager {
    public boolean playedStartup = false;
    private boolean playingStartup = false;
    private Clip startupClip, deathClip, chompClip, startMusic, gameplayMusic, gameOverMusic;

    public MusicManager() {
        loadSounds();
    }

    /**
     * Loads all the sounds and music for the game
     * Creates a Clip object for each sound and music file
     * Opens the AudioInputStream for each sound and music file
     * Sets the music to loop continuously
     */
    protected void loadSounds() {
        try {
            // Get all AudioStreams
            AudioInputStream startupGameStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/SoundEffects/pacman_beginning.wav"));
            AudioInputStream deathStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/SoundEffects/pacman_death.wav"));
            AudioInputStream chompStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/SoundEffects/pacman_chomp.wav"));
            AudioInputStream startScreenStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/Music/Start.wav"));
            AudioInputStream gameplayStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/Music/Gameplay.wav"));
            AudioInputStream gameOverStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/Music/GameOver.wav"));

            // Make an array of all the streams and clips
            startupClip = AudioSystem.getClip();
            deathClip = AudioSystem.getClip();
            chompClip = AudioSystem.getClip();
            startMusic = AudioSystem.getClip();
            gameplayMusic = AudioSystem.getClip();
            gameOverMusic = AudioSystem.getClip();

            startupClip.open(startupGameStream);
            deathClip.open(deathStream);
            chompClip.open(chompStream);
            startMusic.open(startScreenStream);
            gameplayMusic.open(gameplayStream);
            gameOverMusic.open(gameOverStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    protected void playStartScreenSounds() {
        startMusic.start();
        startMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }

    protected void playGameSounds() {
        playStartup();
        if (!startupClip.isActive()) {
            chompClip.start();
            chompClip.loop(Clip.LOOP_CONTINUOUSLY);
            gameplayMusic.start();
            gameplayMusic.loop(Clip.LOOP_CONTINUOUSLY);
            playedStartup = true;
            playingStartup = false;
        }
    }

    public void playStartup() {
        if (!playingStartup) {
            stopSounds();
            startupClip.start();
            playingStartup = true;
        }
    }

    protected void playDeathSounds() {
        stopSounds();
        deathClip.start();
        gameOverMusic.start();
        gameOverMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }

    protected void stopSounds() {
        playedStartup = false;
        startMusic.stop();
        startupClip.stop();
        gameplayMusic.stop();
        chompClip.stop();
        deathClip.stop();
        gameOverMusic.stop();
    }
}
