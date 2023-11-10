package GameState;
import java.awt.*;
import java.awt.event.KeyEvent;

import Main.Game;
import Main.GamePanel;
import TileMap.Background;

//state used for the main menu
public class MenuState extends GameState{
	private Background bg;
	private int currentChoice;
	private Color titleColor;
	private Font titleFont;
	private Font font;
	private Font selectedFont;


	private static final String[] options = {"Play", "Instructions", "Quit"};
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		try {
			bg = new Background("/Backgrounds/menu.jpg", 1);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.BOLD, 42);
			font = new Font("Arial", Font.PLAIN, 16);
			selectedFont = new Font("Arial", Font.BOLD, 20);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() {currentChoice = 0;}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		bg.draw(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString(Game.APP_NAME, 210, 150);
		for(int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setFont(selectedFont);
				g.setColor(Color.BLACK);
			}
			else {
				g.setFont(font);
				g.setColor(Color.LIGHT_GRAY);
			}
			g.drawString(options[i], 300, 250 + i * 30);
		}
	}
	
	private void select() {
		if (currentChoice == 0) {
			gsm.beginState(GameStateManager.LEVELSELECTSTATE);
		}
		if (currentChoice == 1) {
			gsm.setState(GameStateManager.INSTRUCTIONSTATE);
		}
		if (currentChoice == 2) {
			System.exit(0);
		}
	}
	
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice < 0) {
				currentChoice = options.length - 1;
			}
		}
		if (k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice > options.length - 1) {
				currentChoice = 0;
			}
		}
		if (k == KeyEvent.VK_T) {
			gsm.cycleMode();
			System.out.println(gsm.getMode());
		}
	}
	
	public void keyReleased(int k) {}
}