package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.DamageReceiver;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells.GlobalShot;
import com.darkhouse.shardwar.ShardWar;

public class Artillery extends Tower {

    public static class P extends TowerPrototype<Artillery>{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/assault.png", Texture.class),
                    "artillery", 10, 10, 2, 3, new GlobalShot());
        }

        @Override
        public Artillery getObject() {
            return new Artillery(this);
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/assault.png";
        }
    }

    public Artillery(P prototype) {
        super(prototype);
    }

    public void attack(DamageReceiver g) {
        g.dmg(dmg, this);
    }
}
