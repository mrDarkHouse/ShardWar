package com.darkhouse.shardwar.Logic.GameEntity.Wall;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.ShardWar;

public class BrickWall extends Wall {

    public static class P extends WallPrototype{

        public P() {
            super(ShardWar.main.getAssetLoader().get("walls/brickWall.png", Texture.class),
                    ShardWar.main.getAssetLoader().get("walls/brickWallOriginal.png", Texture.class),
                    ShardWar.main.getAssetLoader().getWord("brickWall"), 15, 3, 2);
        }

        @Override
        public GameObject getObject() {
            return new BrickWall(this);
        }
    }

    public BrickWall(P prototype) {
        super(prototype);
    }

    @Override
    public int receiveDamage(int damage, DamageSource source, boolean ignoreDefSpells) {
        return damage;
    }
}
