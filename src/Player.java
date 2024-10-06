import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Player {
	private static int hp = 100, dmg, x, y;
	protected double speed;
	
	public Player() {
		x = 0;
		y = 0;
		this.dmg = dmg;
		this.speed = speed;
	}

	public void shoot() {
		// Code shooting here
	}
	
	public void move() {
		// Code moving here
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

		if (getHealth() > 20) {
			barColour = new ImageIcon("assets/images/textures/greenBar.png");
			g.setColor(Color.green);
			g.drawImage(barColour.getImage(), hpX + 15, hpY + 15, (int) (getHealth() * (hpWidth / 100)) - 30,
					hpHeight - 30, null);
		} else {
			barColour = new ImageIcon("assets/images/textures/redBar.png");
			g.setColor(Color.red);
			g.drawImage(barColour.getImage(), hpX + 15, hpY + 15,
					(int) (getHealth() * (hpWidth / 100) + 30) - 30, hpHeight - 30, null);
		}

		barColour = new ImageIcon("assets/images/textures/healthBar.png");
		g.drawImage(barColour.getImage(), hpX, hpY, hpWidth, hpHeight, null);

		if (getHealth() <= 0) {
			g.fillRect(0, 0, 1200, 1200);
		}
	}

public void sounds() {
		// COde sounds here
	}
}