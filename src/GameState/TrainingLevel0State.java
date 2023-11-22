package GameState;

import TileMap.Background;
import TileMap.TrainingLevel0Map;

public class TrainingLevel0State extends LevelState{
    public TrainingLevel0State(GameStateManager gsm) {
        super(
            gsm, 
            new Background("/Backgrounds/menu.jpg", 0), 
            new TrainingLevel0Map(32), null
            );
    }
}
