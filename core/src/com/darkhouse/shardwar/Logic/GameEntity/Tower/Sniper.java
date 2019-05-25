package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells.GlobalShot;
import com.darkhouse.shardwar.ShardWar;

public class Sniper extends Tower{

    public static class P extends TowerPrototype{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/sniper.png", Texture.class),
                    ShardWar.main.getAssetLoader().getWord("sniper"), 20, 0, 5, 5,
                    new GlobalShot(false));
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/sniper.png";
        }

        @Override
        public Tower getObject() {
            return new Sniper(this);
        }
    }


    public Sniper(P prototype) {
        super(prototype);
    }


//    public void attack(DamageReceiver g) {
//        g.dmg(dmg, this);
////        System.out.println("attack");
//    }

//    @Override
//    public String getProjectileTexture() {
//        return "shard.png";
//    }
}
