package Main;
import javax.swing.JFrame;

//Programmer: Hans Wang
//Project Title: Polygonal Run
//Project description: A remake of Geometry Dash. Play as a cube, ship, ball, or wave and weave through a level of obstacles to win.

public class Game {
	public static void main(String[] args) {
		JFrame window = new JFrame("Polygonal Run");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}
}