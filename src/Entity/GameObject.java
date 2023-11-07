package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import GameState.LevelState;
import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

//abstract parent class of most game objects used
public abstract class GameObject {
	//tile
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;

	//position and vector
	protected double x;
	protected double y;

	//dimensions
	protected int width;
	protected int height;

	//collision box
	protected int cwidth;
	protected int cheight;

	//animation
	protected Animation animation;
	protected BufferedImage[] sprites;

	protected int gravity;
	
	public GameObject(TileMap tm) {
		tileMap = tm;
		tileSize = 	tm.getTileSize();
		gravity = 1;
	}
	
	//method to check whether 2 game objects are touching with their rectangular hitboxes
	public boolean intersects(GameObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	private Rectangle getRectangle() {
		return new Rectangle((int)x - cwidth, (int)y - cheight, cwidth, cheight);
	}
	
	//getters / setters
	public int getx() {return (int)x;}
	public int gety() {return (int)y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public int getCWidth() {return cwidth;}
	public int getCHeight() {return cheight;}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		if (gravity == 1) {		//flips the sprite upside down depending on the gravity
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), width, height, null);
		}
		else {
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap + height / 2), width, -height, null);
		}
	}
}
