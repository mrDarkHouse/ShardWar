package com.darkhouse.shardwar.Logic.GameEntity;

import com.badlogic.gdx.math.Vector2;

public class Empty extends GameObject{

    public Empty() {
        super();
    }

    @Override
    public Vector2 getShootPosition(int line) {
        return null;
    }
//    @Override
//    public Vector2 getShootPosition(int line) {
//        return new Vector2(slot.getX() + slot.getParent().getX() + slot.getWidth()/2,
//                slot.getY() + slot.getParent().getY() + slot.getHeight()/2);
//    }

    @Override
    public int receiveDamage(int damage, DamageSource source) {
        return 0;
    }

    @Override
    public void physic(float delta) {

    }
}
