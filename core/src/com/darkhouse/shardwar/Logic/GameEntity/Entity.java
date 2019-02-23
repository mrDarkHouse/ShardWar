package com.darkhouse.shardwar.Logic.GameEntity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darkhouse.shardwar.Logic.DamageReceiver;

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

    public abstract Vector2 getShootPosition(int line);

    public float getXShoot(int line){
        return getShootPosition(line).x;
    }
    public float getYShoot(int line){
        return getShootPosition(line).y;
    }

//    @Override
//    public void dmg(int dmg, GameObject source) {
//
//    }
}
