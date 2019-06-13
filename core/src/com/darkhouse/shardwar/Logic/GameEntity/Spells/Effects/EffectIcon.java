package com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.FontLoader;

public class EffectIcon extends Actor{

    private Effect effect;
    private Color color;
    private Texture icon;
    public Texture getIcon() {
        return icon;
    }
//    ShapeRenderer sp;
    private int borderSize = 1;

    private Label duration;

    public EffectIcon(Effect effect) {
        this.effect = effect;
        icon = ShardWar.main.getAssetLoader().getEffectIcon(effect.getName());
        if(effect.isPositive())color = Color.FOREST;
        else                   color = Color.FIREBRICK;
//        sp = new ShapeRenderer();//Exception in thread "Timer-0" No OpenGL context found in the current thread.
        duration = new Label(effect.getDuration() + "", FontLoader.effectDurationStyle);
        duration.setAlignment(Align.center);
    }
    public void init(){
        duration.setBounds(getX() + borderSize, getY() + borderSize, getWidth() - borderSize*2, getHeight() - borderSize*2);
    }


    public void updateDurationTime(){
        duration.setText(effect.getCurrentTime());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        ShapeRenderer sp = FightScreen.sp;
        sp.begin(ShapeRenderer.ShapeType.Line);
        sp.setColor(color);
        Gdx.gl.glLineWidth(borderSize);
        Vector2 a = localToStageCoordinates(new Vector2(getX(), getY()));
        sp.rect(a.x /*- getX()*/ + 1 /*- getX()*/, a.y/*getY()*/ + 1 - getY(), getWidth() - 1, getHeight() - 1);
        sp.end();
        Gdx.gl.glLineWidth(1);






        batch.begin();

        batch.draw(icon, getX() + borderSize, getY() + borderSize, getWidth() - borderSize*2, getHeight() - borderSize*2);
        updateDurationTime();//
        duration.draw(batch, parentAlpha);

//        if(effect.isCooldownable() && !effect.getCooldownObject().isHidden()){//if cooldownable and stackable in one time ability must declare own EffectIcon
//            duration.setAlignment(Align.center);
//            int cd = (int)  Math.ceil(effect.getCooldownObject().getCooldown());
//            if(cd == 0)duration.setText("");
//            else duration.setText(cd + "");
//            duration.setBounds(getX() + borderSize, getY() + borderSize, getWidth() - borderSize*2, getHeight() - borderSize*2);
//            duration.draw(batch, parentAlpha);
//        }
//        if(effect.isStackable()){
//            duration.setAlignment(Align.center);
//            int stacks = effect.getStackableObject().getStacks();
//            if(stacks == 0)duration.setText("");
//            else duration.setText(stacks + "");
//            duration.setBounds(getX() + borderSize, getY() + borderSize, getWidth() - borderSize*2, getHeight() - borderSize*2);
//            duration.draw(batch, parentAlpha);
//        }
    }


}
