package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells.GlobalShot;
import com.darkhouse.shardwar.ShardWar;

public class Cannon extends Tower {

    public static class P extends TowerPrototype<Cannon>{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/cannon.png", Texture.class),
                    ShardWar.main.getAssetLoader().getWord("cannon"), 15, 0, 4, 5,
                    new GlobalShot(true));
        }

        @Override
        public Cannon getObject() {
            return new Cannon(this);
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/cannon.png";
        }
    }

    public Cannon(P prototype) {
        super(prototype);
    }
}
