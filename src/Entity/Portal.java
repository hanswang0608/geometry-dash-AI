package Entity;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Portal extends GameObject{
	private int type;

	//static final values of different modes
	public static final int CUBE = 0;
	public static final int SHIP = 1;
	public static final int UFO = 2;		//UFO does nothing
	public static final int WAVE = 3;
	public static final int BALL = 4;
	
	
	public Portal(TileMap tm, int x, int y, int type) {
		super(tm);
		this.type = type;
		
		this.x = x;
		this.y = y;
		
		width = 46;
		height = 92;
		cwidth = 40;
		cheight = 90;
		
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/portals.png"));
			sprites = new BufferedImage[5];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(0, spritesheet.getHeight() - (i + 1) * height, width, height);
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
}
