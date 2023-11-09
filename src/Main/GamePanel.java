package Main;

import javax.swing.JPanel;
import GameState.GameStateManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener{
	//dimensions
	public static final int WIDTH = 640;
	public static final int HEIGHT = 448;
	public static final int SCALE = 1;
	
	//thread
	private Thread thread;
	private boolean running;
	private static int frames;
		
	//image
	private BufferedImage image;
	private Graphics2D g;
	
	//game state manager
	private GameStateManager gsm;
	
	//key value array
	private boolean[] keys;
	
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	public synchronized void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		
		gsm = new GameStateManager();
		
		keys = new boolean[1];
		
		frames = 0;
	}
	
	//game loop
	public void run() {
		init();
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		int maxAmountOfFrames = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int framesPerTick = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
				framesPerTick = 0;
			}
			if (running && framesPerTick < maxAmountOfFrames/amountOfTicks) {
				draw();
				drawToScreen();
				framesPerTick++;
				frames++;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//gsm is a class i made to handle different game states
	private void update() {
		gsm.update();
		gsm.keyUpdate(keys);
	}
	
	private void draw() {
		gsm.draw(g);
	}
	
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent key) {}
	
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();

		gsm.keyPressed(keyCode);
		
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_SPACE) {
			keys[0] = true;
		}
	}
	
	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();

		gsm.keyReleased(keyCode);

		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_SPACE) {
			keys[0] = false;
		}
	}
}