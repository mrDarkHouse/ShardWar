package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.darkhouse.shardwar.Logic.GameEntity.DamageReceiver;
import com.darkhouse.shardwar.Logic.GameEntity.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.Entity;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.Projectile;
import com.darkhouse.shardwar.Logic.Slot.Slot;

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
//        this.shoots = prototype.shoots;
        this.shootDelay = prototype.shootDelay;
        this.shootTime = shootDelay;
    }


    protected int dmg;
    private boolean canShoot;

    private boolean disarm;

    public void setDisarm(boolean disarm) {
        this.disarm = disarm;
    }

    public boolean isDisarm() {
        return disarm;
    }

    public void setCanShoot(boolean canShoot) {
        this.shoots = slot.getNumberSelected();
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

    public void setDmg(int dmg) {
        if(dmg > 0) this.dmg = dmg;
        else this.dmg = 0;
    }

    public void attack(DamageReceiver g){};

    @Override
    public int receiveDamage(int damage, DamageSource source) {
        return damage;
    }

    @Override
    public Slot<TowerPrototype, Tower> getSlot() {
        return ((Slot<TowerPrototype, Tower>) slot);
    }

    public void physic(float delta){
        if(canShoot){
            if(shootTime >= shootDelay){
                shot();
            }else shootTime += delta;
        }
    }

    protected void shot(){
//        if(!disarm) {
            shootProjectile(shootNum);
            afterShoot();
//        }
    }

    private void shootProjectile(int i){
        Entity dr = slot.getOwner().searchTarget(getSlot(), getSlot().getSelected()[i]);
        Vector2 startLocation = new Vector2(getSlot().getX() + getSlot().getParent().getX() + getSlot().getWidth() / 2,
                getSlot().getY() + getSlot().getParent().getY() + getSlot().getHeight() / 2);
        Projectile pr = new Projectile(getSlot(), startLocation, dr, getSlot().getSelected()[i]);
        slot.getStage().addActor(pr);
        slot.getOwner().projectiles.add(pr);
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
