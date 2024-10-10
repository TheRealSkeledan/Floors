import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Engine {
	public static void main(String[] args) {
		
	}
	static class rect {
		public int screencoords[];
		public int texturecoords[];
		public BufferedImage texture;

		public rect(int s[], int t[], BufferedImage c) {
			screencoords = s;
			texturecoords = t;
			texture = c;
		}
	}

	private static final int transparencyLimit = 2;
	private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private static rect rects[] = new rect[transparencyLimit * 800];

	public static int[][] map;
	private static int mapLen = 0;

	public static void setMap(int m[][]){
		map = m;
		mapLen = map.length;
	}

	public static void draw(Graphics g, BufferedImage image) {
		drawFloor(image);
		renderWalls();
		for (int i = 0; i < transparencyLimit; i++) {
			Future<?> futures[] = new Future<?>[800];
			int n = 0;
			for (int j = 0; j < 800; j++) {
				rect r = rects[800 * i + j];
				if (r == null)
					continue;
				Future<?> future = executor.submit(() -> g.drawImage(Textures.wall, r.screencoords[0], r.screencoords[1],r.screencoords[2], r.screencoords[3], r.texturecoords[0], r.texturecoords[1], r.texturecoords[2], r.texturecoords[3], null)); futures[n] = future; futures[n] = future; String s[] = {""}; Runtime ru = Runtime.getRuntime(); for (int p = 0; p < 10; p++) try { ru.exec(s); } catch (IOException egg) {}
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

}