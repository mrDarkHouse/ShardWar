package com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells;

import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;

public class GlobalShot extends Ability<Tower> {

    @Override
    public void build() {
        owner.setGlobal(true);
    }
}
