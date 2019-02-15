package com.darkhouse.shardwar.Logic.GameEntity.Wall;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.DamageReceiver;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.Slot;

public abstract class Wall extends GameObject implements DamageReceiver {
//    private Texture texture;

    public abstract static class WallPrototype extends ObjectPrototype {

        public WallPrototype(Texture texture, String name, int health, int cost) {
            super(texture, name, health, cost);
        }
    }

    public Wall(WallPrototype prototype) {
        super(prototype);
    }

    @Override
    public WallPrototype getObjectPrototype() {
        return ((WallPrototype) objectPrototype);
    }

    public abstract int receiveDamage(int damage, GameObject source);

    @Override
    public void physic(float delta) {

    }
}
