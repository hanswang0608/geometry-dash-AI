package Entity;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

//wave player mode subclass
public class Wave extends Player{	
	public static final int IDLE = 0;
	public static final int UP = 1;
	public static final int DOWN = 2;
	
	public Wave(TileMap tm, PlayerManager pm) {
		super(tm, pm);
		
		initValues();
		
		width = 26;
		height = 21;
		cwidth = 26;
		cheight = 21;

		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/wave.png"));
			sprites = new BufferedImage[spritesheet.getWidth() / width];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setFrame(IDLE);
		animation.setDelay(-1);
	}
	
	public void getNextPosition() {		
		if (jumping) {			//the wave can fly whenever
			dy = -fallSpeed;	//it is similar to the ship but does not have an acceleration, so it is a zig zag
			falling = false;
		}
		
		if (falling == true) {
			dy = fallSpeed;
		}
	}
	
	public void initValues() {
		fallSpeed = 5;
	}
}
