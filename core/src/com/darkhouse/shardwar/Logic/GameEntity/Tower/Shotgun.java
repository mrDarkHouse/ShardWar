package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.ShardWar;

public class Shotgun extends Tower {

    public static class P extends TowerPrototype<Shotgun>{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/shotgun.png", Texture.class),
                    ShardWar.main.getAssetLoader().getWord("shotgun"), 15, 5, 3, 3);
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/shotgun.png";
        }

        @Override
        public Shotgun getObject() {
            return new Shotgun(this);
        }
    }

    public Shotgun(P prototype) {
        super(prototype);
    }
}
