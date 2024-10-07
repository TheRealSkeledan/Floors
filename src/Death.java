import java.awt.Color;
import java.awt.Graphics;

public class Death {
	private static int angel;
	private static int[][] trueLightLines;
	private static int[][] curiousLightLines;
	private static int[][] mischievousLightLines;

	public Death() {
		int angelnum = (int)(Math.random() * 15);
		if(angelnum <= 3)
			angel = 0;
		if(angelnum == 4)
			angel = 1;
		else
			angel = 3;
		
	}

	public static void createAngel(Graphics g) {
		if(angel == 0) {
			g.setColor(new Color(64, 224, 208));
			System.out.println("Curious Angel Speaks");
		}
		else if(angel == 1) {
			g.setColor(new Color(145, 17, 34));
			System.out.println("Mischievous Celestial Speaks");
		}
		else {
			g.setColor(new Color(127, 255, 212));
			System.out.println("True Angel Speaks");
		}
		g.fillRect(0, 0, 1200, 1200);
	}

	private static void talk() {

	}
	
}