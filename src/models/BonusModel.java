/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import bozels.MusicFader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

/**
 * The Bonus Model - only for extras
 * @author Bart Middag
 */
public class BonusModel extends Model {

    private final MainModel mainModel;
    private BufferedImage starsImage;
    private BufferedImage moonImage;
    private BufferedImage moonSwirlImage;
    private BufferedImage cloud1Image;
    private BufferedImage cloud2Image;
    private BufferedImage cloud3Image;
    private BufferedImage cloud4Image;
    private BufferedImage seaImage;
    private BufferedImage mount1Image;
    private BufferedImage mount2Image;
    private BufferedImage mount3Image;
    private BufferedImage shadowImage;
    private BufferedImage explosionImage;
    private BufferedImage wavesImage;
    private BufferedImage pirateShipImage;
    private BufferedImage catapultImage;
    private BufferedImage logoImage;
    private Clip[] sfxScreamArray;
    private final Random random;
    private Clip[] sfxJingleArray;
    private Clip[] sfxSoundsArray;
    private Clip[] sfxCollisionArray;
    private Clip[] ambArray;
    private Clip[] musArray;
    private FloatControl[] musControl;
    private double[] musGain;
    private boolean[] musFading;
    private FloatControl[] ambControl;
    private double[] ambGain;
    private boolean[] ambFading;

    /**
     * Create the Bonus Model
     * @param model The Main Model
     */
    public BonusModel(MainModel model) {
        this.mainModel = model;
        random = new Random();
        try {
            // Load images
            starsImage = ImageIO.read(this.getClass().getResource("/bozels/images/Stars.png"));
            moonImage = ImageIO.read(this.getClass().getResource("/bozels/images/Moon.png"));
            seaImage = ImageIO.read(this.getClass().getResource("/bozels/images/Sea.png"));
            mount1Image = ImageIO.read(this.getClass().getResource("/bozels/images/Mount1.png"));
            mount2Image = ImageIO.read(this.getClass().getResource("/bozels/images/Mount2.png"));
            mount3Image = ImageIO.read(this.getClass().getResource("/bozels/images/Mount3.png"));
            shadowImage = ImageIO.read(this.getClass().getResource("/bozels/images/BigBlackShadow.png"));
            moonSwirlImage = ImageIO.read(this.getClass().getResource("/bozels/images/MoonSwirl.png"));
            cloud1Image = ImageIO.read(this.getClass().getResource("/bozels/images/Cloud1.png"));
            cloud2Image = ImageIO.read(this.getClass().getResource("/bozels/images/Cloud2.png"));
            cloud3Image = ImageIO.read(this.getClass().getResource("/bozels/images/Cloud3.png"));
            cloud4Image = ImageIO.read(this.getClass().getResource("/bozels/images/Cloud4.png"));
            wavesImage = ImageIO.read(this.getClass().getResource("/bozels/images/Waves.png"));
            pirateShipImage = ImageIO.read(this.getClass().getResource("/bozels/images/PirateShip.png"));
            explosionImage = ImageIO.read(this.getClass().getResource("/bozels/images/Explosion.png"));
            catapultImage = ImageIO.read(this.getClass().getResource("/bozels/images/Catapult.png"));
            logoImage = ImageIO.read(this.getClass().getResource("/bozels/images/Logo.png"));

            // Load music
            musArray = new Clip[3];
            musArray[0] = AudioSystem.getClip();
            musArray[1] = AudioSystem.getClip();
            musArray[2] = AudioSystem.getClip();
            musArray[0].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/MusicMelody.wav")));
            musArray[1].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/MusicPiano.wav")));
            musArray[2].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/MusicDrums.wav")));

            // Load ambience
            ambArray = new Clip[2];
            ambArray[0] = AudioSystem.getClip();
            ambArray[1] = AudioSystem.getClip();
            ambArray[0].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/AmbienceCrickets.wav")));
            ambArray[1].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/AmbienceSea.wav")));

            // Load screams
            sfxScreamArray = new Clip[3];
            sfxScreamArray[0] = AudioSystem.getClip();
            sfxScreamArray[1] = AudioSystem.getClip();
            sfxScreamArray[2] = AudioSystem.getClip();
            sfxScreamArray[0].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/ScreamGirly.wav")));
            sfxScreamArray[1].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/ScreamWilhelm.wav")));
            sfxScreamArray[2].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/ScreamTarzan.wav")));

            // Load jingles
            sfxJingleArray = new Clip[2];
            sfxJingleArray[0] = AudioSystem.getClip();
            sfxJingleArray[1] = AudioSystem.getClip();
            sfxJingleArray[0].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/JingleVictory.wav")));
            sfxJingleArray[1].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/JingleGameOver.wav")));

            // Load sounds
            sfxSoundsArray = new Clip[2];
            sfxSoundsArray[0] = AudioSystem.getClip();
            sfxSoundsArray[1] = AudioSystem.getClip();
            sfxSoundsArray[0].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/SoundExplosion.wav")));
            sfxSoundsArray[1].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/SoundBoost.wav")));

            // Load collision sounds
            sfxCollisionArray = new Clip[4];
            sfxCollisionArray[0] = AudioSystem.getClip();
            sfxCollisionArray[1] = AudioSystem.getClip();
            sfxCollisionArray[2] = AudioSystem.getClip();
            sfxCollisionArray[3] = AudioSystem.getClip();
            sfxCollisionArray[0].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/CollisionConcrete.wav")));
            sfxCollisionArray[1].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/CollisionMetal.wav")));
            sfxCollisionArray[2].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/CollisionWood.wav")));
            sfxCollisionArray[3].open(AudioSystem.getAudioInputStream(MainModel.class.getResource("/bozels/audio/CollisionTarget.wav")));

            // Create volume control for music
            musControl = new FloatControl[3];
            musControl[0] = (FloatControl) musArray[0].getControl(FloatControl.Type.MASTER_GAIN);
            musControl[1] = (FloatControl) musArray[1].getControl(FloatControl.Type.MASTER_GAIN);
            musControl[2] = (FloatControl) musArray[2].getControl(FloatControl.Type.MASTER_GAIN);
            musGain = new double[3];
            musGain[0] = 0.0D;
            musGain[1] = 0.0D;
            musGain[2] = 0.0D;
            musControl[0].setValue(-80f);
            musControl[1].setValue(-80f);
            musControl[2].setValue(-80f);
            musFading = new boolean[3];
            musFading[0] = false;
            musFading[1] = false;
            musFading[2] = false;
            
            // Create volume control for ambience
            ambControl = new FloatControl[2];
            ambControl[0] = (FloatControl) ambArray[0].getControl(FloatControl.Type.MASTER_GAIN);
            ambControl[1] = (FloatControl) ambArray[1].getControl(FloatControl.Type.MASTER_GAIN);
            ambGain = new double[2];
            ambGain[0] = 0.0D;
            ambGain[1] = 0.0D;
            ambControl[0].setValue(-80f);
            ambControl[1].setValue(-80f);
            ambFading = new boolean[2];
            ambFading[0] = false;
            ambFading[1] = false;

            // Start music
            musArray[0].loop(-1);
            musArray[1].loop(-1);
            musArray[2].loop(-1);
            
            // Start ambience
            ambArray[0].loop(-1);
            ambArray[1].loop(-1);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(BonusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(BonusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BonusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Play a Bozel's scream.
     */
    public void playScream() {
        int thisClip = random.nextInt(3);
        sfxScreamArray[thisClip].setFramePosition(0);
        sfxScreamArray[thisClip].start();
    }

    /**
     * Play a jingle
     * @param jingle 0 for victory, 1 for loss
     */
    public void playJingle(int jingle) {
        sfxJingleArray[jingle].setFramePosition(0);
        sfxJingleArray[jingle].start();
    }

    /**
     * Play a sound
     * @param snd 0 for explosion, 1 for boost
     */
    public void playSound(int snd) {
        sfxSoundsArray[snd].setFramePosition(0);
        sfxSoundsArray[snd].start();
    }

    /**
     * Play a collision sound
     * @param snd 0 for concrete, 1 for metal, 2 for wood, 3 for target
     */
    public void playCollision(int snd) {
        if (!sfxCollisionArray[snd].isActive()) {
            sfxCollisionArray[snd].setFramePosition(0);
            sfxCollisionArray[snd].start();
        }
    }

    /**
     * Set the music volume
     * @param track 0 for melody, 1 for piano, 2 for drums
     * @param volume the desired volume, a double from 0D to 1D
     */
    public void setMusicVolume(int track, double volume) {
        if (!musFading[track]) {
            try {
                float currDB = (float) musGain[track];
                float targetDB = (float) volume;
                MusicFader fader = new MusicFader(track, currDB, targetDB, musControl[track], this, this.getClass().getMethod("setMusicFading", int.class, boolean.class));
                musGain[track] = volume;
                musFading[track] = true;
                Thread t = new Thread(fader);  // start a thread to fade volume
                t.start();
                musControl[track].setValue((float) (Math.log(musGain[track]) / Math.log(10.0) * 20.0));
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(BonusModel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(BonusModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Set ambience volume
     * @param track 0 for crickets, 1 for sea
     * @param volume the desired volume, a double from 0D to 1D
     */
    public void setAmbienceVolume(int track, double volume) {
        if (!ambFading[track]) {
            try {
                float currDB = (float) ambGain[track];
                float targetDB = (float) volume;
                MusicFader fader = new MusicFader(track, currDB, targetDB, ambControl[track], this, this.getClass().getMethod("setAmbienceFading", int.class, boolean.class));
                ambGain[track] = volume;
                ambFading[track] = true;
                Thread t = new Thread(fader);  // start a thread to fade volume
                t.start();
                ambControl[track].setValue((float) (Math.log(ambGain[track]) / Math.log(10.0) * 20.0));
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(BonusModel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(BonusModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Set the fading state of the music
     * @param track 0 for melody, 1 for piano, 2 for drums
     * @param fading Whether this track of music is fading
     */
    public void setMusicFading(int track, boolean fading) {
        musFading[track] = fading;
    }
    
    /**
     * Set the fading state of the ambience
     * @param track 0 for crickets, 1 for the sea
     * @param fading Whether this track of ambience is fading
     */
    public void setAmbienceFading(int track, boolean fading) {
        ambFading[track] = fading;
    }

    /**
     * Get the current music volume
     * @param track 0 for melody, 1 for piano, 2 for drums
     * @return the current music volume
     */
    public double getMusicVolume(int track) {
        return musGain[track];
    }
    
    /**
     * Get the current ambience volume
     * @param track 0 for crickets, 1 for the sea
     * @return the current ambience volume
     */
    public double getAmbienceVolume(int track) {
        return ambGain[track];
    }

    public BufferedImage getLogoImage() {
        return logoImage;
    }

    public BufferedImage getStarsImage() {
        return starsImage;
    }

    public BufferedImage getWavesImage() {
        return wavesImage;
    }

    public BufferedImage getPirateShipImage() {
        return pirateShipImage;
    }

    public BufferedImage getMoonImage() {
        return moonImage;
    }

    public BufferedImage getShadowImage() {
        return shadowImage;
    }

    public BufferedImage getExplosionImage() {
        return explosionImage;
    }

    public BufferedImage getCatapultImage() {
        return catapultImage;
    }

    public BufferedImage getMoonSwirlImage() {
        return moonSwirlImage;
    }

    public BufferedImage getMount1Image() {
        return mount1Image;
    }

    public BufferedImage getMount2Image() {
        return mount2Image;
    }

    public BufferedImage getMount3Image() {
        return mount3Image;
    }

    public BufferedImage getCloud1Image() {
        return cloud1Image;
    }

    public BufferedImage getCloud2Image() {
        return cloud2Image;
    }

    public BufferedImage getCloud3Image() {
        return cloud3Image;
    }

    public BufferedImage getCloud4Image() {
        return cloud4Image;
    }

    public BufferedImage getSeaImage() {
        return seaImage;
    }
}
