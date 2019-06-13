package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells.MultiShot;
import com.darkhouse.shardwar.ShardWar;

public class DoubleBarrel extends Tower {

    public static class P extends TowerPrototype<DoubleBarrel>{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/doublebarrel.png", Texture.class),
                    ShardWar.main.getAssetLoader().getWord("doublebarrel"), 25, 0, 3, 5,
                    new MultiShot(2, 0.3f));
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/doublebarrel.png";
        }

        @Override
        public DoubleBarrel getObject() {
            return new DoubleBarrel(this);
        }
    }

    public DoubleBarrel(P prototype) {
        super(prototype);
    }
}
