package com.darkhouse.shardwar.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.I18NBundle;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.FontLoader;

public abstract class AbstractLoadingScreen extends AbstractScreen {

    protected int progress;
    protected ProgressBar loadBar;

    public AbstractLoadingScreen() {
        super(null);
    }
    public void init(){
//        batch = new SpriteBatch();
        initBg(ShardWar.main.getAssetLoader().getMainMenuBg());

        loadBar = new ProgressBar(0, 100, 1, false, ShardWar.main.getAssetLoader().getLoadingBarStyle());
        loadBar.setSize(Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/14.4f);
        loadBar.getStyle().background.setMinHeight(loadBar.getHeight());
        loadBar.getStyle().knob.setMinHeight(loadBar.getHeight());
        loadBar.setPosition(Gdx.graphics.getWidth()/2f - loadBar.getWidth()/2,
                Gdx.graphics.getHeight()/2f - loadBar.getHeight()/2);
        stage.addActor(loadBar)/*.align(Align.center | Align.bottom).expandX().row()*/;

        I18NBundle b = ShardWar.main.getAssetLoader().get("Language/text", I18NBundle.class);
        Label loading = new Label(b.get("loading"), FontLoader.generateStyle(0, 34, Color.BLACK));//
        loading.setPosition(loadBar.getX() + loadBar.getWidth()/2 - loading.getWidth()/2,
                loadBar.getY() - loadBar.getHeight());
        stage.addActor(loading);

    }


    @Override
    public void show() {


    }



    @Override
    public void render(float delta) {
        super.render(delta);

//        batch.setProjectionMatrix(camera.combined);

//        batch.begin();
//        bg.draw(batch, 255f);
//        batch.draw(background, 0f, 0f, stage.getWidth(), stage.getHeight());
//        batch.end();
        progress = (int)(ShardWar.main.getAssetLoader().getProgress() * 100);
        update(progress);
        if(ShardWar.main.getAssetLoader().update()){
            //if (Gdx.input.isTouched()) {
                onLoad();
            //}

        }

        stage.act();
        stage.draw();
    }
    private void update(int progress){
        loadBar.setValue(progress);
    }

    protected abstract void onLoad();

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        progress = 0;
    }

    @Override
    public void dispose() {

    }
}
