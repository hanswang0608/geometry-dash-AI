package Entity;

import TileMap.*;

//abstract subclass of game object specifically for the player
public abstract class Player extends GameObject{
	
	protected PlayerManager pm;		//a handler for the 4 different player modes
	
	//movements
	protected boolean moving;
	protected boolean falling;
	protected boolean jumping;
	protected boolean firstClick;	//these variables are for determining whether the player is clicking or holding
	protected boolean firstJump;
	protected long firstClickTime;

	//movement attributes
	protected double dx;
	protected double dy;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpSpeed;
	
	//collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected int tl;
	protected int tr;
	protected int bl;
	protected int br;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;	
	
	protected boolean dead;
	
	public Player(TileMap tm, PlayerManager pm) {
		super(tm);
		this.pm = pm;
		
		initValues();
	}
	
	//calculate the 4 corners of the player, whether they hit a solid block or a special tile
	public void calculateCorners(double x, double y) {
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		int bottomTile = (int)(y + cheight / 2- 1) / tileSize;
		
		tl = tileMap.getType(topTile, leftTile);
		tr = tileMap.getType(topTile, rightTile);
		bl = tileMap.getType(bottomTile, leftTile);
		br = tileMap.getType(bottomTile, rightTile);
		
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}
	
	//calculations for interacting with the map
	public void checkTileMapCollision() {
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;

		xdest = x + dx;
		ydest = y + dy;

		xtemp = x;
		ytemp = y;
		
		double bottom = ydest + cheight / 2 - 1; 
		double top = ydest - cheight / 2;
		
		//calculate special tile interactions
		//for all of these tiles, functions are used to calculate the slope of the tile at a given x coord, to determine whether the player should be hitting
		calculateCorners(xdest, ydest);
		if (br == Tile.SPIKE || bl == Tile.SPIKE) {
			if (bottom >= leftSlopeSpike(xdest, ydest) && bottom >= rightSlopeSpike(xdest, ydest)) {
				dead = true;
			}
		}
		
		if (br == Tile.THORN || bl == Tile.THORN) {
			if (bottom >= leftSlopeThorn(xdest, ydest) && bottom >= rightSlopeThorn(xdest,ydest)) {
				dead = true;
			}
		}
		
		if (tr == Tile.FLIPSPIKE || tl == Tile.FLIPSPIKE) {
			if (top <= leftSlopeFlipSpike(xdest, ydest) && top <= rightSlopeFlipSpike(xdest, ydest)) {
				dead = true;
			}
		}
		
		if (br == Tile.LEFTRAMP) {
			if (bottom >= leftSlopeRamp(xdest, ydest)) {
				dead = true;
			}
		}
		if (bl == Tile.RIGHTRAMP) {
			if (bottom >= rightSlopeRamp(xdest, ydest)) {
				dead = true;
			}
		}
		if (tr == Tile.FLIPLEFTRAMP) {
			if (top <= leftSlopeFlipRamp(xdest, ydest)) {
				dead = true;
			}
		}
		if (tl == Tile.FLIPRIGHTRAMP) {
			if (top <= rightSlopeFlipRamp(xdest, ydest)) {
				dead = true;
			}
		}
		
		if (br == Tile.MINIRAMP || bl == Tile.MINIRAMP) {
			if (bottom >= leftSlopeMiniRamp(xdest, ydest) && bottom >= rightSlopeMiniRamp(xdest, ydest)) {
				dead = true;
			}
		}
		
		if (tr == Tile.FLIPMINIRAMP || tl == Tile.FLIPMINIRAMP) {
			if (top <= leftSlopeFlipMiniRamp(xdest, ydest) && top <= rightSlopeFlipMiniRamp(xdest, ydest)) {
				dead = true;
			}
		}
		
		if (br == Tile.VRAMP || bl == Tile.VRAMP || tr == Tile.VRAMP || tl == Tile.VRAMP) {
			dead = true;
		}
		
		if (this instanceof Wave && (topLeft || topRight || bottomLeft || bottomRight)) {
			dead = true;
		}
		
		if (xdest > tileMap.getWidth() || xdest < 0 || ydest > tileMap.getHeight() || ydest < 0){
			dead = true;
		}
		
		if (dead) return;
		
		
		//calculate tile interaction and physics vertically
		calculateCorners(x, ydest);
		if (dy < 0) {
			if (topLeft || topRight) {
				if (gravity == -1) falling = false;			//if gravity is reversed, then upwards is where the player lands
				else if (this instanceof Cube) dead = true;		//the cube dies when jumping into blocks
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if (dy > 0) {
			if (bottomLeft || bottomRight) {
				if (gravity == 1) falling = false;
				else if (this instanceof Cube) dead = true;
				dy = 0;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		//calculate tile interaction and physics horizontally, decelerate player if not moving
		if (moving) {
			calculateCorners(xdest, y);
			if (dx > 0) {
				if (topRight || bottomRight) {
					dead = true;		//running into a block will kill the player
				}
				else {
					xtemp += dx;
				}
			}
		}
		else {							//when !moving, it's because of a win. 
			if (dx > 0) {
				dx -= 0.2;
				if (dx < 0) dx = 0;		//slow down the player.
				xtemp += dx;
			}
		}
		
		
		//check if player should start falling
		if (!falling && !jumping) {
			if (gravity == 1) {
				calculateCorners(x, ydest + 1);
				if (!bottomLeft && !bottomRight) {
					falling = true;
				}
			}
			else {
				calculateCorners(x, ydest - 1);
				if (!topLeft && !topRight) {
					falling = true;
				}
			}
		}
	}
	
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);	//after all of the calculations done, set position
		
		//changing the wave sprite when moving up or down
		if (this instanceof Wave) {
			if (jumping) animation.setFrame(Wave.UP);
			else if (falling) animation.setFrame(Wave.DOWN);
			else animation.setFrame(Wave.IDLE);
		}
		
		//update animation
		animation.update();
	}
	
	//method to react to hitting a pad
	public void hitPad(Pad pad) {
		if (pad.getType() == Pad.JUMP) {
			if (!(this instanceof Ship)) dy = jumpSpeed * 1.3;
			else dy = jumpSpeed;  
		}
		else {
			flipGravity();
			dy = maxFallSpeed;
			falling = true;
		}
		pad.setActivated(true);
	}
	
	//method to react to activating an orb
	public void hitOrb(Orb orb) {
		if (orb.getType() == Orb.JUMP) {
			if (!(this instanceof Ship))dy = jumpSpeed * 1;
			else dy = jumpSpeed;
		}
		else {
			flipGravity();
			dy = maxFallSpeed;
		}
		orb.setActivated(true);
	}
	
	public boolean atEndOfLevel() {
		return x > tileMap.getWidth() - 96;
	}
	
	//different modes have different actions so it is abstract method
	public abstract void getNextPosition();
	
	//different modes have different attributes so it is abstract method
	public void initValues() {
		moving = true;
		width = 32;
		height = 32;
		dx = 32/6;
		jumpSpeed = -9;
	};
	
	public void updateGravity(int g) {
		if (g == -1) {
			jumpSpeed = Math.abs(jumpSpeed);
			fallSpeed = -Math.abs(fallSpeed);
			maxFallSpeed = -Math.abs(maxFallSpeed);
		}
		else {
			initValues();
			jumpSpeed = -Math.abs(jumpSpeed);
		}
	}
	
	public void flipGravity() {
		gravity *= -1;
		updateGravity(gravity);
	}
	
	public void setGravity(int g) {
		gravity = g;
		updateGravity(g);
	}
				
	//getters / setters
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	public void setMoving(boolean b) {moving = b;}
	public void setJumping(boolean b) {jumping = b;}
	public boolean getJumping() {return jumping;}
	public boolean isFalling() {return falling;}
	public void setMoveSpeed(int dx) {this.dx = dx;}
	public boolean isDead() {return dead;}
	public void setDead(boolean b) {dead = b;}
	public int getGravity() {return gravity;}
	public void setDY(double v) {dy = v;}
	public double getDY() {return dy;}
	public void setDX(double v) {dx = v;}
	public double getDX() {return dx;}
	public void setFirstClick(boolean b) {firstClick = b;}
	public boolean isFirstClick() {return firstClick;}
	public void setFirstJump(boolean b) {firstJump = b;}
	public boolean isFirstJump() {return firstJump;}
	public void setFirstClickTime(long l) {firstClickTime = l;}
	public long getFirstClickTime() {return firstClickTime;}
	
	
	//all of the functions used to calculate the slopes of special tiles
	public double leftSlopeSpike(double x, double y) {
		if (br == Tile.SPIKE) {
			return (((x + cwidth / 2 - 1) - ((int)(x + cwidth / 2 - 1) / 32 * 32) + 1) * -2 + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
		}
		else {
			return (((x + cwidth / 2 - 1) - ((int)(x + cwidth / 2 - 1) / 32 * 32) + 1) * -2 - 64 + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
		}
	}
	
	public double rightSlopeSpike(double x, double y) {
		if (bl == Tile.SPIKE) {
			return ((((int)(x - cwidth / 2) / 32 * 32 + 32) - (x - cwidth / 2)) * -2 + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
		}
		else {
			return ((((int)(x - cwidth / 2) / 32 * 32 + 32) - (x - cwidth / 2)) * -2 - 64 + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
		}
	}
	
	public double leftSlopeThorn(double x, double y) {
		if (br == Tile.THORN) {
			double slope = ((x + cwidth / 2 - 1) - ((int)(x + cwidth / 2 - 1) / 32 * 32) + 1) * -1;
			if (slope > -14) slope = -14;
			return (slope + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
		}
		else {
			return (-14 + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
		}
	}
	
	public double rightSlopeThorn(double x, double y) {
		if (bl == Tile.THORN) {
			double slope = (((int)(x - cwidth / 2) / 32 * 32 + 32) - (x - cwidth / 2)) * -0.78;
			if (slope > -14) slope = -14;
			return (slope + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
		}
		else {
			return (-14 + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
		}
	}
	
	public double leftSlopeFlipSpike(double x, double y) {
		if (tr == Tile.FLIPSPIKE) {
			return (((x + cwidth / 2 - 1) - ((int)(x + cwidth / 2 - 1) / 32 * 32) + 1) * 2 + ((int)(y - cheight / 2) / 32 * 32));
		}
		else {
			return (((x + cwidth / 2 - 1) - ((int)(x + cwidth / 2 - 1) / 32 * 32) + 1) * 2 + 64 + ((int)(y - cheight / 2) / 32 * 32));
		}
	}
	
	public double rightSlopeFlipSpike(double x, double y) {
		if (tl == Tile.FLIPSPIKE) {
			return ((((int)(x - cwidth / 2) / 32 * 32 + 32) - (x - cwidth / 2)) * 2 + ((int)(y - cheight / 2) / 32 * 32));
		}
		else {
			return ((((int)(x - cwidth / 2) / 32 * 32 + 32) - (x - cwidth / 2)) * 2 + 64 + ((int)(y - cheight / 2) / 32 * 32));
		}
	}
	
	public double leftSlopeRamp(double x, double y) {
		return (((x + cwidth / 2 - 1) - ((int)(x + cwidth / 2 - 1) / 32 * 32) + 1) * -1 + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
	}
	
	public double rightSlopeRamp(double x, double y) {
		return ((((int)(x - cwidth / 2) / 32 * 32 + 32) - (x - cwidth / 2)) * -1 + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
	}
	
	public double leftSlopeFlipRamp(double x, double y) {
		return (((x + cwidth / 2 - 1) - ((int)(x + cwidth / 2 - 1) / 32 * 32) + 1) * 1 + ((int)(y - cheight / 2) / 32 * 32));
	}

	public double rightSlopeFlipRamp(double x, double y) {
		return ((((int)(x - cwidth / 2) / 32 * 32 + 32) - (x - cwidth / 2)) * 1 + ((int)(y - cheight / 2) / 32 * 32));
	}
	
	public double leftSlopeMiniRamp(double x, double y) {
		if (br == Tile.MINIRAMP) {
			double slope = ((x + cwidth / 2 - 1) - ((int)(x + cwidth / 2 - 1) / 32 * 32) + 1) * -1;
			if (slope < -16) slope = -16;
			return (slope + (int)(y + cheight / 2 - 1) / 32 * 32 + 32);
		}
		else {
			return (-16 + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
		}
	}
	
	public double rightSlopeMiniRamp(double x, double y) {
		if (bl == Tile.MINIRAMP) {
			double slope = (((int)(x - cwidth / 2) / 32 * 32 + 32) - (x - cwidth / 2)) * -1;
			if (slope < -16) slope = -16;
			return (slope + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
		}
		else {
			return (-16 + ((int)(y + cheight / 2 - 1) / 32 * 32 + 32));
		}
	}
	
	public double leftSlopeFlipMiniRamp(double x, double y) {
		if (tr == Tile.FLIPMINIRAMP) {
			double slope = ((x + cwidth / 2 - 1) - ((int)(x + cwidth / 2 - 1) / 32 * 32) + 1) * 1;
			if (slope > 16) slope = 16;
			return (slope + ((int)(y - cheight / 2) / 32 * 32));
		}
		else {
			return (16 + ((int)(y - cheight / 2) / 32 * 32));
		}
	}
	
	public double rightSlopeFlipMiniRamp(double x, double y) {
		if (tl == Tile.FLIPMINIRAMP) {
			double slope = (((int)(x - cwidth / 2) / 32 * 32 + 32) - (x - cwidth / 2)) * 1;
			if (slope > 16) slope = 16;
			return (slope + ((int)(y - cheight / 2) / 32 * 32));
		}
		else {
			return (16 + ((int)(y - cheight / 2) / 32 * 32));
		}
	}
}
