package Entities;

import Abstract.Entity;

public class RedSun extends Entity {

    public RedSun(String name, int hp, int dmg, double speed) {
        super(name, hp, dmg, speed);
        image = Textures.
    }

    @Override
    public void shoot() {
    };

    @Override
    public void move() {
    };

    @Override
    public int getX() {
        return x;
    };

    @Override
    public int getY() {
        return y;
    };

    @Override
    public void attackPatterns() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sound() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
