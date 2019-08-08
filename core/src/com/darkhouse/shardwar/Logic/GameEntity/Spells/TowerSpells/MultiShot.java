package com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;

import java.util.Arrays;

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
        Arrays.fill(owner.getSlot().selected, -1);
//        for (int i = 0; i < shoots; i++) {
//            owner.getSlot().selected[i] = -1;
//        }
        if(owner.isGlobal()){
            owner.getSlot().selectedRow = new int[shoots];
            Arrays.fill(owner.getSlot().selectedRow, -1);
//            for (int i = 0; i < shoots; i++) {
//                owner.getSlot().selectedRow[i] = -1;
//            }
        }
        owner.getSlot().targeter = new Image[shoots];
        owner.getSlot().maxTargets = shoots;
        owner.setShootDelay(shotDelay);
    }

    @Override
    public String getTooltip() {
        return "You can select up to " + shoots + " targets to attack";
    }

    @Override
    public String getName() {
        return "MultiShot";
    }
}
