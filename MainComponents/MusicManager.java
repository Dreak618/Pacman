package Pacman.MainComponents;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicManager {
    private Clip startupClip, deathClip, chompClip, startMusic, gameplayMusic, gameOverMusic;
    private Clip chompSoundClip;
    private Clip startScreenMusic;
    private Clip gameScreenMusic;
    private Clip deathScreenMusic;

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
            AudioInputStream startupStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/SoundEffects/pacman_beginning.wav"));
            AudioInputStream deathStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/SoundEffects/pacman_death.wav"));
            AudioInputStream chompStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/SoundEffects/pacman_chomp.wav"));
            AudioInputStream startStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/Music/Start.wav"));
            AudioInputStream gameplayStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/Music/Gameplay.wav"));
            AudioInputStream gameOverStream = AudioSystem
                    .getAudioInputStream(new File("Pacman/Assets/Music/GameOver.wav"));

            // Make an array of all the streams and clips
            AudioInputStream[] inputStreams = { startupStream, deathStream, chompStream, startStream, gameplayStream,
                    gameOverStream };
            Clip[] clips = { startupClip, deathClip, chompClip, startMusic, gameplayMusic, gameOverMusic };

            // Have clips open the corresponding streams
            for (int i = 0; i < clips.length; i++) {
                clips[i] = AudioSystem.getClip();
                clips[i].open(inputStreams[i]);
            }

            // Set the music clips and chomp to loop continuously
            startMusic.loop(Clip.LOOP_CONTINUOUSLY);
            gameplayMusic.loop(Clip.LOOP_CONTINUOUSLY);
            gameOverMusic.loop(Clip.LOOP_CONTINUOUSLY);
            chompSoundClip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    protected void playStartupSounds() {
        startupClip.start();
    }

    protected void stopStartupSounds() {
        startupClip.stop();
    }

    protected void playGameSounds() {
        chompClip.start();
        gameplayMusic.start();
    }

    protected void stopGameSounds() {
        chompClip.stop();
        gameplayMusic.stop();
    }

    protected void playDeathSounds() {
        deathClip.start();
        deathScreenMusic.start();
    }

    protected void stopDeathSounds() {
        deathClip.stop();
        deathScreenMusic.stop();
    }

    protected void playStartMusic() {
        // startScreenMusic.setFramePosition(0);
        startScreenMusic.start();
    }

    protected void playGameMusic() {
        if (gameScreenMusic != null) {
            gameScreenMusic.setFramePosition(0);
            gameScreenMusic.loop(Clip.LOOP_CONTINUOUSLY);
            gameScreenMusic.start();
        }
    }

    protected void playDeathMusic() {
        if (deathScreenMusic != null) {
            deathScreenMusic.setFramePosition(0);
            deathScreenMusic.loop(Clip.LOOP_CONTINUOUSLY);
            deathScreenMusic.start();
        }
    }

    protected void playDeathSound() {
        if (deathSoundClip != null) {
            deathSoundClip.setFramePosition(0);
            deathSoundClip.start();
            // Stop the chomp sound and game music when Pacman dies
            stopChompSound();
            stopGameMusic();
        }
    }

    protected void startChompSound() {
        if (chompSoundClip != null && !isChompPlaying) {
            chompSoundClip.loop(Clip.LOOP_CONTINUOUSLY);
            isChompPlaying = true;
        }
    }

    protected void stopChompSound() {
        if (chompSoundClip != null) {
            chompSoundClip.stop();
            isChompPlaying = false;
        }
    }

    protected void stopGameMusic() {
        if (gameScreenMusic != null) {
            gameScreenMusic.stop();
        }
    }

    protected void stopStartMusic() {
        if (startScreenMusic != null) {
            startScreenMusic.stop();
        }
    }

    protected void stopDeathMusic() {
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

}
