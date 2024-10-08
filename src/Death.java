import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Death {
	private static int angel;
	private static ImageIcon angelIcon;
	private static String[][] trueLightLines = {
		{"Rush"}
	};
	private static String[][] curiousLightLines = {
		{"Rush"}
	};
	private static String[][] mischievousLightLines = {
		{"Rush"}
	};

	public Death() {
		int angelnum = (int)(Math.random() * 15);
		if(angelnum <= 3)
			angel = 0;
		if(angelnum == 4)
			angel = 1;
		else
			angel = 3;
	}

	public void createAngel(Graphics g) {
		if(angel == 0) {
			g.setColor(new Color(64, 224, 208));
			angelIcon = "truelight";
			g.drawImage(angelIcon.getImage(), 300, 300, 500, 0, null);
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