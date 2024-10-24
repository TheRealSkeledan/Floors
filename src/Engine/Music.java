package Engine;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {
	private static Clip clip;
	private static String SP;
	private static AudioInputStream audioInputStream;
	private static int movement = 0;

	public Music(String SP) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.SP = SP;
		audioInputStream = AudioSystem.getAudioInputStream(new File(SP + ".wav").getAbsoluteFile());
		clip = AudioSystem.getClip();
	}

	public Music(String SP, boolean hasParts) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.SP = SP;

		if(hasParts) {
			audioInputStream = AudioSystem.getAudioInputStream(new File(SP + ".wav").getAbsoluteFile());
		} else {
			audioInputStream = AudioSystem.getAudioInputStream(new File(SP + movement + ".wav").getAbsoluteFile());
		}
			
		clip = AudioSystem.getClip();
	}

	// Plays continuously
	public static void play() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // open audioInputStream to the clip 
        clip.open(audioInputStream); 
          
        clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	// Plays it one time only, good for sound effects
	public static void playSFX() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		clip.open(audioInputStream);

		clip.start();
	}

	// Stops and plays the ending
	public static void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		clip.stop();

		audioInputStream = AudioSystem.getAudioInputStream(new File(SP + "End.wav").getAbsoluteFile());
		clip.open(audioInputStream);

		clip.start();
	}

	// Moves to the next movement
	public static void next() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		clip.stop();

		movement++;

		audioInputStream = AudioSystem.getAudioInputStream(new File(SP + movement + ".wav").getAbsoluteFile());
		clip.open(audioInputStream);

		clip.start();
	}
}