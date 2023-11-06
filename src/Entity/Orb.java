package Entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Orb extends GameObject{
	private int type;
	private boolean activatedOnce; 		//to limit activation to 1 time
	
	//2 types of orbs
	public static final int JUMP = 0;
	public static final int GRAVITY = 1;
	
	private ArrayList<BufferedImage[]> sprites;
	
	public Orb(TileMap tm, int x, int y, int type) {
		super(tm);
		this.type = type;
		activatedOnce = false;
		
		this.x = x;
		this.y = y;
		
		width = 32;
		height = 32;
		cwidth = 32;
		cheight = 32;
		
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/orbs.png"));
			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < spritesheet.getHeight() / 32; i++) {
				BufferedImage[] bi = new BufferedImage[spritesheet.getWidth() / 32];
				for (int j = 0; j < bi.length; j++) {
					bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
				}
				sprites.add(bi);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites.get(type));
		animation.setDelay(20);
	}
	
	public int getType() {
		return type;
	}
	
	public void update() {
		animation.update();
	}
	
	public boolean getActivatedOnce() {return activatedOnce;}
	
	public void setActivated(boolean b) {activatedOnce = b;}
}
