package com.darkhouse.shardwar.Screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;

public class EndGameScreen extends AbstractScreen{

    public EndGameScreen(int player) {
        super(getBg(player));
        init(player);
    }

    private static Texture getBg(int player){
        AssetLoader l = ShardWar.main.getAssetLoader();

//       if(GameMode == SplitScreen)
        return l.get("levelWinBg.png", Texture.class);
//       else

//        if(player == 0){
//            return l.get("levelWinBg.png", Texture.class);
//        }else {
//            return l.get("levelLooseBg.png", Texture.class);
//        }

    }

    private void init(int player){
        AssetLoader l = ShardWar.main.getAssetLoader();

        //one screen
//        if(player == 0){
//            result = l.getWord("win");
//        }else {
//            result = l.getWord("loose");
//        }


        Label resultLabel = new Label(l.getWord("player") + " " + (player) + " " + l.getWord("won"),
                ShardWar.main.getAssetLoader().getSkin());

        TextButton continueButton = new TextButton(l.getWord("continue"), l.getSkin());
        continueButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ShardWar.main.switchScreen(0);
                return true;
            }
        });


        Table table = new Table();
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());
        table.align(Align.center);
        table.setPosition(0, 0);

        Table inner = new Table();
        inner.setBackground(ShardWar.main.getAssetLoader().getSkin().getDrawable("info-panel"));
        inner.add(resultLabel).pad(10);

        table.add(inner).spaceBottom(30f).row();
        table.add(continueButton);
        continueButton.align(Align.bottom);

        stage.addActor(table);
    }
}
