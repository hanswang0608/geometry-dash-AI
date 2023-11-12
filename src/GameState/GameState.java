package GameState;

import Audio.AudioPlayer;

//parent abstract class of all game states
public abstract class GameState {
	protected GameStateManager gsm;
	protected AudioPlayer music;

	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public void keyTyped(int k) {}

	public void playMusic() {
		if (music == null) return;
		music.play();
	}

	public void resumeMusic() {
		if (music == null) return;
		music.resume();
	}

	public void stopMusic() {
		if (music == null) return;
		music.stop();
	}
}
