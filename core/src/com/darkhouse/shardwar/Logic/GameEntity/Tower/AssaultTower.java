package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.DamageReceiver;
import com.darkhouse.shardwar.ShardWar;

public class AssaultTower extends Tower{

    public static class P extends TowerPrototype{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/assault.png", Texture.class),
                    "Assaut", 15, 7, 2, 3, 2, 0.6f);
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/assault.png";
        }

        @Override
        public Tower getObject() {
            return new AssaultTower(this);
        }
    }


    public AssaultTower(P prototype) {
        super(prototype);
    }

//    public AssaultTower() {
//        super(ShardWar.main.getAssetLoader().get("towers/assault.png", Texture.class),
//                "Assaut", 10, 7, 3);
//    }

    @Override
    public void attack(DamageReceiver g) {
        g.dmg(dmg, this);
//        g.dmg(dmg, this);
    }

//    @Override
//    public String getProjectileTexture() {
//        return "shard.png";
//    }
}
