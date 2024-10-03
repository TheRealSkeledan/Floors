
//required import statements
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Engine extends JPanel {


	static class rect {
		public int pos[] = new int[2];
		public int size[] = new int[2];
		public Color color;

		public rect(int x, int y, int h, int w, Color c) {
			pos[0] = x;
			pos[1] = y;
			size[0] = h;
			size[1] = w;
			color = c;
		}
	}
	
	private static final int map[][] = {
			{ 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 0, 0, 0, 1, 1 },
			{ 1, 1, 1, 0, 1, 1, 1 },
			{ 1, 1, 1, 2, 1, 1, 1 }
	};

	private static double posX = 3;
	private static double posY = 3;
	private static double dirX = -1;
	private static double dirY = 0;
	private static double planeX = 0;
	private static double planeY = 0.65;

	private static final double moveSpeed = 0.1;
	private static final double rotSpeed = 0.04;

	private static final ArrayList<rect> rects = new ArrayList<>();
	private static final ArrayList<Double> dists = new ArrayList<>();

	private class Keyboard implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {

			double oldX = posX;
			double oldY = posY;

			switch (e.getKeyChar()) {
				case 'w' -> {
					posX += dirX * moveSpeed;
					posY += dirY * moveSpeed;
				}
				case 'a' -> {
					posX -= planeX * moveSpeed;
					posY -= planeY * moveSpeed;
				}
				case 's' -> {
					posX -= dirX * moveSpeed;
					posY -= dirY * moveSpeed;
				}
				case 'd' -> {
					posX += planeX * moveSpeed;
					posY += planeY * moveSpeed;
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				double oldDirX = dirX;
				dirX = dirX * Math.cos(rotSpeed) - dirY * Math.sin(rotSpeed);
				dirY = oldDirX * Math.sin(rotSpeed) + dirY * Math.cos(rotSpeed);
				double oldPlaneX = planeX;
				planeX = planeX * Math.cos(rotSpeed) - planeY * Math.sin(rotSpeed);
				planeY = oldPlaneX * Math.sin(rotSpeed) + planeY * Math.cos(rotSpeed);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
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

		@Override
		public void keyReleased(KeyEvent e) {
		}

	}

	// required global variables
	private final BufferedImage image;
	private BufferedImage walltexture;
	private BufferedImage elevatortexture;
	private final Graphics g;
	private final Timer timer;

	// Constructor required by BufferedImage
	public Engine() throws IOException {

		image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		g = image.getGraphics();

		walltexture = ImageIO.read(new File("assets/images/textures/NoWhy.jpg"));
		elevatortexture = ImageIO.read(new File("assets/images/textures/door.png"));

		timer = new Timer(10, new TimerListener());
		timer.start();
		addKeyListener(new Keyboard());
		setFocusable(true);
	}

	// TimerListener class that is called repeatedly by the timer
	private class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 800);

			renderWalls();

			// for (rect r: rects){
			// g.setColor(Color.white);
			// g.drawRect(r.pos[0],r.pos[1], r.size[0], r.size[1]);
			// // System.out.println(r.size[0] + ", " + r.size[1]);
			// }

			rects.clear();

			repaint();
		}

	}

	// do not modify this
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	void renderWalls() {
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

			boolean hit = false;

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

			for (int i = 0; i < 30; i++) {
				if (map[mapX][mapY] == 1) {
					double dist;
					if (side == 0) {
						dist = (lenX - dx);
						double texturedist = dist*raydirY+posY;
                    	wallcoord = texturedist-Math.floor(texturedist);
					} else {
						dist = (lenY - dy);
						double texturedist = dist*raydirX+posX;
                    	wallcoord = texturedist-Math.floor(texturedist);
					}
					TexturePaint t = new TexturePaint((BufferedImage) walltexture, new Rectangle((int)(wallcoord*100), 0, 1, 100));
					int lineHeight = (int) (800 / (dist));
					g.drawImage(walltexture, (int)x, (int)(400 - lineHeight / 2), (int)x+1, (int)(400 + lineHeight / 2), (int)(wallcoord*240), 0, (int)(wallcoord*240)+1, 320, this);
					// g.fillRect(x, 400 - lineHeight / 2, 1, lineHeight);

					break;
				}
				if(map[mapX][mapY] == 2) {
					double dist;
					if (side == 0) {
						dist = (lenX - dx);
						double texturedist = dist*raydirY+posY;
                    	wallcoord = texturedist-Math.floor(texturedist);
					} else {
						dist = (lenY - dy);
						double texturedist = dist*raydirX+posX;
                    	wallcoord = texturedist-Math.floor(texturedist);
					}
					TexturePaint t = new TexturePaint((BufferedImage) elevatortexture, new Rectangle((int)(wallcoord*100), 0, 1, 100));
					int lineHeight = (int) (800 / (dist));
					g.drawImage(elevatortexture, (int)x, (int)(400 - lineHeight / 2), (int)x+1, (int)(400 + lineHeight / 2), (int)(wallcoord*240), 0, (int)(wallcoord*240)+1, 320, this);
					// g.fillRect(x, 400 - lineHeight / 2, 1, lineHeight);

					break;
				}
				if (!hit) {

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

	public static void drawThings() {

	}

	// main method with standard graphics code
	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame("Animation Shell");
		frame.setSize(800, 800);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Engine());
		frame.setVisible(true);
	}

}