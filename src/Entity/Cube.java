package Entity;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import TileMap.*;

//cube player mode
public class Cube extends Player{
	private BufferedImage[] sprites;

	public Cube(TileMap tm, PlayerManager pm) {
		super(tm, pm);
		
		initValues();
		
		cwidth = 32;
		cheight = 32;

		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/cube.png"));
			sprites = new BufferedImage[1];
			sprites[0] = spritesheet;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);	//no animation so delay is -1
	}

	public void getNextPosition() {
		//jumping
		if (jumping && !falling) {		//the cube can continuously jump by holding down up-arrow key
			dy = jumpSpeed;
			falling = true;
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
		super.initValues();
		fallSpeed = 0.57;
		maxFallSpeed = 10;
	}
}
