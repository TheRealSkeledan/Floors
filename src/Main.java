import Engine.Engine;
import Engine.Textures;
import Rooms.Room;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main extends JPanel {


	private class Keyboard implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {

			switch (e.getKeyChar()) {
				case 'w' -> {
					keys[0] = true;
				}
				case 'a' -> {
					keys[1] = true;
				}
				case 's' -> {
					keys[2] = true;
				}
				case 'd' -> {
					keys[3] = true;
				}
				case 'h' -> {
					keys[6] = true;
				}
				case 'q' -> {
					Player.switchItem(-1);
				}
				case 'e' -> {
					Player.switchItem(1);
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				keys[4] = true;
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				keys[5] = true;
			}

			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				keys[7] = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

			switch (e.getKeyChar()) {
				case 'w' -> {
					keys[0] = false;
				}
				case 'a' -> {
					keys[1] = false;
				}
				case 's' -> {
					keys[2] = false;
				}
				case 'd' -> {
					keys[3] = false;
				}
				case 'h' -> {
					keys[6] = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				keys[4] = false;
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				keys[5] = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				keys[7] = false;
			}
		}
	}

	private class MouseWheel implements MouseWheelListener {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int notches = e.getWheelRotation();
			if (notches < 0) {
				Player.switchWeapons(1);
			} else {
				Player.switchWeapons(-1);
			}
		}
	}


	private static final boolean keys[] = new boolean[10];

	private final BufferedImage image;
	private final Graphics g;
	private final Timer timer;
	private int hints = 0;


	public Main() throws IOException {

		image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		g = image.getGraphics();

		

		timer = new Timer(10, new TimerListener());
		timer.start();
		addKeyListener(new Keyboard());
		addMouseWheelListener(new MouseWheel());
		setFocusable(true);

	}

	private class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			long a = System.nanoTime();
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 800);
			move();

			Engine.draw(g, image);
			
			repaint();
			long dif = System.nanoTime() - a;

			// int waitTime = 1000 / 60 - (int) (dif / 1000000);

			// if (waitTime > 0) {
			// 	try {
			// 		Thread.sleep(waitTime);
			// 	} catch (InterruptedException ex) {

			// 	}
			// }

			// dif += waitTime * 1000000;

			double fps = 1000000000.0 / dif;
			g.setColor(Color.red);
			g.drawString("FPS: " + Integer.toString((int) fps), 10, 10);

			Player.drawHands(g);
			Player.drawHealthBar(g);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		if (hints < 1) {
			ImageIcon hint = new ImageIcon("assets/images/hints/hint.png");
			g.drawImage(hint.getImage(), 0, 520, 100, 100, null);
		}

		if (keys[6]) {
			Death.showHints(g);
			hints++;
		}
	}


	public void move() {

		double oldX = Player.posX;
		double oldY = Player.posY;

		double rot = Player.rotSpeed;
		double mov = Player.moveSpeed;

		if (keys[7]){
			rot += Player.rotBoost;
			mov += Player.moveBoost;
		}

		if (keys[0]) {
			Player.posX += Player.dirX * mov;
			Player.posY += Player.dirY * mov;
		}
		if (keys[1]) {
			Player.posX -= Player.planeX * mov;
			Player.posY -= Player.planeY * mov;
		}
		if (keys[2]) {
			Player.posX -= Player.dirX * mov;
			Player.posY -= Player.dirY * mov;
		}
		if (keys[3]) {
			Player.posX += Player.planeX * mov;
			Player.posY += Player.planeY * mov;
		}
		if(keys[6]) {
			Player.changeHealth(-10);
		}

		if (keys[4]) {
			double olddirX = Player.dirX;
			Player.dirX = Player.dirX * Math.cos(rot) - Player.dirY * Math.sin(rot);
			Player.dirY = olddirX * Math.sin(rot) + Player.dirY * Math.cos(rot);
			double oldplaneX = Player.planeX;
			Player.planeX = Player.planeX * Math.cos(rot) - Player.planeY * Math.sin(rot);
			Player.planeY = oldplaneX * Math.sin(rot) + Player.planeY * Math.cos(rot);
		} else if (keys[5]) {
			double olddirX = Player.dirX;
			Player.dirX = Player.dirX * Math.cos(-rot) - Player.dirY * Math.sin(-rot);
			Player.dirY = olddirX * Math.sin(-rot) + Player.dirY * Math.cos(-rot);
			double oldplaneX = Player.planeX;
			Player.planeX = Player.planeX * Math.cos(-rot) - Player.planeY * Math.sin(-rot);
			Player.planeY = oldplaneX * Math.sin(-rot) + Player.planeY * Math.cos(-rot);
		}

		

		if (Engine.map[(int) Player.posX][(int) Player.posY] != 0) {
			Player.posX = oldX;
			Player.posY = oldY;
		}
	}

	// main method with standard graphics code
	public static void main(String[] args) throws IOException, InterruptedException {
		System.setProperty("sun.java2d.opengl", "true");
		JFrame frame = new JFrame("Floors");
		Player.init();
		Textures.init();
		Engine.setMap(Room.createRoom());
		frame.setSize(800, 800);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Main());
		frame.setVisible(true);
	}

}