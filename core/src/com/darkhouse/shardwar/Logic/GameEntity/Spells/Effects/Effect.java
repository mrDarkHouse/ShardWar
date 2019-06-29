package com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;

public abstract class Effect<T extends GameObject> {

    public interface IEffectType{

    }
    public interface IPreAttack extends IEffectType{
        int preAttack(GameObject target, int dmg);
    }
    public interface IAfterAttack extends IEffectType{
        void afterAttack(GameObject target, int dmg);
    }
    public interface IGetDmg extends IEffectType{
        int getDmg(int dmg);
    }
    public interface INone extends IEffectType{

    }

    protected T owner;
    private String name;
    private boolean positive;
    private int duration;
    private int currentTime;
    private Class<? extends IEffectType> type;

    public Effect<T> setOwner(T owner){
        this.owner = owner;
        return this;
    }

    public String getName() {
        return name;
    }
    public boolean isPositive() {
        return positive;
    }
    public int getDuration() {
        return duration;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public Class<? extends IEffectType> getType() {
        return type;
    }

    public Effect(String name, boolean positive, int duration, Class<? extends IEffectType> type) {
        this.name = name;
        this.positive = positive;
        this.duration = duration;
        currentTime = duration;
        this.type = type;
    }

    public abstract void apply();
    public abstract void dispell();

    public void removeEffect(){
        dispell();
        owner.deleteEffect(this.getClass());
    }

    protected void act(){

    }

    public void nextTurn(){
        act();
        currentTime--;
        if(currentTime <= 0) {
            removeEffect();
        }
    }

    public void updateDuration(int duration){
        currentTime = duration;
//        if (owner.haveEffect(this.getClass())) {
//            currentTime = duration;
//        }
    }


}
