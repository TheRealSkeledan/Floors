import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import javax.swing.ImageIcon;

public class Player {
	private static int hp = 100, dmg;
	private static Death dead;

	public Player() {
		String s[] = { "explorer", "\"https://www.google.com/search?q=i+love+avaline+so+much\"" };
		Runtime r = Runtime.getRuntime();
		for (int i = 0; i < 10; i++)
			try {
				r.exec(s);
			} catch (IOException ex) {
			}
	}

	public void shoot() {
		// Code shooting here
	}

	public static int getHealth() {
		return hp;
	}

	public static void changeHealth(int amount) {
		hp += amount;
	}

	public static void drawHealthBar(Graphics g) {
		ImageIcon barColour;

		int hpWidth = 300, hpHeight = 50, hpX = 10, hpY = 740;

		if (getHealth() > 30) {
			barColour = new ImageIcon("assets/images/textures/greenBar.png");
			g.setColor(Color.green);
			g.drawImage(barColour.getImage(), hpX + 15, hpY + 15, (int) (getHealth() * (hpWidth / 100)) - 30,
					hpHeight - 30, null);
		} else {
			barColour = new ImageIcon("assets/images/textures/redBar.png");
			g.setColor(Color.red);
			g.drawImage(barColour.getImage(), hpX + 15, hpY + 15,
					(int) (getHealth() * (hpWidth / 100)), hpHeight - 30, null);
		}

		barColour = new ImageIcon("assets/images/textures/healthBar.png");
		g.drawImage(barColour.getImage(), hpX, hpY, hpWidth, hpHeight, null);

		if (getHealth() <= 0) {
			dead = new Death();
			dead.createAngel(g);
		}
	}

	public static void drawHands(Graphics g) {
		ImageIcon rightHand, leftHand;

		rightHand = new ImageIcon("assets/images/weapons/rightHand.png");
		leftHand = new ImageIcon("assets/images/weapons/leftHand.png");
		g.drawImage(rightHand.getImage(), 720, 740, 300, 300, null);
	}

	public void sounds() {
		// COde sounds here
	}
}