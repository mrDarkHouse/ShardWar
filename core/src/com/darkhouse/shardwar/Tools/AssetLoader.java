package com.darkhouse.shardwar.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ObjectMap;

public class AssetLoader extends AssetManager {


    public void loadAll(){
        load("skins/uiskin.json", Skin.class);

        load("MainMenuBg.png", Texture.class);
        load("backButton.png", Texture.class);
        load("shard.png", Texture.class);
        load("towerSlot.png", Texture.class);
        load("wallSlot.png", Texture.class);
        load("towers/laser.png", Texture.class);
        load("towers/assault.png", Texture.class);
        load("towers/sniper.png", Texture.class);
        load("walls/brickWall.png", Texture.class);
        load("walls/energyWall.png", Texture.class);
        load("walls/spikeWall.png", Texture.class);

        load("target/centerTarget.png", Texture.class);
        load("target/leftTarget.png", Texture.class);
        load("target/rightTarget.png", Texture.class);
        load("target/endTarget.png", Texture.class);

        load("target/targetCenter.png", Texture.class);
        load("target/targetLeft.png", Texture.class);
        load("target/targetRight.png", Texture.class);
        load("target/targetCenterInv.png", Texture.class);
        load("target/targetLeftInv.png", Texture.class);
        load("target/targetRightInv.png", Texture.class);

        load("projectiles/sniper.png", Texture.class);
        load("projectiles/assault.png", Texture.class);


        ObjectMap<String, Object> fontMap = new ObjectMap<String, Object>();
        fontMap.put("description-font", FontLoader.generateFont(0, 16, Color.BLACK));

        SkinLoader.SkinParameter parameter = new SkinLoader.SkinParameter(fontMap);
        load("skins/uiskin.json", Skin.class, parameter);

        finishLoading();
    }
    public Drawable getCenterTarget(int player){
        if(player == 1) return new TextureRegionDrawable(new TextureRegion(get("target/targetCenter.png", Texture.class)));
        else return new TextureRegionDrawable(new TextureRegion(get("target/targetCenterInv.png", Texture.class)));
    }
    public Drawable getRightTarget(int player){
        if(player == 1) return new TextureRegionDrawable(new TextureRegion(get("target/targetRight.png", Texture.class)));
        else return new TextureRegionDrawable(new TextureRegion(get("target/targetRightInv.png", Texture.class)));
    }
    public Drawable getLeftTarget(int player){
        if(player == 1) return new TextureRegionDrawable(new TextureRegion(get("target/targetLeft.png", Texture.class)));
        else return new TextureRegionDrawable(new TextureRegion(get("target/targetLeftInv.png", Texture.class)));
    }

    public void setFilters(){
        get("towerSlot.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("wallSlot.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("shard.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/brickWall.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/energyWall.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("walls/spikeWall.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        get("target/targetCenter.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetCenterInv.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetRight.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetRightInv.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetLeft.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        get("target/targetLeftInv.png", Texture.class).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public Skin getSkin(){
        return get("skins/uiskin.json", Skin.class);
    }
    public Texture getMainMenuBg(){
        return get("MainMenuBg.png");
    }

    public ImageButton.ImageButtonStyle getBackButtonSkin(){
        return generateImageButtonSkin(get("backButton.png", Texture.class));
    }

    public ImageButton.ImageButtonStyle generateImageButtonSkin(Texture t){
        ImageButton.ImageButtonStyle s = new ImageButton.ImageButtonStyle();
        s.up = new TextureRegionDrawable(new TextureRegion(t));
        return s;
    }



}
