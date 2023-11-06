package TileMap;

import java.awt.image.BufferedImage;

//class to store an image and the type of a tile
public class Tile {
	private BufferedImage image;
	private int type;
	
	//tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	//special types
	public static final int SPIKE = 2;
	public static final int THORN = 3;
	public static final int FLIPSPIKE= 4;
	public static final int LEFTRAMP = 5;
	public static final int RIGHTRAMP = 6;
	public static final int FLIPLEFTRAMP = 7;
	public static final int FLIPRIGHTRAMP = 8;
	public static final int MINIRAMP = 9;
	public static final int FLIPMINIRAMP = 10;
	public static final int VRAMP = 11;
	public static final int FLIPVRAMP = 12;
	
	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}
	
	public int getType() {return type;}
	public BufferedImage getImage() {return image;}
}
