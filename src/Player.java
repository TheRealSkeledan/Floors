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

	public void sounds() {
		// COde sounds here
	}
}