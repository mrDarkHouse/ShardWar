package com.darkhouse.shardwar.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkhouse.shardwar.ShardWar;

public class MainMenu extends AbstractScreen{

    public MainMenu() {
        super(ShardWar.main.getAssetLoader().getMainMenuBg());

        TextButton start = new TextButton("Start", ShardWar.main.getAssetLoader().getSkin());
        TextButton options = new TextButton("Options", ShardWar.main.getAssetLoader().getSkin());
        TextButton wiki = new TextButton("Wiki", ShardWar.main.getAssetLoader().getSkin());
        TextButton credits = new TextButton("Credits", ShardWar.main.getAssetLoader().getSkin());
        TextButton exit = new TextButton("Exit", ShardWar.main.getAssetLoader().getSkin());
        start.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ShardWar.main.switchScreen(2);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        options.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ShardWar.main.switchScreen(1);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        wiki.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ShardWar.main.switchScreen(3);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        exit.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.exit(0);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        Table t = new Table();

        t.defaults().space(30).padRight(40).width(200).height(70);
        t.add(start).row();
        t.add(options).row();
        t.add(wiki).row();
        t.add(credits).row();
        t.add(exit);
        t.right();
        t.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(t);
    }
}
