package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.darkhouse.shardwar.Logic.DamageReceiver;
import com.darkhouse.shardwar.Logic.GameEntity.Entity;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.Projectile;
import com.darkhouse.shardwar.Logic.Slot;
import com.darkhouse.shardwar.Logic.TowerSlot;

import java.util.Arrays;

public abstract class Tower extends GameObject {

    public abstract static class TowerPrototype<T extends Tower> extends ObjectPrototype {
        private int dmg;
        private int shoots;
        private float shootDelay;

        public TowerPrototype(Texture texture, String name, int health, int cost, int dmg, int bounty) {
            this(texture, name, health, cost, dmg, bounty, 1, 0f);
        }
        public TowerPrototype(Texture texture, String name, int health, int cost, int dmg, int bounty, int shoots, float shootDelay){
            super(texture, name, health, cost, bounty);
            this.dmg = dmg;
            this.shoots = shoots;
            this.shootDelay = shootDelay;
        }

        @Override
        public abstract T getObject();


        public abstract String getProjectileTexture();
    }

    public Tower(TowerPrototype prototype) {
        super(prototype);
        this.dmg = prototype.dmg;
        this.shoots = prototype.shoots;
        this.shootDelay = prototype.shootDelay;
        this.shootTime = shootDelay;
    }


    protected int dmg;
    private boolean canShoot;

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    private float shootDelay;
    private float shootTime;
    private int shootNum;
    private int shoots;

    @Override
    public TowerPrototype getObjectPrototype() {
        return ((TowerPrototype) objectPrototype);
    }

    public int getDmg() {
        return dmg;
    }

    public void attack(DamageReceiver g){};

    @Override
    public int receiveDamage(int damage, GameObject source) {
        return damage;
    }

    @Override
    public Slot<TowerPrototype, Tower> getSlot() {
        return ((Slot<TowerPrototype, Tower>) slot);
    }

    private void shootProjectile(int i){
        Entity dr = slot.getOwner().searchTarget(getSlot(), getSlot().getSelected()[i]);
        Vector2 startLocation = new Vector2(getSlot().getX() + getSlot().getParent().getX() + getSlot().getWidth() / 2,
                getSlot().getY() + getSlot().getParent().getY() + getSlot().getHeight() / 2);
        Projectile pr = new Projectile(getSlot(), startLocation, dr, getSlot().getSelected()[i]);
        slot.getStage().addActor(pr);
        slot.getOwner().projectiles.add(pr);
    }

    public void physic(float delta){
        if(canShoot){
            if(shootTime >= shootDelay){
                shootProjectile(shootNum);
                afterShoot();
            }else shootTime += delta;
        }
    }
    private void afterShoot(){
        shootNum++;
        shootTime = 0;
        if(shootNum >= shoots) {
            canShoot = false;
            shootNum = 0;
            shootTime = shootDelay;
            getSlot().flushSelect();
            getSlot().flushTargeter();
        }
    }

}
