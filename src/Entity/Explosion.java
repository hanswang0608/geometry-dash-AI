package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

//explosion class to display death explosions
//it does not inherit game object because it has no real interactions with the game
public class Explosion {
	private int x;
	private int y;
	private int xmap;
	private int ymap;
	
	private int width;
	private int height;
	
	private Animation animation;
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	public Explosion(int x, int y) {
		this.x = x;
		this.y = y;
		width = 32;
		height = 32;
		
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/explosion.png"));
			sprites = new BufferedImage[spritesheet.getWidth()/width];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(50);
	}
	
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {remove = true;}	//remove the explosion object when it has played its animation once
	}
	
	public boolean shouldRemove() {return remove;}
	
	public void setMapPosition(int x, int y) {
		xmap = x;
		ymap = y;
	}

	public void draw(Graphics2D g) {
		g.drawImage(animation.getImage(), x + xmap - width / 2, y + ymap - height / 2, null);
	}
}
