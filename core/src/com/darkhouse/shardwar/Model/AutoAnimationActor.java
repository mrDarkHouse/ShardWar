package com.darkhouse.shardwar.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Model.GifDecoderOptimized;

import java.io.File;

public class AutoAnimationActor extends Actor {
    private Animation<TextureRegion> animation;
    private TextureRegion current;
    private float time = 0f;
    private boolean start;

    public AutoAnimationActor(String path, float frameDuration) {
        animation = GifDecoderOptimized.loadGIFAnimation(Animation.PlayMode.LOOP,
                Gdx.files.internal(path).read(), /*4096*200*/0);//contentLength

//        animation = GifDecoderOptimized.loadGIFAnimation(Animation.PlayMode.LOOP,
//                );=
        current = animation.getKeyFrame(time, false);
        setSize(current.getRegionWidth(), current.getRegionHeight());

        addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                start = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                start = false;
                reset();
            }
        });
        animation.setFrameDuration(frameDuration);
    }
    public void dispose(){
        animation = null;
    }

    private void reset(){
        time = 0f;
        current = animation.getKeyFrame(time, false);
    }

    @Override
    public void act(float delta) {
        if(!start) return;
//        super.act(delta);
        time += delta;
        current = animation.getKeyFrame(time, false);
//        if(animation.isAnimationFinished(time)) remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
        batch.draw(current, getX(), getY(), getWidth(), getHeight());
    }
}
