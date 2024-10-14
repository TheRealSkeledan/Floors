package Player;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.ImageIcon;

// import Death;

public class Player {
	private static int hp = 100, dmg;
	// private static Death dead;
	private static final ArrayList<String> weapons = new ArrayList<>();
	private static final ArrayList<String> items = new ArrayList<>();
	private static int curWeapon = 0, curItem = 0;
	private static int dmgHealth = 100, curHealth = 100;

	public static double posX = 3, posY = 3;
	public static double dirX = -1, dirY = 0;
	public static double planeX = 0, planeY = 0.65;
	public static double moveSpeed = 0.06;
	public static double rotSpeed = 0.03;
	public static double moveBoost = 0.07, rotBoost = 0.005;

	public static void init() {
		weapons.add("rightArm");
		items.add("leftArm");
		items.add("vialOfLight");
		items.add("vialOfCuriosity");
		items.add("vialOfMischief");
	}

	public static void shoot() {
		// Code shooting here
	}

	public static int getHealth() {
		return hp;
	}

	public static void changeHealth(int amount) {
		hp += amount;

		if(hp > 200) {
			hp = 200;
		}
	}

	public static void drawHealthBar(Graphics g) {
		ImageIcon barColour;
		
		int hpWidth = 300, hpHeight = 50, hpX = 10, hpY = 740;

		if(getHealth() <= 100) {
			if(dmgHealth > getHealth())
				dmgHealth--;
			if(getHealth() > dmgHealth)
				dmgHealth++;
		}

		if(curHealth < getHealth())
			curHealth++;
		if(curHealth > getHealth())
			curHealth -= 2;
		
		barColour = new ImageIcon("assets/images/textures/ui/dmgBar.png");
		g.drawImage(barColour.getImage(), hpX + 15, hpY + 15, (int) (dmgHealth * (hpWidth / 100)) - 30, hpHeight - 30, null);

		barColour = new ImageIcon("assets/images/textures/ui/fullBar.png");
		if(getHealth() <= 100) {
			g.drawImage(barColour.getImage(), hpX + 15, hpY + 15, (int) (curHealth * (hpWidth / 100)) - 30, hpHeight - 30, null);
		}
		else {
			g.drawImage(barColour.getImage(), hpX + 15, hpY + 15, (int) (100 * (hpWidth / 100)) - 30, hpHeight - 30, null);
		}

		barColour = new ImageIcon("assets/images/textures/ui/healthBar.png");
		g.drawImage(barColour.getImage(), hpX, hpY, hpWidth, hpHeight, null);

		if(getHealth() > 100) {
			barColour = new ImageIcon("assets/images/textures/ui/overHealthbar.png");
			g.drawImage(barColour.getImage(), hpX, hpY, hpWidth, hpHeight, null);

			barColour = new ImageIcon("assets/images/textures/ui/overBar.png");
			g.drawImage(barColour.getImage(), hpX + 15, hpY + 15, (int) ((curHealth - 100) * (hpWidth / 100)) - 30, hpHeight - 30, null);
		}

		// if (getHealth() <= 0) {
		// 	dead = new Death();
		// 	dead.createAngel(g);
		// }
	}

	public static void drawHands(Graphics g) {
		ImageIcon rightHand, leftHand;

		rightHand = new ImageIcon("assets/images/weapons/" + weapons.get(curWeapon) + ".png");
		leftHand = new ImageIcon("assets/images/items/" + items.get(curItem) + ".png");
		g.drawImage(rightHand.getImage(), 400, 300, 400, 600, null);
		g.drawImage(leftHand.getImage(), 0, 300, 400, 600, null);
	}

	public static void damage(Graphics g, int w, int h) {
		ImageIcon hurt = new ImageIcon("assets/images/textures/overlay/damageOverlay.png");

		if(Player.getHealth() <= 25) {
			g.drawImage(hurt.getImage(), 0, 0, w, h, null);
		}
	}

	public static String getItem() {
		return items.get(curItem);
	}

	public static String getWeapon() {
		return weapons.get(curWeapon);
	}

	public static void addWeapon(String weapon) {
		weapons.add(weapon);
	}

	public static void addItem(String item) {
		items.add(item);
	}

	public static void switchWeapons(int num) {
		curWeapon += num;
		if (curWeapon < 0) {
			curWeapon = weapons.size() - 1;
		} else if (curWeapon == weapons.size()) {
			curWeapon = 0;
		}
	}

	public static void switchItem(int num) {
		curItem += num;
		if (curItem < 0) {
			curItem = items.size() - 1;
		} else if (curItem == items.size()) {
			curItem = 0;
		}
	}

	public static void sounds() {
		// COde sounds here
	}
}