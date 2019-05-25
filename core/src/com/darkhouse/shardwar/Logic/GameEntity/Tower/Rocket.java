package com.darkhouse.shardwar.Logic.GameEntity.Tower;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.DamageReceiver;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells.GlobalShot;
import com.darkhouse.shardwar.ShardWar;

public class Rocket extends Tower {

    public static class P extends TowerPrototype<Rocket>{

        public P() {
            super(ShardWar.main.getAssetLoader().get("towers/rocket.png", Texture.class),
                    ShardWar.main.getAssetLoader().getWord("rocket"), 10, 10, 2, 3,
                    new GlobalShot(false));
        }

        @Override
        public Rocket getObject() {
            return new Rocket(this);
        }

        @Override
        public String getProjectileTexture() {
            return "projectiles/rocket.png";
        }
    }

    public Rocket(P prototype) {
        super(prototype);
    }

//    public void attack(DamageReceiver g) {
//        g.dmg(dmg, this);
//    }
}
