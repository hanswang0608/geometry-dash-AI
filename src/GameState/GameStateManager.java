
package GameState;

import java.util.ArrayList;

import Audio.AudioPlayer;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GameStateManager {
	private ArrayList<GameState> gameStates;
	private int currentState;
	private AudioPlayer music;

	//static final values for the different states to be referred as
	public static final int MENUSTATE = 0;
	public static final int LEVELSELECTSTATE = 1;
	public static final int LEVEL1STATE = 2;
	public static final int PAUSESTATE = 3;
	public static final int WINSTATE = 4;
	public static final int INSTRUCTIONSTATE = 5;

	//store all gamestates in an arraylist, through which they can be switched to and updated
	public GameStateManager() {
		gameStates = new ArrayList<GameState>();
		music = new AudioPlayer("/Music/level1music.wav");
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
		gameStates.add(new LevelSelectState(this));
		gameStates.add(new Level1State(this));
		gameStates.add(new PauseState(this));
		gameStates.add(new WinState(this));
		gameStates.add(new InstructionState(this));
	}

	//set to state
	public void setState(int state) {
		currentState = state;
		if (currentState == WINSTATE) return;
		if (currentState == LEVEL1STATE) {
			music.resume();
		}
		else {
			music.stop();
		}
	}
		
	//set to and initialize state
	public void beginState(int state) {
		gameStates.get(state).init();
		currentState = state;
		if (currentState == LEVEL1STATE) {
			music.play();
		}
		else {
			music.stop();
		}
	}

	//update the currentState
	public void update() {
		gameStates.get(currentState).update();
	}

	public void draw(Graphics2D g) {
		gameStates.get(currentState).draw(g);
	}
	
	public AudioPlayer getMusic() {return music;}

	public void keyUpdate(boolean[] keys) {
		if (currentState == LEVEL1STATE) {
			if (keys[0]) {gameStates.get(currentState).keyPressed(KeyEvent.VK_UP);}
			else {gameStates.get(currentState).keyReleased(KeyEvent.VK_UP);}
		}
	}

	public void keyPressed(int k) {
		gameStates.get(currentState).keyPressed(k);
	}

	public void keyReleased(int k) {
		gameStates.get(currentState).keyReleased(k);
	}
}
