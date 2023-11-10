package GameState;

import java.awt.Graphics2D;
import java.util.ArrayList;

import Audio.AudioPlayer;
import TileMap.Background;
import TileMap.TileMap;

public abstract class LevelState extends GameState{
    protected GameStateManager gsm;
    protected Background bg;
	protected TileMap tileMap;

    protected ModeManager mm;

    public LevelState(GameStateManager gsm, Background bg, TileMap tileMap, AudioPlayer music) {
        this.gsm = gsm;
        
        mm = new ModeManager(gsm, bg, tileMap, music);
    }

    public void setMode(int mode) {
        mm.setMode(mode);
    }

    public void init() {
        mm.init();
    }

    public void update() {
        mm.update();
    }

    public void draw(Graphics2D g) {
        mm.draw(g);
    }

    public void keyPressed(int k) {
        mm.keyPressed(k);
    }

    public void keyReleased(int k) {
        mm.keyReleased(k);
    }
}
