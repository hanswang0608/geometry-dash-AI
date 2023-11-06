package Entity;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

//ship player mode subclass
public class Ship extends Player{	
	private BufferedImage[] sprites;
	
	public Ship(TileMap tm, PlayerManager pm) {
		super(tm, pm);
		
		initValues();
		
		width = 44;
		cwidth = 44;
		cheight = 32;

		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/ship.png"));
			sprites = new BufferedImage[1];
			sprites[0] = spritesheet;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	public void getNextPosition() {		
		//jumping
		if (jumping) {		//the ship can fly whenever
			dy += -fallSpeed;		//it flies by reversing the fallspeed
			if (gravity == 1) {
				if (dy < -maxFallSpeed) dy = -maxFallSpeed;
			}
			else {
				if (dy > -maxFallSpeed) dy = -maxFallSpeed;
			}
			falling = false;
		}
		
		//falling
		if (falling) {
			dy += fallSpeed;
			if (gravity == 1) {
				if (dy > maxFallSpeed) dy = maxFallSpeed;
			}
			else {
				if (dy < maxFallSpeed) dy = maxFallSpeed;
			}
		}
	}
	
	public void initValues() {
		fallSpeed = 0.3;
		maxFallSpeed = 6;
	}
}
