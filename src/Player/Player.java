package Player;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	private static float opacity = 0.5f;

	private static int coins = 0, finCoins = 0, floorNum = 100;

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

	public static void addCoins(int amtCoin) {
		finCoins = coins + amtCoin;
	}

	public static void floorUp() {
		floorNum++;
	}

	public static void drawUI(Graphics g) {
		String back;
		if(floorNum < 10) {
			back = "000";
		}
		else if(floorNum < 100) {
			back = "00";
		}
		else if(floorNum < 1000) {
			back = "0";
		} 
		else if(floorNum < 10000) {
			back = "";
		} else {
			back = "Doesn't Matter";
		}

		g.setFont(new Font("Helvetica", Font.BOLD, 30));
		g.setColor(Color.WHITE);

		if(coins < finCoins) {
			coins += 2;
		}
		if(coins > 9999) {
			coins = 9999;
		}

		ImageIcon coin = new ImageIcon("assets/images/textures/ui/coins.png");
		g.drawImage(coin.getImage(), 0, 100, 100, 100, null);
		g.drawString(coins + "", 120, 175);

		ImageIcon boss = new ImageIcon("assets/images/textures/ui/bossIcon.png");
		if(floorNum == 30 || floorNum == 50 || floorNum == 80 || floorNum == 100) {
			g.drawImage(boss.getImage(), 0, 350, 100, 100, null);
			g.setColor(Color.RED);
		}
		
		ImageIcon floorIcon = new ImageIcon("assets/images/textures/ui/floorNum.png");
		g.drawImage(floorIcon.getImage(), 0, 220, 100, 100, null);
		g.drawString(back + floorNum, 120, 293);
	}

	public static void changeHealth(Graphics g, int amount, int w, int h) {
		hp += amount;

		if(hp > 200) {
			hp = 200;
		}

		Graphics2D g2d = (Graphics2D) g;
		ImageIcon hurt = new ImageIcon("assets/images/textures/overlay/damageOverlay.png");

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

		if(Player.getHealth() <= 25) {
			g2d.drawImage(hurt.getImage(), 0, 0, w, h, null);
		}

		if(Player.getHealth() > 100) {
			ImageIcon heal = new ImageIcon("assets/images/textures/overlay/healOverlay.png");
			g2d.drawImage(heal.getImage(), 0, 0, w, h, null);
		}

		opacity--;
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

	// Items stuff
	public static String getItem() {
		return items.get(curItem);
	}

	public static void addItem(String item) {
		items.add(item);
	}

	public static void switchItem(int num) {
		curItem += num;
		if (curItem < 0) {
			curItem = items.size() - 1;
		} else if (curItem == items.size()) {
			curItem = 0;
		}
	}

	// Weapons stuff
	public static String getWeapon() {
		return weapons.get(curWeapon);
	}

	public static void addWeapon(String weapon) {
		weapons.add(weapon);
	}

	public static void switchWeapons(int num) {
		curWeapon += num;
		if (curWeapon < 0) {
			curWeapon = weapons.size() - 1;
		} else if (curWeapon == weapons.size()) {
			curWeapon = 0;
		}
	}

	public static void sounds() {
		// COde sounds here
	}
}