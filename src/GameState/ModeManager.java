package GameState;

import java.awt.Graphics2D;
import java.util.ArrayList;

import Audio.AudioPlayer;
import TileMap.Background;
import TileMap.TileMap;

public class ModeManager {
    private ArrayList<Mode> modes;
    private int currentMode;

    // static final values for modes the game can be in
	public static final int NORMAL_MODE = 0;	// player controlled game
	public static final int TRAINING_MODE = 1;	// AI training 
	public static final int AI_MODE = 2;		// AI playback

    public ModeManager(GameStateManager gsm, Background bg, TileMap tileMap, AudioPlayer music) {
        modes = new ArrayList<Mode>();
        currentMode = 0;
        modes.add(new NormalMode(gsm, bg, tileMap, music));
        modes.add(new TrainingMode(gsm, bg, tileMap, music));
    }

    public void setMode(int mode) {
        currentMode = mode;
    }

    public void init() {
        modes.get(currentMode).init();
    }

    public void update() {
        modes.get(currentMode).update();
    }

    public void draw(Graphics2D g) {
        modes.get(currentMode).draw(g);
    }

    public void keyPressed(int k) {
        modes.get(currentMode).keyPressed(k);
    }

    public void keyReleased(int k) {
        modes.get(currentMode).keyReleased(k);
    }

    public void playMusic() {
        modes.get(currentMode).playMusic();
    }

    public void resumeMusic() {
        modes.get(currentMode).resumeMusic();
    }

    public void stopMusic() {
        modes.get(currentMode).stopMusic();
    }
}
