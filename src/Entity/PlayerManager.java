package Entity;

import java.awt.Graphics2D;
import java.util.ArrayList;

import Audio.AudioPlayer;
import TileMap.TileMap;

//similar to Game state manager but with player modes instead
//handles the changing of modes and the player in general
//Note: the player classes are never used directly in game, they are always interacting through player manager
public class PlayerManager {
	private ArrayList<Player> players;	//arraylist to store the player modes
	private int currentPlayer;			//var to keep track of current player mode
	public AudioPlayer deathSound;		//death sound effect

	//static final int values for the differnet modes
	public static final int CUBE = 0;
	public static final int SHIP = 1;
	public static final int UFO  = 2;		//UFO is not actually implemented so it does nothing but I have to keep it because it is part of the sprites
	public static final int WAVE = 3;
	public static final int BALL = 4;

	public PlayerManager(TileMap tm) {
		players = new ArrayList<Player>();
		deathSound = new AudioPlayer("/SFX/deathsound.mp3");
		currentPlayer = CUBE;
		players.add(new Cube(tm, this));
		players.add(new Ship(tm, this));
		players.add(new UFO(tm, this));
		players.add(new Wave(tm, this));
		players.add(new Ball(tm, this));
	}

	//when changing modes, also transfer attributes and stats
	public void setPlayer(int player) {
		Player p = players.get(currentPlayer);
		players.get(player).setPosition(p.getx(), p.gety());
		players.get(player).setGravity(p.getGravity());
		players.get(player).setDY(p.getDY());
		players.get(player).setDX(p.getDX());
		currentPlayer = player;
	}

	public Player getPlayer() {return players.get(currentPlayer);}

	public void update() {
		players.get(currentPlayer).update();
	}

	public void draw(Graphics2D g) {
		players.get(currentPlayer).draw(g);
	}
}
