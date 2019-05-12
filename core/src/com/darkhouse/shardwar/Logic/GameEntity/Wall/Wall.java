package com.darkhouse.shardwar.Logic.GameEntity.Wall;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.darkhouse.shardwar.Logic.GameEntity.DamageReceiver;
import com.darkhouse.shardwar.Logic.GameEntity.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;

public abstract class Wall extends GameObject implements DamageReceiver {
//    private Texture texture;

    public abstract static class WallPrototype extends ObjectPrototype {

        public WallPrototype(Texture texture, String name, int health, int cost, int bounty) {
            super(texture, name, health, cost, bounty);
        }
    }

    public Wall(WallPrototype prototype) {
        super(prototype);
    }

    @Override
    public Vector2 getShootPosition(int line) {
        return new Vector2(slot.getX() + slot.getParent().getX() + slot.getWidth()/2,
                slot.getY() + slot.getParent().getY() + slot.getHeight()/2);
    }

    @Override
    public WallPrototype getObjectPrototype() {
        return ((WallPrototype) objectPrototype);
    }

    public abstract int receiveDamage(int damage, DamageSource source);

    @Override
    public void physic(float delta) {

    }
}
