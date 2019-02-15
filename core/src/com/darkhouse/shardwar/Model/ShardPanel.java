package com.darkhouse.shardwar.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;

public class ShardPanel extends Table {

    private Label text;
    private Player player;

    public ShardPanel(Player player) {
        this.player = player;
        Image shard = new Image(ShardWar.main.getAssetLoader().get("shard.png", Texture.class));
        text = new Label(Integer.toString(player.getShards()), ShardWar.main.getAssetLoader().getSkin());

        add(text).space(5);
        add(shard);//.size(22, 34);
//        debug();
        pack();
    }

    public void hasChanged(){
        text.setText(Integer.toString(player.getShards()));
    }

    @Override
    public void act(float delta) {
        hasChanged();
    }
}
