package Engine;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Player;
import Abstract.Entity;

public class Engine {

	static class rect {
		public int screencoords[];
		public int texturecoords[];
		public BufferedImage texture;
		public double distance;

		public rect(int s[], int t[], BufferedImage c) {
			screencoords = s;
			texturecoords = t;
			texture = c;
		}

		public rect(int s[], int t[], BufferedImage c, Double d) {
			screencoords = s;
			texturecoords = t;
			texture = c;
			distance = d;
		}
	}

	private static final int transparencyLimit = 2;
	private static final ExecutorService executor = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private static final Future<?> futures[] = new Future<?>[800 * 2];
	private static final rect rects[] = new rect[transparencyLimit * 800];

	private static ArrayList<Entity> entities = new ArrayList<>();

	public static int[][] map;
	private static int mapLen = 0;

	public static void setMap(int m[][]) {
		map = m;
		mapLen = map.length;
	}

	public static void draw(Graphics g, BufferedImage image) {
		drawFloor(image);
		renderWalls();
		for (int i = 0; i < transparencyLimit; i++) {
			int n = 0;
			for (int j = 0; j < 800; j++) {
				rect r = rects[800 * i + j];
				if (r == null)
					continue;
				Future<?> future = executor.submit(() -> g.drawImage(Textures.wall, r.screencoords[0],
						r.screencoords[1], r.screencoords[2], r.screencoords[3], r.texturecoords[0], r.texturecoords[1],
						r.texturecoords[2], r.texturecoords[3], null));
				futures[n] = future;
				futures[n] = future;
				n++;
			}
			for (Future<?> future : futures) {
				try {
					if (future != null)
						future.get();
				} catch (InterruptedException | ExecutionException f) {
				}
			}
		}

		Arrays.setAll(rects, i -> null);
	}

	public static void renderWalls() {

		for (int x = 0; x < 800; x++) {
			double c = x / 400.0 - 1;
			double raydirX = Player.dirX + c * Player.planeX;
			double raydirY = Player.dirY + c * Player.planeY;
			double dx = (raydirX == 0) ? 1e20 : Math.abs(1 / raydirX);
			double dy = (raydirY == 0) ? 1e20 : Math.abs(1 / raydirY);

			int side = 0;

			int mapX = (int) Player.posX;
			int mapY = (int) Player.posY;

			double lenX;
			double lenY;
			double stepX;
			double stepY;

			if (raydirX < 0) {
				lenX = (Player.posX - mapX) * dx;
				stepX = -1;
			} else {
				lenX = (1 - Player.posX + mapX) * dx;
				stepX = 1;
			}
			if (raydirY < 0) {
				lenY = (Player.posY - mapY) * dy;
				stepY = -1;
			} else {
				lenY = (1 + mapY - Player.posY) * dy;
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
						double texturedist = dist * raydirY + Player.posY;
						wallcoord = texturedist - (int) texturedist;
					} else {
						dist = (lenY - dy);
						double texturedist = dist * raydirX + Player.posX;
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
							Textures.wall);
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

	public static void drawFloor(BufferedImage image) {

		DataBufferInt imageBuffer = (DataBufferInt) image.getRaster().getDataBuffer();
		DataBufferInt textureBuffer = (DataBufferInt) Textures.floor.getRaster().getDataBuffer();

		int imageData[] = imageBuffer.getData();
		int textureData[] = textureBuffer.getData();

		for (int x = 0; x < 800; x++) {
			double c = x / 400.0 - 1;
			double raydirX = Player.dirX + c * Player.planeX;
			double raydirY = Player.dirY + c * Player.planeY;
			for (int y = 0; y < 400; y++) {

				double perpDist = 400.0 / y;
				double pixelx = (raydirX * perpDist + Player.posX);
				double pixely = (raydirY * perpDist + Player.posY);
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

	public static void renderEnemies(){
		int sx;
		double sd;
		for (int i = 0; i < entities.size(); i++){
			double x = entities.get(i).getX();
			double y = entities.get(i).getY();
			double dist = Math.sqrt((x-Player.posX)*(x-Player.posX) + (y-Player.posY)*(y-Player.posY));
			double angleC = Math.atan(Math.abs(Player.dirY)/Math.abs(Player.dirX));
			if (Player.dirX < 0) angleC = Math.PI-angleC;
			if (Player.dirY < 0) angleC *= -1;
			if (angleC < 0) angleC+=2*Math.PI;
			double angleE = Math.atan(Math.abs(Player.posY-y)/Math.abs(Player.posX-x));
			if (x-Player.posX < 0) angleE = Math.PI-angleE;
			if (y-Player.posY < 0) angleE *= -1;
			if (angleE < 0) angleE+=2*Math.PI;
			double angledif = angleE-angleC;
			if (angledif > Math.PI) angledif-=Math.PI*2;
			if (angledif < -Math.PI) angledif+=Math.PI*2;
			if (Math.abs(angledif) < 0.78){
				sd = dist*Math.abs(Math.cos(angledif));
				sx = (int)((dist*Math.sin(-angledif)/sd)/0.65*400+400);
				int spriteDim = (int)(350/sd);
				int screenCoords[] = {sx-spriteDim/2, 400-spriteDim/2, sx+spriteDim/2, 400+spriteDim/2};
				rect r = new rect(, null, null);
				enemysprites.push_back(E);
			}
		}
	}

}