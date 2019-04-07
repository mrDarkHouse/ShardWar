package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.DamageReceiver;
import com.darkhouse.shardwar.ShardWar;

public class SniperTower extends Tower{

    public static class P extends TowerPrototype{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/sniper.png", Texture.class),
                    "Sniper", 15, 5, 3, 3);
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/sniper.png";
        }

        @Override
        public Tower getObject() {
            return new SniperTower(this);
        }
    }


    public SniperTower(P prototype) {
        super(prototype);
    }
    public void attack(DamageReceiver g) {
        g.dmg(dmg, this);
//        System.out.println("attack");
    }

//    @Override
//    public String getProjectileTexture() {
//        return "shard.png";
//    }
}
