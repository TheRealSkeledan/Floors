import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
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
				moveSpeed += 0.1;
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
				moveSpeed -= 0.1;
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

	private static final int map[][] = Room.createRoom();
	private static final int mapLen = map.length;

	private static double posX = 3;
	private static double posY = 3;
	private static double dirX = -1;
	private static double dirY = 0;
	private static double planeX = 0;
	private static double planeY = 0.65;

	private static double moveSpeed = 0.06;
	private static final double rotSpeed = 0.03;

	private static boolean keys[] = new boolean[10];

	private final BufferedImage image;
	private final BufferedImage walltexture;
	private final BufferedImage doortexture;
	private final BufferedImage floortexture;
	private final Graphics g;
	private final Timer timer;
	private int hints = 0;

	private Player player = new Player();


	public Main() throws IOException {

		image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		g = image.getGraphics();

		walltexture = ImageIO.read(new File("assets/images/textures/wall.png"));
		doortexture = ImageLoader.loadImageAsRGB("assets/images/textures/door.png");
		floortexture = ImageLoader.loadImageAsRGB("assets/images/textures/floor.png");

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
			
			repaint();
			long dif = System.nanoTime() - a;

			int waitTime = 1000 / 60 - (int) (dif / 1000000);

			if (waitTime > 0) {
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException ex) {

				}
			}

			dif += waitTime * 1000000;

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


	public static void move() {

		double oldX = posX;
		double oldY = posY;

		if (keys[0]) {
			posX += dirX * moveSpeed;
			posY += dirY * moveSpeed;
		}
		if (keys[1]) {
			posX -= planeX * moveSpeed;
			posY -= planeY * moveSpeed;
		}
		if (keys[2]) {
			posX -= dirX * moveSpeed;
			posY -= dirY * moveSpeed;
		}
		if (keys[3]) {
			posX += planeX * moveSpeed;
			posY += planeY * moveSpeed;
		}

		if (keys[4]) {
			double oldDirX = dirX;
			dirX = dirX * Math.cos(rotSpeed) - dirY * Math.sin(rotSpeed);
			dirY = oldDirX * Math.sin(rotSpeed) + dirY * Math.cos(rotSpeed);
			double oldPlaneX = planeX;
			planeX = planeX * Math.cos(rotSpeed) - planeY * Math.sin(rotSpeed);
			planeY = oldPlaneX * Math.sin(rotSpeed) + planeY * Math.cos(rotSpeed);
		} else if (keys[5]) {
			double oldDirX = dirX;
			dirX = dirX * Math.cos(-rotSpeed) - dirY * Math.sin(-rotSpeed);
			dirY = oldDirX * Math.sin(-rotSpeed) + dirY * Math.cos(-rotSpeed);
			double oldPlaneX = planeX;
			planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(-rotSpeed);
			planeY = oldPlaneX * Math.sin(-rotSpeed) + planeY * Math.cos(-rotSpeed);
		}

		if (map[(int) posX][(int) posY] != 0) {
			posX = oldX;
			posY = oldY;
		}
	}

	// main method with standard graphics code
	public static void main(String[] args) throws IOException, InterruptedException {
		System.setProperty("sun.java2d.opengl", "true");
		JFrame frame = new JFrame("Floors");
		Player p = new Player();
		frame.setSize(1280, 720);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Main());
		frame.setVisible(true);
	}

}