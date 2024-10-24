import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Death {
	private static int angel, angelX, angelY, angelW, angelH;
	private static ImageIcon angelIcon;
	private static String[][] trueLightLines = {
			{ "Rush" }
	};
	private static String[][] curiousLightLines = {
			{ "Rush" }
	};
	private static String[][] mischievousLightLines = {
			{ "Rush" }
	};

	public Death() {
		int angelnum = (int) (Math.random() * 15);
		if (angelnum <= 3)
			angel = 0;
		if (angelnum == 4)
			angel = 1;
		else
			angel = 3;
	}

	public void deathScreen(Graphics g) {
		
	}

	public void createAngel(Graphics g) {
		if (angel == 0) {
			g.setColor(new Color(64, 224, 208));
			angelIcon = new ImageIcon("assets/images/entities/curiousAngel.png");
			System.out.println("Curious Angel Speaks");
			angelW = 300;
			angelH = 300;
		} else if (angel == 1) {
			g.setColor(new Color(145, 17, 34));
			angelIcon = new ImageIcon("assets/images/entities/mischievousCelestial.png");
			System.out.println("Mischievous Celestial Speaks");
		} else {
			g.setColor(new Color(127, 255, 212));
			angelIcon = new ImageIcon("assets/images/entities/trueAngel.png");
			System.out.println("True Angel Speaks");
			angelW = 300;
			angelH = 498;
		}
		g.fillRect(0, 0, 1200, 1200);
		g.drawImage(angelIcon.getImage(), 300, 0, angelW, angelH, null);
	}

	public static void showHints(Graphics g) {
		String path = "assets/images/hints/";
		ImageIcon arrow = new ImageIcon(path + "arrowHint.png");
		ImageIcon mouseScroll = new ImageIcon(path + "mouseScrollHint.png");
		ImageIcon shift = new ImageIcon(path + "shiftHint.png");
		ImageIcon wasd = new ImageIcon(path + "wasdHint.png");
		ImageIcon qe = new ImageIcon(path + "qeHint.png");
		g.drawImage(wasd.getImage(), 0, 0, 100, 66, null);
		g.drawImage(shift.getImage(), 0, 66, 100, 39, null);
		g.drawImage(mouseScroll.getImage(), 0, 105, 100, 170, null);
		g.drawImage(qe.getImage(), 0, 275, 100, 50, null);
		g.drawImage(arrow.getImage(), 0, 325, 100, 50, null);
	}

	private static void talk() {

	}

}