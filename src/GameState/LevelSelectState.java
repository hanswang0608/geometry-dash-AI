package GameState;
import java.awt.*;
import java.awt.event.KeyEvent;
import TileMap.Background;

//state used to select the levels
//only 1 level is available because it is VERY time consuming
public class LevelSelectState extends GameState{
	private Background bg;
	private int currentChoice;
	private String[] options = {"Final Battle", "Nothing Here", "Stop Looking"};
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public LevelSelectState(GameStateManager gsm) {
		this.gsm = gsm;
		try {
			bg = new Background("/Backgrounds/menu.jpg", 1);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.BOLD, 42);
			font = new Font("Arial", Font.BOLD, 20);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		init();
	}
	
	public void init() {currentChoice = 0;}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		bg.draw(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Polygonal Run", 180, 150);
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString(options[currentChoice], 275, 250);
	}
	
	private void select() {
		if (currentChoice == 0) {
			gsm.getMusic().setZero();
			gsm.setState(GameStateManager.LEVEL1STATE);
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
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}
	
	public void keyReleased(int k) {};
}