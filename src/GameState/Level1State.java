package GameState;

import Audio.AudioPlayer;
import TileMap.Background;
import TileMap.Level1Map;

//state used for the first level, Final Battle
public class Level1State extends LevelState{
	public Level1State(GameStateManager gsm) {
		super(
			gsm,
			new Background("/Backgrounds/lvl1bg.png", 0.1),
			new Level1Map(32),
			new AudioPlayer("/Music/level1music.wav")
		);
	}
}
