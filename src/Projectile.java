import java.io.IOException;

public abstract class Projectile {
	protected int dmg;
	protected int x, y;
	protected int velX, velY;

	public Projectile(int dmg, int x, int y, int velX, int velY) {
		this.x = x;
		this.y = y;
		this.dmg = dmg;
		this.velX = velX;
		this.velY = velY;
	}

	public void move() {
		// Code moving here
	}

	public abstract void attackPatterns();

	public abstract void sound();
}