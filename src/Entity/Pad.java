package Entity;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Pad extends GameObject{
	private int type;
	private boolean activatedOnce;		//to limit activation to 1 time
	
	//2 types of pads
	public static final int JUMP = 0;
	public static final int GRAVITY = 1;
	
	public Pad(TileMap tm, int x, int y, int type) {
		super(tm);
		this.type = type;
		activatedOnce = false;
		
		this.x = x;
		this.y = y;
		
		width = 27;
		height = 8;
		cwidth = 27;
		cheight = 16;
		
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/pads.png"));
			sprites = new BufferedImage[2];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		animation.setFrame(type);
	}
	
	public int getType() {
		return type;
	}
	
	public boolean getActivatedOnce() {return activatedOnce;}
	
	public void setActivated(boolean b) {activatedOnce = b;}
}
