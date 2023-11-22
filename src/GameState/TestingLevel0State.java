package GameState;

import TileMap.Background;
import TileMap.TestingLevel0Map;

public class TestingLevel0State extends LevelState{
    public TestingLevel0State(GameStateManager gsm) {
        super(
            gsm, 
            new Background("/Backgrounds/menu.jpg", 0), 
            new TestingLevel0Map(32), null
            );
    }
}
