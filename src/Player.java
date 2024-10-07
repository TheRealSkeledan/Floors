import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Player {
	private static int hp = 100, dmg;
	private static Death dead;
	private static String[] weapons = {"rightArm", "gun", "crucifix"};
	private static int curWeapon = 0;

	public Player() {
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

		rightHand = new ImageIcon("assets/images/weapons/" + weapons[curWeapon] + ".png");
		leftHand = new ImageIcon("assets/images/weapons/leftArm.png");
		g.drawImage(rightHand.getImage(), 400, 300, 400, 600, null);
		g.drawImage(leftHand.getImage(), 0, 300, 400, 600, null);
	}

	public static void switchWeapons(int num) {
		curWeapon += num;
		if(curWeapon < 0) {
			curWeapon = weapons.length - 1;
		}
		else if(curWeapon == weapons.length) {
			curWeapon = 0;
		}
	}

	public void sounds() {
		// COde sounds here
	}
}