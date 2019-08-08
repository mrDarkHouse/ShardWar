package com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;

public abstract class Ability<T extends GameObject>{

    protected T owner;

    public void setOwner(T owner) {
        this.owner = owner;
    }

    public abstract void build();
    public abstract String getTooltip();
    public abstract String getName();
}
