package Entity;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class GravityPortal extends GameObject{
	private int type;

	public static final int NORMAL = 0;
	public static final int REVERSED = 1;
	public static final int NORMALH = 2;	//same as normal but horizontal
	public static final int REVERSEDH = 3;	//same as reversed but horizontal
	
	public GravityPortal(TileMap tm, int x, int y, int type) {
		super(tm);
		this.type = type;
		
		this.x = x;
		this.y = y;
		
		if (type > 1) {
			width = 93;
			height = 37;
			cwidth = 75;
			cheight = 30;
		}else {
			width = 37;
			height = 93;
			cwidth = 30;
			cheight = 75;
		}
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/gravityportals.png"));
			sprites = new BufferedImage[4];
			for (int i = 0; i < 2; i++) {
				sprites[i] = spritesheet.getSubimage(0, spritesheet.getHeight() - (i + 1) * 93, 37, 93);
				sprites[i+2] = spritesheet.getSubimage(37, 74 - (i + 1) * 37, 93, 37);
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
