package Engine;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {

	public static void play(String SP) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(SP).getAbsoluteFile());
		Clip clip = AudioSystem.getClip(); 
          
        // open audioInputStream to the clip 
        clip.open(audioInputStream); 
          
        clip.loop(Clip.LOOP_CONTINUOUSLY); 
	}
}