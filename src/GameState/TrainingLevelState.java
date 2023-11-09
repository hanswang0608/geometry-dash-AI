package GameState;

import TileMap.Background;
import TileMap.TrainingLevelMap;

public class TrainingLevelState extends LevelState{
    public TrainingLevelState(GameStateManager gsm) {
        super(gsm);

        // load level specific resources
        this.bg = new Background("/Backgrounds/menu.jpg", 0);
        this.tileMap = new TrainingLevelMap(32);
    }
}
