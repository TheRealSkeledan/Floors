import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Player {
	private static int hp = 100, dmg;
	private static int angel;
	
	public Player() {
		int angelnum = (int)(Math.random() * 15);
		if(angelnum <= 3)
			angel = 0;
		if(angelnum == 4)
			angel = 1;
		else
			angel = 3;
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
	}

public void sounds() {
		// COde sounds here
	}
}