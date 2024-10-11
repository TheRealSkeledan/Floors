package Abstract;

import java.awt.image.BufferedImage;

public abstract class Entity {
	protected String name;
	protected int hp, dmg, x, y;
	protected double speed;
	protected BufferedImage image;

	public Entity(String name, int hp, int dmg, double speed) {
		x = 0;
		y = 0;
		this.name = name;
		this.hp = hp;
		this.dmg = dmg;
		this.speed = speed;
	}

	public void shoot() {
		// hindustan zindabad
	}

	public void move() {
		x += 1;
		y += 1;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public abstract void attackPatterns();

	public abstract void sound();
}