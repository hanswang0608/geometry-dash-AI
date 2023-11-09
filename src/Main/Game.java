package Main;
import javax.swing.JFrame;

/**
 * Programmer: Hans Wang
 * Project Title: Polygonal Run
 * Project description: A remake of Geometry Dash. Play as a cube, ship, ball, or wave and weave through a level of obstacles to win.
 * Date: 2019-01-18
 * 
 * Update 2023-11-09: The project is revived with a new name "GD Evolved". More polished and with AI trained to play by genetic algorithms.
 */


public class Game {
	public static final String APP_NAME = "GD Evolved";
	public static void main(String[] args) {
		JFrame window = new JFrame(APP_NAME);
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}
}