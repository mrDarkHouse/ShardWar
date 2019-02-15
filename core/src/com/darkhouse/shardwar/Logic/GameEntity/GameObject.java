package com.darkhouse.shardwar.Logic.GameEntity;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.DamageReceiver;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.Slot;
import com.darkhouse.shardwar.ShardWar;

public abstract class GameObject implements DamageReceiver {

    public abstract static class ObjectPrototype {

        protected Texture texture;
        protected String name;
        protected int cost;
        protected int maxHealth;

        public Texture getTexture() {
            return texture;
        }
        public String getName() {
            return name;
        }
        public int getCost() {
            return cost;
        }
        public int getMaxHealth() {
            return maxHealth;
        }

        public ObjectPrototype(Texture texture, String name, int health, int cost) {
            this.texture = texture;
            this.name = name;
            this.maxHealth = health;
            this.cost = cost;
        }

        public abstract GameObject getObject();
    }


    protected Texture texture;
//    private TextureRegion t
    protected String name;
    protected int cost;
    protected int maxHealth;
    protected int health;
    protected Slot slot;
    protected ObjectPrototype objectPrototype;

    public ObjectPrototype getObjectPrototype() {
        return objectPrototype;
    }
    //    abstract float getX();
//    abstract float getY();
//    abstract float getWidth();
//    abstract float getHeight();

    public Texture getTexture() {
        return texture;
    }
    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Slot getSlot() {
        return slot;
    }

    public GameObject(ObjectPrototype prototype) {
        this.texture = prototype.texture;
        this.name = prototype.name;
        this.health = prototype.maxHealth;
        this.maxHealth = prototype.maxHealth;
        this.objectPrototype = prototype;
    }
//    public static GameObject create(ObjectPrototype prototype){
//        if(prototype instanceof Tower.TowerPrototype){
//
//        }
//    }
    //    public GameObject(Texture texture, String name, int health, int cost) {
//        this.texture = texture;
//        this.name = name;
//        this.maxHealth = health;
//        this.health = health;
//        this.cost = cost;
//    }

    public int getHealth() {
        return health;
    }

    public void dmg(int dmg, GameObject source){
        health -= receiveDamage(dmg, source);
//        System.out.println("attacked " + health);
        if(health <= 0) die();
        else slot.hasChanged();
//        ShardWar.fightScreen.hasChanged();
    }

    public abstract int receiveDamage(int damage, GameObject source);
    public abstract void physic(float delta);

    private void die(){
        slot.clearObject();
        slot.hasChanged();
        slot = null;
    }
}
