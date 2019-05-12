package com.darkhouse.shardwar.Logic.GameEntity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Array;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.EffectIcon;
import com.darkhouse.shardwar.ShardWar;

import java.util.HashMap;

public abstract class Entity extends Image implements DamageReceiver {

    public Entity(Texture texture) {
        super(texture);
    }

    public Entity(TextureRegion region) {
        super(region);
    }
    abstract public boolean isExist();

    public Entity() {
        super();
    }

    public boolean contains(float x, float y){
        return (x >= getX() + getParent().getX() && x <= getX() + getParent().getX() + getWidth())
                && (y >= getY() + getParent().getY() && y <= getY() + getParent().getY() + getHeight());
    }




}
