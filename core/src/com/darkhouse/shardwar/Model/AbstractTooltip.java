package com.darkhouse.shardwar.Model;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public abstract class AbstractTooltip extends Window {
    public AbstractTooltip(String title, Skin skin) {
        super(title, skin, "description");
        getTitleLabel().setAlignment(Align.center);
    }

    public AbstractTooltip(String title, Skin skin, String styleName) {
        super(title, skin, styleName);
        getTitleLabel().setAlignment(Align.center);
    }

    public void show(){
        setVisible(true);
        toFront();
//        AlphaAction a = new AlphaAction();
//        a.setAlpha(1.0f);
//        a.setDuration(0.2f);
//        addAction(a);
    }

    public void hide(){
        setVisible(false);
//        AlphaAction a = new AlphaAction();
//        a.setAlpha(0.0f);
//        a.setDuration(0.2f);
//        RunnableAction run = new RunnableAction();
//        run.setRunnable(new Runnable() {
//            @Override
//            public void run() {
//                setVisible(false);
//            }
//        });
//        addAction(sequence(a, run));


    }

    public abstract void hasChanged();
    public void init(Stage stage){}
//    public abstract void init();//adding to stage
}
