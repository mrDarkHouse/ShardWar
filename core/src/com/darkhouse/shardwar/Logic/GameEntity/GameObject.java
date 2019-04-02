package com.darkhouse.shardwar.Logic.GameEntity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Array;
import com.darkhouse.shardwar.Logic.DamageReceiver;
import com.darkhouse.shardwar.Logic.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.EffectIcon;
import com.darkhouse.shardwar.Logic.Slot;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameObject implements DamageReceiver, DamageSource {

    public abstract static class ObjectPrototype {

        protected Texture texture;
        protected String name;
        protected int cost;
        protected int maxHealth;
        protected int bounty;

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

        public ObjectPrototype(Texture texture, String name, int health, int cost, int bounty) {
            this.texture = texture;
            this.name = name;
            this.maxHealth = health;
            this.cost = cost;
            this.bounty = bounty;
        }

        public abstract GameObject getObject();
    }

    public class EffectBar extends WidgetGroup {
        private float staticWidth;
        private float staticHeight;

        public void setStaticSize(float width, float height){
            this.staticWidth = width;
            this.staticHeight = height;
        }
        //        public float getStaticWidth() {
//            return staticWidth;
//        }
        public float getStaticHeight() {
            return staticHeight;
        }

        public Array<EffectIcon> getChildrenArray() {
//            return super.getChildren();
            Array<EffectIcon> icons = new Array<EffectIcon>();
            for (Actor a:super.getChildren()){
                icons.add(((EffectIcon) a));
            }
            return icons;
        }

        @Override
        public void addActor(Actor actor) {
            super.addActor(actor);
            updatePosition();
        }

        private void updatePosition(){
            int size = getChildrenArray().size;
            for (int i = 0; i < size; i++){
                getChildrenArray().get(i).setY(staticHeight - (i + 1) * (getChildrenArray().get(i).getHeight() + 2));
//                float y = staticHeight/2 - getChildrenArray().get(i).getWidth()/2*size - 3.5f*(size-1);
//                getChildrenArray().get(i).setY(y + 7*i + 20*i);
//                float x = staticWidth/2 - getChildrenArray().get(i).getWidth()/2*size - 3.5f*(size-1);
//                getChildrenArray().get(i).setX(x + 7*i +20*i);
                pack();
            }


        }
        public EffectBar() {

        }

        private void updateEffectIcon(){
            for (EffectIcon a:getChildrenArray()){
                a.updateDurationTime();
            }
        }

        public void removeIcon(Texture icon){
            for (EffectIcon e:getChildrenArray()) {
                if (e.getIcon() == icon) {
                    removeActor(e);
                    updatePosition();
                }
            }
        }
    }

//    private ArrayList<Effect> effects;
    protected HashMap<Class<? extends Effect.IEffectType> , Array<Effect>> effects;
    public EffectBar effectBar;

    private void setEffect(Effect d){
        Class<? extends Effect.IEffectType> tt = d.getType();
        if(effects.containsKey(tt)){
            effects.get(tt).add(d);
        }else {
            Array<Effect> arr = new Array<Effect>();
            arr.add(d);
            effects.put(tt, arr);
        }
    }

    public void addEffect(Effect e){
        if(!haveEffect(e.getClass())) {
            setEffect(e);
            e.apply();//start effect
//            if(!e.isHidden()) {
                EffectIcon ei = new EffectIcon(e);
                ei.setSize(effectBar.getStaticHeight()/3, effectBar.getStaticHeight()/3);
                effectBar.addActor(ei);
                ei.init();
//            }
        }else {
            getEffect(e.getClass()).updateDuration();
        }
    }

    public boolean haveEffect(Class e){
        return (getEffect(e) != null);
    }
    public Array<Effect> getEffects(){
        Array<Effect> a = new Array<Effect>();
        for (java.util.Map.Entry<Class<? extends Effect.IEffectType> , Array<Effect>> e:effects.entrySet()){
//            System.out.println("a " + e.getValue() + "|" + e.getKey());
            for (Effect ab:e.getValue()){
                a.add(ab);
            }
        }
        return a;
    }
    public Effect getEffect(Class d){
        for (Effect e:getEffects()){
            if(e.getClass() == d) return e;
        }
        return null;
    }

    public void deleteEffect(Class d){
        for (java.util.Map.Entry<Class<? extends Effect.IEffectType> , Array<Effect>> e:effects.entrySet()){
            for (Effect ab:e.getValue()){
                if(ab.getClass() == d){
                    e.getValue().removeValue(ab, true);
//                    if(ab.getIconPath() != null) {
                        effectBar.removeIcon(ShardWar.main.getAssetLoader().getEffectIcon(ab.getName()));
                        effectBar.pack();
//                    }
                }
            }
        }
    }

    public void actEffects(){
        for (Effect e:getEffects()){
            e.nextTurn();
            effectBar.updateEffectIcon();
        }
    }


    protected Texture texture;
//    private TextureRegion t
    protected String name;
    protected int cost;
    protected int maxHealth;
    protected int health;
    protected Slot slot;
    protected ObjectPrototype objectPrototype;
    protected int bounty;
    protected Player owner;

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public Player getOwnerPlayer() {
        return owner;
    }

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
        this.bounty = prototype.bounty;
        this.objectPrototype = prototype;
        effects = new HashMap<Class<? extends Effect.IEffectType>, Array<Effect>>();
        effectBar = new EffectBar();
//        effectBar.debug();
        effectBar.debugAll();
    }

    public void init(){
        effectBar.setStaticSize(slot.getWidth(), slot.getHeight());
        effectBar.setPosition(slot.getX() + slot.getParent().getX() + slot.getWidth() + 3,
                slot.getY() + slot.getParent().getY());
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

    public void dmg(int dmg, DamageSource source){
        health -= receiveDamage(dmg, source);
//        System.out.println("attacked " + health);
        if(health <= 0) die(source);
        else slot.hasChanged();
//        ShardWar.fightScreen.hasChanged();
    }

    public abstract int receiveDamage(int damage, DamageSource source);
    public abstract void physic(float delta);

    private void die(DamageSource source){
        slot.clearObject();
        slot.hasChanged();
        slot = null;
        source.getOwnerPlayer().addShards(bounty);
    }


}
