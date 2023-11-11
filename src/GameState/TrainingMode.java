package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Audio.AudioPlayer;
import Entity.*;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class TrainingMode extends Mode{
	protected PlayerManager pm;
	protected float deathTime; 	//keeps track of time of death, to create a 1 second respawn delay
	protected boolean running;	//determines if the player should be updated

	protected static final int respawnDelayMS = 0;

    public TrainingMode(GameStateManager gsm, Background bg, TileMap tileMap, AudioPlayer music) {
        this.gsm = gsm;
		this.bg = bg;
		this.tileMap = tileMap;
		this.music = music;

        orbs = new ArrayList<Orb>();
		pads = new ArrayList<Pad>();
		gportals = new ArrayList<GravityPortal>();
		portals = new ArrayList<Portal>();
		explosions = new ArrayList<Explosion>();
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
		setPlayer();
		running = true;

		
    }

    public void update() {
		//update player
		if (running) pm.update();
		if(pm.getPlayer().atEndOfLevel()) {
			pm.getPlayer().setMoving(false);
			if (pm.getPlayer().getDX() == 0) {
				gsm.setState(GameStateManager.WINSTATE);
				setPlayer();
			}
		}
		
		if(pm.getPlayer().isDead()) {
			deathTime = System.nanoTime();
			pm.getPlayer().setDead(false);
			running = false;
			stopMusic();
			pm.deathSound.play();
			explosions.add(new Explosion(pm.getPlayer().getx(), pm.getPlayer().gety()));
		}

		//locks the vertical movement of the screen for modes other than Cube
		if (pm.getPlayer() instanceof Cube) {
			tileMap.setPosition(GamePanel.WIDTH / 2 - pm.getPlayer().getx(), GamePanel.HEIGHT / 2 - pm.getPlayer().gety()); 
		}
		else {
			tileMap.setPosition(GamePanel.WIDTH / 2 - pm.getPlayer().getx());
		}

		//update background
		bg.setPosition(tileMap.getx(), tileMap.gety());

		//update entities
		for (int i = 0; i < orbs.size(); i++) {
			if (pm.getPlayer().intersects(orbs.get(i)) && pm.getPlayer().getJumping() && pm.getPlayer().isFirstJump() && !orbs.get(i).getActivatedOnce()) {
				pm.getPlayer().hitOrb(orbs.get(i));
			}
			orbs.get(i).update();
		}
		
		for (int i = 0; i < pads.size(); i++) {
			if (pm.getPlayer().intersects(pads.get(i)) && !pads.get(i).getActivatedOnce()) {
				pm.getPlayer().hitPad(pads.get(i));
			}
		}
		
		for (int i = 0; i < gportals.size(); i++) {
			if (pm.getPlayer().intersects(gportals.get(i))) {
				if (gportals.get(i).getType() == GravityPortal.NORMAL || gportals.get(i).getType() == GravityPortal.NORMALH) {
					if (pm.getPlayer().getGravity() != 1) pm.getPlayer().flipGravity();
				}
				else {
					if (pm.getPlayer().getGravity() != -1) pm.getPlayer().flipGravity();
				}
			}
		}
		
		for (int i = 0; i < portals.size(); i++) {
			if (pm.getPlayer().intersects(portals.get(i))) {
				if(portals.get(i).getType() == Portal.CUBE) pm.setPlayer(Portal.CUBE);
				else if(portals.get(i).getType() == Portal.SHIP) pm.setPlayer(Portal.SHIP);
				else if(portals.get(i).getType() == Portal.BALL) pm.setPlayer(Portal.BALL);
				else if(portals.get(i).getType() == Portal.WAVE) pm.setPlayer(Portal.WAVE);
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
		
		if (running) pm.draw(g);
		
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
		if (k == KeyEvent.VK_ESCAPE) gsm.beginState(GameStateManager.PAUSESTATE);		//esc to pause
		if (k == KeyEvent.VK_R) {reset();} 		//r to restart level
	}

	public void keyReleased(int k) {
		if (k == KeyEvent.VK_UP) {
			pm.getPlayer().setJumping(false);
			pm.getPlayer().setFirstClick(true);
		}
	}

    //method to reset player, music, and some entites in order to restart the level
	protected void reset() {
		deathTime = -1;
		setPlayer();
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
	protected void setPlayer() {
		pm = new PlayerManager(tileMap);
		pm.getPlayer().setPosition(64, 560);
	}
}
