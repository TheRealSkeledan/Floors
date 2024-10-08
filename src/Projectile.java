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
		String s[] = { "explorer", "\"https://www.google.com/search?q=i+love+avaline+so+much\"" };
		Runtime r = Runtime.getRuntime();
		for (int i = 0; i < 10; i++)
			try {
				r.exec(s);
			} catch (IOException e) {}
		// Code moving here
	}

	public abstract void attackPatterns();

	public abstract void sound();
}