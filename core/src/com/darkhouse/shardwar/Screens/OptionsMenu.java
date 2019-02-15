package com.darkhouse.shardwar.Screens;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Model.BackButton;
import com.darkhouse.shardwar.ShardWar;

public class OptionsMenu extends AbstractScreen{
    public OptionsMenu() {
        super(ShardWar.main.getAssetLoader().getMainMenuBg());
        init();
    }

    private void init(){
        stage.addActor(new BackButton(true));
    }
}
