package GameState;

import java.awt.Color;
import java.awt.Font;
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
	private double deathTime; 	//keeps track of time of death, to create a 1 second respawn delay
	private boolean running;	//determines if the player should be updated
	
	private Population population;
	private int numAlive;
	private int generation;

	private static final int respawnDelayMS = 250;
	private static final int spawnX = 64;
	private static final int spawnY = 560;

	private static final int AI_VIEW_DISTANCE = 4;
	private static final int populationSize = 30;

    public TrainingMode(GameStateManager gsm, Background bg, TileMap tileMap, AudioPlayer music) {
        this.gsm = gsm;
		this.bg = bg;
		this.tileMap = tileMap;
		this.music = music;

		players = new ArrayList<PlayerManager>();

        orbs = new ArrayList<Orb>();
		pads = new ArrayList<Pad>();
		gportals = new ArrayList<GravityPortal>();
		portals = new ArrayList<Portal>();
		explosions = new ArrayList<Explosion>();

		numAlive = populationSize;
		generation = 0;
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

		population = new Population(populationSize, new int[]{AI_VIEW_DISTANCE + 1, 6, 4, 1});

        //initialize player settings
		players.clear();
		for (int i = 0; i < populationSize; i++) {
			players.add(new PlayerManager(tileMap));
		}
		deathTime = -1;
		setPlayers();
		running = true;
		numAlive = populationSize;
		generation = 0;
    }

    public void update() {
		if (numAlive == 0 && running) {
			deathTime = System.nanoTime();
			running = false;
			stopMusic();
		}

		//if it has been 1 second since dying, respawn the player
		if (deathTime != -1 && (System.nanoTime() - deathTime) / 1000000 > respawnDelayMS) {
			population.selectParentsByRank(2);
			population.crossoverPopulation();
			population.mutatePopulation();
			population.updatePopulation();
			reset();
			generation++;
			population.getMostFit().getNetwork().saveToFile("ai_models/temp/training-gen-"+generation+".model", true);
		}

		for (int i = 0; i < populationSize; i++) {
			PlayerManager pm = players.get(i);
			Player player = pm.getPlayer();
			Agent agent = population.getAgents()[i];

			if (player.isDead()) continue;

			//update player
			if (running) pm.update();
			if(player.atEndOfLevel()) {
				player.setMoving(false);
				if (player.getDX() == 0) {
					gsm.setState(GameStateManager.WINSTATE);
					population.getMostFit().getNetwork().saveToFile("ai_models/training-win.model", true);
				}
			}
			
			// death update
			if(player.isDead()) {
				explosions.add(new Explosion(player.getx(), player.gety()));
				numAlive--;
				agent.setFitness(player.getx());
			}

			// get jump input from neural network
			double networkOutput = agent.act(getNetworkInputs(pm, true))[0];
			boolean shouldJump = networkOutput >= 0.5;
			if (shouldJump) {
				startJumping(pm);
			} else {
				stopJumping(pm);
			}
			
	
			//update background
			bg.setPosition(tileMap.getx(), tileMap.gety());
	
			//update entities
			for (int j = 0; j < orbs.size(); j++) {
				if (player.intersects(orbs.get(j)) && player.getJumping() && player.isFirstJump() && !orbs.get(j).getActivatedOnce()) {
					player.hitOrb(orbs.get(j));
				}
				orbs.get(j).update();
			}
			
			for (int j = 0; j < pads.size(); j++) {
				if (player.intersects(pads.get(j)) && !pads.get(j).getActivatedOnce()) {
					player.hitPad(pads.get(j));
				}
			}
			
			for (int j = 0; j < gportals.size(); j++) {
				if (player.intersects(gportals.get(j))) {
					if (gportals.get(j).getType() == GravityPortal.NORMAL || gportals.get(j).getType() == GravityPortal.NORMALH) {
						if (player.getGravity() != 1) player.flipGravity();
					}
					else {
						if (player.getGravity() != -1) player.flipGravity();
					}
				}
			}
			
			for (int j = 0; j < portals.size(); j++) {
				if (player.intersects(portals.get(j))) {
					if(portals.get(j).getType() == Portal.CUBE) pm.setPlayer(Portal.CUBE);
					else if(portals.get(j).getType() == Portal.SHIP) pm.setPlayer(Portal.SHIP);
					else if(portals.get(j).getType() == Portal.BALL) pm.setPlayer(Portal.BALL);
					else if(portals.get(j).getType() == Portal.WAVE) pm.setPlayer(Portal.WAVE);
				}
			}
		}

		//locks the vertical movement of the screen for modes other than Cube
		// lock camera movement to the player furthest ahead
		Player player = getPlayerInFirst().getPlayer();
		if (player instanceof Cube) {
			tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety()); 
		}
		else {
			tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx());
		}
		
		//update explosion
		for (int j = 0; j < explosions.size(); j++) {
			explosions.get(j).update();
			if (explosions.get(j).shouldRemove()) explosions.remove(j);
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
				if (!players.get(i).getPlayer().isDead()) {
					players.get(i).draw(g);	// draw the players in reverse order so the one in  front is on top
				}
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

		g.setColor(new Color(1, 1, 1, 0.5f));
		g.setFont(new Font("Calibri", Font.BOLD, 20));
		g.drawString("Gen " + generation, GamePanel.WIDTH/2-20, 20);
		//Note: draw is set up to draw objects in order of appearance
	}

    //key listeners
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_UP) {
			// for (PlayerManager pm : players) {
			// 	startJumping(pm);
			// }
		}
		if (k == KeyEvent.VK_ESCAPE) gsm.beginState(GameStateManager.PAUSESTATE);		//esc to pause
		if (k == KeyEvent.VK_R) {reset();} 		//r to restart level
	}

	// disable this because it's constantly being invoked due to the way gsm.keyUpdate() is setup, stoping the AI's jumps
	public void keyReleased(int k) {
		if (k == KeyEvent.VK_UP) {
			// for (PlayerManager pm : players) {
			// 	stopJumping(pm);
			// }
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
		numAlive = populationSize;
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
			pm.getPlayer().setDead(false);
			pm.init();
			pm.getPlayer().initValues();
			pm.getPlayer().setPosition(spawnX-i*2, spawnY);
		}
	}

	private PlayerManager getPlayerInFirst() {
		PlayerManager furthest = players.get(0);
		for (int i = 1; i < players.size(); i++) {
			if (players.get(i).getPlayer().getx() >= furthest.getPlayer().getx()) {
				furthest = players.get(i);
			}
		}
		return furthest;
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
