package Abstract;

import java.awt.image.BufferedImage;

public abstract class Entity {
	protected String name;
	protected int hp, dmg;
	protected double x, y;
	protected double speed;
	protected BufferedImage image;

	public Entity(String name, int hp, int dmg, double speed, double X, double Y) {
		x = X;
		y = Y;
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

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public BufferedImage getImage(){
		return image;
	}

	public abstract void attackPatterns();

	public abstract void sound();
}