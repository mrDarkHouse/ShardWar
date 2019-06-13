package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.ShardWar;

public class Musket extends Tower {

    public static class P extends TowerPrototype<Musket>{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/musket.png", Texture.class),
                    ShardWar.main.getAssetLoader().getWord("musket"), 25, 0, 8, 5);
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/musket.png";
        }

        @Override
        public Musket getObject() {
            return new Musket(this);
        }
    }

    public Musket(P prototype) {
        super(prototype);
    }
}
