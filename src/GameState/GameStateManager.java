
package GameState;

import java.util.ArrayList;
import java.util.Stack;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GameStateManager {
	private ArrayList<GameState> gameStates;
	private int currentState;
	private Stack<Integer> prevStates;

	
	private ArrayList<LevelState> levelStates;
	private int mode;

	//static final values for the different states to be referred as
	public static final int MENUSTATE = 0;
	public static final int LEVELSELECTSTATE = 1;
	public static final int LEVEL1STATE = 2;
	public static final int PAUSESTATE = 3;
	public static final int WINSTATE = 4;
	public static final int INSTRUCTIONSTATE = 5;
	public static final int TRAINING_LEVEL_STATE = 6;

	//store all gamestates in an arraylist, through which they can be switched to and updated
	public GameStateManager() {
		gameStates = new ArrayList<GameState>();
		prevStates = new Stack<Integer>();
		levelStates = new ArrayList<LevelState>();

		currentState = MENUSTATE;
		mode = ModeManager.NORMAL_MODE;
		gameStates.add(new MenuState(this));
		gameStates.add(new LevelSelectState(this));
		gameStates.add(new Level1State(this));
		gameStates.add(new PauseState(this));
		gameStates.add(new WinState(this));
		gameStates.add(new InstructionState(this));
		gameStates.add(new TrainingLevelState(this));

		levelStates.add((LevelState)gameStates.get(LEVEL1STATE));
		levelStates.add((LevelState)gameStates.get(TRAINING_LEVEL_STATE));
	}

	//set to state
	public void setState(int state) {
		gameStates.get(currentState).stopMusic();
		prevStates.push(currentState);
		currentState = state;
		gameStates.get(currentState).resumeMusic();
	}
		
	//set to and initialize state
	public void beginState(int state) {
		gameStates.get(state).init();
		gameStates.get(currentState).stopMusic();
		prevStates.push(currentState);
		gameStates.get(state).playMusic();
		currentState = state;
	}

	public int getLastState() {
		return prevStates.pop();
	}

	public int getMode() { return this.mode; }
	
	public void setMode(int mode) { 
		this.mode = mode; 
		for (LevelState level : levelStates) {
			level.setMode(mode);
		}
	}

	public void cycleMode() { 
		setMode((mode + 1) % 3);
	}

	//update the currentState
	public void update() {
		gameStates.get(currentState).update();
	}

	public void draw(Graphics2D g) {
		gameStates.get(currentState).draw(g);
	}
	
	public void keyUpdate(boolean[] keys) {
		if (currentState == LEVEL1STATE || currentState == TRAINING_LEVEL_STATE) {
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
