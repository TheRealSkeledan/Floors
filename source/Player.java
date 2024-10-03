public class Player {
	private int hp, dmg, x, y;
	protected double speed;
	
	public Player(int hp, int dmg, double speed) {
		x = 0;
		y = 0;
		this.hp = hp;
		this.dmg = dmg;
		this.speed = speed;
	}

	public void shoot() {
		// Code shooting here
	}
	
	public void move() {
		// Code moving here
	}

	public void sounds() {
		// COde sounds here
	}
}