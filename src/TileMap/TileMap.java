package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public abstract class TileMap {
	//position
	protected double x;
	protected double y;
	
	//bounds
	protected int xmin;
	protected int ymin;
	protected int xmax;
	protected int ymax;
	
	protected double tween;
	
	//map
	protected byte[][] map;
	protected int tileSize;
	protected int numRows;
	protected int numCols;
	protected int width;
	protected int height;
	
	//tileset
	protected BufferedImage tileset;
	protected int tilesetWidth;
	protected int tilesetHeight;
	protected Tile[][] tiles;
	
	//drawing
	protected int rowOffset;
	protected int colOffset;
	protected int numRowsToDraw;
	protected int numColsToDraw;


	/* Static final values for different blocks/entities. 
	* This helps with creating the level, as I can just specify the entities here instead of initializing them 1 by 1 in the level state
	* It also aligns them into the 32x32 grid */

	/* blocks */
	public static final int A	= 0;	// air
	public static final int S	= 1;	// solid borderless
	public static final int GR	= 11;	// ground
	public static final int B	= 12;	// solid block
	public static final int SLB	= 13;	// half slab
	public static final int SPK	= 22;	// spike
	public static final int TH	= 23;	// thorn
	public static final int USP	= 24;	// upside down spike

	/* entities */
	public static final int JO	= 33;	// jump orb
	public static final int BO	= 34;	// blue orb
	public static final int JP	= 35;	// jump pad
	public static final int FP	= 36;	// gravity pad
	public static final int NP	= 37;	// normal gravity portal
	public static final int GP	= 38;	// reverse gravity portal
	public static final int CP	= 39;	// cube portal
	public static final int SP	= 40;	// ship portal
	public static final int BP	= 41;	// ball portal
	public static final int WP	= 42;	// wave portal
	public static final int NH	= 43;	// horizontal normal gravity portal
	public static final int GH	= 44;	// horizontal reverse gravity portal
	
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}

	public void loadTiles() {
		try {
			tileset = ImageIO.read(getClass().getResourceAsStream("/Tilesets/prtileset.png"));	//reads the tileset image into a spritesheet
			tilesetWidth = tileset.getWidth() / tileSize;
			tilesetHeight = tileset.getHeight() / tileSize;
			tiles = new Tile[tilesetHeight][tilesetWidth];		//create a 2d array of Tiles to store the individual tiles on the tileset

			//reading in the regular types
			for(int col = 0; col < tilesetWidth; col++) {
				tiles[0][col] = new Tile(tileset.getSubimage(col * tileSize, 0, tileSize, tileSize), Tile.NORMAL);
				tiles[1][col] = new Tile(tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize), Tile.BLOCKED);
			}
			
			//reading in the special types which are all on the third row of the tileset
			for (int i = 0; i < 11; i++) {
				tiles[2][i] = new Tile(tileset.getSubimage(i * tileSize, 2 * tileSize, tileSize, tileSize), i+2);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//abstract method to be implemented by subclasses of maps as they all have a different map
	public abstract void loadMap();
	
	//getters / setters
	public int getTileSize() {return tileSize;}
	public double getx() {return x;}
	public double gety() {return y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public byte[][] getMap(){return map;}
	
	//returns the type of a tile given its position in the level map
	public int getType(int row, int col) {
		int rc = map[row][col];
		if (rc > 32) return Tile.NORMAL;
		int r = rc / tilesetWidth;
		int c = rc % tilesetWidth;
		return tiles[r][c].getType();
	}
	
	//setting map position
	public void setPosition(double x, double y) {
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds();
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
	}
	
	//setting only the x position (this is for gamemodes other than the Cube)
	public void setPosition(double x) {
		this.x += (x - this.x) * tween;
		this.y = -192.0;
		
		fixBounds();
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
	}
	
	public void setTween(double t) {tween = t;}

	//if the map moves out of bounds, i.e. at the beginning and end of level, fix it
	private void fixBounds() {
		if (x < xmin) x = 	xmin;
		if (y < ymin) y = 	ymin;
		if (x > xmax) x = 	xmax;
		if (y > ymax) y = 	ymax;
	}
	
	//drawing the tilemap
	public void draw(Graphics2D g) {
		//use offsets to draw in relevant tiles
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			if (row >= numRows) break;
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				if (col >= numCols) break;
				if (map[row][col] == 0 || map[row][col] > 32) continue; //0 in the tileset is blank space, and >32 are entities which are not drawn here
				int rc = map[row][col];
				int r = rc / tilesetWidth;
				int c = rc % tilesetWidth;
				g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);
			}
		}
	}
}
