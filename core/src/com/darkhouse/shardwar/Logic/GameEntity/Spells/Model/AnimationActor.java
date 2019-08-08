package com.darkhouse.shardwar.Logic.GameEntity.Spells.Model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimationActor extends Actor {
    private Animation<TextureAtlas.AtlasRegion> animation;
    private TextureRegion current;
    private float time = 0f;

    public AnimationActor(Animation<TextureAtlas.AtlasRegion> animation) {
        this.animation = animation;
        current = animation.getKeyFrame(time, false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
        current = animation.getKeyFrame(time, false);
        if(animation.isAnimationFinished(time)) remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(current, getX(), getY(), getWidth(), getHeight());
    }
}