package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Audio.AudioPlayer;

//state used when pausing the level
public class PauseState extends GameState{
	protected int currentChoice;
	protected String[] options = {"Restart", "Resume", "Quit"};
	protected Color titleColor;
	protected Font titleFont;
	protected Font font;
	
	public PauseState(GameStateManager gsm) {
		this.gsm = gsm;
		titleColor = new Color(128, 0, 0);
		titleFont = new Font("Century Gothic", Font.BOLD, 42);
		font = new Font("Arial", Font.PLAIN, 16);
		init();
	}
	
	public void init() {currentChoice = 1;}
	public void update() {}
	
	public void draw(Graphics2D g) {
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("PAUSED", 250, 150);
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.BLACK);
			}
			g.drawString(options[i], 200 + i * 100, 300);
		}
	}
	
	private void select() {
		if (currentChoice == 0) {
			gsm.beginState(GameStateManager.LEVEL1STATE);
		}
		if (currentChoice == 1) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if (currentChoice == 2) {
			gsm.beginState(GameStateManager.MENUSTATE);
		}
	}
	
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k == KeyEvent.VK_LEFT) {
			currentChoice--;
			if (currentChoice < 0) {
				currentChoice = options.length - 1;
			}
		}
		if (k == KeyEvent.VK_RIGHT) {
			currentChoice++;
			if (currentChoice > options.length - 1) {
				currentChoice = 0;
			}
		}
		if (k == KeyEvent.VK_ESCAPE) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
	}
	
	public void keyReleased(int k) {};
}
