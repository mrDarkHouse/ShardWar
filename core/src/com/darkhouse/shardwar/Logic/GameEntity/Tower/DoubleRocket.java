package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells.GlobalShot;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells.MultiShot;
import com.darkhouse.shardwar.ShardWar;

public class DoubleRocket extends Tower {

    public static class P extends TowerPrototype<DoubleRocket>{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/doublerocket.png", Texture.class),
                    ShardWar.main.getAssetLoader().getWord("doublerocket"), 18, 0, 2, 5,
                    new GlobalShot(false), new MultiShot(2, 0.6f));
        }

        @Override
        public DoubleRocket getObject() {
            return new DoubleRocket(this);
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/doublerocket.png";
        }
    }

    public DoubleRocket(P prototype) {
        super(prototype);
    }
}
