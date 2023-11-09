package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

//state used when the player beats the level
public class WinState extends GameState{
	protected int currentChoice;
	protected String[] options = {"Play Again", "Quit"};
	protected Color titleColor;
	protected Font titleFont;
	protected Font font;
	
	public WinState(GameStateManager gsm) {
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
		g.drawString("YOU WON", 220, 150);
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.BLACK);
			}
			g.drawString(options[i], 250 + i * 100, 300);
		}
	}
	
	private void select() {
		if (currentChoice == 0) {
			gsm.beginState(gsm.getLastState());
		}
		if (currentChoice == 1) {
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
			gsm.beginState(GameStateManager.MENUSTATE);
		}
	}
	
	public void keyReleased(int k) {};
}
