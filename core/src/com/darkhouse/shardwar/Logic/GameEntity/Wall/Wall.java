package com.darkhouse.shardwar.Logic.GameEntity.Wall;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.darkhouse.shardwar.Logic.GameEntity.DamageReceiver;
import com.darkhouse.shardwar.Logic.GameEntity.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;

public abstract class Wall extends GameObject implements DamageReceiver {
//    private Texture texture;

    public abstract static class WallPrototype extends ObjectPrototype {

        public WallPrototype(Texture texture, Texture originalTexture, String name, int health, int cost, int bounty) {
            super(texture, originalTexture, name, health, cost, bounty);
        }
    }


    public Wall(WallPrototype prototype) {
        super(prototype);
    }

    @Override
    public Vector2 getShootPosition(int line) {
        return new Vector2(slot.getX() + slot.getParent().getX() + slot.getWidth()/2,
                slot.getY() + slot.getParent().getY() + slot.getHeight()/2);
    }

    @Override
    public String getTooltip() {
        AssetLoader l = ShardWar.main.getAssetLoader();
        String s = "";
        s += l.getWord("health") + ": " + health + "/" + maxHealth/*+ System.getProperty("line.separator")*/;

        String t = getEffectTooltip();
        if(t.length() > 0) s += System.getProperty("line.separator") + t;

        return s;
    }

    @Override
    public WallPrototype getObjectPrototype() {
        return ((WallPrototype) objectPrototype);
    }

//    public abstract int receiveDamage(int damage, DamageSource source, boolean ignoreDefSpells);

    @Override
    public void physic(float delta) {

    }
}
