package com.darkhouse.shardwar.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkhouse.shardwar.Model.BackButton;
import com.darkhouse.shardwar.ShardWar;

public class GameModeChooseScreen extends AbstractScreen{

    public GameModeChooseScreen() {
        super(ShardWar.main.getAssetLoader().getMainMenuBg());

        TextButton online = new TextButton("vs player online", ShardWar.main.getAssetLoader().getSkin());
        TextButton split = new TextButton("vs player splitscreen", ShardWar.main.getAssetLoader().getSkin());
        TextButton bot = new TextButton("vs bot", ShardWar.main.getAssetLoader().getSkin());
        online.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ShardWar.main.startPlayerGame();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        split.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ShardWar.main.startSplitGame();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        Table t = new Table();

        t.defaults().space(30).padRight(40).width(300).height(70);
        t.add(online).row();
        t.add(split).row();
        t.add(bot);
        t.right();
        t.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(t);

        BackButton b = new BackButton(true);
        stage.addActor(b);

    }
}
