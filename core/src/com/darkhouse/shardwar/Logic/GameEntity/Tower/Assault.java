package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells.MultiShot;
import com.darkhouse.shardwar.ShardWar;

public class Assault extends Tower{

    public static class P extends TowerPrototype<Assault>{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/assault.png", Texture.class),
                    ShardWar.main.getAssetLoader().getWord("assault"), 15, 7, 2, 3,
                    new MultiShot(2, 0.6f));
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/assault.png";
        }

        @Override
        public Assault getObject() {
            return new Assault(this);
        }
    }


    public Assault(P prototype) {
        super(prototype);
    }

//    public AssaultTower() {
//        super(ShardWar.main.getAssetLoader().get("towers/assault.png", Texture.class),
//                "Assaut", 10, 7, 3);
//    }

//    @Override
//    public void attack(DamageReceiver g) {
//        g.dmg(dmg, this);
////        g.dmg(dmg, this);
//    }

//    @Override
//    public String getProjectileTexture() {
//        return "shard.png";
//    }
}
