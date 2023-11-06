package Entity;

import java.awt.image.BufferedImage;

//class to handle the animation of objects
public class Animation {
	private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay;				//delay set in the object to specify long in between each frame of the animation
	
	private boolean playedOnce;
	
	public Animation() {
		playedOnce = false;
	}
	
	public void setFrames (BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	public void	setDelay(long d) {delay = d;}
	public void setFrame(int f) {currentFrame = f;}
	
	public void update() {
		if(delay == -1) return; //if an object only has 1 frame, it does not need to be animated and can be set to -1 to avoid unnecessary updates
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if(elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if(currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}
	}
	
	public int getFrame() {return currentFrame;}
	public BufferedImage getImage() {return frames[currentFrame];}
	public boolean hasPlayedOnce() {return playedOnce;}
}

