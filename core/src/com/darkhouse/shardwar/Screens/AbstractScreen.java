package com.darkhouse.shardwar.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class AbstractScreen implements Screen {

    protected SpriteBatch batch;
//    protected Texture background;
    protected Image background;
    protected Stage stage;
    protected OrthographicCamera camera;
    protected Viewport viewport;


    public AbstractScreen(Texture texture) {
        batch = new SpriteBatch();
        stage = new Stage();
        if(texture != null){
            initBg(texture);
        }
    }
    protected void initBg(Texture texture){
        background = new Image(texture);
        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(background);
    }

    public void hideBackGround(){
        background.setColor(background.getColor().r, background.getColor().g, background.getColor().b, 0);
    }
    public void showBackGround(){
        background.setColor(background.getColor().r, background.getColor().g, background.getColor().b, 1);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

//        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }
}
