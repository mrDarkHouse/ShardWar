package com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells;

import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;

public class GlobalShot extends Ability<Tower> {

    private boolean attackPlayer;

    public GlobalShot(boolean attackPlayer) {
        this.attackPlayer = attackPlayer;
    }

    @Override
    public void build() {
        owner.setGlobal(true);
        owner.getSlot().selectedRow = new int[]{-1};
    }

    @Override
    public String getTooltip() {
        return "You can select any target on field";
    }

    @Override
    public String getName() {
        return "GlobalShot";
    }
}
