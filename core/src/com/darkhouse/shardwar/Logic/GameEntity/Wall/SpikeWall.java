package com.darkhouse.shardwar.Logic.GameEntity.Wall;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.DamageReceiver;
import com.darkhouse.shardwar.Logic.GameEntity.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.ShardWar;

public class SpikeWall extends Wall {

    public static class P extends WallPrototype{
        private int spikeDmg;
        public P(int spikeDmg) {
            super(ShardWar.main.getAssetLoader().get("walls/spikeWall.png", Texture.class),
                    "Spike Wall", 20, 7, 4);
            this.spikeDmg = spikeDmg;
        }

        @Override
        public GameObject getObject() {
            return new SpikeWall(this);
        }
    }

    private int spikeDmg;

    public SpikeWall(P prototype) {
        super(prototype);
        this.spikeDmg = prototype.spikeDmg;
    }

    @Override
    public int receiveDamage(int damage, DamageSource source) {
        if(source instanceof DamageReceiver) {
            ((DamageReceiver) source).dmg(spikeDmg, this);
        }
        return damage;
    }
}
