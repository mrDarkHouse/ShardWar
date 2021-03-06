package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.darkhouse.shardwar.Logic.GameEntity.DamageReceiver;
import com.darkhouse.shardwar.Logic.GameEntity.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells.Ability;
import com.darkhouse.shardwar.Logic.Projectile;
import com.darkhouse.shardwar.Logic.Slot.Slot;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.Arrays;

public abstract class Tower extends GameObject {

    public abstract static class TowerPrototype<T extends Tower> extends ObjectPrototype {
        private int dmg;

        public int getDmg() {
            return dmg;
        }

        //        public TowerPrototype(Texture texture, String name, int health, int cost, int dmg, int bounty,
//                              Ability... abilities) {
//            this(texture, name, health, cost, dmg, bounty, abilities);
//        }
        public TowerPrototype(Texture texture, String name, int health, int cost, int dmg, int bounty,
                              Ability... abilities){
            super(texture, texture, name, health, cost, bounty, abilities);
            this.dmg = dmg;
        }

        @Override
        public abstract T getObject();


        public abstract String getProjectileTexture();
    }

    public Tower(TowerPrototype prototype) {
        super(prototype);
        this.dmg = prototype.dmg;
//        this.shoots = prototype.shoots;
        this.shootDelay = 0;
        this.shootTime = shootDelay;
    }

    @Override
    public Vector2 getShootPosition(int line) {
        return new Vector2(slot.getX() + slot.getParent().getX() + slot.getWidth()/2,
                slot.getY() + slot.getParent().getY() + slot.getHeight()/2);
    }

//    @Override
//    public Vector2 getShootPosition(int line) {
//        return new Vector2(slot.getX(), slot.getY());
//    }

    private boolean global;
    public boolean isGlobal() {
        return global;
    }
    public void setGlobal(boolean global) {
        this.global = global;
    }

    protected int dmg;
    protected int bonusDmg;
    private boolean canShoot;

    public void addBonusDmg(int value){
        bonusDmg += value;
    }

    @Override
    public String getTooltip() {
        AssetLoader l = ShardWar.main.getAssetLoader();
        String s = "";

        s += l.getWord("health") + ": " + health + "/" + maxHealth + System.getProperty("line.separator") +
                l.getWord("dmg") + ": ";
        if(slot.selected.length > 1 && bonusDmg != 0) s += "(";
        s += dmg;
        if(bonusDmg > 0) s += "+" + FontLoader.colorString(String.valueOf(bonusDmg),0);
        if(bonusDmg < 0) s += FontLoader.colorString(String.valueOf(bonusDmg),4);
        if(slot.selected.length > 1 && bonusDmg != 0) s += ")";

        if(slot.selected.length > 1) s += "x" + slot.selected.length;

        String t = getEffectTooltip();
        if(t.length() > 0) s += System.getProperty("line.separator") + t;

        return s;
    }

    private boolean disarm;

    public boolean isDisarm() {
        return disarm;
    }
    public void setDisarm(boolean disarm) {
        this.disarm = disarm;
    }

    public void setCanShoot(boolean canShoot) {
        this.shoots = slot.getNumberSelected();
        this.canShoot = canShoot;
    }

    private float shootDelay;

    public void setShootDelay(float shootDelay) {
        this.shootDelay = shootDelay;
        this.shootTime = shootDelay;
    }

    private float shootTime;
    private int shootNum;
    private int shoots;

    @Override
    public TowerPrototype getObjectPrototype() {
        return ((TowerPrototype) objectPrototype);
    }

    public int getDmg() {
        if(dmg + bonusDmg >= 0) return dmg + bonusDmg;
        else return 0;
    }

    public void setDmg(int dmg) {
        if(dmg > 0) this.dmg = dmg;
        else this.dmg = 0;
    }

    public void attack(GameObject g) {
        int realDmg = getDmg();
        if (effects.containsKey(Effect.IPreAttack.class)) {
            for (Effect e : effects.get(Effect.IPreAttack.class)) {
                realDmg = ((Effect.IPreAttack) e).preAttack(g, realDmg);
            }
        }
        g.dmg(realDmg, this, isImmune());
        if (effects.containsKey(Effect.IAfterAttack.class)) {
            for (Effect e : effects.get(Effect.IAfterAttack.class)) {
                ((Effect.IAfterAttack) e).afterAttack(g, realDmg);
            }
        }
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

    @Override
    public void die(DamageSource source, boolean giveBounty, boolean disableSlotAfter) {
        if(slot != null) slot.clearSelecting();
        else System.out.println("Slot null " + this);
        super.die(source, giveBounty, disableSlotAfter);
    }

    private void shootProjectile(int i){
//        if(getSlot().getSelected()[i] == -1) return;//target not chosen
        Slot dr;
        if(getSlot().getObject().isGlobal()) {
            dr = slot.getOwner().searchTarget(getSlot(), getSlot().getSelected()[i], getSlot().getSelectedRow()[i]);
        }else {
            dr = slot.getOwner().searchTarget(getSlot(), getSlot().getSelected()[i], -1);
        }
        if(dr.getObject() == null) return;
//        Vector2 startLocation = new Vector2(getSlot().getX() + getSlot().getParent().getX() + getSlot().getWidth() / 2,
//                getSlot().getY() + getSlot().getParent().getY() + getSlot().getHeight() / 2);
        Projectile pr;
        if(getSlot().getObject().isGlobal()) {
            pr = new Projectile(getSlot(), getSlot().getCenter(), dr, getSlot().getSelected()[i], getSlot().getSelectedRow()[i]);
        }else {
            pr = new Projectile(getSlot(), getSlot().getCenter(), dr, getSlot().getSelected()[i]);
        }
        slot.getOwner().projectiles.add(pr);
        slot.getStage().addActor(pr);
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
//            getSlot().flushNumberSelected();
        }
    }

}
