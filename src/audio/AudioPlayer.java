package audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple player that plays audio files
 */
public class AudioPlayer {
    /** used to play the audio */
   private final Clip clip;

    /** used to load the audio */
    private final AudioInputStream audioInputStream;

    /**
     * Initialises the player with a path to an audio-file.
     * <p>
     * The audio may sometimes not be played when repeatedly playing the clip
     * (this is caused by the buffer-size being too large for the interval in which the audio is to be played).
     * <p>
     * Use {@link #AudioPlayer(String, int)} if the audio should be cut off instead.
     * @param filePath Path to the audio file which is to be played (as String).
     */
    public AudioPlayer(String filePath) {
        try {

            //create AudioInputStream
             audioInputStream =
                    AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(filePath)));


            clip = AudioSystem.getClip();

            // open the clip (acquire the resources needed)
            clip.open(audioInputStream);

        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialises the player with a path to an audio-file
     * and the timeWindow in which the sound is to be played.
     * The timeWindow is used to calculate the buffer-size, which is needed to play the clip.
     * <p>
     * The audio file is cut of, if it is too long to play in the given time-window.
     * <p>
     * According to <a href="https://stackoverflow.com/questions/45179495/audio-clip-occasionally-not-playing">...</a>:
     * <ul>
     * <li> buffer size too large -> sometimes sounds are not played when repeatedly playing the clip.
     * <li> buffer size too small -> artifacts like clicking or tearing.
     * </ul>
     * @param filePath Path to the audio file which is to be played (as String).
     * @param timeWindow time in which the sound is to be played [in ms] ( for example: time between 2 frames in the game).
     */
    public AudioPlayer(String filePath, int timeWindow) {
        try {

            //create AudioInputStream
            audioInputStream =
                    AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(filePath)));


            clip = AudioSystem.getClip();

            //calculate needed buffer size
            float frameSize = audioInputStream.getFormat().getFrameSize();
            float fr = audioInputStream.getFormat().getFrameRate();

            //frames already consider multiple channels (in comparison to samples)
            float bytesPerSecond = fr
                    * frameSize;

            //game frameRate = 1/gameUpdateTime[s]
            float fps = 1 / (timeWindow / 1000.f);

            //calculate the bytes that are needed for each frame
            int bufferSize = (int) (bytesPerSecond / fps);
            // but somehow buffer is still too large and causes problems
            bufferSize = bufferSize * 3 / 4;


            //buffer does not need to be and should not be larger than the byteLength of the whole file
            byte[] audioByteArray = audioInputStream.readAllBytes();
            //System.out.println(filePath+ ": " + audioByteArray.length);
            //System.out.println("audiolength of " + filePath+ ": " + audioByteArray.length / bytesPerSecond );
            if (audioByteArray.length < bufferSize) {
                bufferSize = audioByteArray.length;
            }

            //System.out.println(filePath+ ": " + bufferSize);
            clip.open(audioInputStream.getFormat(), audioByteArray, 0, bufferSize);

        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * plays the audio file from the start.
     * <p>
     * if this method is called while the clip is already playing,
     * the old playback is stopped and the new playback starts.
     */
    public void play()
    {
        //stop and reset to the start position (frame 0) if the sound was already played
        clip.stop();
        //clip.flush();
        clip.setFramePosition(0);
        //play the audio
        clip.start();
    }
    /*
    public void playAndWait()
    {
        clip.stop();
        //clip.flush();
        clip.setFramePosition(0);
        clip.start();
        try {
            Thread.sleep(clip.getMicrosecondLength()/1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    */

    /**
     * plays the audio file from the start and then frees the resources.
     */
    public void playAndClose()
    {
        //play the audio
        clip.start();

        Timer closeTimer = new Timer();
        TimerTask closeTask= new TimerTask(){
            @Override
            public void run(){
                AudioPlayer.this.close();
                //System.out.println(Thread.currentThread());
                //make sure the Timer Thread terminates after the task was executed
                closeTimer.cancel();
            }
        };

        //schedule the task to free the resources, after the audio was completely played.
        //using clip.close() without an extra delay somehow freezes the other threads calling clip.open(),
        //therefore we add an extra delay on top of the clip duration
        //maybe find a better solution later
        closeTimer.schedule(closeTask, clip.getMicrosecondLength()/1000 + 500);
        //closeTimer.schedule(closeTask, clip.getMicrosecondLength()/1000);
    }



    /**
     * sets the volume of the audio.
     * @param volume value from 0 to 1, where 1 is full volume and 0 is no sound.
     */
    public void setVolume(float volume) {
        //copied from here "https://stackoverflow.com/questions/40514910/set-volume-of-java-clip"
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        //"MASTER_GAIN FloatControl value is in decibels, meaning it's a logarithmic scale, not a linear one"
        // https://stackoverflow.com/questions/40514910/set-volume-of-java-clip
        //to get a linear scale, we convert it.
        //0 dB gain for volume=1, and -∞ db gain for volume = 0
        //gainControl.setValue(20f * (float) Math.log10(volume));
        // for human hearing ~10 DB steps double/half perceived audio volume
        gainControl.setValue(10 * (float) (Math.log10(volume) / Math.log10(2)));
    }

    /*
    public void setSampleRate(float volume) {

        //does not work, apparently FloatControl.Type.SAMPLE_RATE not supported
        //FloatControl srControl = (FloatControl) clip.getControl(FloatControl.Type.SAMPLE_RATE);
        //srControl.setValue(srControl.getValue()*4);
        //but adjusting volume works
        //FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        //gainControl.setValue(20f * (float) Math.log10(0.05));
        //also not supported:
        //FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
    }
    */
    /**
     * frees the resources used.
     */
    public void close(){
        /*
        if(clip.isActive()){
            clip.stop();
        }
        */
        clip.close();

        try {
            audioInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
