package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import Audio.AudioPlayer;
import Entity.*;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

import JavaNN.Training.*;

public class TrainingMode extends Mode{
	private ArrayList<PlayerManager> players;
	private float deathTime; 	//keeps track of time of death, to create a 1 second respawn delay
	private boolean running;	//determines if the player should be updated

	private Population population;

	private static final int respawnDelayMS = 0;
	private static final int spawnX = 64;
	private static final int spawnY = 560;

	private static final int AI_VIEW_DISTANCE = 4;
	private static final int populationSize = 10;

    public TrainingMode(GameStateManager gsm, Background bg, TileMap tileMap, AudioPlayer music) {
        this.gsm = gsm;
		this.bg = bg;
		this.tileMap = tileMap;
		this.music = music;

		players = new ArrayList<PlayerManager>();
		for (int i = 0; i < populationSize; i++) {
			players.add(new PlayerManager(tileMap));
		}

        orbs = new ArrayList<Orb>();
		pads = new ArrayList<Pad>();
		gportals = new ArrayList<GravityPortal>();
		portals = new ArrayList<Portal>();
		explosions = new ArrayList<Explosion>();

		int networkInputSize = AI_VIEW_DISTANCE + 1;
		// population = new Population(10, new int[]{networkInputSize, 6, 4, 1});
    }

    public void init() {
        // initialize tilemap
        tileMap.loadTiles();
		tileMap.loadMap();
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);

		// clear old entities
		orbs.clear();
		pads.clear();
		gportals.clear();
		portals.clear();
		explosions.clear();

        // create entities by scanning the level's tilemap
		scanMap(tileMap.getMap());

        //initialize player settings
		deathTime = -1;
		setPlayers();
		running = true;
    }

    public void update() {
		for (PlayerManager pm : players) {
			Player player = pm.getPlayer();

			//update player
			if (running) pm.update();
			if(player.atEndOfLevel()) {
				player.setMoving(false);
				if (player.getDX() == 0) {
					gsm.setState(GameStateManager.WINSTATE);
				}
			}
			
			if(player.isDead()) {
				deathTime = System.nanoTime();
				player.setDead(false);
				running = false;
				stopMusic();
				pm.deathSound.play();
				explosions.add(new Explosion(player.getx(), player.gety()));
			}
	
			//locks the vertical movement of the screen for modes other than Cube
			if (player instanceof Cube) {
				tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety()); 
			}
			else {
				tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx());
			}
	
			//update background
			bg.setPosition(tileMap.getx(), tileMap.gety());
	
			//update entities
			for (int i = 0; i < orbs.size(); i++) {
				if (player.intersects(orbs.get(i)) && player.getJumping() && player.isFirstJump() && !orbs.get(i).getActivatedOnce()) {
					player.hitOrb(orbs.get(i));
				}
				orbs.get(i).update();
			}
			
			for (int i = 0; i < pads.size(); i++) {
				if (player.intersects(pads.get(i)) && !pads.get(i).getActivatedOnce()) {
					player.hitPad(pads.get(i));
				}
			}
			
			for (int i = 0; i < gportals.size(); i++) {
				if (player.intersects(gportals.get(i))) {
					if (gportals.get(i).getType() == GravityPortal.NORMAL || gportals.get(i).getType() == GravityPortal.NORMALH) {
						if (player.getGravity() != 1) player.flipGravity();
					}
					else {
						if (player.getGravity() != -1) player.flipGravity();
					}
				}
			}
			
			for (int i = 0; i < portals.size(); i++) {
				if (player.intersects(portals.get(i))) {
					if(portals.get(i).getType() == Portal.CUBE) pm.setPlayer(Portal.CUBE);
					else if(portals.get(i).getType() == Portal.SHIP) pm.setPlayer(Portal.SHIP);
					else if(portals.get(i).getType() == Portal.BALL) pm.setPlayer(Portal.BALL);
					else if(portals.get(i).getType() == Portal.WAVE) pm.setPlayer(Portal.WAVE);
				}
			}
		}

		
		//update explosion
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if (explosions.get(i).shouldRemove()) explosions.remove(i);
		}
		
		//if it has been 1 second since dying, respawn the player
		if (deathTime != -1 && (System.nanoTime() - deathTime) / 1000000 > respawnDelayMS) {
			reset();
		}
	}

    public void draw(Graphics2D g) {
		//draw background
		bg.draw(g);
		
		//draw map
		tileMap.draw(g);
		
		//draw entities and player
		for(int i = 0; i < orbs.size(); i++) {
			orbs.get(i).draw(g);
		}
		
		for (int i = 0; i < pads.size(); i++) {
			pads.get(i).draw(g);
		}
		
		if (running) {
			for (int i = players.size()-1; i >= 0; i--) {
				players.get(i).draw(g);	// draw the players in reverse order so the one in  front is on top
			}
		}
		
		for (int i = 0; i < gportals.size(); i++) {
			gportals.get(i).draw(g);
		}
		
		for (int i = 0; i < portals.size(); i++) {
			portals.get(i).draw(g);
		}
		
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
		//Note: draw is set up to draw objects in order of appearance
	}

    //key listeners
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_UP) {
			for (PlayerManager pm : players) {
				startJumping(pm);
			}
		}
		if (k == KeyEvent.VK_ESCAPE) gsm.beginState(GameStateManager.PAUSESTATE);		//esc to pause
		if (k == KeyEvent.VK_R) {reset();} 		//r to restart level
	}

	public void keyReleased(int k) {
		if (k == KeyEvent.VK_UP) {
			for (PlayerManager pm : players) {
				stopJumping(pm);
			}
		}
	}

	private void startJumping(PlayerManager pm) {
		pm.getPlayer().setJumping(true);
		//calculating the firstJump condition
		if (pm.getPlayer().isFirstClick()) {
			pm.getPlayer().setFirstClickTime(System.nanoTime());
			pm.getPlayer().setFirstClick(false);
		}
		if ((System.nanoTime() - pm.getPlayer().getFirstClickTime()) / 1000000 < 100) {
			pm.getPlayer().setFirstJump(true);
		}
		else pm.getPlayer().setFirstJump(false);
	}

	private void stopJumping(PlayerManager pm) {
		pm.getPlayer().setJumping(false);
		pm.getPlayer().setFirstClick(true);
	}

    //method to reset player, music, and some entites in order to restart the level
	protected void reset() {
		deathTime = -1;
		setPlayers();
		running = true;
		playMusic();
		for (int i = 0; i < orbs.size(); i++) {
			orbs.get(i).setActivated(false);
		}
		for (int i = 0; i < pads.size(); i++) {
			pads.get(i).setActivated(false);
		}
	}

    //creating and spawning the player
	protected void setPlayers() {
		for (int i = 0; i < players.size(); i++) {
			PlayerManager pm = players.get(i);
			pm.init();
			pm.getPlayer().setPosition(spawnX-i*5, spawnY);
		}
	}

	private double[] getNetworkInputs(PlayerManager pm, boolean shouldNormalize) {
		int tileSize = tileMap.getTileSize();
		int playerFront = pm.getPlayer().getx() + pm.getPlayer().getCWidth()/2;
		int nextColX = Math.ceilDiv(playerFront, tileSize) * tileSize;

		double[] output = new double[AI_VIEW_DISTANCE + 1]; // network can see 10 blocks in front
		output[0] = nextColX - playerFront;
		
		byte[][] map = tileMap.getMap();
		byte[] row = map[spawnY/32];
		int col = (int)Math.ceil((double)playerFront / tileSize);
		for (int i = 0; i+col < row.length && i < AI_VIEW_DISTANCE; i++) {
			output[i+1] = (double)row[i+col];
		}

		if (shouldNormalize) {
			output[0] /= tileSize;
			for (int i = 1; i < output.length; i++) {
				if (output[i] > 0) output[i] = 1;
			}
		}

		return output;
	}
}
