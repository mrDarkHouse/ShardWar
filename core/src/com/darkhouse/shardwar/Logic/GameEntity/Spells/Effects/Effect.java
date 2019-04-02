package com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;

public abstract class Effect<T extends GameObject> {

    public interface IEffectType{

    }
    public interface IAttack extends IEffectType{
        void attack();
    }
    public interface IGetDmg extends IEffectType{
        int getDmg();
    }
    public interface INone extends IEffectType{

    }

    protected T owner;
    private String name;
    private boolean positive;
    private int duration;
    private int currentTime;
    private Class<? extends IEffectType> type;

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

    public void nextTurn(){
        currentTime--;
        if(currentTime <= 0) {
            removeEffect();
        }
    }

    public void updateDuration(){
        currentTime = duration;
//        if (owner.haveEffect(this.getClass())) {
//            currentTime = duration;
//        }
    }


}