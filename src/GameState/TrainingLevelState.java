package GameState;

import TileMap.Background;
import TileMap.Level1Map;

public class TrainingLevelState extends LevelState{
    public TrainingLevelState(GameStateManager gsm) {
        super(gsm);

        // load level specific resources
        this.bg = new Background("/Backgrounds/menu.jpg", 0);
        this.tileMap = new Level1Map(32);

        init();
    }
}
