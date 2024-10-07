import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Engine extends JPanel {

	static class rect {
		public int screencoords[];
		public int texturecoords[];
		public BufferedImage texture;

		public rect(int s[], int t[], BufferedImage c, ImageObserver o) {
			screencoords = s;
			texturecoords = t;
			texture = c;
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

			if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
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
			if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
				keys[7] = false;
				moveSpeed -= 0.1;
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

	private static final int transparencyLimit = 2;

	rect rects[] = new rect[transparencyLimit * 800];

	private static boolean keys[] = new boolean[8];

	private final BufferedImage image;
	private final BufferedImage walltexture;
	private final BufferedImage doortexture;
	private final BufferedImage floortexture;
	private final Graphics g;
	private final Timer timer;

	private Player player = new Player();

	private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public Engine() throws IOException {

		image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		g = image.getGraphics();

		walltexture = ImageIO.read(new File("assets/images/textures/wall.png"));
		doortexture = ImageLoader.loadImageAsRGB("assets/images/textures/door.png");
		floortexture = ImageLoader.loadImageAsRGB("assets/images/textures/floor.png");

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
			drawFloor();
			renderWalls();
			for (int i = 0; i < transparencyLimit; i++) {
				Future<?> futures[] = new Future<?>[800];
				int n = 0;
				for (int j = 0; j < 800; j++) {
					rect r = rects[800 * i + j];
					if (r == null)
						continue;
					Future<?> future = executor.submit(() -> g.drawImage(walltexture, r.screencoords[0],
							r.screencoords[1], r.screencoords[2], r.screencoords[3], r.texturecoords[0],
							r.texturecoords[1], r.texturecoords[2], r.texturecoords[3], null));
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

			int waitTime = 1000 / 60 - (int) (dif / 1000000);

			if (waitTime > 0) {
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException ex) {

				}
			}

			dif += waitTime*1000000;

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
						wallcoord = texturedist - (int) texturedist;
					} else {
						dist = (lenY - dy);
						double texturedist = dist * raydirX + posX;
						wallcoord = texturedist - (int) texturedist;
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

	// public void renderEntities(){
	// double sx;
	// double sd;
	// for (int i = 0; i < enemies.size(); i++){
	// double x = enemies[i].getX();
	// double y = enemies[i].getY();
	// double dist = sqrt((x-posX)*(x-posX) + (y-posY)*(y-posY));
	// double angleC = std::atan(std::abs(dirY)/std::abs(dirX));
	// if (dirX < 0) angleC = M_PI-angleC;
	// if (dirY < 0) angleC *= -1;
	// if (angleC < 0) angleC+=2*M_PI;
	// double angleE = std::atan(std::abs(posY-y)/std::abs(posX-x));
	// if (x-posX < 0) angleE = M_PI-angleE;
	// if (y-posY < 0) angleE *= -1;
	// if (angleE < 0) angleE+=2*M_PI;
	// double angledif = angleE-angleC;
	// if (angledif > M_PI) angledif-=M_PI*2;
	// if (angledif < -M_PI) angledif+=M_PI*2;
	// RectangleShape E;
	// if (std::abs(angledif) < 0.78){
	// int alpha = 255-0.88*dist*dist;
	// if (alpha < 0){
	// alpha = 0;
	// }
	// E.setFillColor(Color(255, 255, 255, alpha));
	// if (enemies[i].isDamaged()){
	// E.setFillColor(Color(255, 0, 0, alpha));
	// }
	// E.setTexture(&s);
	// if (enemies[i].deathprogress() == 1){
	// E.setTexture(&d);
	// E.setTextureRect(IntRect(0, 0, 360, 360));
	// E.setFillColor(Color::White);
	// }
	// sd = dist*std::abs(cos(angledif));
	// sx = (dist*sin(-angledif)/sd)/0.65*400+400;
	// E.setSize(Vector2f(700/sd, 700/sd));
	// E.setPosition(sx-350/sd, 400-350/sd);
	// enemysprites.push_back(E);
	// enemydists.push_back(sd);
	// }
	// else{
	// E.setFillColor(Color::Transparent);
	// }
	// }
	// }

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
		JFrame frame = new JFrame("Floors");
		Player p = new Player();
		frame.setSize(800, 800);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Engine());
		frame.setVisible(true);
	}

}