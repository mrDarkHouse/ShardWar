package com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;

public class MultiShot extends Ability<Tower>{

    private int shoots;
    private float shotDelay;

    public MultiShot(int shoots, float shotDelay) {
        this.shoots = shoots;
        this.shotDelay = shotDelay;
    }

    @Override
    public void build() {
        owner.getSlot().selected = new int[shoots];
        for (int i = 0; i < shoots; i++) {
            owner.getSlot().selected[i] = -1;
        }
        owner.getSlot().targeter = new Image[shoots];
        owner.getSlot().maxTargets = shoots;
        owner.setShootDelay(shotDelay);
    }
}
