package GameState;

import TileMap.Background;
import TileMap.TrainingLevelMap;

public class TrainingLevelState extends LevelState{
    public TrainingLevelState(GameStateManager gsm) {
        super(
            gsm, 
            new Background("/Backgrounds/menu.jpg", 0), 
            new TrainingLevelMap(32), null
            );
    }
}
