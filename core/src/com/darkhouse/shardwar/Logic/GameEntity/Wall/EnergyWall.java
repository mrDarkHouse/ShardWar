package com.darkhouse.shardwar.Logic.GameEntity.Wall;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.ShardWar;

public class EnergyWall extends Wall {

    public static class P extends WallPrototype{
        private int blockDmg;
        public P(int blockDmg) {
            super(ShardWar.main.getAssetLoader().get("walls/energyWall.png", Texture.class),
                    "Energy Wall", 12, 5, 3);
            this.blockDmg = blockDmg;
        }

        @Override
        public GameObject getObject() {
            return new EnergyWall(this);
        }
    }

    private int blockDmg;

    public EnergyWall(P prototype) {
        super(prototype);
        this.blockDmg = prototype.blockDmg;
    }

    @Override
    public int receiveDamage(int damage, DamageSource source) {
        if(damage > blockDmg) return damage - blockDmg;
        else return 1;
    }
}
