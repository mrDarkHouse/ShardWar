package com.darkhouse.shardwar.Model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.darkhouse.shardwar.ShardWar;

public class BackButton extends ImageButton {

    public BackButton(boolean ownPosition) {
        super(ShardWar.main.getAssetLoader().getBackButtonSkin());
        //super(mainClass.getSkin());
        int backButtonsSize[] = {64, 64};

        setSize(backButtonsSize[0], backButtonsSize[1]);
        if(ownPosition) {
            setPosition(10, Gdx.graphics.getHeight() - backButtonsSize[1]);
        }
        addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                close();
                return true;
            }
        });

    }
    protected void close(){
        //mainClass.setScreen(new MainMenu(mainClass));
        ShardWar.main.setPreviousScreen();
    }



}
