package GameState;

import java.util.ArrayList;

import Entity.Explosion;
import Entity.GravityPortal;
import Entity.Orb;
import Entity.Pad;
import Entity.Portal;
import TileMap.Background;
import TileMap.TileMap;

public abstract class Mode extends GameState{
    protected Background bg;
	protected TileMap tileMap;

    //entities
	protected ArrayList<Orb> orbs;
	protected ArrayList<Pad> pads;
	protected ArrayList<GravityPortal> gportals;
	protected ArrayList<Portal> portals;
	protected ArrayList<Explosion> explosions;
    
    //this method scans the level map for entities and add them to arraylists already made
	//check TileMap class for better understanding
	protected void scanMap(byte[][] map) {
		int tileSize = tileMap.getTileSize();
		for (int i = 0; i < map[0].length; i++) {
			for (int j = 0; j < map.length - 2; j++) {
				int rc = map[j][i];
				if (rc > 23) {
					if (rc == TileMap.JO) orbs.add(new Orb(tileMap, i * tileSize + 16, j * tileSize + 16, Orb.JUMP));
					else if (rc == TileMap.BO) orbs.add(new Orb(tileMap, i * tileSize + 16, j * tileSize + 16, Orb.GRAVITY));
					else if (rc == TileMap.JP) pads.add(new Pad(tileMap, i * tileSize + 16, j * tileSize + 28, Pad.JUMP));
					else if (rc == TileMap.FP) pads.add(new Pad(tileMap, i * tileSize + 16, j * tileSize + 28, Pad.GRAVITY));
					else if (rc == TileMap.NP) gportals.add(new GravityPortal(tileMap, i * tileSize + 16, j * tileSize + 16, GravityPortal.NORMAL));
					else if (rc == TileMap.GP) gportals.add(new GravityPortal(tileMap, i * tileSize + 16, j * tileSize + 16, GravityPortal.REVERSED));
					else if (rc == TileMap.CP) portals.add(new Portal(tileMap, i * tileSize + 16, j * tileSize + 16, Portal.CUBE));
					else if (rc == TileMap.SP) portals.add(new Portal(tileMap, i * tileSize + 16, j * tileSize + 16, Portal.SHIP));
					else if (rc == TileMap.BP) portals.add(new Portal(tileMap, i * tileSize + 16, j * tileSize + 16, Portal.BALL));
					else if (rc == TileMap.WP) portals.add(new Portal(tileMap, i * tileSize + 16, j * tileSize + 16, Portal.WAVE));
					else if (rc == TileMap.NH) gportals.add(new GravityPortal(tileMap, i * tileSize + 16, j * tileSize + 16, GravityPortal.NORMALH));
					else if (rc == TileMap.GH) gportals.add(new GravityPortal(tileMap, i * tileSize + 16, j * tileSize + 16, GravityPortal.REVERSEDH));
				}
			}
		}
	}
}
