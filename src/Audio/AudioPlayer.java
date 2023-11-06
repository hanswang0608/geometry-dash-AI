package Audio;

import javax.sound.sampled.*;

//audio player class
public class AudioPlayer {
	
	private Clip clip;		//data type that stores audio
	private int pausePosition;
	
	public AudioPlayer(String s) {
		try {
			//sourcing and decoding audio file
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(s));
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_UNSIGNED, 
					baseFormat.getSampleRate(), 
					16, 
					baseFormat.getChannels(), 
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(),
					false
					);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
			pausePosition = 0;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		if (clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}
	
	public void resume() {
		if (clip == null) return;
		stop();
		clip.setFramePosition(pausePosition);
		clip.start();
	}
	
	public void stop() {
		if (clip.isRunning()) {
			pausePosition = clip.getFramePosition();
			clip.stop();
		}
	}
	
	public void setZero() {
		if (clip == null) return;
		clip.setFramePosition(0);
		pausePosition = 0;
	}
	
	public void close() {
		stop();
		clip.close();
	}
}
