package com.darkhouse.shardwar.Model;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.darkhouse.shardwar.ShardWar;

import java.util.Locale;


public class PlayerSearchTimer extends Label {

    private float time;
    private boolean work;

    public PlayerSearchTimer() {
        super("", ShardWar.main.getAssetLoader().getSkin());
        work = true;
    }

    public void stop(){
        work = false;
        time = 0f;
    }
    public void go(){
        work = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!work) return;
        time += delta;
        setText(String.format(Locale.ENGLISH,"%02d:%02d", (int)time / 60, (int)time % 60));

    }
}
