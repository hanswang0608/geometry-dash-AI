package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileMap.Background;

//state used for the instruction screen
public class InstructionState extends GameState{
	private Background bg;
	private Font titleFont;
	private Color titleColour;
	private Font font;
	private String[] instructions;
	
	public InstructionState(GameStateManager gsm) {
		this.gsm = gsm;
		try {
			bg = new Background("/Backgrounds/menu.jpg", 1);
			titleColour = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.BOLD, 36);
			font = new Font("Arial", Font.BOLD, 16);
			
			instructions = new String[] {
					"Your objective is to dodge and avoid obstacles and make it to the end.",
					"Use up-arrow key to jump. Different modes have different actions.",
					"There are two types of portals: one switches the player to different modes.",
					"The other type switches around the gravity of the player.",
					"Colourful circles are orbs. They do different things when tapped.",
					"The flat ones are pads. They do different things when stepped on."
					};
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		bg.draw(g);
		g.setColor(titleColour);
		g.setFont(titleFont);
		g.drawString("INSTRUCTIONS", 200, 100);
		g.setColor(Color.BLACK);
		g.setFont(font);
		for (int i = 0; i < instructions.length; i++) {
			g.drawString(instructions[i], 50, 150 + i * 50);
		}
	}
	
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ESCAPE) {
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}
	
	public void keyReleased(int k) {}
}
