package Main;

import javax.swing.JPanel;
import GameState.GameStateManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener {
	//dimensions
	public static final int WIDTH = 640;
	public static final int HEIGHT = 448;
	public static final int SCALE = 1;
	
	//thread
	private Thread thread;
	private boolean running;
	private static int frames;
	private static int ticks;
		
	//image
	private BufferedImage image;
	private Graphics2D g;
	
	//game state manager
	private GameStateManager gsm;
	
	//key value array
	private boolean[] keys;

	// game physics
	public static int numTicks = 60;
	public static int maxNumFrames = 60;

	private final String[] modes = {"", "Training", "AI"};
	private final Font modeFont = new Font("Calibri", Font.BOLD, 20);
	
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
			addMouseListener(this);
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
		ticks = 0;
	}
	
	//game loop
	public void run() {
		init();
		
		long lastTime = System.nanoTime();
		double delta = 0;
		long timer = System.currentTimeMillis();
		int framesPerTick = 0;
		while (running) {
			double ns = 1000000000 / (double)numTicks;
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
				framesPerTick = 0;
				ticks++;
			}
			if (running && framesPerTick < (double)maxNumFrames/numTicks) {
				draw();
				drawToScreen();
				framesPerTick++;
				frames++;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				// System.out.println("FPS: " + frames + "\tTicks: " + ticks);
				frames = 0;
				ticks = 0;
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

		// draw mode indicator overlay
		g.setColor(new Color(1, 1, 1, 0.5f));
		g.setFont(modeFont);
		g.drawString(modes[gsm.getMode()], 5, 20);
	
	}
	
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		gsm.keyPressed(keyCode);
		
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_SPACE) {
			keys[0] = true;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		gsm.keyReleased(keyCode);

		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_SPACE) {
			keys[0] = false;
		}
	}

	public void keyTyped(KeyEvent e) {}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) keys[0] = true;
	}
	
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) keys[0] = false;
	}

	/* unused mouse events */
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}