package Entity;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

//the ball player mode
public class Ball extends Player{
	public Ball(TileMap tm, PlayerManager pm) {
		super(tm, pm);

		initValues();

		width = 35;
		height = 35;
		cwidth = 32;
		cheight = 32;

		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/ball.png")); //load its sprite
			sprites = new BufferedImage[3];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//the ball is the only mode to have an animation playing on repeat
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(50);
	}

	public void getNextPosition() {
		//jumping
		if (jumping && firstJump && !falling) {		//the ball has a special interaction where holding down jump does not let you continuously jump
			flipGravity();		//the ball's jump/action is reversing gravity
			falling = true;		//this is done by simply flipping the gravity and falling
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
		fallSpeed = 0.6;
		maxFallSpeed = 10;
	}
}
