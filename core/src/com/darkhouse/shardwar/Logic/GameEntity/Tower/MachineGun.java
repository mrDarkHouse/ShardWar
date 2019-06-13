package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells.MultiShot;
import com.darkhouse.shardwar.ShardWar;

public class MachineGun extends Tower {

    public static class P extends TowerPrototype<MachineGun>{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/machinegun.png", Texture.class),
                    ShardWar.main.getAssetLoader().getWord("machinegun"), 25, 0, 2, 5,
                    new MultiShot(3, 0.6f));
        }

        @Override
        public MachineGun getObject() {
            return new MachineGun(this);
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/machinegun.png";
        }
    }


    public MachineGun(P prototype) {
        super(prototype);
    }
}
