public abstract class Entity {
	protected String name;
	protected int hp, dmg, x, y;
	protected double speed;
	
	public Entity(String name, int hp, int dmg, double speed) {
		x = 0;
		y = 0;
		this.name = name;
		this.hp = hp;
		this.dmg = dmg;
		this.speed = speed;
	}

	public void shoot() {
		// Code shooting here
	}
	
	public void move() {
		x += 1;
		y += 1;
	}

	public int getCurX() {
		return x;
	}

	public int getCurY() {
		return y;
	}

	public abstract void attackPatterns();

	public abstract void sound();
}