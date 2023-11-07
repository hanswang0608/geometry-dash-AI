package GameState;

import Audio.AudioPlayer;
import TileMap.Background;
import TileMap.Level1Map;

//state used for the first level, Final Battle
public class Level1State extends LevelState{
	public Level1State(GameStateManager gsm) {
		super();
		this.gsm = gsm;
		this.music = new AudioPlayer("/Music/level1music.wav");
		init();
	}

	public void init() {
		//loading the level map from TileMap class
		tileMap = new Level1Map(32);
		super.init(tileMap);
		
		//load background
		bg = new Background("/Backgrounds/lvl1bg.png", 0.1);
	}
}
